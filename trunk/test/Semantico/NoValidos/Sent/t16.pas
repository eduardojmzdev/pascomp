{Sentencia compuesta-Sentencia case}
program SentI;
const b=true;
      i=123;
var a :array[1..5] of boolean;
begin
 case a[4] of 
  2,3,4:;  {  tipo incompatible con la expresion case}
  3: write(succ(33));
 end;

 case pred(i) of
 end;

 
end.