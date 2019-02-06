/**
 *  Code for Indexed heaps
 * @author - Priyanka Bura - 11-13-2016
 */

import java.util.Comparator;

public class IndexedHeap<T extends Index> extends BinaryHeap<T> 
{
	 /**
     * Constructor - Build a priority queue with a given array q
     * @param : q - input array    
     * @param : comp - Comparator used for sorting
     */
    IndexedHeap(T[] q, Comparator<T> comp)
    {
    	super(q, comp);
    }

    /**
     * Constructor - Create an empty priority queue of given maximum size
     * @param : n - size    
     * @param : comp - Comparator used for sorting
     */
    IndexedHeap(int n, Comparator<T> comp)
    {
    	super(n, comp);
    }

    /**
     * function to restore heap order property after the priority of x has decreased 
     * @param : x - element from which heap order has to be updated 
     */
    void decreaseKey(T x) {
	percolateUp(x.getIndex());
    }
    
    @Override
    /**
     * function to change value of an element at given index  
     * @param : pq : input array     
     * @param : newindex - element at this index has to be changed
     * @param : oldindex : value at this index has to be assigned to new index  
     */
    void move(Object[] pq, int newindex, int oldindex)
    {
    	pq[newindex] = pq[oldindex];
    	((T)pq[newindex]).putIndex(newindex);	//update the corresponding index
    }
    
    /**
     * function to assign given value to element at given index
     * @param : pq : input array     
     * @param : newindex - value at this index has to be changed
     * @param : newvalue - value to be assigned
     */
    void move(Object[] pq, int newindex, T newvalue)
    {
    	pq[newindex] = newvalue;
    	((T)pq[newindex]).putIndex(newindex);	//update the corresponding index
    }
}
