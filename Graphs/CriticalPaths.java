/**
 * Class to represent critical paths
 * @author - Priyanka Bura - 12-06-2016
 */

public class CriticalPaths 
{
	Graph inputgraph;
	
	/**
     * Constructor for CriticalPaths
     * 
     * @param g : input graph
     */
    CriticalPaths(Graph g)
    {
    	inputgraph = g;	
    	
    }

    /**
     * Function to find the list of critical paths
     * 
     */
    void findCriticalPaths()
    {
		int criticalnodes = 0;
		    	
		inputgraph.calculateEC();
		inputgraph.calculateLC();
    	
    	for(Vertex v:inputgraph)
    	{
    		v.seen = false;
    		v.cno = 0;
    		v.parent = null;
    	}
    	
    	//find critical path length
    	System.out.println(inputgraph.t.ec);
    	
    	//find a critical path from s to t
    	inputgraph.dfs();
    	System.out.println();
    	
    	
    	//get the number of vertices in critical paths
    	for(Vertex v:inputgraph)
    	{
    		if(v.lc == v.ec && v!=inputgraph.s && v!=inputgraph.t)
    		{
    			criticalnodes++;
    		}
    	}
    	
    	//array of critical paths
    	inputgraph.pathArray = new Vertex[criticalnodes+2];
    	
    	
    	System.out.println();
    	System.out.println("Task" + "	" + "EC" + "	" + "LC" + "	" + "Slack");
    	
    	//print the task its EC, LC and Slack values
    	for(Vertex v:inputgraph)
    	{
    		if(v!=inputgraph.s && v!=inputgraph.t)
    			System.out.println(v + "	" + v.ec + "	" + v.lc + "	" + v.slack);
    	}
    	
    	//print the number of nodes in a critical path
    	System.out.println();
    	System.out.println(criticalnodes);
    	
    	//calculate number of critical paths
    	inputgraph.calculateCriticalPaths();
    	System.out.println(inputgraph.t.criticalpaths);
    	
    	//print all critical paths
    	inputgraph.enumeratePaths(inputgraph.s,0);
    	System.out.println();
    	
    }
}
