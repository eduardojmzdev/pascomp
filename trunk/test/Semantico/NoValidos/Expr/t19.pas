{no como parametro por referencia, e.d no tiene que
tirar se espera variables como parametro formal}

program ExprI;
var a:integer;     
    b:boolean;

procedure pVal(i:integer); begin end;
begin
 b:=b and pVal; {se espera un id de variable, cte, funcion o parametro}
end.

