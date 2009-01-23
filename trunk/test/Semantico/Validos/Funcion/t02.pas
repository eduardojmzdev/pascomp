program Funcion;
var a:integer;
function f(b:integer):integer;
begin
  f:=b+2;
end;
begin
a:=f(10);
writeln(a); {12}
end.