{
El proposito de este test es verificar que el analizador
lexico se ejecute con exito cuando encuentra *) sin
haber leido previamente (*.

Resultado esperado: Test Valido
Tokens: PROGRAM, ID, PUNTO_Y_COMA,CONST, ID
PUNTO_Y_COMA, BEGIN, OP_MULT, PAR_CIERRA, END, 
PUNTO, EOF.
}
program pepe;
const T;
begin
*)
end.