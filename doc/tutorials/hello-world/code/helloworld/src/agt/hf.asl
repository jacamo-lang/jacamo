!start.

+!start : message(X)
   <- for ( numMsg(_)[artifact_name(Name)] &
            focused(_,Name[artifact_type("display.GUIConsole")],ArtId) ) {
          printMsg(X)[artifact_id(ArtId)]
      }.
+!start : true       <- .print("hello world!").

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
