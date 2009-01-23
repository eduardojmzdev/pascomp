program Funcion;
type boleano=boolean;
var a,b:integer;
function inter(var a1,a2:integer):BOLEANO;
 {intercambia los valores,retorna true si son iguales}
var aux:integer;
begin
  aux:=a1;
  a1:=a2;
  a2:=aux;
  if a1<>a2 then
   inter:=false
  else
   inter:=true;
end;

begin
a:=3;
b:=4;
if not inter(a,b) then writeln(222222);

writeln(a); {4}
writeln(b); {3}
a:=3;
writeln(a); {3}
writeln(b);  {3}
end.