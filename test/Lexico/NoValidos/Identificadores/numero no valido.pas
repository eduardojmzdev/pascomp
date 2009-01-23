{
El proposito de este test es incluir un numero no 
valido, por ej: cuando termina con uno o mas caracteres

Resultado esperado: Test Fallido,
"Error Lexico en linea 14: '4568m' identificador no valido"
}
program pepe;
{comentario #####,}
const T;
(* $$@@*)
begin
var x,y: integer;
x: 4568mm;
end.