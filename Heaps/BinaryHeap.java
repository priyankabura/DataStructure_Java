/**
 * Code for Binary Heap implementation
 * @author - Priyanka Bura - 11-13-2016
 */
import java.util.Comparator;

public class BinaryHeap<T> implements PQ<T>
{
    Object[] pq;
    Comparator<T> c;
    int size;

    /**
     * Constructor - Build a priority queue with a given array q
     * @param : q : input array    
     * @param : comp - Comparator used for sorting
     */
    BinaryHeap(T[] q, Comparator<T> comp) 
    {
		pq = q;
		c = comp;
		size = q.length-1;
		buildHeap();
	}

    /**
     * Constructor - Create an empty priority queue of given maximum size
     * @param : n : size    
     * @param : comp - Comparator used for sorting
     */
    BinaryHeap(int n, Comparator<T> comp) 
    { 
    	pq = new Object[n+1];
    	c = comp;
    	size=0;
    }

    /**
     * function to insert new value into heap 
     * @param : x - element to be inserted  
     */
    public void insert(T x)
    {
    	add(x);
    }

    /**
     * function to delete the smallest element 
     */
    public T deleteMin() 
    {
    	return remove();
    }

    /**
     * function to return the minimum/small/first element
     */
    public T min() 
    { 
    	return peek();
    }
    
    /**
     * function to resize the heap  
     */
    private void resize()
    {
    	int new_size = size*2;
    	
    	//create new heap with double the current size and assign this back to pq
		T newarray[] = (T[]) new Object[new_size];
		
		for(int i=0; i<=size; i++)
		{
			newarray[i] = (T)pq[i];
		}
		pq = newarray;
    }
    
    /**
     * function to add/insert new element  
     * @param : x - element to be inserted
     */
    public void add(T x) 
    {
    	//resize if the heap is full
    	if(size == pq.length-1)
    	{
    		resize();
    	}
    	int hole = ++size;
    	pq[hole] = x;
    	
    	//check and correct the heap order wrt parent
    	percolateUp(hole);
    }

    /**
     * function to remove first or minimum value element and return it
     */
    public T remove()
    { 
    	if(size > 0)
    	{
    		T minElement = (T)pq[1];
    		pq[1] = pq[size--];
    		
    		//check and correct the heap order wrt children
    		percolateDown(1);
    		return minElement;
    	}
    	else
    		return null;
    }

    /**
     * function to return the first element in binary heap 
     */
    public T peek()
    {
    	if(size > 0)
    		return (T)pq[1];
    	else
    		return null;
    }

    /**
     * function to maintain the heaporder with respect to parent when pq[i] has been changed  
     * @param : i - index of pq at which element has changed 
     */
    void percolateUp(int i) 
    { 
    	pq[0] = pq[i];
    	
    	/** Loop Invariant
    	 *  i - index of an element whose parent is checked for heaporder and moved to correct location 'i'
    	 */
    	while(c.compare((T)pq[i/2],(T)pq[0]) > 0)
    	{
    		move((T[])pq,i,i/2);
    		i = i/2;
    	}
    	//move element at index '0' to index 'i' in array 'pq' finally
    	move(pq, i, 0);
    }

    /**
     * function to maintain the heaporder with respect to children when pq[i] has been changed  
     * @param : i - index of pq at which element has changed 
     */
    void percolateDown(int i) 
    { 
    	T currentElement = (T)pq[i];
    	int schild; //child index
    	
    	while(true)
    	{
    		//check if the child index of 'i' exceeds size
    		if(2*i > size)
    			break;
    		else
    		{
    			//get the child index(schild) of 'i'
    			if(2*i == size)
    				schild = 2*i;
    			else
    			{
    				//get the smallest index among children whose value is smallest
    				if(c.compare((T)pq[2*i],(T)pq[2*i + 1]) < 0)
    					schild = 2*i;
    				else
    					schild = 2*i+1;
    			}
    		}
    		//check if element at 'i' is greater/smaller than child element
    		if(c.compare(currentElement,(T)pq[schild])<0)
    			break;
    		else
    		{
    			move((T[])pq,i,schild);
    			i = schild;	
    		}
    			
    	}
    	
    	//move the current element to given position 'i'
    	move(pq, i, currentElement);
    }

    /**
     * function to change value of an element at given index  
     * @param : pq : input array     
     * @param : newindex - element at this index has to be changed
     * @param : oldindex : value at this index has to be assigned to new index  
     */
    void move(Object[] pq, int newindex, int oldindex)
    {
    	if(newindex > 0 && oldindex > 0 && pq!=null)
    		pq[newindex] = pq[oldindex];
    }
    
    /**
     * function to assign given value to element at given index
     * @param : pq : input array     
     * @param : newindex - value at this index has to be changed
     * @param : newvalue - value to be assigned
     */
    void move(Object[] pq, int newindex, T newvalue)
    {
    	if(newindex > 0 && pq!=null)
    		pq[newindex] = newvalue;
    }
    
    /**
     * function to create a heap 
     */
    void buildHeap() 
    {
    	int length = size;
    	//start from middle of the array and percolatedown at each position respectively
    	for (int i=length/2; i>0; i--)
    	      percolateDown(i);
    }

    /* sort array A[1..n].  A[0] is not used. 
       Sorted order depends on comparator used to buid heap.
       min heap ==> descending order
       max heap ==> ascending order
     */
    /**
     * function to sort the given binary heap  
     * @param : A : input array to be sorted     
     * @param : comp - comparator used for sorting  
     */
    public static<T> void heapSort(T[] A, Comparator<T> comp)
    {
    	//create binary heap object with given array and comparator
    	BinaryHeap<T> bh = new BinaryHeap<>(A, comp);
    	
    	//size of array
    	int n = bh.size;
    	
    	/** Loop Invariants
    	 * 'bh' contains next smallest element at index 1 after 'builheap' on remaining elements
    	 */
    	for(int i = 1; i<n; i++)
    	{
    		//swap the smallest element at index '1' with last element
    		bh.swap(1,bh.size--);
    		//buildheap on remaining array to get smallest element at '1'
    		bh.buildHeap();
    	}
    	//print the elements in ascending order
    	for(int i=A.length-1; i>0; i--)
    	{
    		System.out.println(A[i]);
    	}
    }
    
    /**
     * function to swap two elements in an array at given indexes
     * @param : index1, index2 : indexes of an array    
     */
    void swap(int index1, int index2)
    {
    	//pq[0] is used as temp 
    	pq[0] = pq[index1];
		pq[index1] = pq[index2];
		pq[index2] = pq[0];
    }
    
    //testing heapsort
  /*  public static void main(String args[])
    {
    	Integer A[] = new Integer[11];
    	A[0] = 0;
    	A[1] = 20;
    	A[2] = 3;
    	A[3] = 31;
    	A[4] = 11;
    	A[5] = 40;
    	A[6] = 6;
    	A[7] = 27;
    	A[8] = 15;
    	A[9] = 25;
    	A[10] = 80;
    	
    	heapSort(A, Integer::compare);
    }*/
}
