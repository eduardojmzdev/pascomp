{
El proposito de este test es detectar el error cuando no se
termina una definicion de constante con punto y coma

Resultado esperado: Test Fallido ,
"Error Sintactico en linea 11: Se espera un punto y coma"
}
program p;
const
	A=+A
	B=44;
begin
end.
