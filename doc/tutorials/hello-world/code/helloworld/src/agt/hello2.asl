msg(fr,"bonjour").
msg(br,"bom dia").
msg(it,"Buon giorno").
msg(us,"Good morning").

!start.

+!start : country(C) & msg(C,M) <- printMsg(M).

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
{ include("$moise/asl/org-obedient.asl") }
