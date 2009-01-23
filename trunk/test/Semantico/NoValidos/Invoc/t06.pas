{Parametros Actuales}

program InvocI;
type Arr= array[1..10] of integer;
var r:Arr;
    i:integer;
    b:boolean;

procedure a(a: Arr); begin end;
procedure c(a:boolean; var d:integer); begin end;

begin
  a(r);
  c(true, r[4]);
  c(false, 2); {no se permite pasar por referencia un valor entero o boolean}
end.