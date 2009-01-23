program fact;
var n:integer;
function f(var n:integer):integer;
 var pN:integer;
 begin
 pN:=n-1;
 if n<=1 then
    f:=1
 else
    f:=n*f(pN);
 end;
begin
n:=10;
writeln(f(n)); {3628800}
end.