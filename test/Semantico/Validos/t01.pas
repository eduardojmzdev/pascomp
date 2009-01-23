program p;
type A = array[1..5] of integer;
var r: A;
procedure proc1(var x:A);
	procedure proc2(var y:A);
	var z:A;
	begin 
		z[1] := 5; z[2] := 6; z[3] := 7;		
		y := z;		
	end;
begin
	proc2(x);
	writeln(x[1],x[2],x[3]);	
end;
begin		
	proc1(r);	
	writeln(r[1],r[2],r[3]);	
end.