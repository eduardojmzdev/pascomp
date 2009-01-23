{parametros actuales}
program InvocI;

type Arr=array[boolean]of boolean;
var r:Arr;

procedure a; begin end;

procedure b(a: integer); begin end;

procedure c(a:boolean; d:integer);
begin
end;

begin
	b(123,123);{el nro de param actuales no corresponde con el nro de param. formales}
end.