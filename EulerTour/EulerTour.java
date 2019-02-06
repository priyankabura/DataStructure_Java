/**
 * Driver program using the graph class
 * @author - Priyanka Bura - 10-01-2016
 */
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.io.File;

//driver class
public class EulerTour 
{
	public static void main(String[] args) throws FileNotFoundException 
    {
		//read the input file
		Scanner in;
	        if (args.length > 0) {
	            File inputFile = new File(args[0]);
	            in = new Scanner(inputFile);
	        } else {
	            in = new Scanner(System.in);
	        }
	     
	   //load the input file into a graph      
	   Graph g = Graph.readGraph(in);

	   //break the input graph into tours
	   List<CircularList<Element>>  outlist = g.breakGraphIntoTours();
	    
	   //stitch the above list of tours
	   CircularList<Element> euler_tour = g.stitchTours(outlist);
	   
	   //verify whether the above euler tour is valid
	   if(!g.verifyTour(euler_tour))
	   {
		   System.out.println("Graph is not eularian");
	   }
	   else
	   {
		   for(Element e:euler_tour)
		   {
				   System.out.println(e);
		   }
	   }
	}   
}

