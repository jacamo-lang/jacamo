!start.

+!start : message(X)
   <- for ( numMsg(_)[artifact_name(Name)] &
            focused(_,Name[artifact_type("display.GUIConsole")],ArtId) ) {
          printMsg(X)[artifact_id(ArtId)]
      }.
+!start : true       <- .print("hello world!").

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
{ include("$moise/asl/org-obedient.asl") }
