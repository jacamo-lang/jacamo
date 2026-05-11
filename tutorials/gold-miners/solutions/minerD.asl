
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
     !choose_gold.


/* new plan for event +!ensure(drop,_) */

+!ensure(drop, _) : carrying_gold & pos(X,Y) & depot(_,X,Y)
  <- drop.
