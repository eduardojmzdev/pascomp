{no como parametro por referencia, e.d no tiene que
tirar se espera variables como parametro formal}
program ExprI;
var a:integer;
    
      b:boolean;
procedure pVal(i:integer); begin end;

begin
 b:=+a>-2;
 b:=-b;{el op unario - solo se aplica a enteros}
end.

end.{no importa se corta antes}