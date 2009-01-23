{Objetivo: testear bloque

Resultado esperado: Test Valido
}
program prog;

begin
 a:=3;
 begin
	c:=3;
	f(x);
 end;
 begin
	f(x);
	c:= f(f(x)+5);
 end
end.