{
El proposito de este test es detectar el error de poner la definicion de 
funciones antes de las de tipo

Resultado esperado: Test Fallido ,
Error Sintactico en linea 15 : Se espera un begin
}
program p;

var n: boolean;
 j:integer;
 function p:integer;
 begin
 end;
 type
	f=integer;
begin
end.
