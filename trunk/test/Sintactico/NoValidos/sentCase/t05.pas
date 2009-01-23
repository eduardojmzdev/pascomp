{El objetivo del test es probar el error de omitir la 
expresion  de un case

Resultado esperado: Test Fallido
Error Sintactico en linea  13: Se espera una expresion
}
program prog;

	const A = 34;
	  B = 55;
	
begin
 case  of 
 g: y:=h
 end;
end.
