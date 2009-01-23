program fact;
function f(n:integer):integer;
 begin
 if n<=1 then
    f:=1
 else
    f:=n*f(n-1);
 end;
begin
writeln(f(10)); {3628800}
end.