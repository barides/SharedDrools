@echo off
@call "%~dp0\setenv.bat"

echo deploying persistency ....

gs.bat deploy "%DROOLS_PROJ_FOLDER%\persistency\target\persistency.jar"

set /p DUMMY=Hit ENTER to continue...
