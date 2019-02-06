/** Driver program for MP4
 *  @author - Priyanka Bura - 12-06-2016
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class Driver
{
    public static void main(String[] args) throws FileNotFoundException 
    {
    	Scanner in;
        if (args.length > 0)
        {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }
		Graph g = Graph.readDirectedGraph(in);
		for(Vertex u: g)
		{
		    u.d = in.nextInt();
		}

		Timer timer = new Timer();
	/*List<Vertex> output = g.findIndegreeTopologicalOrder();
	
	Iterator<Vertex> itr = output.iterator();
	System.out.println("IndegreeTopologicalOrder");
	while(itr.hasNext())
	{
		System.out.println(itr.next());
	}
	

	List<Vertex> output2 = g.findDFSTopologicalOrder();
	Iterator<Vertex> itr2 = output2.iterator();
	System.out.println("findDFSTopologicalOrder");
	while(itr2.hasNext())
	{
		System.out.println(itr2.next());
	}*/
	
		CriticalPaths cp = new CriticalPaths(g);
		cp.findCriticalPaths();
		
		System.out.println(timer.end());
    }
}

