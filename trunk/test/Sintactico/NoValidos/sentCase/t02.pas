{El objetivo del test es probar el error de omitir 
el : en la etiqueta  de un case

Resultado esperado: Test Fallido
Error Sintactico en linea 14 : Se espera un :
}
program prog;

	const A = 34;
	  B = 55;
	
begin
 case t of 
 55 d:=6 
 end;
end.
