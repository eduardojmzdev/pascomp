
program ExprI;
var a:integer;
      b:boolean;
procedure pVal(i:integer); begin end;
begin
  b:=+a>-2;
  b:=succ(false) and true or a;{se espera un operador de tipo entero o subrango}
end.

