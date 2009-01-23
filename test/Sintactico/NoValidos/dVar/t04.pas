{
El proposito de este test es detectar el error de poner 
un simbolo distinto a ';' al termino de una definicion 
de variable
Resultado esperado: Test Fallido ,
"Error Sintactico en linea 10: Se espera un punto y coma ';'"
}
program p;
var
	A:Boolean,;
begin
end.
