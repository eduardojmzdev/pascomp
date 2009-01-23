program Expr;
{asociatividad , op multi , * y /}
var a:integer;
begin
a:=4*2 div 2;
writeln(a); {4 y no 1}
a:=8 div 2 div 2;
writeln(a);{2 y no 1}
a:=4-2-2;
writeln(a); {0 y no 4}

end.