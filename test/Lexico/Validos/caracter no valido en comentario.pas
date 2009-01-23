{
El proposito de este test es incluir un caracter 
no perteneciente al alfabeto en el medio de algun comentario

Resultado esperado: Test Valido ,
Tokens: PROGRAM,ID,PUNTO_Y_COMA,CONST,ID,PUNTO_Y_COMA,BEGIN
,END,PUNTO,EOF
}
program pepe;
{comentario #####,}
const T;
(* $$@@*)
begin
end.