{Parametros Actuales}
program InvocI;
type Arr=array[1..4]of integer;
var r:Arr;

procedure a; begin end;

procedure b(a: integer); begin end;

procedure c(a:boolean; d:integer);
begin 
end;

begin
b(r);{incompatibilidad tipo entre parametro actual y  formal}
end.