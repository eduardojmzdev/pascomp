{
El proposito de este test es detectar el error de omitir 
la palabra reservada array

Resultado esperado: Test Fallido, 
Error Sintactico en linea 12: identificador de tipo invalido
}
program p;
const
	A=654;
type
	x= [ m..n];
	
begin
end.
