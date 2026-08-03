@echo off
REM ---------------------------------------------------------------------------
REM Builds the compiler without Maven.
REM
REM The two runtime dependencies declared in pom.xml are fetched into lib\ on
REM first run and cached there afterwards. lib\ is gitignored, exactly like the
REM .venv\ that setup_environment.bat creates, so nothing binary is committed.
REM
REM   build.bat          compile everything in src\ to out\
REM   run.bat Main       run the compiler
REM   run.bat tests.PythonErrorTests    run the Python error test suite
REM ---------------------------------------------------------------------------
setlocal

set "LIB_DIR=lib"
set "OUT_DIR=out"

set "ANTLR_JAR=%LIB_DIR%\antlr4-runtime-4.13.2.jar"
set "GSON_JAR=%LIB_DIR%\gson-2.14.0.jar"

set "ANTLR_URL=https://repo1.maven.org/maven2/org/antlr/antlr4-runtime/4.13.2/antlr4-runtime-4.13.2.jar"
set "GSON_URL=https://repo1.maven.org/maven2/com/google/code/gson/gson/2.14.0/gson-2.14.0.jar"

if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"

if not exist "%ANTLR_JAR%" (
    echo Fetching antlr4-runtime 4.13.2 ...
    curl -sSfL -o "%ANTLR_JAR%" "%ANTLR_URL%"
    if errorlevel 1 (
        echo Could not download %ANTLR_URL%
        exit /b 1
    )
)

if not exist "%GSON_JAR%" (
    echo Fetching gson 2.14.0 ...
    curl -sSfL -o "%GSON_JAR%" "%GSON_URL%"
    if errorlevel 1 (
        echo Could not download %GSON_URL%
        exit /b 1
    )
)

if exist "%OUT_DIR%" rmdir /s /q "%OUT_DIR%"
mkdir "%OUT_DIR%"

dir /s /b src\*.java > "%OUT_DIR%\sources.txt"

javac -nowarn -encoding UTF-8 -d "%OUT_DIR%" -cp "%ANTLR_JAR%;%GSON_JAR%" "@%OUT_DIR%\sources.txt"
if errorlevel 1 (
    echo Build failed.
    exit /b 1
)

echo Build completed.
