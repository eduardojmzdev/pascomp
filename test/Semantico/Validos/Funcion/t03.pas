program Funcion;
var a:integer;
function mayor(a,b:integer):integer;{retrona el mayor}
begin
  if a>b  then
   mayor:=a
  else
   mayor:=b;
end;

begin
a:=mayor(-123,123);
writeln(a); {123}
end.