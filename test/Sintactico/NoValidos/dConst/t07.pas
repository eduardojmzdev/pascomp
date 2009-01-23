{
El proposito de este test es detectar el error cuando definimos
varias constantes en una misma declaracion

Resultado esperado: Test Fallido ,
"Error Sintactico en linea 10: Se espera un ="
}
program p;
const
	A,B,-44;
	
begin
end.
