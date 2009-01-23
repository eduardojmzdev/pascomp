program SentI;
const b=true;
      i=123;
var a :integer;
begin
 case a of
  b,i:; {imcompatibilidad en la lista de lementos case}
  3: write(succ(3));
 end;
 case pred(a) of
 end;

end.