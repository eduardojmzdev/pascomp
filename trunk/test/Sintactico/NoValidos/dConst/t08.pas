{
El proposito de este test es detectar el error de poner como identificador 
de constante una palabra reservada

Resultado esperado: Test Fallido ,
"Error Sintactico en linea 10: Se espera un identificador"
}
program p;
const
	program = -44;
begin
end.
