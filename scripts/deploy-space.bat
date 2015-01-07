@echo off
@call "%~dp0\setenv.bat"

echo deploying space .....
rem sleep hack for dos/windows

gs.bat deploy "%DROOLS_PROJ_FOLDER%\space\target\space.jar"

set /p DUMMY=Hit ENTER to continue...
