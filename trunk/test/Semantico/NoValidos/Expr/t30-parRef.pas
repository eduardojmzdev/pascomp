{evaluacion de expresiones en contextos de llamadas}
program ExprI;
var a:integer;
    b:boolean;
    x:integer; 	

procedure pVal(var i:integer); begin end;
begin
 b:=+a>-2;
 pVal(x and);  {se espera el nombre de una variable o componente de un arreglo como parametro actual}
end.

