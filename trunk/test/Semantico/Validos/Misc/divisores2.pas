{testea:
nivel de anidamiento:2

accseo a entidades:
-acceso a parametros fornales por valor  y referencia
-acc. a contante global desde n.l 2
-modificadocion de paramtro por ref

op arimeticos:
- unario
*
/
op booleanos
not
op relacionales
<
>
<>


precedencia
* sonbre -

fun y procediientos predefinidos
pred(integer)
readln
writeln

semtencias:
vacia (en el then y en el esle)
while
if then
if then - else
}

program divisores;

{CONSTANTES}
const  Menos1=-1;

{VARIABLES}
var x,den:integer;

{SUBPROGRAMAS]
{calcula el modulo del primer argumento con respecto
al segundo }
function mod(var num:integer; var den:integer):integer;
 {SUBPROGRAMAS LOCALES}
 procedure calAbs; {calcula el val absoluto de num y den}
 begin {calAbs}
  if not(num>0) then num:=-num   else   ;
  if den>0 then {sentencia vacia}else den:=Menos1*den;
 end; {calcAbs}
 {SENTECIAS}
 begin { mod}
  calAbs;
  mod:=num - den * (num div den);
 end; {mod}

{SENTENCIAS PRINCIPAL}
begin
readln(x);    {lee x}
den:=x;
while (den<>0) do  {escribe los divisores positivos de x}
 begin
 if mod(x,den)=0 then
  writeln(den);
 den:=pred(den);
 end;
writeln(x);{debe escribir el valor absoluto}
end.
{12=1,2,3,4,6,12 en orden inverso}
{1024=1,2,4,8,16,32,64,128,512,1024 en orden inverso}