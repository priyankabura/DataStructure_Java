/**
 * Class to represent a vertex of a graph
 * @author rbk
 * @Modified - Priyanka Bura - 10-01-2016
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Vertex implements Iterable<Edge>{
    int name; // name of the vertex
    boolean seen; // flag to check if the vertex has already been visited
    int distance; // distance of vertex from a source
    List<Edge> adj, revAdj; // adjacency list; use LinkedList or ArrayList
    Iterator<Edge> edge_iterator;
    
    /**
     * Constructor for the vertex
     * 
     * @param n
     *            : int - name of the vertex
     */
    Vertex(int n) {
	name = n;
	seen = false;
	distance = Integer.MAX_VALUE;
	adj = new ArrayList<Edge>();
	revAdj = new ArrayList<Edge>();   /* only for directed graphs */
	edge_iterator = null;
    }

    /**
     * Method to represent a vertex by its name
     */
    public String toString() {
    return Integer.toString(name);
    }
    
	public Iterator<Edge> iterator()
	{
		if(edge_iterator == null)
			edge_iterator = this.adj.iterator();
		return edge_iterator;
	}
    
}
