package cs5V81_ProblemSet;

import java.util.LinkedList;
import java.util.Queue;

public class BST <T extends Comparable <? super T>>{
	public static void main(String [] args){
		BST<Integer> tree = new BST<>();
		
		tree.add(18);
		tree.add(9);
		tree.add(24);		
		tree.add(63);
		tree.add(65);
		tree.add(5);
		tree.add(10);
		tree.add(35);
		tree.add(12);		
		tree.add(71);
		
		BST<Integer> tree2 = new BST<>();
		tree2.root = buildTreeFromSortedArray(new Integer []{12,89,330,389,2000});
		tree2.printByLevelOrder();
		
	}
	
	Entry<T> root;
	int size;
	
	BST(){
		root = null;
		size = 0;
	}	

	
	public static Entry<Integer> buildTreeFromSortedArray(Integer [] arr){
		int length = arr.length;
		int mid = (length-1)/2;
		Entry<Integer> root = new Entry<>(arr[mid], null, null,null);
		root.left = buildTreeFromSortedArrayHelper(arr, 0, mid - 1);
		root.right = buildTreeFromSortedArrayHelper(arr, mid + 1, length - 1); 
		return root;
	}
	
	public static Entry<Integer> buildTreeFromSortedArrayHelper(Integer [] arr, int left, int right){
		if(left > right){
			return null;
		}
		int mid = (left+right)/2;
		Entry<Integer> newNode = new Entry<>(arr[mid], null, null, null);
		newNode.left = buildTreeFromSortedArrayHelper(arr, left, mid - 1);
		newNode.right = buildTreeFromSortedArrayHelper(arr, mid + 1, right);		
		return newNode;
	}
	
	// Find x in subtree rooted at node t.  Returns node where search ends.
	Entry<T> find(Entry<T> t, T x){
		Entry<T> pre = t;
		while(t != null){
			pre = t;
			int cmp = x.compareTo(t.element);
			if(cmp < 0){
				t = t.left;				
			}else if(cmp > 0){
				t = t.right;
			}else{
				return t;
			}			
		}
		return pre;			
	}
	
	public boolean contains(T x){
		Entry<T> node = find(this.root, x);
		return node == null ? false : node.element.equals(x);
	}
	
	public boolean add(T x){
		if(size == 0){
			root = new Entry<>(x, null, null, null);
		}
		else{
			Entry<T> node = find(this.root, x);
			int cmp = x.compareTo(node.element);
			if(cmp == 0){
				node.element = x;
				return false;
			}
			Entry<T> newNode = new Entry<>(x, null , null, null);
			if(cmp < 0){
				node.left = newNode;				
			}else{
				node.right = newNode;
			}			
		}
		size++;
		return true;
	}
	
	public T remove(T x){
		T rv = null;
		if(this.size > 0){
			Entry<T> node = find(root, x);
			if(x.equals(node.element)){
				rv = node.element;
				remove(node);
				size--;
				}
		}
		return rv;
	}
	
	void remove(Entry<T> node){
		if(node.left != null && node.right != null){
			removeTwo(node);
		}
		else{
			removeOne(node);
		}
	}
	
	Entry<T> oneChild(Entry<T> node){
		return node.left == null? node.right : node.left;
	}
	
	void removeOne(Entry<T> node){
		if(node == root){
			Entry<T> nc = oneChild(root);
			root = nc;
			root.parent = null;
		}else{
			Entry<T> p = node.parent;
			Entry<T> nc = oneChild(node);
			
			if(p.left == node){
				p.left = nc;
			}else{
				p.right = nc;
			}
		}
	}
	
	void removeTwo(Entry<T> node){
		Entry<T> minRight = node.right;
		while(minRight.left !=null){
			minRight = minRight.left;
		}
		node.element = minRight.element;
		removeOne(node.right);
	}
	
	void printByLevelOrder(){
		if(this.root != null){
			Queue<Entry<T>> queue = new LinkedList<>();
			queue.add(this.root);
			while(!queue.isEmpty()){
				Entry<T> visiting = queue.poll();
				System.out.print(visiting.element+" ");
				if(visiting.left != null){
					queue.add(visiting.left);
				}
				if(visiting.right != null){
					queue.add(visiting.right);
				}
			}			
		}		
	}	
}



class Entry<T>{
	T element;
	Entry<T> left, right, parent;
	Entry(){}
	Entry(T x, Entry<T> left, Entry<T> right, Entry<T> parent){
		this.element = x;
		this.left = left;
		this.right = right;
		this.parent = parent;
	}	
}
