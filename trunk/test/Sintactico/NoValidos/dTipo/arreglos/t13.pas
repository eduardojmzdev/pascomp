{
El proposito de este test es detectar el error de no poner
el tipo de subindice de arreglo

Resultado esperado: Test Fallido, 
Error Sintactico en linea 13: Se espera un identificador o
 constructor  de tipo Simple valido
}
program p;
const
	A=654;
type
	x= array [] of end;	
begin
end.