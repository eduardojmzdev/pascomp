program InvocI;
type Arr= array[1..10] of integer;
var r:Arr;
    i:integer;
    b:boolean;

procedure a(a: Arr); begin end;
procedure c(var a:boolean; var d:integer); begin end;

begin
 a(r);
 c(b, r[4]);
 c(b, 2); {no se permite pasar por referencia un valor numerico o boolean}
end.