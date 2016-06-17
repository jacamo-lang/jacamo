// The code for all agents in the application

/* Initial beliefs and rules */

// Organisation Specification structuring and regulating the Request for tender process
// my_org_spec("./src/rftmngt.xml").
my_org_spec("./src/rftmngtnew.xml").
// Workspace for the management of the organisation created from the OS
my_org_wsp("rftOrgMngtWsp").
// Workspace for the management of RFTs, where the rft_board is created
my_rft_wsp("rftWsp").
// the singleton artifact for the announcement of the request for tender existing in the system
my_rft_board("rft_board").
// the singleton artifact use to store the name of bad tenders
my_rft_bad_list("rft_bad_list").
// the name of global group including all subgroups in the rft organization
my_org_name("rftGr").
//cartago_main("192.168.0.24").
cartago_main("localhost").
