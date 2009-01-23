{
El proposito de este test es detectar el error de no poner 
end al terminar un procedimiento anidado en una funcion

Resultado esperado: Test Fallido ,
Error Sintactico en linea 18 : Se espera un end
}
program p;

var n: boolean;
 j:integer;
 function p :integer;
		procedure ya (var t: boolean);
		begin
 begin
 end;
begin
end.
