{evaluacion de expresiones en contextos de llamadas}
program ExprI;
var a:integer;
    b:boolean;
    x:integer; 	

function f(x:integer):integer;
begin
end;

procedure pVal(var i:integer); begin end;
begin
 b:=+a>-2;
 pVal(f(2));  {no se puede pasar una funcion por referencia}
end.

