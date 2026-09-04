/* new plan for event +!handle(_) */

+!handle(gold(X,Y))
  :  not free
  <- .print("Handling ",gold(X,Y)," now.");
     !pos(X,Y);
     !ensure(pick,gold(X,Y));
     ?depot(_,DX,DY);
     !pos(DX,DY);
     !ensure(drop, 0);
     .print("Finish handling ",gold(X,Y));
     ?score(S);
     -+score(S+1);
     .send(leader,tell,dropped);
     !choose_gold.
