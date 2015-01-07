@echo off
@call "%~dp0\setenv.bat"

start %~dp0\gs-drools-agent.bat

echo Waiting 30 seconds before deploying space
rem sleep hack for dos/windows
ping 1.1.1.1 /n 30 /w 1000 > nul

rem gs.bat deploy "%DROOLS_PROJ_FOLDER%\space\target\space.jar"
start %~dp0\deploy-space.bat

echo Waiting 20 seconds before deploying persistency
ping 1.1.1.1 /n 20 /w 1000 > nul
rem gs.bat deploy "%DROOLS_PROJ_FOLDER%\persistency\target\persistency.jar"
start %~dp0\deploy-persistency.bat

start %~dp0\rule-loader.bat

set /p DUMMY=Hit ENTER to continue...