package editortrees;


// A height-balanced binary tree with rank that could be the basis for a text editor.

public class EditTree {
	public static final Node NULL_NODE = new Node('\0', null );
	private Node root;
	private int rotationCount = 0;

	/**
	 * Construct an empty tree
	 */
	public EditTree() {
		this.root = NULL_NODE;
	}

	/**
	 * Construct a single-node tree whose element is c
	 * 
	 * @param c
	 */
	public EditTree(char c) {
		add(c);
	}

	/**
	 * Create an EditTree whose toString is s. This can be done in O(N) time,
	 * where N is the length of the tree (repeatedly calling insert() would be
	 * O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {

	}

	/**
	 * Make this tree be a copy of e, with all new nodes, but the same shape and
	 * contents.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {

	}

	/**
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		return this.root.height();
	}

	/**
	 * 
	 * returns the total number of rotations done in this tree since it was
	 * created. A double rotation counts as two.
	 *
	 * @return number of rotations since tree was created.
	 */
	public int totalRotationCount() {
		return this.rotationCount;
	}

	/**
	 * return the string produced by an inorder traversal of this tree
	 */
	@Override
	public String toString() {
		return this.root.toString();

	}

	/**
	 * This one asks for more info from each node. You can write it like 
	 * the arraylist-based toString() method from the
	 * BST assignment. However, the output isn't just the elements, but the
	 * elements, ranks, and balance codes. Former CSSE230 students recommended
	 * that this method, while making it harder to pass tests initially, saves
	 * them time later since it catches weird errors that occur when you don't
	 * update ranks and balance codes correctly.
	 * For the tree with node b and children a and c, it should return the string:
	 * [b1=, a0=, c0=]
	 * There are many more examples in the unit tests.
	 * 
	 * @return The string of elements, ranks, and balance codes, given in
	 *         a pre-order traversal of the tree.
	 */
	public String toDebugString() {
		String st = root.toDebugString();
		if (st.length() > 0) {
			return "[" + st.substring(0, st.length() - 2) + "]";
		}
		return "[" + st + "]";
	}

	
	
	
	/**
	 * 
	 * @param pos
	 *            position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		if(pos < 0 || pos >= this.size()){
			throw new IndexOutOfBoundsException();
		}
		return this.root.get(pos).element;
	}

	/**
	 * 
	 * @param c
	 *            character to add to the end of this tree.
	 */
	public void add(char c) {
		// Notes:
		// 1. Please document chunks of code as you go. Why are you doing what
		// you are doing? Comments written after the code is finalized tend to
		// be useless, since they just say WHAT the code does, line by line,
		// rather than WHY the code was written like that. Six months from now,
		// it's the reasoning behind doing what you did that will be valuable to
		// you!
		// 2. Unit tests are cumulative, and many things are based on add(), so
		// make sure that you get this one correct.
		add(c,this.size());//add char c to the end of this tree

	}

	/**
	 * 
	 * @param c
	 *            character to add
	 * @param pos
	 *            character added in this inorder position
	 * @throws IndexOutOfBoundsException
	 *             id pos is negative or too large for this tree
	 */
	public void add(char c, int pos) throws IndexOutOfBoundsException {
		if(pos < 0 || pos > this.size()){
			throw new IndexOutOfBoundsException();
		}
		//empty tree and construct a root
		if(this.root == NULL_NODE){
			this.root = new Node(c, NULL_NODE);
		}
		
		//some nodes are already in the tree
		else{
			
			Node prev = this.root.add(c, pos);
			Node curr = prev.getParent();
			
			while(curr != NULL_NODE){//look up from current node to root
				Node.Code dir = (curr.left == prev) ? Node.Code.LEFT : Node.Code.RIGHT;
				
				
				//check the node balance
				if(curr.balance == Node.Code.SAME){//current is balanced
					curr.balance = dir;
					prev = curr;
					curr = curr.getParent();
				}
				//add a right node to / or add a left node to \ 
				else if(curr.balance != dir){//add this new node make the height balanced
					curr.balance = Node.Code.SAME;
					break;
				}
				//
				else{//this is where we need rotation
					Node rotateNode = null;
					Node parent = curr.getParent();
					
					rotateNode = triNodeRotate(curr);
					
					if(parent == NULL_NODE){//this node is a root
						this.root = rotateNode;
						this.root.setParent(NULL_NODE);
					}
					else { //this node is inside the tree
						if(parent.left == curr){
							parent.left = rotateNode;
						}
						else {
							parent.right = rotateNode;
						}
						rotateNode.setParent(parent);
						
					}
					
					break;
				}//end else
				
			}//end while
		}//end outer else
		

	}//end method

	private Node triNodeRotate(Node unbalanced) {
		if(unbalanced == NULL_NODE){
			throw new RuntimeException("Cannot be a NULL_NODE");
		}
		if(unbalanced.balance == Node.Code.LEFT){//left subtree is higher
			return triNodeRotate(unbalanced, unbalanced.left);
		}
		else if(unbalanced.balance == Node.Code.RIGHT){//right subtree is higher
			return triNodeRotate(unbalanced, unbalanced.right);
		}
		else{//the node is balanced
			return unbalanced;
		}
	}

	private Node triNodeRotate(Node unbalanced, Node unbalChild) {
		//validation
		if(unbalanced == NULL_NODE || unbalChild == NULL_NODE){
			throw new RuntimeException("Cannot be a NULL_NODE");
		}
		if(!unbalChild.getParent().equals(unbalanced)){
			throw new RuntimeException("wrong child node");
		}
		
		//do rotate
		if(unbalChild.balance ==Node.Code.LEFT){
			return triNodeRotate(unbalanced, unbalChild, unbalChild.left);
		}
		else if(unbalChild.balance == Node.Code.RIGHT){
			return triNodeRotate(unbalanced, unbalChild, unbalChild.right);
		}
		else{//this happens in deletion
			if(unbalanced.left.equals(unbalChild)){
				return triNodeRotate(unbalanced, unbalChild, unbalChild.left);
			}
			else 
				return triNodeRotate(unbalanced, unbalChild, unbalChild.right);
		}

	}

	//here is where actual rotation happens
	//idea from http://www.mathcs.emory.edu/~cheung/Courses/323/Syllabus/Trees/AVL-insert.html
	//and https://www.ics.uci.edu/~pattis/ICS-23/lectures/notes/AVL.txt
	private Node triNodeRotate(Node unbalanced, Node unbalChild, Node unbalGrandChild) {
		if(unbalanced == NULL_NODE || unbalChild == NULL_NODE || unbalGrandChild == NULL_NODE){
			throw new RuntimeException("Cannot be a NULL_NODE");
		}
		if(!unbalGrandChild.getParent().equals(unbalChild) || !unbalChild.getParent().equals(unbalanced)){
				throw new RuntimeException("wrong child node");
		}
		
		//
		Node a = null, b = null, c = null;
		Node t0 = null, t1= null, t2 = null, t3 = null;
		
		//check rotation tyoe
		int single;
		/**
		 * 			unbal     or      unbal
		 * 			/					\
		 * 	   unbalChild              unbalChild
		 * 			/						\
		 * unbalGrandChild                unbalGrandChild
		 * 
		 * when this occurs, we only need to to single left or single right
		 * 
		 * new structure will be 
		 * 					
		 * 			unbalChild         or          unbalChild
		 * 			   /	\						/     \
		 * 		unbalGrand   unbal           unbal    unbalGrand
		 * 
		 */
		if((unbalanced.left == unbalChild && unbalChild.left == unbalGrandChild) || (unbalanced.right == unbalChild && unbalChild.right == unbalGrandChild)){
			single = 1;
		}
		else single = 0;
		
		if(single == 1){
			this.rotationCount++; //Increment counter
			b = unbalChild;
			if(unbalanced.left == unbalChild && unbalChild.left == unbalGrandChild){
				//single right
				a = unbalGrandChild;
				c = unbalanced;
				t1 = a.right;
				t2 = b.right;
				
				if(b.balance == Node.Code.SAME){
					b.balance = Node.Code.RIGHT;
					c.balance = Node.Code.LEFT;
				}
				else{
					b.balance = Node.Code.SAME;
					c.balance = Node.Code.SAME;
				}
				
				//update rank
				c.rank = c.rank - b.rank - 1;
				
			}//end single  right rotate
			
			else { //single left
				a = unbalanced;
				c = unbalGrandChild;
				
				t1 = b.left; 
				t2 = c.left;
				
				if(b.balance == Node.Code.SAME){
					b.balance = Node.Code.LEFT;
					a.balance = Node.Code.RIGHT;
				}
				else {
					b.balance = Node.Code.SAME;
					a.balance = Node.Code.SAME;
					
				}
				
				//update rank
				b.rank = b.rank + a.rank + 1;
			}
				
		}// end if single
		
		
		
		/**
		 * 				unbal        or          unbal
		 * 				/							\
		 * 			unbalChild 					unbalChild
		 * 				\							/
		 * 				unbalGrandChild			unbalGrandChild
		 * 
		 * when this structure happens,  the rotation will be double left or double right
		 * 
		 * and structure will be
		 * 
		 * 
		 * 				unbalGrand    		or 			unbalGrand
		 * 			/				\				 /				\
		 * 		unbalChild			unbal		unbal				unbalChild
		 * 
		 * 
		 * 
		 * 
		 */
		else {//if single == 0, we do double rotate
			
			//update rotation count
			this.rotationCount = this.rotationCount + 2;
			b = unbalGrandChild;
			t1 = b.left;
			t2 = b.right;
			
			if(unbalanced.right == unbalChild && unbalChild.left == unbalGrandChild){//structure 1 double left
				a = unbalanced;
				c = unbalChild;
				
				c.rank = c.rank - b.rank - 1;
			}
			
			else { // structure 2, double left
				a = unbalChild;
				c = unbalanced;
				
				c.rank = c.rank - b.rank - a.rank - 2;
			}
			
			if(b.balance == Node.Code.SAME){
				a.balance = Node.Code.SAME;
				c.balance = Node.Code.SAME;
			}
			else if(b.balance == Node.Code.LEFT){
				a.balance = Node.Code.SAME;
				c.balance = Node.Code.RIGHT;
			}
			else {//b.balance == Node.Code.RIGHT
				a.balance = Node.Code.LEFT;
				c.balance = Node.Code.SAME;
			}
			
			b.balance = Node.Code.SAME;
			
			b.rank = b.rank + a.rank + 1;
		}//end double rotate
		
		//reconnect subtree to root
		t0 = a.left;
		t3 = c.right;
		b.left = a;
		b.right = c;
		a.left = t0;
		a.right = t1;
		c.left = t2;
		c.right = t3;
		
		
		a.setParent(b);
		c.setParent(b);
		t0.setParent(a);
		t1.setParent(a);
		t2.setParent(c);
		t3.setParent(c);
		
		
		return b;//return root
	}

	/**
	 * 
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return this.root.size();
	}

	/**
	 * 
	 * @param pos
	 *            position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		// Implementation requirement:
		// When deleting a node with two children, you normally replace the
		// node to be deleted with either its in-order successor or predecessor.
		// The tests assume assume that you will replace it with the
		// *successor*.
		return '#'; // replace by a real calculation.
	}

	/**
	 * This method operates in O(length*log N), where N is the size of this
	 * tree.
	 * 
	 * @param pos
	 *            location of the beginning of the string to retrieve
	 * @param length
	 *            length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException
	 *             unless both pos and pos+length-1 are legitimate indexes
	 *             within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		return "";
	}

	/**
	 * This method is provided for you, and should not need to be changed. If
	 * split() and concatenate() are O(log N) operations as required, delete
	 * should also be O(log N)
	 * 
	 * @param start
	 *            position of beginning of string to delete
	 * 
	 * @param length
	 *            length of string to delete
	 * @return an EditTree containing the deleted string
	 * @throws IndexOutOfBoundsException
	 *             unless both start and start+length-1 are in range for this
	 *             tree.
	 */
	public EditTree delete(int start, int length)
			throws IndexOutOfBoundsException {
		if (start < 0 || start + length >= this.size())
			throw new IndexOutOfBoundsException(
					(start < 0) ? "negative first argument to delete"
							: "delete range extends past end of string");
		EditTree t2 = this.split(start);
		EditTree t3 = t2.split(length);
		this.concatenate(t3);
		return t2;
	}

	/**
	 * Append (in time proportional to the log of the size of the larger tree)
	 * the contents of the other tree to this one. Other should be made empty
	 * after this operation.
	 * 
	 * @param other
	 * @throws IllegalArgumentException
	 *             if this == other
	 */
	public void concatenate(EditTree other) throws IllegalArgumentException {

	}

	/**
	 * This operation must be done in time proportional to the height of this
	 * tree.
	 * 
	 * @param pos
	 *            where to split this tree
	 * @return a new tree containing all of the elements of this tree whose
	 *         positions are >= position. Their nodes are removed from this
	 *         tree.
	 * @throws IndexOutOfBoundsException
	 */
	public EditTree split(int pos) throws IndexOutOfBoundsException {
		return null; // replace by a real calculation.
	}

	/**
	 * Don't worry if you can't do this one efficiently.
	 * 
	 * @param s
	 *            the string to look for
	 * @return the position in this tree of the first occurrence of s; -1 if s
	 *         does not occur
	 */
	public int find(String s) {
		return -2;
	}

	/**
	 * 
	 * @param s
	 *            the string to search for
	 * @param pos
	 *            the position in the tree to begin the search
	 * @return the position in this tree of the first occurrence of s that does
	 *         not occur before position pos; -1 if s does not occur
	 */
	public int find(String s, int pos) {
		return -2;
	}

	/**
	 * @return The root of this tree.
	 */
	public Node getRoot() {
		return this.root;
	}
}
