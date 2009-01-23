{testea:
op arimeticos:
- unario
- binario
* y /
op relacionales
<
>=
=
precedencia
* sobre -

fun y procediientos predefinidos
succ
readln
writeln

semtencias:
while
if then
if then - else
}

program divisores;
{valor absoluto}
var x,den:integer;
function abs(n:integer):integer;
begin
 if n<0 then
  abs:=-n
 else
  abs:=n
end;
{calcula el modulos del primer argumento con respecto
al segundo}
function mod(num:integer; den:integer):integer;
var numPos,denPos,divi:integer;
begin
 numPos:=abs(num);
 denPos:=abs(den);
 divi:=numPos div denPos;
 mod:=numPos- denPos * divi;
end;
begin
den:=1;
readln(x);    {lee x}
x:=abs(x);
while x>=den do  {escribe los divisores positivos de x}
 begin
 if mod(x,den)=0 then
  writeln(den);
 den:=succ(den);
 end;
end.
{12=1,2,3,4,6,12}
{1024=1,2,4,8,16,32,64,128,512,1024}