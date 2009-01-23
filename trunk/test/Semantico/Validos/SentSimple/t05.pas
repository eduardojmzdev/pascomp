program SentSimple;{asigancion , todos por ref}
var a,b:integer;
procedure p(var a:integer; var b:integer);
 begin
 a:=b;  {asig}
 b:=10000;{ tiene que afectar a b principal}
 end;

begin
 b:=14;
 p(a,b);
 writeln(a);  {14}
 writeln(b);{1000}
end.

