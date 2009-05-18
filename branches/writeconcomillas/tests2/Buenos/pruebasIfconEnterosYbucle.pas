program pruebasIfconEnterosYbucle;
var
x,y,z:integer;

begin
	x:=1;
	y:=5;
	z:=0;
	WHILE (z<5) DO
		BEGIN
		if (z > 2) then
			BEGIN
			write(x);
			END;
		else
			BEGIN
			write(y);
			END;
		z:=z+1;
		END;
end.