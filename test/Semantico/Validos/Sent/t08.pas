program sent;
var x:integer;
begin
 x:=-2;
 case x of
 1,2,3,4,5,6,7,8,9:write(x);{no entra}
 end;
 case x *( -2) of
  1,2,3,4,5,6,7,8,9:write(x+1);  {entra}
 end;
end.










