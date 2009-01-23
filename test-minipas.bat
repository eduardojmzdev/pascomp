cls
@echo off
echo ###################################
echo ##     COMPILADOR MINIPASCAL     ##
echo ##                               ##
echo ## AUTORES:                      ##	
echo ##    Mayra Echandi              ##
echo ##    Esteban A. Bett            ##
echo ##                               ##
echo ## Help file: readme.txt         ##
echo ## Config file: minipas.conf     ##
echo ###################################
echo loading java...
java -jar dist/minipascal.jar main.MiniPas %1 |more
pause