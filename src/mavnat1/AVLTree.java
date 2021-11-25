package mavnat1;

import java.util.Stack;

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with distinct integer keys and info.
 *
 */

public class AVLTree {

	public static void main(String[] args) {
		System.out.println("Starting...");
		AVLNode node = new AVLNode("hello", 0);

	}

	private IAVLNode root;

	/**
	 * public boolean empty()
	 *
	 * Returns true if and only if the tree is empty.
	 *
	 */
	public boolean empty() {
		return this.root == null;
	}

	/**
	 * public String search(int k)
	 *
	 * Returns the info of an item with key k if it exists in the tree. otherwise,
	 * returns null.
	 */
	public String search(int k) {
		return "searchDefaultString"; // to be replaced by student code
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * Inserts an item with key k and info i to the AVL tree. The tree must remain
	 * valid, i.e. keep its invariants. Returns the number of re-balancing
	 * operations, or 0 if no re-balancing operations were necessary. A
	 * promotion/rotation counts as one re-balance operation, double-rotation is
	 * counted as 2. Returns -1 if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) {
		IAVLNode node = this.root;
		IAVLNode insertedNode = new AVLNode(i, k);
		insertedNode.setHeight(0);
		insertedNode.setLeft(new AVLNode());
		insertedNode.setRight(new AVLNode());
		int reBalanceCount = 0;

		// if tree is empty
		if (node == null) {
			this.root = insertedNode;
			return 0;
		}

		boolean notInserted = true;

		while (notInserted) {
			if (k > node.getKey()) {
				if (!node.getRight().isRealNode()) {
					node.setRight(insertedNode);
					break;
				}
				node = node.getRight();
			}
			else if(k < node.getHeight()){
				if (!node.getLeft().isRealNode()) {
					node.setLeft(insertedNode);
					break;
				}
				node = node.getLeft();
			}
			else {
				return -1;
			}
		}
		
		while (node != null) {
			reBalanceCount += rebalanceNode(node);

			node = node.getParent();
		}

		return reBalanceCount; 
	}

	/**
	 * public int delete(int k)
	 *
	 * Deletes an item with key k from the binary tree, if it is there. The tree
	 * must remain valid, i.e. keep its invariants. Returns the number of
	 * re-balancing operations, or 0 if no re-balancing operations were necessary. A
	 * promotion/rotation counts as one re-balance operation, double-rotation is
	 * counted as 2. Returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) {
		int reBalanceCount = 0;
		IAVLNode node = this.getNode(this.root, k);

		if (node == null) {
			return -1;
		}

		if (!node.getRight().isRealNode() || !node.getLeft().isRealNode()) {
			node = deleteNodeEasyCase(node);
		}

		// delete node with 2 children:
		// find successor.
		IAVLNode successor = getSuccessor(node);

		// replace node to be deleted with it.
		IAVLNode newRoot = new AVLNode(successor.getValue(), successor.getKey());
		newRoot.setRight(node.getRight());
		newRoot.getRight().setParent(newRoot);
		newRoot.setLeft(node.getLeft());
		newRoot.getLeft().setParent(newRoot);

		// delete successor and keep only the new one.
		if (k < successor.getKey()) {
			node = deleteNodeEasyCase(node.getRight());
		} else {
			node = deleteNodeEasyCase(node.getLeft());
		}

		// rebalance until root.
		while (node != null) {
			reBalanceCount += rebalanceNode(node);

			node = node.getParent();
		}
		
		return reBalanceCount;
	}

	public void setRoot(IAVLNode root) {
		this.root = root;
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null if
	 * the tree is empty.
	 */
	public String min() {
		if (this.root == null) {
			return null;
		}
		if (this.root.getLeft() == null) {
			return this.root.getValue();
		}
		return minRecursive(this.root.getLeft());
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty.
	 */
	public String max() {
		if (this.root == null) {
			return null;
		}
		if (this.root.getRight() == null) {
			return this.root.getValue();
		}
		return maxRecursive(this.root.getRight());
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 */
	public int[] keysToArray() {
		int[] array = new int[this.size()];
		int index = 0;
		IAVLNode node = this.root;
		
		Stack<IAVLNode> passedNodes = new Stack<IAVLNode>();
		
		while (node != null || passedNodes.size() > 0) {
			while (node != null) {
				passedNodes.push(node);
				node = node.getLeft();
			}
		}
		
		node = passedNodes.pop();

		array[index] = node.getKey();
		index++;

		node = node.getRight();

		return array;
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		String[] array = new String[this.size()];
		int index = 0;
		IAVLNode node = this.root;

		Stack<IAVLNode> passedNodes = new Stack<IAVLNode>();

		while (node != null || passedNodes.size() > 0) {
			while (node != null) {
				passedNodes.push(node);
				node = node.getLeft();
			}

		}

		node = passedNodes.pop();

		array[index] = node.getValue();
		index++;

		node = node.getRight();

		return array;
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 */
	public int size() {
		return sizeRecursive(this.root);
	}

	
	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 */
	public IAVLNode getRoot() {
		return this.root;
	}

	/**
	 * public AVLTree[] split(int x)
	 *
	 * splits the tree into 2 trees according to the key x. Returns an array [t1,
	 * t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * 
	 * precondition: search(x) != null (i.e. you can also assume that the tree is
	 * not empty) postcondition: none
	 */
	public AVLTree[] split(int x) {
		IAVLNode node = getNode(this.root, x);
		AVLTree rightTree = new AVLTree();
		rightTree.setRoot(node.getRight());
		AVLTree leftTree = new AVLTree();
		rightTree.setRoot(node.getLeft());

		while (node != null) {
			if (node.getParent().getRight() == node) {
				node = node.getParent();
				AVLTree smallerTree = new AVLTree();
				smallerTree.setRoot(node.getLeft());
				smallerTree.join(node, leftTree);

				leftTree = smallerTree;
			} else {
				node = node.getParent();
				AVLTree biggerTree = new AVLTree();
				biggerTree.setRoot(node.getRight());
				rightTree.join(node, biggerTree);
			}
		}

		AVLTree[] result = new AVLTree[2];
		result[0] = leftTree;
		result[1] = rightTree;

		return result;
	}

	/**
	 * public int join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. Returns the complexity of the operation
	 * (|tree.rank - t.rank| + 1).
	 *
	 * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be
	 * empty (rank = -1). postcondition: none
	 */
	public int join(IAVLNode x, AVLTree t) {
		return -1;
	}

	/*
	 * private helper methods.
	 */
	private String maxRecursive(IAVLNode node) {
		if (node.getRight() == null) {
			return node.getValue();
		}
		return maxRecursive(node.getRight());
	}
	
	private String minRecursive(IAVLNode node) {
		if (node.getLeft() == null) {
			return node.getValue();
		}
		return maxRecursive(node.getLeft());
	}

	/*
	 * gets node by key, if the node is virtual returns null.
	 */
	private IAVLNode getNode(IAVLNode root, int key) {
		if (root.getKey() == key) {
			return root;
		}
		if (root == null || !root.isRealNode()) {
			return null;
		}

		if (key > root.getKey()) {
			return this.getNode(root.getRight(), key);
		}
		return this.getNode(root.getLeft(), key);
	}

	private IAVLNode getSuccessor(IAVLNode node) {
		int key = node.getKey();
		if (node.getRight() != null && node.getRight().isRealNode()) {
			node = node.getRight();
			while (node.getLeft() != null && node.getLeft().isRealNode()) {
				node = node.getLeft();
			}
			return node;
		}
		if (node.getLeft() != null && node.getLeft().isRealNode()) {
			while (node.getRight() != null && node.getRight().isRealNode()) {
				node = node.getRight();
			}
			return node;
		}
		return null;
	}

	private IAVLNode rotateLeft(IAVLNode root, int[] rotatingCount) {
		IAVLNode newRoot = root.getRight();
		IAVLNode oldRootRightTree = newRoot.getLeft();

		// Rotation
		newRoot.setLeft(root);
		root.setRight(oldRootRightTree);

		// Update heights
		root.setHeight(Math.max(root.getLeft().getHeight(), root.getRight().getHeight()) + 1);
		newRoot.setHeight(Math.max(newRoot.getLeft().getHeight(), newRoot.getRight().getHeight()) + 1);
		
		rotatingCount[0] += setHeight(root) + setHeight(newRoot);

		return newRoot;
	}

	private IAVLNode rotateRight(IAVLNode root, int[] rotatingCount) {
		IAVLNode newRoot = root.getLeft();
		IAVLNode oldRootLeftTree = newRoot.getRight();

		// Rotation
		newRoot.setRight(root);
		root.setLeft(oldRootLeftTree);

		// Update heights
		root.setHeight(Math.max(root.getLeft().getHeight(), root.getRight().getHeight()) + 1);
		newRoot.setHeight(Math.max(newRoot.getLeft().getHeight(), newRoot.getRight().getHeight()) + 1);
		
		rotatingCount[0] += setHeight(root) + setHeight(newRoot);
		
		return newRoot;
	}

	private int getBalance(IAVLNode node) {
		return node.getLeft().getHeight() - node.getRight().getHeight();
	}

	private IAVLNode deleteNodeEasyCase(IAVLNode node) {
		if (node.getRight().isRealNode()) {
			node = node.getRight();
			return node;
		} else if (node.getLeft().isRealNode()) {
			node = node.getLeft();
			return node;
		} else {
			IAVLNode parent = node.getParent();
			IAVLNode virutal = new AVLNode(null, -1);
			parent.setLeft(virutal);
			parent.setRight(virutal);
			node = null;
			return parent;
		}
	}

	private int rebalanceNode(IAVLNode node) {
		int reBalanceCount = 0;
		reBalanceCount += setHeight(node);
		int[] rotatingCount = new int [1];

		int balance = getBalance(node);
		// left is lower than right.
		if (balance < -1) {
			// checks if case 2 or 3
			if (node.getRight().getRight().getHeight() <= node.getRight().getLeft().getHeight()) {
				node = rotateLeft(node, rotatingCount);
			}
			// case 4
			else {
				node.setRight(rotateRight(node.getRight(), rotatingCount));
				node = rotateLeft(node, rotatingCount);
			}
		} // same for the other side
		else if (balance > 1) {
			if (node.getLeft().getLeft().getHeight() >= node.getLeft().getRight().getHeight()) {
				node = rotateRight(node, rotatingCount);
			} else {
				node.setLeft(rotateLeft(node.getLeft(), rotatingCount));
				node = rotateRight(node, rotatingCount);
			}
		}

		return reBalanceCount + rotatingCount[0];
	}
	
	//return 1 if promoted/demoted.
	private int setHeight(IAVLNode node) {
		int newHeight = Math.max(node.getLeft().getHeight(), root.getRight().getHeight()) + 1;
		if (newHeight != node.getHeight()) {
			node.setHeight(newHeight);
			return 1;
		}
		
		return 0;
	}
	
	private int sizeRecursive(IAVLNode node) {
		if (!node.isRealNode()) {
			return 0;
		}
		return sizeRecursive(node.getLeft()) + sizeRecursive(node.getRight()) + 1;
	}
	
	public void printTree() {
		int size = this.size();
		int height = (int)(Math.log10(size) / Math.log10(2));
		int width = (int) Math.pow(2, height);
		int[][] map = new int[height][width];
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				
			}
		}
	}

	/**
	 * public interface IAVLNode ! Do not delete or modify this - otherwise all
	 * tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); // Returns node's key (for virtual node return -1).

		public String getValue(); // Returns node's value [info], for virtual node returns null.

		public void setLeft(IAVLNode node); // Sets left child.

		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.

		public void setRight(IAVLNode node); // Sets right child.

		public IAVLNode getRight(); // Returns right child, if there is no right child return null.

		public void setParent(IAVLNode node); // Sets parent.

		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.

		public void setHeight(int height); // Sets the height of the node.

		public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree (for example AVLNode), do
	 * it in this file, not in another file.
	 * 
	 * This class can and MUST be modified (It must implement IAVLNode).
	 */
	public class AVLNode implements IAVLNode {
		// fields.
		private int key;
		private int rank;
		private String info;
		private IAVLNode left;
		private IAVLNode parent;
		private IAVLNode right;

		// Constructors.

		// create virtual node.
		public AVLNode() {
			this.rank = -1;
		}

		public AVLNode(String info, int key) {
			this.info = info;
			this.key = key;
		}

		public int getKey() {
			if (this.rank == -1) {
				return -1;
			}
			return this.key;
		}

		public String getValue() {
			return this.info;
		}

		public void setLeft(IAVLNode node) {
			this.left = node;
		}

		public IAVLNode getLeft() {
			return this.left;
		}

		public void setRight(IAVLNode node) {
			this.right = node;
		}

		public IAVLNode getRight() {
			return this.right;
		}

		public void setParent(IAVLNode node) {
			this.parent = node;
		}

		public IAVLNode getParent() {
			return this.parent;
		}

		public boolean isRealNode() {
			// node with rank of -1 isn't real.
			return this.rank != -1;
		}

		// TODO: Check about rank and height
		public void setHeight(int height) {
			this.rank = height;
		}

		public int getHeight() {
			return this.rank;
		}
	}

}