/**
 * Class to represent a vertex of a graph
 * @author - Priyanka Bura - 12-06-2016
 *
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Vertex implements Iterable<Edge> {
    int name; // name of the vertex
    boolean seen; // flag to check if the vertex has already been visited
    int d;  // duration of task corresponding to vertex
    List<Edge> adj, revAdj; // adjacency list; use LinkedList or ArrayList
    int indegree = 0; // number of incoming edges  
    int toporder; 
    int cno = 0; //component number
    int lc = 0,ec = 0, slack; //latest completion time, expected completion time and slack of tasks
    Vertex parent; //parent vertex
    boolean isCritical; //check whether a vertex is critical
    int criticalpaths; //enumerated critical paths
    
    /**
     * Constructor for the vertex
     * 
     * @param n: int - name of the vertex
     */
    Vertex(int n) 
    {
    	//initialize all the vertex parameters
		name = n;
		seen = false;
		d = 0;
		isCritical = false;
		adj = new ArrayList<Edge>();
		revAdj = new ArrayList<Edge>();   /* only for directed graphs */
    }

    /**
     * Function to return edge iterator
     * 
     */
    public Iterator<Edge> iterator()
    { 
    	return adj.iterator(); 
    }

    /**
     * Method to represent a vertex by its name
     */
    public String toString() 
    {
    	return Integer.toString(name);
    }
}
