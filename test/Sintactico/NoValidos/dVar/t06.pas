{
El proposito de este test es detectar el error cuando no se
termina una definicion de var con punto y coma

Resultado esperado: Test Fallido ,
"Error Sintactico en linea 10: Se espera un punto y coma"
}
program p;
var
	A:array[6..10]of boolean
	B: boolean;
begin
end.
