

/* new plans for event +free */

+free : gsize(_,W,H) & jia.random(RX,W-1) & jia.random(RY,H-1)
   <-  .print("I am going to go near (",RX,",", RY,")");
       !go_near(RX,RY).

+free  // gsize is unknown
   <- .wait(100); -+free.

