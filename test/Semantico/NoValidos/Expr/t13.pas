{no como parametro por referencia, e.d no tiene que
tirar se espera variables como parametro formal}

program ExprI;
var a:integer;
    b:boolean;
    c:array[boolean] of integer;
procedure pVal(i:integer); begin end;
begin
 b:=+a>-2;
 b:=b and  c[true];{tipos incompatibles en expresion}
end.

