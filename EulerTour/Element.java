/**
 * class holds the details of vertex and its incident edge
 * @author - Priyanka Bura - 10-01-2016
 */
public class Element {

	Vertex vertex;
	Edge edge;
	
	/**
     * Constructor for Element
     * 
     * @param v
     *            : Vertex - Vertex object
     * @param e
     *            : Edge - Edge incident on vertex v
     */
	Element(Vertex v, Edge e)
	{
		vertex = v;
		edge = e;
	}
	 
	/**
     * Method to represent an element by its vertex name
     */
	public String toString() 
	{
	    return Integer.toString(vertex.name);
	}
}
