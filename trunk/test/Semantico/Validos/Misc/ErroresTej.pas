program IO;
var
i:integer;
begin
writeln(maxint);
i:=1;
while i<>0 do
begin
 readln(i); {error si i>maxint o i<(-maxint -1)  }
 {probado con
   2147483647:(maxint) Ok, no disp Error
   123:OK, no disp Error
   2147483646:OK no disp, Error
   -2147483647:(-maxint) OK, no disp Error
   -2147483648:(-maxint-1) OK, no disp Error
   2147483648:maxint+1, OK, DISP. ERROR
   -2147483649:(-maxint -2) OK, DISP ERROR
 }
 writeln(i);
end;
i:=1;
while i<>0 do
begin
 read(i);   {error si i>maxint o i<(-maxint -1)  }
 {idem redln}
 writeln(i);
end;
end.