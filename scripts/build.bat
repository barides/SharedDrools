@echo off

rem Use local variables
setlocal
@call "%~dp0\setenv.bat"

cd %DROOLS_PROJ_FOLDER%
mvn package

set /p DUMMY=Hit ENTER to continue...
