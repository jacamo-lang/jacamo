extra(150).

!start.

+!start <- +vl(20). // by adding this belief, the NPL will trigger an obligation

+unfulfilled(O) <- .print("Unfulfilled ",O).

+sanction(Ag,remove_from_systems)
   <- .println("**** I am implementing the sanction for ",Ag," ****").

+sanction(Ag,Sanction)[norm(NormId,Event)]
   <- .print("Sanction ",Sanction," for ",Ag," created from norm ", NormId, " that is ",Event).
