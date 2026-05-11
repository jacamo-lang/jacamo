
/* new initial beliefs */

last_dir(null). // the last movement I did
free.
score(0).


/* new plan for event +!handle(_) */

+!handle(gold(X,Y))
  :  not free
  <- .print("Handling ",gold(X,Y)," now.");
     !pos(X,Y);
     !ensure(pick,gold(X,Y));
     !pos(0,0);
     !ensure(drop, 0);
     .print("Finish handling ",gold(X,Y));
     ?score(S);
     -+score(S+1);
     !!choose_gold.
