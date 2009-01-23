program sent;
var a,b:integer;
begin
a:=3;
while a>=0 do
 begin
 b:=a;
 while 0<=b do
  begin
   write(b); {3210210100}
   b:=b-1;
  end;
 a:=a-1;
 end;

end.