@echo off

rem set CLIENT_FOLDER=C:\_XAPAdminTraining\Labs\Drools\xap-drools-integration-master\
rem set ADDITIONAL_CP=C:\Users\AndroidDev\.m2\repository\log4j\log4j\1.2.16\*;C:\Users\AndroidDev\.m2\repository\com\thoughtworks\xstream\xstream\1.3.1\*;C:\Users\AndroidDev\.m2\repository\xpp3\xpp3\1.1.4c\*

@call %XAPHOMEDIR%\bin\setenv.bat
@call "%~dp0\setenv.bat"

echo Waiting 1 seconds before loading rules
ping 1.1.1.1 /n 1 /w 1000 > nul

"%JAVA_HOME%"\bin\java -cp %GS_JARS%;%ADDITIONAL_CP%;%DROOLS_PROJ_FOLDER%\common\target\common.jar;%DROOLS_PROJ_FOLDER%\client\target\client-1.0.jar; com.gigaspaces.droolsintegration.loader.DroolsRulesLoader 

set /p DUMMY=Hit ENTER to continue...
