program SentSimple;
var a,b:integer;
procedure p(var i:integer);
 begin
 a:=i;
 end;

begin
 b:=13;
 p(b);
 writeln(a);  {13}
end.

