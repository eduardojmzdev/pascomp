{evaluacion de expresiones en contextos de llamadas}
program ExprI;
var a:integer;
    b:boolean;
    x:integer; 	

procedure pVal(var i:integer); begin end;
begin
 b:=+a>-2;
 pVal(not x);  {no se permite pasar por referencia un valor numerico o boolean}
end.

