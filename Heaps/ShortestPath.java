/**
 * Code for Dijkstra's Shortest path algorithm
 * @author - Priyanka Bura - 11-13-2016
 */

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class ShortestPath {
    static final int Infinity = Integer.MAX_VALUE;

    /**
     * function to find ShortestPath using the priority of vertices and indexed heap
     * @param : g - input graph 
     * @param : s - start vertex
     */
    static void DijkstraShortestPaths(Graph g, Vertex s)
    {
    	// Shortest paths will be stored in fields d,p in the Vertex class
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
        	Vertex other;
        	current.seen = true;
        	
        	for(Edge e: current.adj)
        	{
        		other = e.otherEnd(current);
        		//check if the other end vertex is visited and the edge weight is less than other end distance
        		if(!other.seen && e.weight+current.d < other.d)
        		{

        			//update the distance and parent of other end vertex and percolate up to maintain the heap order wrt parent
        			other.d = e.weight+current.d;
        			other.p = current;
        			ih.decreaseKey(other);
        		}
        	}
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
	Scanner in;

        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }

	Graph g = Graph.readGraph(in);
	int src = in.nextInt();
	int target = in.nextInt();
        Vertex s = g.getVertex(src);
	Vertex t = g.getVertex(target);
        DijkstraShortestPaths(g, s);

	System.out.println(t.d);
    }
}
