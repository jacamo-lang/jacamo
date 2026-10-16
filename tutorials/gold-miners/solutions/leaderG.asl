/* new plans for event +dropped */

+dropped[source(A)] : score(A,S) & winning(L,SL) & S+1>SL
   <- -score(A,S);
      +score(A,S+1);
      -dropped[source(A)];
      -+winning(A,S+1);
      .print("Agent ",A," is winning with ",S+1," pieces of gold");
      .broadcast(tell,winning(A,S+1)).

+dropped[source(A)] : score(A,S)
   <- -score(A,S);
      +score(A,S+1);
      -dropped[source(A)];
      .print("Agent ",A," has dropped ",S+1," pieces of gold").
