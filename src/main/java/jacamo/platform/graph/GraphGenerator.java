package jacamo.platform.graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class mainly generates a graphviz file with environment view, it shows all current created artifacts
 * its observable properties and operations lining with observing agents and linked artifacts
 * 
 * @author cleberjamaral
 */
public class GraphGenerator {

    private Map<String, Collection<GraphNode>> nodesByWorkspace = new HashMap<>();
    private List<String> observingAgents = new ArrayList<>();
    private String workspaceFilter; // if empty, filtering is off
    private static PrintWriter out;

    public GraphGenerator (String wsfilter) {
        this.workspaceFilter = wsfilter;
    }
    
    public static void main(String[] args) throws Exception {
        String filename;
        if (args.length != 1) {
            filename = "graph.gv";
        } else {
            filename = args[0];
        }
        String graph = new GraphGenerator("").generateGraph();
        
        FileWriter fw = new FileWriter(filename, false);
        BufferedWriter bw = new BufferedWriter(fw);
        out = new PrintWriter(bw);
        out.println(graph);
        out.flush();
        out.close();
    }
    
    /**
     * Add a new agent that is observing some artifact (if it is not included yet)
     * @param observingAgent name of the observing agent
     */
    public void addNewObservingAgent(String observingAgent) {
        if (!this.observingAgents.contains(observingAgent))
            this.observingAgents.add(observingAgent);
    }

    /**
     * Add a graph node grouped by workspaces
     * @param workspace were the artifact is placed
     * @param item the graphnode
     */
    public void addNode(String workspace, GraphNode item) {
        Collection<GraphNode> values = nodesByWorkspace.get(workspace);
        if (values == null) {
            values = new ArrayList<>();
            nodesByWorkspace.put(workspace, values);
        }
        values.add(item);
    }

    /**
     * Generate a graphviz creating a file called "graph.gv" where the 
     * application is running
     */
    public String generateGraph() {
        StringBuilder sb = new StringBuilder();

        sb.append("digraph G {\n");
        sb.append("graph [\n");
        sb.append("rankdir = \"LR\"\n");
        sb.append("]\n");

        int clusterCounter = 0;
        for (Entry<String, Collection<GraphNode>> entry : nodesByWorkspace.entrySet()) {
            
            // do not add info from other workspaces
            if (!workspaceFilter.equals("") && !workspaceFilter.equals(entry.getKey()))
                break;
            
            sb.append("\tsubgraph cluster_" + clusterCounter + " {\n");
            sb.append("\t\tlabel=\"" + entry.getKey() + "\"\n");

            entry.getValue().forEach(x -> {
                if (x.getType().equals("cartago.WorkspaceArtifact")) ; // do not print system artifacts
                else if (x.getType().equals("cartago.tools.Console")) ;
                else if (x.getType().equals("cartago.ManRepoArtifact")) ;
                else if (x.getType().equals("cartago.tools.TupleSpace")) ;
                else if (x.getType().equals("cartago.NodeArtifact")) ;
                else if (x.getType().equals("cartago.AgentBodyArtifact")) {
                    sb.append("\t\t\"" + x.getName() + "\" [ " + "\n\t\t\tlabel = \"" + x.getName() + "\"\n");
                    sb.append("\t\t\tshape = \"box\" color=\"gray\" style=\"filled\"\n");
                    sb.append("\t\t];\n");
                } else {
                    // artifact name on first list item <f0> then artifact type on list item <f1>
                    sb.append("\t\t\"" + x.getName() + "\" [ " + "\n\t\t\tlabel = \"<f0> " + x.getName());
					sb.append("| <f1> " + x.getType());

					// print observable properties on third list item <f2>
					sb.append("| <f2> ");
					x.getObservableProperties().forEach(y -> sb.append(y + "\\n"));

					// print operations on forth list item <f3>
					sb.append("| <f3> ");
					x.getOperations().forEach(y -> sb.append(y + "\\n"));

					sb.append("\"\n");
                    sb.append("\t\t\tshape = \"record\"\n");
                    sb.append("\t\t];\n");
                }

                x.getObservingAgents().forEach(
                        y -> sb.append("\t\t\"" + y + "\" -> \"" + x.getName() + "\" [label=\"observes\",color=\"blue\"];\n"));
                x.getLinkedArtifacts().forEach(
                        y -> sb.append("\t\t\"" + x.getName() + "\" -> \"" + y + "\" [label=\"link\",color=\"purple\"];\n"));
            });

            sb.append("\t}\n");
            clusterCounter++;
        }

        sb.append("}\n");
        
        return sb.toString();
    }
}
