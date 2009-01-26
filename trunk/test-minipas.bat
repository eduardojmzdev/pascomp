cls
@echo off
echo ###################################
echo ##     COMPILADOR PLG 08/09      ##
echo ##                               ##
echo ##            GRUPO 8            ##	
echo ## 			      ##
echo ## 			      ##
echo ##                               ##
echo ###################################
echo Cargando...
java -jar dist/minipascal.jar main.MiniPas %1 |more
pause