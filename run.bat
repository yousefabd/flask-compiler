@echo off
REM ---------------------------------------------------------------------------
REM Runs a class from the last build.bat output.
REM
REM   run.bat Main                      compile+render the default route
REM   run.bat Main filter_test          compile+render one route by function name
REM   run.bat tests.PythonErrorTests    run the Python error test suite
REM   run.bat tests.PythonErrorTests --show    ... and print every report
REM ---------------------------------------------------------------------------
setlocal

if not exist "out" (
    echo No build found. Run build.bat first.
    exit /b 1
)

java -cp "out;lib\antlr4-runtime-4.13.2.jar;lib\gson-2.14.0.jar" %*
