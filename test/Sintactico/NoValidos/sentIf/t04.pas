{
El proposito de este test es detectar el error de 
cambiar un else por un if

Resultado esperado: Test Fallido ,
Error Sintactico en linea 13: se espera un end
}
program p;

begin
if d then 
    if  a then b:=g 
	if z:=y{ este if tendria q ser un else}
else
 lk:=08
end.
