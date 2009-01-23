{
El proposito de este test es detectar el error de  poner
un simbolo distinto al '[' en una definicion de arreglo

Resultado esperado: Test Fallido, 
Error Sintactico en linea 13: Se espera un '['
}
program p;
const
	A=654;
type
	x= array: [] of end;	
begin
end.