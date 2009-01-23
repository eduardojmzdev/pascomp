{
Objetivo: testear sent IF

Resultado esperado: Test Valido
}
program p;
begin
	if (d or c <4) then 
		if B = C then begin
			if(a)then b:=2 else c:=2;
			if(a)then b:=2 else c:=2;
			if(a)then b:=2 else c:=2;
		end
		else b:=3
	else 
		c:=3;
		
end.