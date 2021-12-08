package mavnat1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * AVLTree
 * <p>
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 */

public class AVLTree {
	
	private static void shuffleArray(int[] ar)
	  {
	    // If running on Java 6 or older, use `new Random()` on RHS here
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	
    public static void main(String[] args) {
    	for(int i = 1; i < 11; i++) {
    		
    		AVLTree tree = new AVLTree();
    		int n = (int)(1000 * Math.pow(2, i));
    		int[] array = new int[n];
    		for(int j = 0; j < n; j++) {
    			array[j] = n - j;
    		}
    		int swapCost = 0;
    		/*for(int j = 0; j < n - 1; j++) {
    			for(int k = j + 1; k < n; k++) {
    				if(array[k] < array[j]) {
    					swapCost += 1;
    				}
    			}
    		}*/
    		int[] insertionCost = new int[]{1};
    		/*for(int item : array) {
    			tree.insertFST(item, insertionCost);
    			tree.insert(item, "" + item);
    		}*/
    		
    		double prediction = 0;
    		/*for(int b = 1; b < n + 1; b++) {
    			prediction += Math.log(b) / Math.log(2);;
    		}*/
    		System.out.println("----" + n + "-----");
    		//System.out.println("REVERSED: swap cost: " + swapCost + ". insertion cost: " + insertionCost[0] 
    			//	+ ". prediction: " + prediction);
    		
    		shuffleArray(array);
    		
    		
    		tree = new AVLTree();
    		AVLTree tree1 = new AVLTree();
    		swapCost = 0;
    		/*for(int j = 0; j < n - 1; j++) {
    			for(int k = j + 1; k < n; k++) {
    				if(array[k] < array[j]) {
    					swapCost += 1;
    				}
    			}
    		}*/
    		insertionCost = new int[]{1};
    		for(int item : array) {
    			//tree.insertFST(item, insertionCost);
    			tree.insert(item, "" + item);
    			tree1.insert(item, "" + item);
    		}
    		//[0]: count, [1]: sum, [2]: maximal
    		int[] averageComponents = new int[] {0,0,0};
    		Random rnd = new Random();
    		int randomKey = rnd.nextInt(n-1) + 1;
    		AVLTree[] treeArray = tree.splitTheoretical(randomKey, averageComponents);
    		//System.out.println("RANDOM: swap cost: " + swapCost + ". insertion cost: " + insertionCost[0]);
    		System.out.println("RANDOM: maximal join cost: " + averageComponents[2] + ". average join cost: " + (double)averageComponents[1] / averageComponents[0]);
    		averageComponents = new int[] {0,0,0};
    		//tree = treeArray[0];
    		//tree.join(new AVLNode(randomKey, "" + randomKey) , treeArray[1]);
    		tree1.splitTheoretical(findMaxOfSubLeft(tree1.getRoot()), averageComponents);
    		System.out.println("MAX OF LEFT SUBTREE: maximal join cost: " + averageComponents[2] + ". average join cost: " + (double)averageComponents[1] / averageComponents[0]);
    		System.out.println("---------");
    	}
    	
    	
    }
    
    private static int findMaxOfSubLeft(IAVLNode node) {
    	node = node.getLeft();
    	if(!node.isRealNode()) {
    		return node.getParent().getKey();
    	}
    	while(node.getRight().isRealNode()) {
    		node = node.getRight();
    	}
		return node.getKey();
	}

    private IAVLNode root;
    private IAVLNode max;
    private IAVLNode min;



    /**
     * public boolean empty()
     * <p>
     * Returns true if and only if the tree is empty.
     */
    public boolean empty() {
        return this.root == null;
    }
    
    public int insertFST(int k, int[] count) {
    	if(this.empty()) {
    		return 1;
    	}
    	
    	count[0]++;
    	
    	IAVLNode node = this.max;
    	
    	while(node.getParent() != null && k < node.getKey()) {
    		node = node.getParent();
    		count[0]++;
    	}
    	if(node.getParent() == null) {
    		treePositionFST(node, k, count);
    	}
    	else {
    		count[0]--;
    		 treePositionFST(node.getRight(), k, count);
    	}
    	
    	return count[0];
    }
    
    /**
     * public String search(int k)
     * <p>
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k) {
        if (this.empty()) // the tree is empty
            return null;
        IAVLNode node = treePosition(this.root, k);
        if (node.getKey() == k) { // key is in the tree
            return node.getValue();
        }
        return null; // key is not in the tree
    }


    /**
     * public IAVLNode treePosition(IAVLNode node, int k)
     * <p>
     * Look for k in the subtree of node.
     * Returns the last node encountered.
     */
    public IAVLNode treePosition(IAVLNode node, int k) { // complexity O(log(n))
        IAVLNode final_node = node;
        while (node.isRealNode()) {
            final_node = node;
            if (node.getKey() == k) { // the key exist in the subtree
                return node;
            }
            else if (node.getKey() > k) { // looking at the left subtree
                node = node.getLeft();
            }
            else { // looking at the right subtree
                node = node.getRight();
            }
        }
        return final_node;
    }
    
    /**
     * public IAVLNode treePosition(IAVLNode node, int k)
     * <p>
     * Look for k in the subtree of node.
     * Returns the last node encountered.
     */
    private IAVLNode treePositionFST(IAVLNode node, int k, int[] count) {
    	// complexity O(log(n))
        IAVLNode final_node = node;
        while (node.isRealNode()) {
            final_node = node;
            if (node.getKey() == k) { // the key exist in the subtree
                return node;
            }
            else if (node.getKey() > k) { // looking at the left subtree
            	count[0]++;
                node = node.getLeft();
            }
            else { // looking at the right subtree
                node = node.getRight();
                count[0]++;
            }
        }
        
        return final_node;
    }

    /**
     * public int insert(int k, String i)
     * <p>
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        IAVLNode node = this.root;
        IAVLNode insertedNode = new AVLNode(k, i);
        insertedNode.setHeight(0);
        insertedNode.setLeft(new AVLNode());
        insertedNode.setRight(new AVLNode());

        // if tree is empty
        if (node == null) {
            this.root = insertedNode;
            this.root.setParent(null);
            updateSize(this.root);
            this.max = insertedNode;
            this.min = insertedNode;
            return 0;
        }
        if (this.min.getKey() > insertedNode.getKey()) {
            this.min = insertedNode;
        }
        if (this.max.getKey() < insertedNode.getKey()) {
            this.max = insertedNode;
        }

        boolean notInserted = true;

        //Insertion.
        while (notInserted) {
            if (k > node.getKey()) {
                if (!node.getRight().isRealNode()) { //can't go down further to the right, insert our node.
                    node.setRight(insertedNode);
                    insertedNode.setParent(node);
                    break;
                }
                node = node.getRight();
            }
            else if (k < node.getKey()){
                if (!node.getLeft().isRealNode()) { //can't go down further to the left, insert our node.
                    node.setLeft(insertedNode);
                    insertedNode.setParent(node);
                    break;
                }
                node = node.getLeft();
            }
            else {
                return -1;
            }
        }
        //update size.
        node.setSize();
        updateSize(node);
        
        //rebalancing up to root.
        IAVLNode node_for_rebalance = node;
        int[] cost = {0};
        IAVLNode last_node = null;
        while (node_for_rebalance != null) {
            last_node = node_for_rebalance;
            node_for_rebalance = rebalance(node_for_rebalance, cost);
        }
        updateSize(last_node);
        return cost[0];
    }



    /**
     * public int delete(int k)
     * <p>
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        if (this.empty()) { // key is not in the tree
            return -1;
        }
        if (this.root.getKey() == k && !this.root.getLeft().isRealNode() && !this.root.getRight().isRealNode()) {
            this.root = null;
            return 0;
        }
        IAVLNode node = treePosition(this.root, k);
        if (node.getKey() != k) {
            return -1;
        }
        if (this.min.getKey() == node.getKey()) {
            this.min = successor(node);
        }
        if (this.max.getKey() == node.getKey()) {
            this.max = predecessor(node);
        }
        IAVLNode node_for_rebalance;
        if (nodeIsLeafOrUnary(node)) {
            node_for_rebalance = deleteLeafOrUnary(node);
        }
        else {
            node_for_rebalance = deleteBinary(node);
        }
        int[] cost = {0};
        IAVLNode last_node = null;
        while (node_for_rebalance != null) {
            last_node = node_for_rebalance;
            node_for_rebalance = rebalance(node_for_rebalance, cost);
        }
        if (last_node != null) {
            updateSize(last_node);
        }
        return cost[0];
    }

    /**
     * public Boolean nodeIsLeafOrUnary(IAVLNode node)
     * <p>
     * Returns true if the node is a leaf or a unary node,
     * false means that the node is binary node
     */
    public Boolean nodeIsLeafOrUnary(IAVLNode node) {
        return !node.getRight().isRealNode() || !node.getLeft().isRealNode();
    }



    /**
     * public IAVLNode delete_leaf(IAVLNode node)
     * <p>
     * Deletes the given node.
     * The given node is a leaf or unary node.
     * Returns node's parent.
     * if the node is the root, the tree become empty and Returns null.
     */
    public IAVLNode deleteLeafOrUnary(IAVLNode node) { // complexity O(1)
        IAVLNode node_parent = node.getParent();
        if (node_parent == null) {
            if (!node.getRight().isRealNode()) {
                this.root = node.getLeft();
                return this.root;
            }
            else if (!node.getLeft().isRealNode()) {
                this.root = node.getRight();
                return this.root;
            }
            else {
                this.root = null;
                return null;
            }
        }
        if (node_parent.getKey() > node.getKey()) {
            if (!node.getRight().isRealNode()) {
                node_parent.setLeft(node.getLeft());
                node.getLeft().setParent(node_parent);
            }
            else {
                node_parent.setLeft(node.getRight());
                node.getRight().setParent(node_parent);
            }
        }
        else {
            if (!node.getRight().isRealNode()) {
                node_parent.setRight(node.getLeft());
                node.getLeft().setParent(node_parent);
            }
            else {
                node_parent.setRight(node.getRight());
                node.getRight().setParent(node_parent);
            }
        }
        node.setParent(null);
        node.setLeft(null);
        updateSize(node_parent);
        return node_parent;
    }


    /**
     * public void updateSize(IAVLNode node)
     * <p>
     * the function updates the size of the nodes from given node up to the root
     */
    public void updateSize(IAVLNode node) { // complexity O(log(n))
        while (node.getParent() != null) {
            node.setSize();
            node = node.getParent();
        }
        node.setSize();
    }



    /**
     * IAVLNode deleteBinary(IAVLNode node)
     * <p>
     * Deletes the given node.
     * The given node is a binary node.
     * Returns successor origin parent.
     */
    public IAVLNode deleteBinary(IAVLNode node) { // complexity O(log(n))
        IAVLNode successor = successor(node);
        IAVLNode node_parent = node.getParent();
        if (node_parent == null) { // if the node to delete is the root
            this.root = successor;
        }
        else {
            if (node_parent.getKey() > node.getKey()) {
                node_parent.setLeft(successor);
            } else {
                node_parent.setRight(successor);
            }
        }
        IAVLNode successor_origin_parent = deleteLeafOrUnary(successor); // remove the successor from the tree
        successor.setHeight(node.getHeight()); // insert and update successor new place at the tree
        successor.setLeft(node.getLeft());
        successor.setRight(node.getRight());
        successor.setParent(node_parent);
        node.getRight().setParent(successor);
        node.getLeft().setParent(successor);
        node.setParent(null); // delete the origin node
        node.setLeft(null);
        node.setRight(null);
        if (successor_origin_parent.getLeft() == null) {
            updateSize(successor);
            return successor;
        }
        updateSize(successor_origin_parent);
        return successor_origin_parent; // return the successor origin parent for next rebalancing
    }

    /**
     * public IAVLNode successor(IAVLNode node)
     * <p>
     * Returns the successor of given node
     */
    public IAVLNode successor(IAVLNode node) { // complexity O(log(n))
        IAVLNode successor;
        if (node.getRight().isRealNode()) { // successor is the min node in the right subtree
            successor = node.getRight();
            while (successor.getLeft().isRealNode()) {
                successor = successor.getLeft();
            }
        }
        else { // successor is the lowest ancestor that node is in its left subtree
            successor = node.getParent();
            while (successor != null && successor.getRight() == node) {
                node = successor;
                successor = node.getParent();
            }
        }
        return successor;
    }



    /**
     * public IAVLNode predecessor(IAVLNode node)
     * <p>
     * Returns the predecessor of given node
     */
    public IAVLNode predecessor(IAVLNode node) { // complexity O(log(n))
        IAVLNode predecessor;
        if (node.getLeft().isRealNode()) { // predecessor is the max node in the left subtree
            predecessor = node.getLeft();
            while (predecessor.getRight().isRealNode()) {
                predecessor = predecessor.getRight();
            }
        }
        else { // predecessor is the lowest ancestor that node is in its right subtree
            predecessor = node.getParent();
            while (predecessor != null && predecessor.getLeft() == node) {
                node = predecessor;
                predecessor = node.getParent();
            }
        }
        return predecessor;
    }

    /**
     * IAVLNode rightRotate(IAVLNode node)
     * <p>
     * the function makes one right rotate
     */
    public void rightRotation(IAVLNode node) { // complexity O(1)
        IAVLNode node_son = node.getLeft();
        if (this.root.getKey() == node.getKey()) {
            this.root = node_son;
        }
        node.setLeft(node_son.getRight());
        node_son.getRight().setParent(node);
        node_son.setRight(node);
        node_son.setParent(node.getParent());
        node.setParent(node_son);
        updateParentsSon(node, node_son);
    }


    /**
     * IAVLNode leftRotate(IAVLNode node)
     * <p>
     * the function makes one left rotate
     */
    public void leftRotation(IAVLNode node) { // complexity O(1)
        IAVLNode node_son = node.getRight();
        if (this.root.getKey() == node.getKey()) {
            this.root = node_son;
        }
        node.setRight(node_son.getLeft());
        node_son.getLeft().setParent(node);
        node_son.setLeft(node);
        node_son.setParent(node.getParent());
        node.setParent(node_son);
        updateParentsSon(node, node_son);
    }


    /**
     * public void updateParentsSon(IAVLNode node, IAVLNode node_son)
     * <p>
     * the function updates the node.parent.son to be node.son
     */
    public void updateParentsSon(IAVLNode node, IAVLNode node_son) { // complexity O(1)
        if (node_son.getParent() != null) { // node_son is not root
            if (node_son.getParent().getKey() > node.getKey()) {
                node_son.getParent().setLeft(node_son);
            }
            else {
                node_son.getParent().setRight(node_son);
            }
        }
    }

    /**
     * IAVLNode leftRotate(IAVLNode node)
     * <p>
     * the function makes one left rotate
     */
    public void doubleRotationRightLeft(IAVLNode node) { // complexity O(1)
        IAVLNode node_son = node.getRight(); // start the rotation from the right son
        rightRotation(node_son);
        leftRotation(node);
    }


    /**
     * IAVLNode leftRotate(IAVLNode node)
     * <p>
     * the function makes one left rotate
     */
    public void doubleRotationLeftRight(IAVLNode node) { // complexity O(1)
        IAVLNode node_son = node.getLeft(); // start the rotation from left son
        leftRotation(node_son);
        rightRotation(node);
    }
    
    /*
     * Gets the node type as referred to in the class lectures.
     * nodeType[0] represents left child and nodeType[1] represents right child.
     */
    private int[] getNodeType(IAVLNode node) {
        int[] nodeType = new int[] {node.getHeight() - node.getLeft().getHeight(),
                node.getHeight() - node.getRight().getHeight()};
        return nodeType;
    }


    public IAVLNode rebalance(IAVLNode node, int[] cost) {
        int[] nodeType = getNodeType(node);
        // node is in balance
        if ((nodeType[1] == 1 && (nodeType[0] == 1 || nodeType[0] == 2)) || (nodeType[1] == 2 && nodeType[0] == 1)) {
            return null;
        }
        // delete rebalance cases
        if (nodeType[1] == 2 && nodeType[0] == 2) { // case 1
            node.setHeight(node.getHeight() - 1); // demote
            cost[0] += 1; // cost update
            return node.getParent(); // problem is either fixed or moved up
        }
        if (nodeType[0] == 3 && nodeType[1] == 1) {
            int[] sonType = getNodeType(node.getRight());
            if (sonType[0] == 1 && sonType[1] == 1) { // case 2
                leftRotation(node);
                node.setHeight(node.getHeight() - 1); // demote
                node.getParent().setHeight(node.getParent().getHeight() + 1); // promote
                node.setSize();
                node.getParent().setSize();
                cost[0] += 3; // cost update
                return null; // problem is fixed
            } else if (sonType[0] == 2 && sonType[1] == 1) { // case 3
                leftRotation(node);
                node.setHeight(node.getHeight() - 2); // demote
                node.setSize();
                node.getParent().setSize();
                cost[0] += 2;
                return node.getParent().getParent(); // problem is either fixed or moved up
            } else if (sonType[0] == 1 && sonType[1] == 2) { // case 4
                doubleRotationRightLeft(node);
                node.setHeight(node.getHeight() - 2); // demote
                node.getParent().setHeight(node.getParent().getHeight() + 1); // promote
                node.getParent().getRight().setHeight(node.getParent().getRight().getHeight() - 1); // demote
                node.setSize();
                node.getParent().getRight().setSize();
                node.getParent().setSize();
                cost[0] += 5;
                return node.getParent().getParent(); // problem is either fixed or moved up
            }
        }
        if (nodeType[0] == 1 && nodeType[1] == 3) { // symmetric 2,3,4 cases
            int[] sonType = getNodeType(node.getLeft());
            if (sonType[0] == 1 && sonType[1] == 1) { // case 2
                rightRotation(node);
                node.setHeight(node.getHeight() - 1); // demote
                node.getParent().setHeight(node.getParent().getHeight() + 1); // promote
                node.setSize();
                node.getParent().setSize();
                cost[0] += 3; // cost update
                return null; // problem is fixed
            } else if (sonType[0] == 1 && sonType[1] == 2) { // case 3
                rightRotation(node);
                node.setHeight(node.getHeight() - 2); // demote
                node.setSize();
                node.getParent().setSize();
                cost[0] += 2;
                return node.getParent().getParent(); // problem is either fixed or moved up
            } else if (sonType[0] == 2 && sonType[1] == 1) { // case 4
                doubleRotationLeftRight(node);
                node.setHeight(node.getHeight() - 2); // demote
                node.getParent().setHeight(node.getParent().getHeight() + 1); // promote
                node.getParent().getLeft().setHeight(node.getParent().getLeft().getHeight() - 1); // demote
                node.setSize();
                node.getParent().getLeft().setSize();
                node.getParent().setSize();
                cost[0] += 5;
                return node.getParent().getParent(); // problem is either fixed or moved up
            }
        }
        // insert rebalance cases
        if ((nodeType[0] == 0 && nodeType[1] == 1) || (nodeType[1] == 0 && nodeType[0] == 1)) { // case 1
            node.setHeight(node.getHeight() + 1); //promote
            cost[0] += 1;
            return node.getParent();
        }
        if (nodeType[0] == 0 && nodeType[1] == 2) {
            int[] sonType = getNodeType(node.getLeft());
            if (sonType[0] == 1 && sonType[1] == 2) { // case 2
                rightRotation(node);
                node.setHeight(node.getHeight() - 1); // demote
                node.setSize();
                node.getParent().setSize();
                cost[0] += 2;
                return null;
            }
            else if (sonType[0] == 2 && sonType[1] == 1) { // case 3
                doubleRotationLeftRight(node);
                node.setHeight(node.getHeight() - 1); // demote
                node.getParent().setHeight(node.getParent().getHeight() + 1); // promote
                node.getParent().getLeft().setHeight(node.getParent().getLeft().getHeight() - 1); // demote
                node.setSize();
                node.getParent().getLeft().setSize();
                node.getParent().setSize();
                cost[0] += 5;
                return null;
            }
            else if (sonType[0] == 1 && sonType[1] == 1) { // join rebalancing case
                rightRotation(node);
                node.getParent().setHeight(node.getParent().getHeight() + 1); // promote
                node.setSize();
                node.getParent().setSize();
                return null;
            }
        }
        if (nodeType[0] == 2 && nodeType[1] == 0) { // symmetric 2,3 cases
            int[] sonType = getNodeType(node.getRight());
            if (sonType[0] == 2 && sonType[1] == 1) { // case 2
                leftRotation(node);
                node.setHeight(node.getHeight() - 1); // demote
                node.setSize();
                node.getParent().setSize();
                cost[0] += 2;
                return null;
            } else if (sonType[0] == 1 && sonType[1] == 2) { // case 3
                doubleRotationRightLeft(node);
                node.setHeight(node.getHeight() - 1); // demote
                node.getParent().setHeight(node.getParent().getHeight() + 1); // promote
                node.getParent().getRight().setHeight(node.getParent().getRight().getHeight() - 1); // demote
                node.setSize();
                node.getParent().getRight().setSize();
                node.getParent().setSize();
                cost[0] += 5;
                return null;
            }
            else if (sonType[0] == 1 && sonType[1] == 1) { // join rebalancing case symmetric
                leftRotation(node);
                node.getParent().setHeight(node.getParent().getHeight() + 1); // promote
                node.setSize();
                node.getParent().setSize();
                return null;
            }
        }
        return null;
    }


    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min() {
        if (this.empty()) {
            return null;
        }
        return this.min.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max() { // O(1)
        if (this.empty()) {
            return null;
        }
        return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] keys = new int[this.size()];
        if (this.empty()) {
            return new int[]{};
        }
        keysToArrayRecursive(this.root, keys, 0);
        return keys;
    }

    private int keysToArrayRecursive(IAVLNode node, int[] array, int index) {
        if (node == null || !node.isRealNode()) {
            return index;
        }
        /* first recur on left child */
        index = keysToArrayRecursive(node.getLeft(), array, index);

        array[index] = node.getKey();
        index += 1;
        /* now recur on right child */
        index = keysToArrayRecursive(node.getRight(), array, index);
        return index;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() { // complexity O(log(n))
        if (this.empty()) {
            return new String[]{};
        }
        String[] array = new String[this.size()];
        infoToArrayRec(this.root, array, 0);
        return array;
    }

    /**
     * public void infoToArrayRec(IAVLNode node, String[] array, int index)
     * <p>
     * the function scan the tree inorder and update an array with nodes value.
     */
    public int infoToArrayRec(IAVLNode node, String[] array, int index) {

        if (node == null || !node.isRealNode()) {
            return index;
        }
        index = infoToArrayRec(node.getLeft(), array, index);
        array[index] = node.getValue();
        index++;
        index = infoToArrayRec(node.getRight(), array, index);
        return index;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() { // complexity O(1)
        if (this.empty()) {
            return 0;
        }
        return this.root.getSize();
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot() { // complexity O(1)
        if(this.empty()) {
            return null;
        }
        return this.root;
    }

    /**
     * public AVLTree[] split(int x)
     * <p>
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * <p>
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        IAVLNode split_node = treePosition(this.root, x);
        AVLTree tree_lower1 = new AVLTree();
        AVLTree tree_greater1 = new AVLTree();
        if (split_node.getRight().isRealNode()) { // there is right subtree
            tree_greater1.root = split_node.getRight();
            tree_greater1.root.setParent(null);
        }
        if (split_node.getLeft().isRealNode()) { // there is left subtree
            tree_lower1.root = split_node.getLeft();
            tree_lower1.root.setParent(null);
        }
        split_node.setRight(null); // delete x sons pointers
        split_node.setLeft(null);
        IAVLNode node = split_node.getParent();
        split_node.setParent(null);
        while (node != null) { // climbing the tree
            //System.out.println("currently joins node: " + node.getKey());
            IAVLNode parent;
            parent = node.getParent();
            if (node.getKey() < split_node.getKey()) { // node is a right son
                node.setRight(null);
                node.setParent(null);
                IAVLNode new_node = new AVLNode(node.getKey(), node.getValue());
                new_node.setHeight(node.getHeight());
                AVLTree tree_lower2 = new AVLTree();
                if (node.getLeft().isRealNode()) {
                    tree_lower2.root = node.getLeft();
                    node.getLeft().setParent(null);
                }
                tree_lower1.join(new_node, tree_lower2); // join lowers values
                if(!Tester.checkBalanceOfTree(tree_lower1.getRoot())) {
                    System.out.println("error after join in split - lower");
                }
            } else { // node is a left son
                node.setLeft(null);
                node.setParent(null);
                IAVLNode new_node = new AVLNode(node.getKey(), node.getValue());
                new_node.setHeight(node.getHeight());
                AVLTree tree_greater2 = new AVLTree();
                if (node.getRight().isRealNode()) {
                    tree_greater2.root = node.getRight();
                    node.getRight().setParent(null);
                }
                tree_greater1.join(new_node, tree_greater2); // join greater values
                if(!Tester.checkBalanceOfTree(tree_greater1.getRoot())) {
                    System.out.println("error after join in split - greater");
                }
            }
            node = parent;
        }
        if (!tree_greater1.empty()) {
            update_min(tree_greater1);
            update_max(tree_greater1);
        }
        if (!tree_lower1.empty()) {
            update_min(tree_lower1);
            update_max(tree_lower1);
        }
        return new AVLTree[]{tree_lower1, tree_greater1};
    }
    
    public AVLTree[] splitTheoretical(int x, int[] averageComponents) {
    	int maxJoinCost = 0;
        IAVLNode split_node = treePosition(this.root, x);
        AVLTree tree_lower1 = new AVLTree();
        AVLTree tree_greater1 = new AVLTree();
        if (split_node.getRight().isRealNode()) { // there is right subtree
            tree_greater1.root = split_node.getRight();
            tree_greater1.root.setParent(null);
        }
        if (split_node.getLeft().isRealNode()) { // there is left subtree
            tree_lower1.root = split_node.getLeft();
            tree_lower1.root.setParent(null);
        }
        split_node.setRight(null); // delete x sons pointers
        split_node.setLeft(null);
        IAVLNode node = split_node.getParent();
        split_node.setParent(null);
        while (node != null) { // climbing the tree
            //System.out.println("currently joins node: " + node.getKey());
            IAVLNode parent;
            parent = node.getParent();
            if (node.getKey() < split_node.getKey()) { // node is a right son
                node.setRight(null);
                node.setParent(null);
                IAVLNode new_node = new AVLNode(node.getKey(), node.getValue());
                new_node.setHeight(node.getHeight());
                AVLTree tree_lower2 = new AVLTree();
                if (node.getLeft().isRealNode()) {
                    tree_lower2.root = node.getLeft();
                    node.getLeft().setParent(null);
                }
                averageComponents[0]++;
                
                int currentJoinCost = tree_lower1.join(new_node, tree_lower2); // join lowers values
                if (currentJoinCost > maxJoinCost) {
                	maxJoinCost = currentJoinCost;
                }
                System.out.println("joined left: " + currentJoinCost);
                averageComponents[1] += currentJoinCost;
                
                if(!Tester.checkBalanceOfTree(tree_lower1.getRoot())) {
                    System.out.println("error after join in split - lower");
                }
            } else { // node is a left son
                node.setLeft(null);
                node.setParent(null);
                IAVLNode new_node = new AVLNode(node.getKey(), node.getValue());
                new_node.setHeight(node.getHeight());
                AVLTree tree_greater2 = new AVLTree();
                if (node.getRight().isRealNode()) {
                    tree_greater2.root = node.getRight();
                    node.getRight().setParent(null);
                }
                averageComponents[0]++;
                int currentJoinCost = tree_greater1.join(new_node, tree_greater2); // join greater values
                if (currentJoinCost > maxJoinCost) {
                	maxJoinCost = currentJoinCost;
                }
                System.out.println("joined right: " + currentJoinCost);
                averageComponents[1] += currentJoinCost;
                if(!Tester.checkBalanceOfTree(tree_greater1.getRoot())) {
                    System.out.println("error after join in split - greater");
                }
            }
            node = parent;
        }
        if (!tree_greater1.empty()) {
            update_min(tree_greater1);
            update_max(tree_greater1);
        }
        if (!tree_lower1.empty()) {
            update_min(tree_lower1);
            update_max(tree_lower1);
        }
        
        averageComponents[2] = maxJoinCost;
        return new AVLTree[]{tree_lower1, tree_greater1};
    }

    
    public void update_max(AVLTree t) {
        if (t.empty()) {
            return;
        }
        IAVLNode max_tree = t.root;
        while (max_tree.getRight().isRealNode()) {
            max_tree = max_tree.getRight();
        }
        t.max = max_tree;
    }

    public void update_min(AVLTree t) {
        if (t.empty()) {
            return;
        }
        IAVLNode min_tree = t.root;
        while (min_tree.getLeft().isRealNode()) {
            min_tree = min_tree.getLeft();
        }
        t.min = min_tree;
    }



    /**
     * public int join(IAVLNode x, AVLTree t)
     * <p>
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * <p>
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {
        int rank_difference = 0;
        if(!this.empty() && !t.empty()) {
            //to ensure t.keys() are lower than our keys.
            if (this.root.getKey() < t.getRoot().getKey()) {
                //set new max.
                this.max = t.max;
                //join the trees.
                int result = t.join(x, this);
                this.root = t.root;
                return result;
            }
            //set new minimum.
            this.min = t.min;
            rank_difference = Math.abs(this.root.getHeight() - t.root.getHeight()) + 1;

            if (this.root.getHeight() == t.root.getHeight()) { // when the trees with same rank
                x.setRight(this.root);
                this.root.setParent(x);
                x.setLeft(t.root);
                x.setHeight(t.root.getHeight() + 1);
                t.root.setParent(x);
                this.root = x;
                this.root.setSize();
                return 1;
            }
            //opposite of lecture - left tree with higher rank
            else if (this.root.getHeight() < t.root.getHeight()) {
                IAVLNode rightNode = this.root;
                IAVLNode leftNode = t.root;
                this.root = t.root;
                while (leftNode.getHeight() > rightNode.getHeight() && leftNode.getRight().isRealNode()) {
                    leftNode = leftNode.getRight();
                }

                if (leftNode.getParent() != null) {
                    x.setParent(leftNode.getParent());
                    x.getParent().setRight(x);
                }
                else {
                    this.root = x;
                }

                //set x left and right
                x.setLeft(leftNode);
                leftNode.setParent(x);
                x.setRight(rightNode);
                rightNode.setParent(x);
                x.setHeight(Math.max(x.getLeft().getHeight(), x.getRight().getHeight()) + 1);
            }
            //like in lecture - right tree with higher rank
            else {
                IAVLNode leftNode = t.root;
                IAVLNode rightNode = this.root;

                while (rightNode.getHeight() > leftNode.getHeight() && rightNode.getLeft().isRealNode()) {
                    rightNode = rightNode.getLeft();
                }

                if (rightNode.getParent() != null) {
                    x.setParent(rightNode.getParent());
                    x.getParent().setLeft(x);
                }
                else {
                    this.root = x;
                }

                //set x left and right
                x.setLeft(leftNode);
                leftNode.setParent(x);
                x.setRight(rightNode);
                rightNode.setParent(x);
                x.setHeight(Math.max(x.getLeft().getHeight(), x.getRight().getHeight()) + 1);
            }
        }
        // handling empty trees.
        else if (this.empty()) {
            //both trees are empty
            if (t.empty() && x == null) {//if nothing is added.
                return 1;
            }
            if (t.empty()) {//add x as the only node.
                this.root = x;
                this.max = x;
                this.min = x;
                x.setRight(new AVLNode());
                x.setLeft(new AVLNode());
                x.setHeight(0);
                x.setSize();
                return 1;
            }
            update_max(t);
            update_min(t);
            x.setLeft(new AVLNode());
            x.setRight(new AVLNode());
            x.setHeight(0);
            x.setSize();
            if (x.getKey() > t.max.getKey()) { //new node is bigger than t.keys(): add x as maximum.
                x.setParent(t.max);
                t.max.setRight(x);
                this.max = x;
                this.min = t.min;
            }
            else {//new node is smaller than t.keys(): add x as minimum.
                x.setParent(t.min);
                t.min.setLeft(x);
                this.min = x;
                this.max = t.max;
            }
            //set root.
            this.root = t.root;
            rank_difference = this.root.getHeight() + 2;
        }
        else {//t is empty.
            update_max(this);
            update_min(this);
            x.setLeft(new AVLNode());
            x.setRight(new AVLNode());
            x.setHeight(0);
            x.setSize();
            if (x.getKey() > this.max.getKey()) {//new node is bigger than this.keys(): add x as maximum.
                x.setParent(this.max);
                this.max.setRight(x);
                this.max = x;
            }
            else {//new node is smaller than this.keys(): add x as minimum.
                x.setParent(this.min);
                this.min.setLeft(x);
                this.min = x;
            }
            rank_difference = this.root.getHeight() + 2;
        }
        
        //update size.
        updateSize(x);
        
        //rebalance up to root.
        IAVLNode node_for_rebalance = x.getParent();
        int[] cost = {0};
        IAVLNode last_node = null;
        while (node_for_rebalance != null) {
            last_node = node_for_rebalance;
            node_for_rebalance = rebalance(node_for_rebalance, cost);
            if(last_node.getParent() != null && node_for_rebalance == null) {
                node_for_rebalance = last_node.getParent();
            }
        }
        if (last_node != null) {
            updateSize(last_node);
        } else {
            last_node = x;
        }
        //go up until root node (if didn't got there already).
        while(last_node.getParent() != null) {
            last_node = last_node.getParent();
        }
        
        //set the new root of our tree.
        this.root = last_node;

        return rank_difference;
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
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

        public int getSize(); // Returns the size of the node subtree.

        public void setSize(); // update nodes size.


    }

    /**
     * public class AVLNode
     * <p>
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in another file.
     * <p>
     * This class can and MUST be modified (It must implement IAVLNode).
     */
    public static class AVLNode implements IAVLNode {
        // Fields
        private int key;
        private int rank;
        private String info;
        private IAVLNode left;
        private IAVLNode parent;
        private IAVLNode right;
        private int size;

        // Constructor
        public AVLNode() {
            this.rank = -1;
        }

        public AVLNode(int key, String info) {
            this.key = key;
            this.info = info;
            this.size = 1;
        }

        public int getKey() {
            if (this.rank == -1)
                return -1;
            return key;
        }

        public String getValue() {
            return info;
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
            // node with rank of -1 is not a real node
            return this.rank != -1;
        }

        public void setHeight(int height) {
            this.rank = height;
        }

        public int getHeight() {
            return this.rank; // node.rank == node.height
        }

        public int getSize() {
            return this.size;
        }

        public void setSize() {
            this.size = this.left.getSize() + this.right.getSize() + 1;
        }
    }
}

