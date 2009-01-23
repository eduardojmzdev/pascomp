{objetivo del test:
  * usar variables globales
  * usar recursion y pasaje de parametros por  referencia de arreglos
  }
program dPyFI;

type
 A=array [-5..3] of integer;
var
  b:integer;
  c:A;
  i:integer;
 function fun(var y:A):integer;
    procedure p2(var x:A);
	begin
		x[1] := 5;
		x[2] := 5;
		x[3] := 5;
	end;
 begin
	i := 1;
	while(i<=3)do begin
		y[i] := i;
		write(y[i]);		
		i:= i+1;		
	end;
	writeln;
	p2(y);
	write(y[1],y[2],y[3]);
	writeln;
	fun := i;
 end;
 
 function fibo(n:integer):integer;
 begin
	if (n=1) or (n= 0) then
	    fibo := 1
	else
		fibo := fibo(n-1)+fibo(n-2);
 end;
 
 function fact(n:integer):integer;
 begin
	if (n=1) or (n= 0) then
	    fact := 1
	else
		fact := fact(n-1)* n;
 end;
begin
 c[1] := 0;
 c[2] := c[1];
 c[3]:= c[2];
 b:= fun(c);
 write(c[1],c[2],c[3]);
 writeln;
 writeln;
 write(fact(pred(pred(succ(succ(c[1])+1)+1))));
 writeln;
 writeln;
 write(fibo(5));
end.