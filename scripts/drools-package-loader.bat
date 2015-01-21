@echo off

 

@call %JSHOMEDIR%\bin\setenv.bat

@call "%~dp0\setenv.bat"

 

echo Waiting 3 seconds before loading rules

ping 1.1.1.1 /n 3 /w 1000 > nul

echo %GS_JARS%

"%JAVA_HOME%"\bin\java -cp %GS_JARS%;%ADDITIONAL_CP%;%DROOLS_PROJ_FOLDER%\common\target\common.jar;%DROOLS_PROJ_FOLDER%\client\target\client-1.0.jar; com.c123.demo.drools.client.loader.DroolsPackageLoader

set /p DUMMY=Hit ENTER to continue...