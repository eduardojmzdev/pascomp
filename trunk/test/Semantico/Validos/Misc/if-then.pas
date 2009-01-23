{objetivo del test:
 * Usar if-then con y sin else
 * Usar if-then con sentencia compuesta
}
program p;
var x,y:integer;
begin
x:=34;
y:=2;
if x<y then
begin
	y:= y +2;
	x:= (x * 2) div 0;
	writeln(x,y);
end
else begin
	y := x * x div 2;
	writeln(y,x);
end;
if not (x=y) then	
	if x <> y then 
		if x<y then
			if x<y then	
				writeln(x)
			else writeln(x);
writeln;
write(x,y);
end.