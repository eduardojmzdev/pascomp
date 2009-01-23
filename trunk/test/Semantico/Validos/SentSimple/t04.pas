program SentSimple;{asigancion , pas mixto}
var a,b:integer;
procedure p(var a:integer; b:integer);
 begin
 a:=b;  {asig}
 b:=10000;{no tiene que afectar a b principal}
 end;

begin
 b:=14;
 p(a,b);
 writeln(a);  {14}
 writeln(b);{14}
end.

