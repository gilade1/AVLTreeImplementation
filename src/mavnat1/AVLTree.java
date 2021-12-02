package mavnat1;

import java.util.Arrays;
import java.util.Stack;

/**
 * AVLTree
 * <p>
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 */

public class AVLTree {

    public static void main(String[] args) {

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
                if (!node.getRight().isRealNode()) {
                    node.setRight(insertedNode);
                    insertedNode.setParent(node);
                    break;
                }
                node = node.getRight();
            }
            else if (k < node.getKey()){
                if (!node.getLeft().isRealNode()) {
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
        node.setSize();
        updateSize(node);
        //balancing up to root.
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
    	if(node == null) {
    		return;
    	}
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
        return this.root.getSize();
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot() { // complexity O(1)
    	if(this.empty()) {
    		return new AVLNode();
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
            IAVLNode min_tree_greater = tree_greater1.root;
            while (min_tree_greater.getLeft().isRealNode()) {
                min_tree_greater = min_tree_greater.getLeft();
            }
            tree_greater1.min = min_tree_greater;
            tree_greater1.max = this.max;
        }
        if (split_node.getLeft().isRealNode()) { // there is left subtree
            tree_lower1.root = split_node.getLeft();
            IAVLNode max_tree_lower = tree_lower1.root;
            while (max_tree_lower.getRight().isRealNode()) {
                max_tree_lower = max_tree_lower.getRight();
            }
            tree_lower1.max = max_tree_lower;
            tree_lower1.min = this.min;
        }
        split_node.setRight(null); // delete x sons pointers
        split_node.setLeft(null);
        IAVLNode node = split_node;
        IAVLNode parent = node.getParent();
        while (parent != null) { // climbing the tree
            if (parent.getKey() < node.getKey()) { // node is a right son
                if (tree_lower1.max == null) {
                    tree_lower1.max = parent;
                }
                parent.setRight(null);
                node.setParent(null);
                AVLTree tree_lower2 = new AVLTree();
                if (parent.getLeft().isRealNode()) {
                    tree_lower2.root = parent.getLeft();
                    parent.getLeft().setParent(null);
                }
                tree_lower1.join(parent, tree_lower2); // join lowers values
            }
            else { // node is a left son
                if (tree_greater1.min == null) {
                    tree_greater1.min = parent;
                }
                parent.setLeft(null);
                node.setParent(null);
                AVLTree tree_greater2 = new AVLTree();
                if (parent.getRight().isRealNode()) {
                    tree_greater2.root = parent.getRight();
                    parent.getRight().setParent(null);
                }
                tree_greater2.max = this.max;
                tree_greater1.join(parent, tree_greater2); // join greater values
            }
            node = parent;
            parent = parent.getParent();
        }

        if (!tree_lower1.empty()) {
            IAVLNode max_tree_lower = tree_lower1.root;
            while (max_tree_lower.getRight().isRealNode()) {
                max_tree_lower = max_tree_lower.getRight();
            }
            tree_lower1.max = this.max;
        }
        return new AVLTree[]{tree_lower1, tree_greater1};
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
	        //opposite of lecture - left tree with higher rank
	        if (this.root.getHeight() < t.root.getHeight()) {
	
	        	IAVLNode rightNode = this.root;
	        	IAVLNode leftNode = t.root;
	
	            while(leftNode.getHeight() >= rightNode.getHeight()) {
	                leftNode = leftNode.getRight();
	                System.out.print(leftNode.getKey());
	            }
	            x.setParent(leftNode.getParent());
	            if(x.getParent() != null) {
	            x.getParent().setRight(x);
	            }
	            
	            //set x left and right
	            x.setLeft(leftNode);
	            leftNode.setParent(x);
	            x.setRight(rightNode);
	            rightNode.setParent(x);
	            x.setHeight(Math.max(leftNode.getHeight(), rightNode.getHeight()) + 1);
	        }
	        //like in lecture - right tree with higher rank
	        else {
	        	IAVLNode leftNode = t.root;
	        	IAVLNode rightNode = this.root;
	        	
	            while(rightNode.getHeight() > leftNode.getHeight()) {
	                rightNode = rightNode.getLeft();
	                System.out.print(rightNode.getKey());
	            }
	            x.setParent(rightNode.getParent());
	            if(x.getParent() != null) {
	            	x.getParent().setLeft(x);
	            }
	            
	            
	            //set x left and right
	            x.setLeft(leftNode);
	            leftNode.setParent(x);
	            x.setRight(rightNode);
	            rightNode.setParent(x);
	            x.setHeight(Math.max(leftNode.getHeight(), rightNode.getHeight()) + 1);
	        }
        }
        // handling empty trees.
        if(this.empty()) {
    		//both trees are empty
    		if(t.empty()) {
    			return 1;
    		}
    		if (x.getKey() > t.max.getKey()) {//new node is bigger than t.keys():
    			x.setParent(t.max);
    			t.max.setRight(x);
    			this.max = x;
    			this.min = t.min;
    		} 
    		else {//new node is smaller than t.keys():
    			x.setParent(t.min);
    			t.min.setLeft(x);
    			this.max = t.max;
    			this.min = x;
    		}
    	} 
        else if (t.empty()){//t is empty.
        	t = new AVLTree();
        	if (x.getKey() > this.max.getKey()) {//new node is bigger than t.keys():
    			x.setParent(this.max);
    			this.max.setRight(x);
    			this.max = x;
    		} 
    		else {//new node is smaller than this.keys():
    			x.setParent(this.min);
    			this.min.setLeft(x);
    			this.min = x;
    		}
    	}

        updateSize(x);
        
       

        //rebalance up top root
        int[] cost = {0};
        IAVLNode node = x.getParent();
        
        IAVLNode node_for_rebalance = node;
        
        IAVLNode last_node = null;
        while (node_for_rebalance != null) {
            last_node = node_for_rebalance;
            node_for_rebalance = rebalance(node_for_rebalance, cost);
        }
        if(last_node == null) {
        	last_node = x;
        }
        updateSize(last_node);
        
        //go up until root node
        while(last_node.getParent() != null) {
        	last_node = last_node.getParent();
        }
        //set new root
        this.root = last_node;
        
        //int thisHeight; int tHeight;
        //if(t.empty())
        return Math.abs(this.root.getHeight() - t.getRoot().getHeight()) + 1;
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
    public class AVLNode implements IAVLNode {
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
  
  