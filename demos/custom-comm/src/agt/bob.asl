!start(0).

+!start(X) <- .wait(1000+math.random*1000); .send(alice,tell,hello(X)); !start(X+1).
-!start(X)[error_msg(M)] <- .print("Error ",M,", but ok, I'll continue."); !start(X+1).
