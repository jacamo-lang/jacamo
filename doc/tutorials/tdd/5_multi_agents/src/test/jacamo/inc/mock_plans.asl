+!add_mock_plans_room_agent
    <-
    /**
     * Add mock plans to do not call the artifact.
     * It produces a mocked answer. The belief status(X) 
     * is being used to assert whether is is correct
     */
    .add_plan({ 
    +!temperature(T): 
        now_is_warmer_than(T) &
        temperature(C)
        <-  
        if (not status(cooling)) {
            /*Mock removing the external action startCooling;*/
            -+status(cooling);
            .log(warning,C," is too hot -> cooling until ",T);
        }
        !temperature(T);
    }, self, begin);

    .add_plan({ 
    +!temperature(T): 
        now_is_colder_than(T) &
        temperature(C)
        <-  
        if (not status(heating)) {
            /*Mock removing the external action startHeating;*/
            -+status(heating);
            .log(warning,C," is too hot -> cooling until ",T);
        }
        !temperature(T);
    }, self, begin);
    
    .add_plan({ 
    +!temperature(T):
        temperature_in_range(T)
        <-  
        if (not status(idle)) {
            /*Mock removing the external action stopAirConditioner;*/
            -+status(idle);
            .log(warning,"Temperature achieved: ",T);
        }
        !temperature(T);
    }, self, begin);

    .add_plan({ 
    +!add_preference(T)[source(S)]
        <-
        .abolish(preference(S,_));
        +preference(S,T);
        .findall(X,preference(_,X),L);
        /*Mock temperature with the average*/
        +temperature(math.average(L));
    }, self, begin);
.