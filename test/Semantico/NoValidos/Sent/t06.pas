program SentI;
var
a:array[1..3] of boolean;
b:array[-5..-2] of boolean;

c:boolean;

begin
  
  a[1]:= c;
  c:=b[-2];
  a:=b;    {imcompatibilidad de tipos en asignacion}
end.