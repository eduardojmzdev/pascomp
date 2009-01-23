{
El proposito de este test es detectar el error de incluir mas de una 
declaracion de variable anteponiendo la palabra var

Resultado esperado: Test Fallido ,
Error Sintactico en linea 11 : Se espera un identificador de variable o alguna de las sig. palabras reservadas: const,type,
var,procedure,function o begin
}
program p;
var n: boolean;
var  j:integer;
begin
end.
