+!print_hello
   <- makeArtifact(display, "display.GridDisplay",["Hello!"],ArtId);
      !print_h(ArtId);
      !print_e(ArtId);
      !print_l1(ArtId);
      !print_l2(ArtId);
      !print_o1(ArtId);
      !print_spc(ArtId);
      !print_w(ArtId);
      !print_o2(ArtId);
      !print_r(ArtId);
      !print_l3(ArtId);
      !print_d(ArtId);
      !print_excl(ArtId);
   .
+!print_h(ArtId)    <- printChar(0,1,"H")[artifact_id(ArtId)]; .wait(700).
+!print_e (ArtId)   <- printChar(0,2,"e")[artifact_id(ArtId)]; .wait(700).
+!print_l1(ArtId)   <- printChar(0,3,"l")[artifact_id(ArtId)]; .wait(700).
+!print_l2(ArtId)   <- printChar(0,4,"l")[artifact_id(ArtId)]; .wait(700).
+!print_o1(ArtId)   <- printChar(0,5,"o")[artifact_id(ArtId)]; .wait(700).
+!print_spc(ArtId)  <- printChar(0,6," ")[artifact_id(ArtId)]; .wait(700).
+!print_w(ArtId)    <- printChar(1,1,"W")[artifact_id(ArtId)]; .wait(700).
+!print_o2(ArtId)   <- printChar(1,2,"o")[artifact_id(ArtId)]; .wait(700).
+!print_r(ArtId)    <- printChar(1,3,"r")[artifact_id(ArtId)]; .wait(700).
+!print_l3(ArtId)   <- printChar(1,4,"l")[artifact_id(ArtId)]; .wait(700).
+!print_d(ArtId)    <- printChar(1,5,"d")[artifact_id(ArtId)]; .wait(700).
+!print_excl(ArtId) <- printChar(1,6,"!")[artifact_id(ArtId)]; .wait(700).
