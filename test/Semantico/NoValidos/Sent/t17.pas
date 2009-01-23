program SentI;
const b=true;
      i=123;
var a : integer;
begin
 case a of
  2,3,true:; {imcompatibilidad en la lista de elementos case}
  3: write(succ(3));
 end;
 
 case pred(a) of
 end;

end.