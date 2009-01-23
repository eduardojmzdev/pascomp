{evaluacion de expresiones en contextos de llamadas}
program ExprI;
var a:integer;
    b:boolean;
    x:integer; 	

procedure pVal(var i:integer); begin end;
begin
 b:=+a>-2;
 pVal(x>3);  {se espera el nombre de una variable o componente de arreglo}
end.

