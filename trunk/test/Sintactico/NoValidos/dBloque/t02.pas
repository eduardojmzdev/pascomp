{
El proposito de este test es detectar el error de poner la definicion de 
variables  antes de las de constantes

Resultado esperado: Test Fallido ,
Error Sintactico en linea 12: Se espera un identificador de variable o  alguna 
de las siguientes palabras reservadas: procedure,function o begin
}
program p;
var n: boolean;
 j:integer;
 const
	g=5;
 function p:integer;
 begin
 end;
begin
