/**
 * Class to represent a graph
 *  @author rbk
 * @Modified - Priyanka Bura - 10-01-2016
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Graph implements Iterable<Vertex> 
{
    List<Vertex> v; // vertices of graph
    int size; // number of verices in the graph
    boolean directed;  // true if graph is directed, false otherwise
    HashMap<Edge,Integer> hashmap_graph = new HashMap<>();
    HashMap<Vertex, CircularList<Element>.Entry<Element>> hashmap_tour = new HashMap<>();
    /**
     * Constructor for Graph
     * 
     * @param size
     *            : int - number of vertices
     */
    Graph(int size) 
    {
		this.size = size;
		this.v = new ArrayList<>(size + 1);
		this.v.add(0, null);  // Vertex at index 0 is not used
		this.directed = false;  // default is undirected graph
		// create an array of Vertex objects
		for (int i = 1; i <= size; i++)
		    this.v.add(i, new Vertex(i));
    }

    /**
     * Find vertex no. n
     * @param n
     *           : int
     */
    Vertex getVertex(int n)
    {
    	return this.v.get(n);
    }
    
    /**
     * Method to add an edge to the graph
     * 
     * @param a
     *            : int - one end of edge
     * @param b
     *            : int - other end of edge
     * @param weight
     *            : int - the weight of the edge
     */
    void addEdge(Vertex from, Vertex to, int weight) 
    {
		Edge e = new Edge(from, to, weight);
		if(this.directed) {
		    from.adj.add(e);
	            to.revAdj.add(e);
		} else {
		    from.adj.add(e);
		    to.adj.add(e);
		}
		this.hashmap_graph.put(e, 0); //add all the edges to hashmap with value 0(marked as unchecked)
    }

    /**
     * Method to create iterator for vertices of graph
     */
    public Iterator<Vertex> iterator()
    {
		Iterator<Vertex> it = this.v.iterator();
		it.next();  // Index 0 is not used.  Skip it.
		return it;
    }

     // read a directed graph using the Scanner interface
    public static Graph readDirectedGraph(Scanner in) 
    {
    	return readGraph(in, true);
    }
    
    // read an undirected graph using the Scanner interface
    public static Graph readGraph(Scanner in)
    {
    	return readGraph(in, false);
    }
    /**
     * Method to read the graph from console input
     * 
     * @param in 
     *            : Scanner object
     * @param directed
     *            : boolean - to know if the graph is directed or not
     */
    public static Graph readGraph(Scanner in, boolean directed) 
    {	
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph
	
		// create a graph instance
		Graph g = new Graph(n);
		g.directed = directed;
		for (int i = 0; i < m; i++) {
		    int u = in.nextInt();
		    int v = in.nextInt();
		    int w = in.nextInt();
		    g.addEdge(g.getVertex(u), g.getVertex(v), w);
		}
		in.close();
		return g;
    }
    /**
     * Method to break the graph into various tours
     *
     */
    public List<CircularList<Element>> breakGraphIntoTours()
	{
        //declaration of queue, circular lists for each tour and list of tours
		List<CircularList<Element>> all_tours = new LinkedList<>();        
		Queue<Vertex> queue = new LinkedList<Vertex>();
		CircularList<Element> single_tour = new CircularList<>();
		
		//get the first vertex node and add to queue and hash-map
		Vertex from_vertex = this.getVertex(1);
		queue.add(from_vertex);
		hashmap_tour.put(from_vertex, single_tour.header);
		
		Iterator<Edge> edge_itr;
		Edge current_edge;
		Vertex to_vertex;
		
		/** loop invariants
		 * from_vertex - the vertex being processed
		 * to_vertex - other end of processed vertex
		 * current_edge - holds the edge(from_vertex to to_vertex)
		 * edge_itr - holds the current vertex edge iterator to traverse its edge adjacency list
		 * single_tour - holds the circular list of current tour being generated
		 * all_tours - holds the list of all generated circular lists(single tours)
		 * hashmap_stitch - hash-map(used for stitching) is added with the vertex to be processed as key and its corresponding edge as value
		 * queue - contains the list of vertices whose adjacency list is to be processed
		 */
		while(!queue.isEmpty())
		{
			//check if the current vertex's has unprocessed edges
			edge_itr = from_vertex.iterator();
			while(edge_itr.hasNext())
			{
				current_edge = edge_itr.next();
				if(!current_edge.visited)
				{
					current_edge.visited = true;
					
					single_tour.add(new Element(from_vertex, current_edge));
					to_vertex = current_edge.otherEnd(from_vertex);
					
					//check if a cycle is formed, if yes add the tour to all_tours list
					if(to_vertex == single_tour.header.next.element.vertex)
					{
						all_tours.add(single_tour);
						single_tour = new CircularList<Element>();
					}
					//if cycle is not yet formed, update queue and hash-map
					else
					{
						if(!hashmap_tour.containsKey(to_vertex))
							hashmap_tour.put(to_vertex, single_tour.tail);
						queue.add(to_vertex);
					}
					//update the current vertex and edge iterator
					from_vertex = to_vertex;
					edge_itr = from_vertex.iterator();
				}
			}
			from_vertex = queue.remove();
		}
		return all_tours;
	}
    /**
     * Method to stitch the tours 
     * 
     * @param listOfTours
     *            : list of tours we got after breaking the graph
     */
    public CircularList<Element> stitchTours(List<CircularList<Element>> listOfTours)
    {
    	CircularList<Element> final_list = new CircularList<>();
    	
    	/** loop invariants
    	 * final_list - holds the final tour after stitching the processed tours
    	 * current_list - holds the current tour from listOfTours that is being processed
    	 * head - holds the first vertex in the current list
    	 * element - holds the element of current vertex from the lits
    	 * 
    	 */
    	for(CircularList<Element> current_list:listOfTours)
    	{
    		//add the first list to final euler tour list if final_list is empty
    		if(final_list.size < 1)
    		{
    			final_list = current_list;
    		}
    		//if final_list is not empty stitch the next tour into final_list
    		else
    		{
    			//get the first vertex of the current tour
    			Vertex head = current_list.header.next.element.vertex;
    			//get the entry object of above vertex
    			CircularList<Element>.Entry<Element> element = hashmap_tour.get(head);
    			if(element != null)
    			{
    				//assign the above element to tail of current tour
    				current_list.tail.next = element.next; 
    				//assign the header of current tour to above element
    				element.next = current_list.header.next;
    			}
    		}
    	}
    	return final_list;
    }
    /**
     * Method to verify whether the given tour is a eulerian tour
     * 
     * @param tour
     *            : final tour after stitching
     */
    public boolean verifyTour(CircularList<Element> tour)
    {
    	boolean status = true;
    	//get the first entry of input tour
    	CircularList<Element>.Entry<Element> current_element = tour.header.next;
    	//get the edge of current element
		Edge current_edge = current_element.element.edge; 	
		
		/** loop invariants
		 * current_element - holds the current processing element object of given tour
		 * current_edge - holds the current processing edge of given tour
		 * status - boolean - holds the status whether the given tour is eulerian
		 */
		//loop until all the edges in hashmap(graph) are checked or the entire input tour is traversed
		while(this.hashmap_graph.containsValue(0) || current_edge != null)
    	{
			//check if the graph contains the edge(from input tour) and whether it is already checked
    		if(this.hashmap_graph.containsKey(current_edge) && this.hashmap_graph.get(current_edge) == 0)
    		{
    			//mark the edge as checked
    			this.hashmap_graph.replace(current_edge, 0, 1);
    			//traverse to next element in tour
    			current_element = current_element.next;
    			//check if the current element is null
    			if(current_element.element != null)
    				current_edge = current_element.element.edge; //get the edge from element object
    			else
    				current_edge = null;
    		}
    		else
    		{
    			status = false;
    			break;
    		}
    	}
       	return status;
    }
}
