/**
 * Class to represent a vertex of a graph
 * @author - Priyanka Bura - 11-13-2016
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Vertex implements Index, Comparator<Vertex>, Iterable<Edge> {
    int name; // name of the vertex
    boolean seen; // flag to check if the vertex has already been visited
    int d, index;  Vertex p;  // fields used in algorithms of Prim and Dijkstra
    List<Edge> adj, revAdj; // adjacency list; use LinkedList or ArrayList

    /**
     * function to get the index of vertex
     */
    public int getIndex()
    { 
    	return this.index;
    }

    /**
     * function to update the index of vertex
     */
    public void putIndex(int i)
    { 
    	this.index = i;
    }

    /**
     * function to return the difference between the distance of given two vertices
     */
    public int compare(Vertex u, Vertex v) 
    { 
    	return u.d-v.d;
    	
    }

    /**
     * Constructor for the vertex
     * 
     * @param n
     *            : int - name of the vertex
     */
    Vertex(int n) {
	name = n;
	seen = false;
	d = Integer.MAX_VALUE;
	p = null;
	adj = new ArrayList<Edge>();
	revAdj = new ArrayList<Edge>();   /* only for directed graphs */
    }

    public Iterator<Edge> iterator() { return adj.iterator(); }

    /**
     * Method to represent a vertex by its name
     */
    public String toString() {
	return Integer.toString(name);
    }
}
