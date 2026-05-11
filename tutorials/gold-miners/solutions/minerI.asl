/* new plans for event +gold(_,_) */

@pcell[atomic]           // atomic: so as not to handle another event until handle gold is initialised
+gold(X,Y)
   :  not carrying_gold & free
   <- -free;
      .print("Gold perceived: ",gold(X,Y));
      !init_handle(gold(X,Y)).

// if I see gold and I'm not free but also not carrying gold yet
// (I'm probably going towards one), abort handle(gold) and pick up
// this one which is nearer
@pcell2[atomic]
+gold(X,Y)
  :  not carrying_gold & not free &
     .desire(handle(gold(OldX,OldY))) &   // I desire to handle another gold which
     pos(AgX,AgY) &
     jia.dist(X,   Y,   AgX,AgY,DNewG) &
     jia.dist(OldX,OldY,AgX,AgY,DOldG) &
     DNewG < DOldG                        // is farther than the one just perceived
  <- .drop_desire(handle(gold(OldX,OldY)));
     .print("Giving up current gold ",gold(OldX,OldY)," to handle ",gold(X,Y)," which I am seeing!");
     !init_handle(gold(X,Y)).
