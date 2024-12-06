/* new plans for event +winning(_,_) */

+winning(A,S)[source(leader)] : .my_name(A)
   <-  -winning(A,S);
       .print("I am the greatest!!!").

+winning(A,S)[source(leader)] : true
   <-  -winning(A,S).
