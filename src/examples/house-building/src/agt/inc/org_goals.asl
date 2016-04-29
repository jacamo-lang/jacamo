// plan to execute organisational goals

+!site_prepared      // the goal (introduced by the organisational obligation) 
   <- prepareSite. // simulation of the action (in GUI artifact)

+!floors_laid                   <- layFloors.
+!walls_built                   <- buildWalls.
+!roof_built                    <- buildRoof.
+!windows_fitted                <- fitWindows.
+!doors_fitted                  <- fitDoors.
+!electrical_system_installed   <- installElectricalSystem.
+!plumbing_installed            <- installPlumbing.
+!exterior_painted              <- paintExterior.
+!interior_painted              <- paintInterior.

/* // generic plan for all goals related to schemes (replaced by the above plans)
+!G[scheme(S)] 
   <- .print(" ---> working to achieve ",G);
      G; // simulates the action (in GUI artifact)
	  .print(" <--- done.").
*/
