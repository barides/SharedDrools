@echo off

rem Use local variables
setlocal

set XAPNETHOMEDIR=C:\GigaSpaces\XAP.NET 10.0.1 x64\NET v4.0
set XapNet.Groups=CLIENT
set command_line=%*
call "%XAPNETHOMEDIR%\Bin\Gs-ui.exe" %command_line%

endlocal
title Command Prompt
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
exit /B 0
