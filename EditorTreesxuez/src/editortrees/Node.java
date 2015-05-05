package editortrees;

// A node in a height-balanced binary tree with rank.
// Except for the NULL_NODE (if you choose to use one), one node cannot
// belong to two different trees.

public class Node {
	
	enum Code {
		SAME, LEFT, RIGHT;
		// Used in the displayer and debug string
		@Override
		public String toString() {
			switch (this) {
				case LEFT:
					return "/";
				case SAME:
					return "=";
				case RIGHT:
					return "\\";
				default:
					throw new IllegalStateException();
			}
		}
	}
	
	// The fields would normally be private, but for the purposes of this class, 
	// we want to be able to test the results of the algorithms in addition to the
	// "publicly visible" effects
	
	char element;            
	Node left, right; // subtrees
	int rank;         // inorder position of this node within its own subtree.
	Code balance; 
	Node parent;  // You may want this field.
	// Feel free to add other fields that you find useful

	// You will probably want to add several other methods

	// For the following methods, you should fill in the details so that they work correctly
	//Constructor
	public Node(char c, Node parent){
		this.element = c;
		this.rank = 0;
		this.parent = parent;
		this.balance = Code.SAME;
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
	}
	
	public void setParent(Node parent){
		if(this == EditTree.NULL_NODE){
			return ;
		}
		this.parent = parent;
	}
	
	public Node getParent(){
		return this.parent;
	}
	
	
	public int height() {
		if(this == EditTree.NULL_NODE){
			return -1;
		}
		
		Node tiltedNode = getTiltedNode();
		if (tiltedNode == null) {
			tiltedNode = this.left;
		}
		return tiltedNode.height() + 1;
	}

	private Node getTiltedNode() {
		if (this.balance == Code.LEFT) {
			return this.left;
		} else if (this.balance == Code.RIGHT) {
			return this.right;
		} else {
			return null;
		}
	}

	public int size() {
		if(this == EditTree.NULL_NODE){
			return 0;
		}
		return this.rank + this.right.size() + 1;
	}
	
	public String toString(){
		if(this == EditTree.NULL_NODE){
			return "";
		}
		String str = this.left.toString() + this.element + this.right.toString();
		return str;
	}

	public String toDebugString() {
		if (this == EditTree.NULL_NODE) {
			return "";
		}
		return "" + this.element + this.rank + this.balance + ", "
				+ this.left.toDebugString() + this.right.toDebugString();
	
	}

	public Node get(int pos) {
		if(pos == this.rank){
			return this;
		}
		else if(this.rank > pos){
			return this.left.get(pos);
		}
		else{
			return this.right.get(pos-this.rank-1);
		}
	}

	public Node add(char c, int pos) {
		if(this.rank >= pos){//go to left
			this.rank++; //increase rank
			if(this.left == EditTree.NULL_NODE){//current has no left child
				Node addNode = new Node(c, this);
				this.left = addNode;
				return addNode;
				
			}
			else{//left if not null
				return this.left.add(c, pos);//keep add to left
			}
		}
		else{//else go to right
			if (this.right == EditTree.NULL_NODE) { // Leaf
				Node newNode = new Node(c, this);
				this.right = newNode;
				return newNode;
			}
			else {
				return this.right.add(c, pos - this.rank - 1);
			}
		}
	}
	

}