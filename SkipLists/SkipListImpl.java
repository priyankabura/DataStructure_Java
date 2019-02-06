/**
 * Implementation of skiplist
 * @author - Priyanka Bura - 10-23-2016
 */

import java.util.*;

// Skeleton for skip list implementation.
public class SkipListImpl<T extends Comparable<? super T>> implements SkipList<T> 
{
	//Node represents each entry in the skiplist
	public class Node<E>
	{
		//element holds the value of the node
		E element;
		
		//width is used to maintain the distance traveled at each level 
		int width[];
		
		//next[] points to all the nodes at several levels rooting from given node 
		Node<E> next[];
		
		/**
	     * Constructor for Node
	     * 
	     * @param value : the value which node holds
	     * @param nodelevel : the level selected randomly which is <=maximum skiplist level
	     *            
	     */
		Node(E value, int nodelevel)
		{
			this.width = new int[nodelevel];
			
			for(int i=0; i<nodelevel; i++)
			{
				this.width[i] = 0;
			}
			
			this.element = value;
			this.next = new Node[nodelevel];
		}
	}
	
	//Class that stores the previous node details returned by 'find' function
	public class PrevNode<E>
	{
		int[] distance; //distance traveled at each level in find method to get the previous node
		Node<E> nodeArray[]; //array of previous nodes
		
		/**
	     * Constructor for PrevNode
	     *            
	     */
		PrevNode()
		{
			distance = new int[maxlevel];
			nodeArray = new Node[maxlevel];
		}
	}
	
	Node<T> header;
	int maxlevel,size;
	Random rnd = new Random();
	int counter = 0;
	
	/**
     * Constructor for SkipListImpl           
     */
	SkipListImpl()
	{
		maxlevel = 10; //default - can be changed or gets changed during rebuild
		header = new Node<>(null,maxlevel);
		size = 0;
	}
	
	/**
     * function to return the previous node object of given node value  
     * @param : value : the value which node holds     
     * @param : head - header node to start the search with   
     */
	public PrevNode<T> find(T value, Node<T> head)
	{
		PrevNode<T> prev = new PrevNode<>();
		Node<T> current = head;
		
		/** loop invariants
    	 * leveldistance - distance traversed at each level to find the node with given value
    	 * current - starts with header node and goes on until previous node is found
    	 */
		for(int i=maxlevel-1;i>=0;i--)
		{
			int leveldistance = 0;
			while(current.next[i]!=null && current.next[i].element.compareTo(value)<0)
			{
				leveldistance+=current.width[i];
				current = current.next[i];
			}
			prev.distance[i] = leveldistance;
			prev.nodeArray[i] = current;	
		}
		return prev;
	}
	
	/**
     * function to randomly select a level  
     * @param : maxlevel : the maximum level a node in a skiplist can have    
     */
	public int choice(int maxlevel)
	{
		int level;
		
		//loop until the rnd.nextBoolean() is true
		for(level = 0; level < maxlevel; )
		{
			if(rnd.nextBoolean())
			{
				break;
			}
			else
			{
				level++;
			}
		}
		//check if level exceeds the maxlevel because of increment operator above
		if(level >= maxlevel)
			return level-1;
		else			
			return level;
	}
	
	/**
     * function to calculate the width of node at all its levels 
     * @param : prev : previous node returned by find function
     * @param : newnode - node that is being added   
     */
	public void calculateWidth(PrevNode<T> prev, Node<T> newnode)
	{
		//case when the previous node's level is more than new node's level 
		if(prev.nodeArray[0].next.length >= newnode.next.length)
		{
			for(int i=0; i<newnode.next.length; i++)
	    	{
    			newnode.width[i] = prev.nodeArray[0].width[i];
    			prev.nodeArray[0].width[i] = 1;
	    	}
		}
		//case when the previous node's level is less than new node's level 
		else if(prev.nodeArray[0].next.length < newnode.next.length)
		{
			//adjust the width of new node until the level equals previous node level
			for(int l=0; l<prev.nodeArray[0].next.length; l++)
			{
				newnode.width[l] = prev.nodeArray[0].width[l];
    			prev.nodeArray[0].width[l] = 1;
			}
			
			//adjust the width for nodes at level above the previous node level
			for(int m=newnode.next.length-1; m>=prev.nodeArray[0].next.length; m--)
			{
				newnode.width[m] = 0;
				int initialprevlength = prev.nodeArray[m].width[m];
				int newprevlength = 1;
				//calculate the new width for previous node
				for(int n=0; n<m; n++)
				{
					newprevlength += prev.distance[n];
				}
				//update previous and new node's widths
				prev.nodeArray[m].width[m] = newprevlength;
				newnode.width[m] = initialprevlength - prev.nodeArray[m].width[m] + 1;
			}
		}
		
		//for the nodes above the new node level increment their width by 1
		for(int k=newnode.next.length; k<maxlevel; k++)
		{
			prev.nodeArray[k].width[k] = prev.nodeArray[k].width[k] + 1;
		}

	}
	
	/**
     * function to insert a new element into skiplist
     * @param : x - value of new node
     */
    @Override
    public boolean add(T x) 
    {
    	int level = choice(maxlevel);
    	PrevNode<T> prev = find(x, this.header);
		
		//check if the element already exists in the list
    	if(prev.nodeArray[0].next[0]!=null && prev.nodeArray[0].next[0].element.equals(x))
    	{
    		prev.nodeArray[0].next[0].element = x;
    		return false;
    	}
    	else
    	{
    		//create new node with new level
	    	Node<T> newnode = new Node<T>(x,level+1);

	    	//update the links 
	    	for(int i=0; i<newnode.next.length; i++)
	    	{
	    		newnode.next[i] = prev.nodeArray[i].next[i];
	    		prev.nodeArray[i].next[i] = newnode;
	    	}
	    	
	    	//update the previous and new node's width
	    	calculateWidth(prev, newnode);
	    	this.size ++;
    	}
    	
    	//rebuild the skiplist into perfect list when the size exceeds 2^maxlevel
    	if(size > Math.pow(2, maxlevel))
    	{
    		rebuild();
    	}
    	return true;
    }

    /**
     * function to find the node with value >= x
     * @param : x - value of node
     */
    @Override
    public T ceiling(T x) 
    {
    	PrevNode<T> prev = find(x, this.header);
    	if(prev.nodeArray[0].next[0] != null)
    		return prev.nodeArray[0].next[0].element;
    	else
    		return null;
    }

    /**
     * function to check if given element exists in the list
     * @param : x - value of node
     */
    @Override
    public boolean contains(T x) 
    {
    	PrevNode<T> prev = find(x, this.header);
    	if(prev.nodeArray[0].next[0] != null)
    		return prev.nodeArray[0].next[0].element.equals(x);
    	else
    		return false;
    }

    /**
     * function to find the node with given index
     * @param : n - index of node
     */
    @Override
    public T findIndex(int n) 
    {
    	if(n<size)
    	{
    		n=n+1;
	    	Node<T> node = this.header;
	    	//n - gets updated after traversing each level
	    	//node - gets updated with next node to be searched for
	    	for(int level=this.maxlevel-1; level >=0; level--)
	    	{
	    		//loop until the node is found
	    		while(node.next[level]!=null && n!=0 && n>=node.width[level])
	    		{
	    			n = n-node.width[level];
	    			node = node.next[level];
	    		}
	    	}
	    	return node.element;
    	}
    	else
    	{
    		return null;
    	}
    }

    /**
     * function to return the first element in the list
     */
    @Override
    public T first() 
    {
    	if(this.header.next[0] != null)
    		return this.header.next[0].element;
    	else
    		return null;
    }

    /**
     * function to find the node with value <= x
     * @param : x - value of node
     */
    @Override
    public T floor(T x) 
    {
    	PrevNode<T> prev = find(x,this.header);
    	if(prev.nodeArray[0].next[0] != null && prev.nodeArray[0].next[0].element.compareTo(x) == 0)
    		return prev.nodeArray[0].next[0].element;	
    	else
    		return prev.nodeArray[0].element;
    }

    /**
     * function to check whether the list is empty
     */
    @Override
    public boolean isEmpty() 
    {
    	return size == 0;
    }

    /**
     * function that return the ietartor of skiplist class
     */
    @Override
    public Iterator<T> iterator()
    {
    	return new SkipListIterator<>(this.header);
    }

    /**
     * skiplist iterator class
     * @param : n - index of node
     */
    private class SkipListIterator<T> implements Iterator<T> 
    {
    	Node<T> cursor, prev;
    	boolean ready;  // is item ready to be removed?

    	/**
         * Constructor 
         * @param : head - header node of the skiplist
         */
    	SkipListIterator(Node<T> head) 
    	{
    	    cursor = head;
    	    prev = null;
    	    ready = false;
    	}

    	//returns true if the list doesn't end
    	public boolean hasNext() 
    	{
    	    return cursor.next[0] != null;
    	}
    	
    	//returns the next element to be processed
    	public T next() 
    	{
    	    prev = cursor;
    	    cursor = cursor.next[0];
    	    ready = true;
    	    return cursor.element;
    	}

    	// Removes the current element (retrieved by the most recent next())
      /*public void remove() 
    	{
    	    if(!ready)
    	    {
    	    	throw new NoSuchElementException();
    	    }
    	    else
    	    {
    	    	SkipListImpl.this.remove(cursor.element);
    	    }
    	    
    	}*/
    }
    
    /**
     * function to get the last element in the list
     */
    @Override
    public T last() {
    	Node<T>[] prev = new Node[maxlevel];
		Node<T> current = this.header;
		
		for(int i=maxlevel-1;i>=0;i--)
		{
			while(current.next[i]!=null)
			{
				current = current.next[i];
			}
			prev[i] = current;
		}
		return prev[0].element;
    }

    /**
     * function to rebuild the entire skiplist
     */
    @Override
    public void rebuild() 
    {
    	Node<T>[] nodeArray = new Node[size]; //create an array to hold the values of skiplist in order
    	this.maxlevel = this.maxlevel+5; //incrementing maxlevel by 5 - can be changed
    	Node<T> newheader = new Node<>(null,this.maxlevel); //header for new skip list
    	
    	//build the perfect skiplist structure with new maxlevel
    	rebuildStructure(nodeArray, 0, size-1, this.maxlevel);
    	
    	Node<T> current = this.header.next[0];
    	PrevNode<T> previousnode = new PrevNode<T>();
    		
    	int i=0;
    	//
    	while(current!=null)
    	{
    		//store the current element in the node array
    		nodeArray[i].element = current.element;
    		
    		//get the previous node for new node
			previousnode = find(nodeArray[i].element,newheader);
			
			//update the links for previous nodes
			for(int j=0; j<nodeArray[i].next.length; j++)
			{
				previousnode.nodeArray[j].next[j] = nodeArray[i];
			}
			//update the width of previous nodes
			calculateWidth(previousnode, nodeArray[i]);
    		current = current.next[0];
    		i++;
    	}
    	//assign the newheader as skiplist's header node
    	this.header = newheader;
    }
    
    /**
     * function to create the prefect skiplist structure
     * @param : inputlist - array of skiplist nodes
     * @param : start index of new list
     * @param : endindex - end index of new list
     * @param : newlevel - new maximum level for skiplist
     */
    public void rebuildStructure(Node[] inputlist, int startindex, int endindex, int newlevel)
    {
    	if(startindex<=endindex)
    	{
    		if(newlevel == 1)
    		{
    			for(int i=startindex; i<=endindex; i++)
    			{
    				inputlist[i] = new Node<T>(null,1);
    			}
    		}
    		//when newlevel > 1, split the array into half with newlevel as newlevel-1 and rebuild the spilt arrays 
    		else
    		{
    			int middle = (startindex+endindex)/2;
    			inputlist[middle] = new Node<T>(null,newlevel);
    			rebuildStructure(inputlist,startindex,middle-1,newlevel-1);
    			rebuildStructure(inputlist,middle+1,endindex,newlevel-1);
    		}
    	}
    }

    /**
     * function to remove the given element
     * @param : x - value of the node
     */
    @Override
    public T remove(T x) 
    {
    	PrevNode<T> prev = find(x,this.header);
    	Node<T> n = prev.nodeArray[0].next[0];
    	
    	//check if the node is found
    	if(n!=null && n.element.compareTo(x) == 0)
    	{
    		//update the links and width of previous nodes of deleted node at each level
    		for(int i=0; i<maxlevel; i++)
    		{
    			if(i<n.next.length)
    			{
	    			if(prev.nodeArray[i].next[i] == n)
	    			{
	    				prev.nodeArray[i].next[i] = n.next[i];
	    				prev.nodeArray[i].width[i] = prev.nodeArray[i].width[i] + n.width[i] - 1; 
	    			}
    			}
    			else
    			{
    				prev.nodeArray[i].width[i]--;
    			}
    		}
    		size--;
    		return n.element;
    	}
    	else
    	{
    		return null;
    	}
    }

    /**
     * function to return the skiplist size
     */
    @Override
    public int size()
    {
    	return size;
    }
}