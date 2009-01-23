program sent;
var x:integer;
begin
readln(x);   {para 3=3,2,1, para 0=false, para -1=true
                    para -2=nada}
if x>0 then
 while x>0 do
  begin
  writeln(x); {x,x-1,......1}
  x:=pred(x)
  end
else
 case x of
 0:write(x);
 -1:writeln(x-5);
 end;
end.