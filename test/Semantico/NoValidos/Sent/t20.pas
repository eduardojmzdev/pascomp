{sentencia while}
program SentI;
const b=true;
      i=123;
var a :array[-2..10]of integer;
begin
 a[0]:= 123 div 3;
 write(a[0]); 
 while (pred(a[4])=pred(3)) or b do
   ;
 while succ(maxint) >pred(a[i]) do
  ;
 while succ(123 div 3) do{se espera una expresion booleana}
 ;
end.