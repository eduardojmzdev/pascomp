program EjemploBucle3iteracionesMultiplicando;
var
x:integer;

begin
	x:=0;
	y:=3;
	while (x<3) do
	begin
		y:=y*y;
		x:=x+1;
	end;
	write(x);
end.