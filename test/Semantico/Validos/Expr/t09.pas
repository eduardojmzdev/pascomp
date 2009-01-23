program Expr;
{booleanas ,con todos los operadores or, and y not}
var a:boolean;
begin
a:= false or not false and true ;
writeln(1); {1}
a:= not true or false ;
writeln(2);{2}
a:=false and false or true;
writeln(3);{2 }

end.