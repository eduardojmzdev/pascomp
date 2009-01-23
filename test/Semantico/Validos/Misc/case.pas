program p;
const
   A=-5;
   B=3;
   C=A;
var x,y:integer;
begin
x:=34;
y:=2;
case x+y of
 A:;;;
 B,5,C,6: begin write(x,y); end;
 C,36: writeln(x,y);
end;

end.