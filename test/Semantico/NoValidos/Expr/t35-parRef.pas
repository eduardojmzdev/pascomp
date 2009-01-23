{evaluacion de expresiones en contextos de llamadas}
program ExprI;
const const1=23;
var a:integer;
      b:boolean;
    x:integer; 	

function f(x:integer):integer;
begin
end;

procedure pVal(var i:integer); begin end;
begin
 b:=+a>-2;
 pVal(const1);  {no se puede pasar un numero o funcion por referencia}
end.

