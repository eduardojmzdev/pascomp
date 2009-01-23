{
El proposito de este test es detectar el error de poner una palabra reservada como tipo
 de retorno de funcion

Resultado esperado: Test Fallido ,
Error Sintactico en linea 12 : identificador de tipo de retorno invalido
}
program p;

var n: boolean;
 j:integer;
 function p:end;
 begin
 end;
begin
end.
