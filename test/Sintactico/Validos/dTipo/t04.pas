{
El proposito de este test es  poner mas de una def de tipo

Resultado esperado: Test Valido
}
program p;
const li = -5;
	  ls = +5;
type
	x = integer;
	T = boolean;
	v = li..ls;
	B = -5..-1;
	A = array[li..ls]of T;
var y:x;
z:A;
w:B;
i:integer;
function fact(n:x):x;
begin
   if (n = 0) or (n=1) then
	fact := 1
   else
    fact:= fact(n-1) * n;
end;
begin
	{writeln(3, 4);
	w:= -2;
	z[-4]:=true;
	y := 8;
	writeln(succ(succ(y)) + pred(y));
	i := li;
	while(i<=ls) do begin
		z[i]:= false;
		i:=i+1;
	end;
	z[-4] := z[-2] = z[3];
	if ( not z[-4]) then
		writeln(y+1, 3+5+4*5)
	else
		write(y);
		}
	i:= 17;
	fact(i);
end.

