+!print_hello
   <- makeArtifact(display, "display.GridDisplay",["Hello"],ArtId);
      for ( .range(I,1,13)) {
         !print(I, ArtId);
         .wait(700);
      } 
   .
+!print( 1,ArtId)  <- printChar(0,1,"H")[artifact_id(ArtId)].
+!print( 2,ArtId)  <- printChar(0,2,"e")[artifact_id(ArtId)].
+!print( 3,ArtId)  <- printChar(0,3,"l")[artifact_id(ArtId)].
+!print( 4,ArtId)  <- printChar(0,4,"l")[artifact_id(ArtId)].
+!print( 5,ArtId)  <- printChar(0,5,"o")[artifact_id(ArtId)].
+!print( 6,ArtId)  <- printChar(0,6," ")[artifact_id(ArtId)].
+!print( 7,ArtId)  <- printChar(1,1,"W")[artifact_id(ArtId)].
+!print( 8,ArtId)  <- printChar(1,2,"o")[artifact_id(ArtId)].
+!print( 9,ArtId)  <- printChar(1,3,"r")[artifact_id(ArtId)].
+!print(10,ArtId)  <- printChar(1,4,"l")[artifact_id(ArtId)].
+!print(11,ArtId)  <- printChar(1,5,"d")[artifact_id(ArtId)].
+!print(12,ArtId)  <- printChar(1,6,"!")[artifact_id(ArtId)].
