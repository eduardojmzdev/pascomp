{El objetivo del test es probar el error de omitirel id de etiqueta
en la lista de etiquetas de un case

Resultado esperado: Test Fallido
Error Sintactico en linea 12 : Se espera un numero o identificador
de constante 
}
program prog;

begin
 case 6+3 of 
 g,9,: y:=h
 end;
end.
