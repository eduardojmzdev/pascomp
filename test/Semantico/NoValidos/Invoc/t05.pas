program InvocI;

procedure a; begin end;

procedure b(a: integer); begin end;

procedure c(a:boolean; d:integer); 
begin
end;

begin
	c(true);{nro de param. actuales no corresponde con el nr de param formales}
end.