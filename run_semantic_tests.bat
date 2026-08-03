@echo off
REM Builds the compiler and runs the Python semantic-analysis test suite.
REM Run from the project root: the tests read tests\app.py and tests\templates.

setlocal
set CP=lib\antlr4-runtime-4.13.2.jar;lib\gson-2.14.0.jar

if not exist build\classes mkdir build\classes

dir /s /b src\*.java > build\sources.txt

javac --release 21 -nowarn -cp "%CP%" -d build\classes @build\sources.txt
if errorlevel 1 (
    echo BUILD FAILED
    exit /b 1
)

java -cp "build\classes;%CP%" semantic_tests.SemanticTestRunner
exit /b %errorlevel%
