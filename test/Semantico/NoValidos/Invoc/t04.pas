program InvocI;

procedure a; begin end;

procedure b(a: integer); begin end;

procedure c(a:boolean; d:integer); 
begin
end;

begin
	c(true,false; {incompatibilidad de tipo entre parametro formal y actual}
end.