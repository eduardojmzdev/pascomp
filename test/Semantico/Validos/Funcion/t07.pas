program funcion;
function fac1(n:integer):integer;
  function fac2(n:integer):integer;
  begin
  if n<=1 then
   fac2:=1
  else
   fac2:=n*fac1(n-1);
  end;
begin
 if n<=1 then
  fac1:=1
 else
  fac1:=n*fac2(n-1);
end;

begin
writeln(fac1(5));{120}
end.