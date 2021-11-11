!start.

+!start
   <- .print("hello world.");
      // focus on the institution artifact just to print some messages, it is not necessary for this agent task
      joinWorkspace("/main/i1",_);
      lookupArtifact("i1_art", InstArt);
      focus(InstArt);

      inc; // creates the brute fact "count(a,11)" (from artifact a)
           // this brute fact count as c(a)
           // c(a) is the trigger for norm (in org.xml):
           //     norm npl1: c(a) -> obligation(bob,true,g(X),`now`+`1 day`).
           // so that moise creates an obligation for bob, fulfilled by the next plan
   .

+obligation(A,R,G,D) : .my_name(A)
   <- .print("I am obliged to ",G," doing inc to fulfill");
      inc; // this inc will increatse the counter that count-as filfilling the norm
           // i.e. the brute fact count(a,12) count as g(12) that fulfills the obligation
   .

//+sai_is(BruteFact,InstFact) <- .print(BruteFact," in counting as ",InstFact).
//+state_sf(StateSF) <- .print("new state status function constituted: ",StateSF).
+oblFulfilled(O) <- .print(O).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
