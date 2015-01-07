@echo off

rem Use local variables
setlocal
@call "%~dp0\setenv.bat"

set command_line=%*
call "%XAPHOMEDIR%\bin\gs.bat" %command_line%

endlocal
title Command Prompt
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
exit /B 0
