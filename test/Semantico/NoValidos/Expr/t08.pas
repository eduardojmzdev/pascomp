{no como parametro por referencia, e.d no tiene que
tirar se espera variables como parametro formal}

program ExprI;
var a:integer;
    
    b:boolean;

procedure pVal(i:integer); begin end;
begin
 b:=+a>-2;
 b:=succ(maxint)-a+b;{ tipos incompatibles en expresion}
end.

end.{no importa se corta antes}