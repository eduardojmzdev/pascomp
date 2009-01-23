program Funcion;
var a:integer;
function f(var a:integer):boolean;{pass a a su val abs.
       retora true si ya erra positivo}
begin
  if a<0  then
   begin
   a:=-a;
   f:=false;
   end
  else
   f:=true;
end;

begin
a:=123;
writeln(a);{123}
a:=-12;
writeln(a);{12}
end.