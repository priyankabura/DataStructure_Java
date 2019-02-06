/**
 * Treemap test driver program
 * @author - Priyanka Bura - 10-23-2016
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

public class TreemapTest {

	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc;
		if (args.length > 0) {
		    File file = new File(args[0]);
		    sc = new Scanner(file);
		} else {
		    sc = new Scanner(System.in);
		}
		//sc.close();
		String operation = "";
		long operand = 0;
		long result = 0;
		int modValue = 999983;
		TreeMap<Long, Long> treeMap = new TreeMap<>();
		// Initialize the timer
		Timer timer = new Timer();
		
		while (!((operation = sc.next()).equals("End")))
		{
			//lineCounter++;
		    switch (operation) 
		    {
			    case "Add": 
			    {
				    operand = sc.nextLong();
				    if(treeMap.put(operand, (long) 1) == null) {
						result = (result + 1) % modValue;
					}
					break;
			    }
			    case "Remove": 
			    {
					operand = sc.nextLong();
					if (treeMap.remove(operand) != null) {
					    result = (result + 1) % modValue;
					}
					break;
				}
			    case "Contains":
			    {
					operand = sc.nextLong();
					if (treeMap.containsKey(operand)) 
					{
					    result = (result + 1) % modValue;
					}
					break;
			    }
			}
		}

		// End Time
		timer.end();
		System.out.println(result);
		System.out.println(timer);
	}
}
