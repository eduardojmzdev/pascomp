program sentV;
const diez=10;

var x:integer;
begin
x:=0;{tiene que escribir 1,2,3,4,5,6,7,8,9,10,11,12}
while x<=10 do
begin
 case x of
 1:writeln(x);
 2:writeln(x);
 3:writeln(x);
 4:writeln(x);
 5:writeln(x);
 6,7,8,9:writeln(x);
 diez: while x<13 do
       begin
       writeln(x);
       x:=succ(x);
       end;

 end;
 x:=x+1;
end;
end.