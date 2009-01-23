program InvocI;
type Arr= array[1..10] of integer;
var r:Arr;
    i:integer;
    b:boolean;
procedure a(a: Arr); begin end;
procedure c(var a:boolean; var d:integer); begin end;

begin
 a(r);
 c(r[2], r); {incompatibilidad de tipos entre param actual  y formal}
 
end.