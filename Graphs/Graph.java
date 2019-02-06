/**
 * Class to represent a graph
 *  @author - Priyanka Bura - 12-06-2016
 *
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Graph implements Iterable<Vertex> {
    List<Vertex> v; // vertices of graph
    int size; // number of verices in the graph
    boolean directed;  // true if graph is directed, false otherwise
    Vertex s,t; //source and target vertices
    List<Vertex> indegreetoplist, dfstoplist; //lists of vertices in toplogical order
    Vertex[] pathArray; //array of critical paths
    
    /**
     * Constructor for Graph
     * 
     * @param size
     *            : int - number of vertices
     */
    Graph(int size) 
    {
    	//set the graph size and create a vertex array
		this.size = size;
		this.v = new ArrayList<>(size + 1);
		this.v.add(0, null);  // Vertex at index 0 is not used
		this.directed = false;  // default is undirected graph
		// create an array of Vertex objects
		for (int i = 1; i <= size; i++)
		    this.v.add(i, new Vertex(i));
    }

    /**
     * Get the vertex at index n
     * @param n : int - index of vertex
     * 
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
		if(this.directed) 
		{
		    from.adj.add(e);
	            to.revAdj.add(e);
		}
		else
		{
		    from.adj.add(e);
		    to.adj.add(e);
		}
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

    /**
     * Function to run BFS from a given source
     * 
     * @param n: int - name of the vertex
     * Precondition: nodes have already been marked unseen
     */ 
    public void bfs(Vertex src) 
    {
		src.seen = true;
		Queue<Vertex> q = new LinkedList<>();
		q.add(src);
		while(!q.isEmpty())
		{
		    Vertex u = q.remove();
		    for(Edge e: u.adj)
		    {
				Vertex v = e.otherEnd(u);
				//only visit tight edges
				if(!v.seen && e.isTight && v!=t) 
				{
				    v.seen = true;
				    q.add(v);
				    System.out.print(v+",");
				}
		    }
		}
    }

    /**
     * Function to run DFS
     * 
     */ 
    public void dfs()
    {
    	//initialize all vertices to default values
    	for(Vertex v:this)
    	{
    		v.seen = false;
    		v.parent = null;
    	}
    	DFSVisit(s); //start from source vertex
    }
    
    /**
     * Function to run DFSVisit from given vertex 
     * @param u : Vertex
     */ 
    public void DFSVisit(Vertex u)
    {
    	u.seen = true;
    	if(u!=s && u!=t)
    		System.out.print(u + " ");
    	
    	Vertex v;
    	//iterate through the other unseen tight edges
    	for(Edge e: u.adj)
    	{
    		v = e.otherEnd(u);
    		if(!v.seen && e.isTight)
    		{
    			v.parent = u;
    			DFSVisit(v);
    			break;
    		}
    	}
    }
    
    /**
     * Function to find the topological order using indegree property 
     * 
     */ 
    public List<Vertex> findIndegreeTopologicalOrder()
    {
    	List<Vertex> toplist = new LinkedList<>();
    	
    	//set the indegree property of each vertex
    	for(Vertex v:this)
    	{
    		v.indegree = v.revAdj.size();
    	}
    	
    	Queue<Vertex> q = new LinkedList<>();
    	int count = 0;
    	
    	//set the source and target incident edges
    	for(Vertex u:this)
    	{
    		if(u.indegree == 0 && u!=s && u!=t)
    		{
    			this.addEdge(s, u, 1);
    			++u.indegree;
    		}
    		
    		if(u.adj.isEmpty() && u!=s && u!=t)
    		{
    			this.addEdge(u, t, 1);
    			++t.indegree;
    		}
    	}
    	
    	//add starting vertices to queue
    	for(Vertex u:this)
    	{
    		if(u.indegree == 0)
    			q.add(u);
    	}
    	
    	//loop until the queue is empty
    	//current - holds the vertex being processed
    	//toplist - list of vertices in topological order
    	//otherend - other end of edge incident on current vertex
    	while(!q.isEmpty())
    	{
    		Vertex current = q.remove();
    		toplist.add(current);
    		current.toporder = ++count;
    		Vertex otherEnd;
  
    		for(Edge e: current.adj)
    		{
    			otherEnd = e.otherEnd(current);
    			//if the indegree of a vertex becomes 0 add it to queue
    			if(--otherEnd.indegree == 0)
    			{
    				q.add(otherEnd);
    			}
    		}
    	}
    	
    	//if all the vertices in the graph are not processed then it is not a DAG
    	if(count != size)
    	{
    		System.out.println("Given graph is not a DAG");
    	}
    	return toplist;
    }
    
    /**
     * Function to find the topological order using DFS 
     * 
     */ 
    public List<Vertex> findDFSTopologicalOrder()
    {
    	List<Vertex> toplist = new LinkedList<>();
    	
    	//initialization
    	for(Vertex v:this)
    	{
    		v.seen = false;
    		v.cno = 0;
    		v.parent = null;
    	}
    	
    	int component = 0;
    	
    	//visit all the unseen vertices
    	for(Vertex v:this)
    	{
    		if(!v.seen)
    		{
    			v.cno = component++;
    			DFSVisit(v, toplist);
    		}
    	}
    	return toplist;
    }
    
    /**
     * Function to perform DFSVisit
     * @param u - Starting vertex
     * @param toplist - list of vertices in topological order
     */ 
    public void DFSVisit(Vertex u, List<Vertex> toplist)
    {
    	u.seen = true;
    	Vertex v;
    	
    	//loop invariants - v : other end of edge incident on current vertex
    	for(Edge e: u.adj)
    	{
    		v = e.otherEnd(u);
    		if(!v.seen)
    		{
    			v.cno =  u.cno;
    			v.parent = u;
    			DFSVisit(v,toplist); //recursively visit all the outgoing edge vertices
    		}
    	}
    	toplist.add(u);
    }
    
    /**
     * Function to calculation of Estimated Completion(EC) Time
     * 
     */ 
    public void calculateEC()
    {
    	//initialization
    	for(Vertex v:this)
    	{
    		v.ec = 0;
    	}
    	s.ec = 0;
    	
    	//get the list of vertices in topological order
    	indegreetoplist = findIndegreeTopologicalOrder();
    	Iterator<Vertex> itr = indegreetoplist.iterator();
    	Vertex current, otherend;
    	
    	//iterate through each vertex and update its EC
    	while(itr.hasNext())
    	{
    		current = itr.next();
    		for(Edge e:current.adj)
    		{
    			otherend = e.otherEnd(current);
    			otherend.ec = Math.max(otherend.ec, current.ec+otherend.d);
    		}
    	}
    	
    }
    
    /**
     * Function to calculation of Latest Completion(LC) Time
     * 
     */ 
    public void calculateLC()
    {
    	t.lc = t.ec;
    	
    	//initialization
    	for(Vertex  v:this)
    	{
    		v.lc = t.lc;
    	}
    	
    	//get the list of vertices in reverse DFS topological order
    	dfstoplist = findDFSTopologicalOrder();
    	Iterator<Vertex> itr = dfstoplist.iterator();
    	Vertex current, predecessor;
    	
    	//iterate through each vertex and update its LC
    	while(itr.hasNext())
    	{
    		current = itr.next();
    		
    		//loop invariants
    		//predecessor - from vertex of the edge incident on current vertex
    		for(Edge e: current.revAdj)
    		{
    			predecessor = e.otherEnd(current);
    			predecessor.lc = Math.min(predecessor.lc, current.lc-current.d);
    			predecessor.slack = predecessor.lc - predecessor.ec;
    			
    			//if the slack is 0
    			if(predecessor.slack == 0)
    			{	
    				predecessor.isCritical = true;
    				//check whether the edge is tight
    				if(current.isCritical && (e.from.lc+e.to.d == e.to.lc))
    					e.isTight = true;
    			}
    		}
    	}
    }
    

    /**
     * Function to calculate number of critical paths
     * 
     */ 
    public void calculateCriticalPaths()
    {
    	//initialization
    	for(Vertex v:this)
    	{
    		v.criticalpaths = 0;
    	}
    	s.criticalpaths = 1;
    	
    	//iterate through the vertices in topological order
    	for(Vertex v:indegreetoplist)
    	{
    		//update the #criticalpaths property through a vertex if its incident edge is tight
    		for(Edge e:v.adj)
    		{
    			if(e.isTight)
    				e.otherEnd(v).criticalpaths += v.criticalpaths; 
    		}
    	}
    }
    
    /**
     * Function to get the enumerated list of critical paths
     * @param current - Vertex being processed
     * @param index - index of the arraylist
     */ 
    public void enumeratePaths(Vertex current, int index)
    {
    	pathArray[index] = current;
    	
    	//if target is reached - end of path
    	if(current == t)
    	{
    		for(int i=1; i<index; i++)
    		{
    			System.out.print(pathArray[i] + " ");
    		}
    		System.out.println();
    	}	
    	//else recursively find enumerate paths on other end vertices of adjacent edges 
    	else
    	{
    		for(Edge e:current.adj)
    		{
    			if(e.isTight)
    				enumeratePaths(e.otherEnd(current), index+1);
    		}
    	}
    }
    
    
    /**
     * Function to Check if graph is bipartite, using BFS
     *
     */ 
    public boolean isBipartite() {
	for(Vertex u: this) {
	    u.seen = false;
	}
	for(Vertex u: this) {
	    if(!u.seen) {
		bfs(u);
	    }
	}
	for(Vertex u:this) {
	    for(Edge e: u.adj) {
		Vertex v = e.otherEnd(u);
		if(u.d == v.d) {
		    return false;
		}
	    }
	}
	return true;
    }


    /**
     * Function to read a directed graph using the Scanner interface
     *
     */
    public static Graph readDirectedGraph(Scanner in) 
    {
    	return readGraph(in, true);
    }
    
    /**
     * Function to read an undirected graph using the Scanner interface
     *
     */
    public static Graph readGraph(Scanner in) 
    {
    	return readGraph(in, false);
    }
    
    /**
     * Function to read (un)directed graph 
     *
     */
    public static Graph readGraph(Scanner in, boolean directed) 
    {
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph
	
		// create a graph instance
		Graph g = new Graph(n);
		g.directed = directed;
		for (int i = 0; i < m; i++) 
		{
		    int u = in.nextInt();
		    int v = in.nextInt();
		    int w = in.nextInt();
		    g.addEdge(g.getVertex(u), g.getVertex(v), w);
		}
		
		g.s = g.getVertex(n-1);
		g.s.isCritical = true;
		g.s.d = 0;
		g.t = g.getVertex(n);
		g.t.isCritical = true;
		g.t.d = 0;
		
		return g;
    }

}
