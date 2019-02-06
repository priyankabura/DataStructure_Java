package cs5V81_ProblemSet;

public class RB_Tree  <T extends Comparable <? super T>> {
	
	public static void main(String [] args){
		RB_Tree<Integer> tree = new RB_Tree<>();
		
			 tree.add(1);
			 tree.add(3);
			 tree.add(5);
			 tree.add(7);
			 tree.add(9);
			 tree.add(6);
			 tree.add(4);
			 tree.add(2);
			tree.printTree(tree.root);
			System.out.println();		
	}
	
	RB_Node <T> root;
	int size;
	
	RB_Tree(){
		this.root = null;
		this.size = 0;		
	}
		
	//@Override
	public boolean add(T x){
		if(size == 0){
			this.root = new RB_Node<>(x, new RB_Node<>(null, null,null,null,1), new RB_Node<>(null, null,null,null,1), null);
			this.root.setToBlack();
		}
		else{
			RB_Node<T> node = (RB_Node<T>)find(this.root, x);
			int cmp = x.compareTo(node.element);
			if(cmp == 0){
				node.element = x;
				return false;
			}
			RB_Node<T> newNode = new RB_Node<>(x,new RB_Node<>(null, null,null,null,1) , new RB_Node<>(null, null,null,null,1), null);
			if(cmp < 0){
				node.setLeft(newNode);			
			}else{
				node.setRight(newNode);
			}
			repair(newNode);
		}		
		size++;
		return true;
	}
	
	RB_Node<T> find(RB_Node<T> t, T x){
		RB_Node<T> pre = t;
		while(t != null && t.element != null){
			pre = t;
			int cmp = x.compareTo(t.element);
			if(cmp < 0){
				t = t.getLeft();				
			}else if(cmp > 0){
				t = t.getRight();
			}else{
				return t;
			}			
		}
		return pre;			
	}
	
	//left zig zag:	/	right zig zag:	\
	//				\					/
	public void repair(RB_Node<T> node){
		if(node.isBlack() || node.getParent().isBlack()){
			return;
		}else if(node.getParent().isRed()){
			//case 1
			if(node.getParent().getSibling().isRed()){
				recolor(node);
				repair(node.getGrandParent());
			}		
			
			//case 2
			else if(isZigZig(node)){
				RB_Node<T> oldRoot = node.getGrandParent();				
				if(oldRoot != this.root){
					if(node.getGrandParent().isLeftChild()){
						node.getParent().getGrandParent().setLeft(node.getParent());					
					}else{
						node.getParent().getGrandParent().setRight(node.getParent());
					}
				}								
				
				//left zig zig
				if(node.isLeftChild())
					rotate(node.getParent(), oldRoot, node.getParent().getRight(), node);
				//right zig zig
				else
					rotate(node.getParent(), oldRoot, node.getParent().getLeft(), node);
				
				node.getParent().setToBlack();
			}
			//case 3
			else if(isZigZag(node)){
				RB_Node<T> oldRoot = node.getParent();
				if(node.getParent().isLeftChild()){
					node.getGrandParent().setLeft(node);
					rotate(node, oldRoot,node.getLeft(), null);
				}
					
				else{
					node.getGrandParent().setRight(node);
					rotate(node, oldRoot,node.getRight(), null);
				}
				repair(oldRoot);					
			}
		}
	}
	
	public void rotate(RB_Node<T> newRootOfSubTree, RB_Node<T> oldRootOfSubTree, RB_Node<T> movingChild, RB_Node<T> problemetic){
		//left zig zig
		if(newRootOfSubTree == oldRootOfSubTree.getLeft()){
			oldRootOfSubTree.setLeft(movingChild);			
		}
		//right zig zig
		else if(newRootOfSubTree == oldRootOfSubTree.getRight()){
			oldRootOfSubTree.setRight(movingChild);
		}
		if(problemetic != null){
			//left zig zig
			if(newRootOfSubTree.getLeft() == problemetic){
				newRootOfSubTree.setRight(oldRootOfSubTree);
			}
			//right zig zig
			else if(newRootOfSubTree.getRight() == problemetic){
				newRootOfSubTree.setLeft(oldRootOfSubTree);
			}
			oldRootOfSubTree.setToRed();
		}
		//zig zag rotation
		else{
			if(newRootOfSubTree.getLeft() == movingChild){
				newRootOfSubTree.setLeft(oldRootOfSubTree);
			}
			else if(newRootOfSubTree.getRight() == movingChild){
				newRootOfSubTree.setRight(oldRootOfSubTree);
			}
		}
		if(oldRootOfSubTree == this.root){
			this.root = newRootOfSubTree;
			this.root.setToBlack();
		}
	}
	
	public void recolor(RB_Node<T> node){
		node.getParent().setToBlack();
		node.getParent().getSibling().setToBlack();
		if(node.getGrandParent() != this.root)	
			node.getGrandParent().setToRed();
	}
	
	public boolean isZigZig(RB_Node<T> node){
		RB_Node<T> grand = node.getGrandParent();
		return (node==node.getParent().getLeft() && node.getParent() == grand.getLeft()) || (node==node.getParent().getRight() && node.getParent() == grand.getRight());
	}
	
	public boolean isZigZag(RB_Node<T> node){
		RB_Node<T> grand = node.getGrandParent();
		return (node==node.getParent().getLeft() && node.getParent() == grand.getRight()) || (node==node.getParent().getRight() && node.getParent() == grand.getLeft());
	}
	
	public static <T> void printTree(RB_Node<T> node){
		if(node == null || node.element == null){
			return;
		}
		printTree(node.getLeft());
		
		System.out.print(node.element);
		if(node.isBlack()){
			System.out.print("(B) ");
		}else{
			System.out.print("(R) ");
		}
		
		printTree(node.getRight());		
	}
}



class RB_Node <T>{	
	private int redBlack;//0-red 1-black
	T element;
	private RB_Node<T> left, right, parent;
	
	RB_Node(T x, RB_Node<T> left, RB_Node<T> right, RB_Node<T> parent){
		this.element = x;
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.redBlack = 0;		
	}
	
	RB_Node(T x, RB_Node<T> left, RB_Node<T> right, RB_Node<T> parent, int color){
		this.element = x;
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.redBlack = color;
	}
	
	public void setToBlack(){
		this.redBlack = 1;
	}

	public void setToRed(){
		this.redBlack = 0;
	}
	
	public boolean isRed(){
		return redBlack==0;
	}
	
	public boolean isBlack(){
		return redBlack==1;
	}
	
	public void setLeft(RB_Node<T> leftChild){
		this.left = leftChild;
		leftChild.parent = this;
	}
	
	public void setRight(RB_Node<T> rightChild){
		this.right = rightChild;
		rightChild.parent = this;
	}
	
	public RB_Node<T> getRight(){
		return this.right;
	}
	
	public RB_Node<T> getLeft(){
		return this.left;
	}
	
	public RB_Node<T> getParent(){
		return this.parent;
	}
	
	public RB_Node<T> getGrandParent(){
		return this.parent.parent; 
	}
	
	public RB_Node<T> getSibling(){
		if(this == this.parent.left)
			return this.parent.right;
		else
			return this.parent.left;
	}
	public boolean isLeftChild(){
		return this == this.parent.left;
	}
	
	public boolean isRightChild(){
		return this == this.parent.right;
	}	
}
