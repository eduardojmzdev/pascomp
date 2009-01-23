program ExprI;

var a:integer;
    arr : array[1..9] of integer;
    b:boolean;

procedure pVal(i:integer); begin end;

begin
	b:= a>2;
	b:= a=arr[3];
	b:= a>arr;{los operadores relacionales aplican a tipos simples}
end.

end.{no importa se corta antes}