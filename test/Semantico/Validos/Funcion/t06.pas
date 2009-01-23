program Funcion;
type ENTERO=integer;
var a:integer;
function asig(var a:integer; b:integer):entero;
 {pone en a el valor de b; retorna la diferencia a-b}
begin
 asig:=a-b;
 a:=b;
end;
begin
a:=10;
writeln(asig(a,9));{1}
writeln(a);{9}
end.