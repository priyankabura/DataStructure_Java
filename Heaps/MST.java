/**
 * Code for Prim's MST algorithm
 * @author - Priyanka Bura - 11-13-2016
 */

import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class MST {
    static final int Infinity = Integer.MAX_VALUE;

    /**
     * function to find weight of MST using the priority of vertices and indexed heap
     * @param : g - input graph 
     * @param : s - start vertex
     */
    static int PrimMST2(Graph g, Vertex s)
    {
		//weight of minimum spanning tree
        int wmst = 0; 
        IndexedHeap<Vertex> ih = new IndexedHeap<>(g.size, new Vertex(0));
        
        //initialize all the vertices in a given graph
        for(Vertex v:g)
        {
        	v.d = Infinity;
        	v.p = null;
        	ih.add(v);
        }
        //initialize start index and perculate up to maintain the heap order wrt parent
        s.d = 0;
        ih.decreaseKey(s);
        
        /** Loop Invariant
         * current - holds the vertex being processed(updating its distance)
         */
        while(ih.size > 0)
        {
        	Vertex current = ih.remove();
        	current.seen = true;
        	
        	Vertex other;
        	
        	//update weight of minimum MST 
        	wmst += current.d;
        	
        	for(Edge e: current.adj)
        	{
        		other = e.otherEnd(current);
        		//check if the other end vertex is visited and the edge weight is less than other end distance
        		if(!other.seen && e.weight < other.d)
        		{
        			//update the distance and parent of other end vertex and percolate up to maintain the heap order wrt parent
        			other.d = e.weight;
        			other.p = current;
        			ih.decreaseKey(other);
        		}
        	}
        }
       
        return wmst;
    }
    
    /**
     * function to find weight of MST using the priority of edges and Java's priority queue
     * @param : g - input graph 
     * @param : s - start vertex
     */
    static int PrimMST(Graph g, Vertex s)
    {
    	int wmst = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        s.seen = true;
        
        //add all the adjacent edges of start vertex to priority queue
        for(Edge e:s.adj)
        {
        	pq.add(e);
        }
        
        //loop until the priority queue is empty
        while(!pq.isEmpty())
        {
        	Edge currentEdge = pq.remove();
        	
        	//check if the current edge is already processed
        	if(currentEdge.from.seen && currentEdge.to.seen)
        		continue;
           
        	//if from vertex of edge is visited
        	if(currentEdge.from.seen)
        	{
        		//visit the to vertex and update its parent
        		currentEdge.to.seen = true;
        		currentEdge.to.p = currentEdge.from;
        		
        		//add all the agjacent edges of to vertex to priority queue
        		for(Edge e:currentEdge.to.adj)
        		{
        			pq.add(e);
        		}
        	}
        	//if to vertex of edge is visited
        	else 
        	{
        		//visit the from vertex and update its parent
        		currentEdge.from.seen = true;
        		currentEdge.from.p = currentEdge.to;
        		
        		//add all the agjacent edges of from vertex to priority queue
        		for(Edge e:currentEdge.from.adj)
        		{
        			pq.add(e);
        		}
        	}
        	
        	//update weight of minimum MST 
        	wmst += currentEdge.weight;
        }
       
        return wmst;
    }

    public static void main(String[] args) throws FileNotFoundException 
    {
    	Scanner in;

        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }

	Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);
        System.out.println("PrimMST using the priority of vertices and indexed heap:");
        System.out.println(PrimMST2(g, s));
    }
}
