{Sentencia Compuesta-Sentenci if}
program SentI;
var a :integer;       
begin
if true or false then
 write(3);
if a>=4 then
 write(a);
if a + maxint then   {se espera una expresion booleana}
 write(a);
end.