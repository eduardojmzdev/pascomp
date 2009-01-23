
program ExprI;

var a:integer;
    b:boolean;
	c:array[1..3] of 1..100;
procedure pVal(i:integer); begin end;
begin
 b:=+a>-2;
 b:= c or b;{tipos incompatibles en expresion}
end.

end.{no importa se corta antes}