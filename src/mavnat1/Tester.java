package mavnat1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mavnat1.AVLTree.AVLNode;

public class Tester {

    public static void main(String[] args) {
        boolean errorDelete = false;

        while (!errorDelete){                               // while there is no error - keep testing
            int size =(int) Math.pow(2,2);                     // set the size of your tree
            int numberOfDelete = (int) Math.pow(2,0);          // set the number of delete
            ArrayList<Integer> numToInsert = new ArrayList<>();
            for (int i = 0; i < size - 1; i++) {                 // insert all the numbers in to array-list
                numToInsert.add(i + 1);
            }
            numToInsert.add(size - 1);                           // double insert
            Collections.shuffle(numToInsert);                    // make the list in a random order
                  System.out.println(numToInsert);  // print the list if you want
        //int numberOfDelete = (int) Math.pow(2,2);
        //int[] numToInsert = {3, 1, 4, 2, 5 , 7, 6};
            AVLTree testing = new AVLTree();
            for (int i : numToInsert) {
                testing.insert(i, ""+i);
//           if (i % 2 == 0) {                              // check every second insert that the tree is balanced
//               if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
//                   System.out.println("error in balance");
            }
            
            numToInsert = new ArrayList<>();
            for (int i = size + 5; i < 2*size + 10; i++) {                 // insert all the numbers in to array-list
                numToInsert.add(i + 1);
            }
            numToInsert.add(size - 1);                           // double insert
            Collections.shuffle(numToInsert);                    // make the list in a random order
                  System.out.println(numToInsert);
            
            AVLTree testing2 = new AVLTree();
            int counter = 4; //change testing2 initial size
            for (int i : numToInsert) {
            	if(counter < 1) {
            		break;
            	}
            	counter--;
                testing2.insert(i * 2 + size, ""+(i * 2 + size));
//           if (i % 2 == 0) {                              // check every second insert that the tree is balanced
//               if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
//                   System.out.println("error in balance");
            }
            AVLTree t = new AVLTree();
            t.insert(size + 1, "" + (size + 1));
            System.out.println(size + 1);
            System.out.println("inorder: " + Arrays.asList(testing.infoToArray()));
            System.out.println("inorder2: " + Arrays.asList(testing2.infoToArray()));
            testing2.join(t.getRoot(), testing);
            
            
            
            System.out.println("inorderAfterJoin: " + Arrays.asList(testing2.infoToArray()));
            AVLTree[] splits = testing2.split(5);
            System.out.println("inorderAftersplit, t1: " + Arrays.asList(splits[0].infoToArray()));
            System.out.println("inorderAfterSplit, t2: " + Arrays.asList(splits[1].infoToArray()));
            //System.out.println(Arrays.toString(testing.infoToArray()));




                  //System.out.println(testing.getRoot().getRight().getSize());        // insanity checks
                  //System.out.println(testing.getRoot().getLeft().getSize());
                  //System.out.println(testing.size());
                  //System.out.println(testing.getRoot().getHeight());

            if (!Tester.checkBalanceOfTree(testing2.getRoot())) {          // checking if the tree is balanced
                System.out.println("error in insert - in balanced" );
                System.exit(-1);
                }
            else {
            	
            }
            if (!Tester.checkOrderingOfTree(testing2.getRoot())){          // checking if the tree is a BST
                System.out.println("error in insert - in order" );
                System.exit(-1);
                }
            
            String[] inorder = testing2.infoToArray();
            
        	if(testing2 != null && testing2.max() != inorder[inorder.length - 1]) {
        		System.out.println("error in insert - max is " + Integer.parseInt(testing.max())
        		+ "instead of " + inorder[inorder.length - 1] );
                System.exit(-1);
        	}
        	if(testing2 != null && testing2.min() != inorder[0]) {
        		System.out.println("error in insert - max is " + Integer.parseInt(testing.min())
        		+ "instead of " + inorder[0] );
                System.exit(-1);
        	}
//            TreePrinter.print(testing.getRoot());
            else {
                System.out.println("insert work");}

            //Collections.shuffle(numToInsert);                                 // shuffle the list again for random order in delete
            //numToInsert = new int[] {6};
            int[] numToDelete = {13,11};
            int cnt = 0;
            
            for (int i : numToInsert) {
                     System.out.println(i);  
                     System.out.println("inorder: " + Arrays.asList(testing2.infoToArray()));// if you want to see the number that is being deleted
                testing2.delete(i);
                
                cnt++;

                if (!Tester.checkBalanceOfTree(testing2.getRoot())) {           // checking if the tree is balanced
                    errorDelete = true;
                    System.out.println("error in delete" + i +"- balanced"); }

                if (!Tester.checkOrderingOfTree(testing2.getRoot())){
                    errorDelete = true;
                    System.out.println("error in delete  - in order" + i);}    // checking if the tree is a BST
                
                inorder = testing2.infoToArray();
                if(inorder.length < 1) {
                	continue;
                }
            	if(testing2.max() != inorder[inorder.length - 1]) {
            		System.out.println("error in delete - max is " + Integer.parseInt(testing.max())
            		+ "instead of " + inorder[inorder.length - 1] );
                    System.exit(-1);
            	}
            	if(testing2.min() != inorder[0]) {
            		System.out.println("error in delete - max is " + Integer.parseInt(testing.min())
            		+ "instead of " + inorder[0] );
                    System.exit(-1);
            	}
                // print

                //if (cnt > numberOfDelete ){
                  //  break;}
            }
            if (!errorDelete){
                System.out.println("delete work");}
            else{
                System.out.println("delete failed");}}

        }


    public static  boolean checkBalanceOfTree(AVLTree.IAVLNode current) {
    	if(current == null) {
    		return true;
    	}
        boolean balancedRight = true, balancedLeft = true;
        int leftHeight = 0, rightHeight = 0;
        if (current.getRight() != null) {
            balancedRight = checkBalanceOfTree(current.getRight());
            rightHeight = getDepth(current.getRight());
        }
        if (current.getLeft() != null) {
            balancedLeft = checkBalanceOfTree(current.getLeft());
            leftHeight = getDepth(current.getLeft());
        }
        
        if(Math.abs(leftHeight - rightHeight) >= 2) {
        	System.out.println("   " + current.getKey() + " \r\n" + 
        					   "  / \\   \r\n " +
        						current.getLeft().getKey() + "  " + current.getRight().getKey());
        	System.out.println("current node is: " + (current.getHeight() - current.getLeft().getHeight()) + ", " +
        			(current.getHeight() - current.getRight().getHeight()));
        }
        return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
    }

    private static int getDepth(AVLTree.IAVLNode n) {
        int leftHeight = 0, rightHeight = 0;

        if (n.getRight() != null)
            rightHeight = getDepth(n.getRight());
        if (n.getLeft() != null)
            leftHeight = getDepth(n.getLeft());

        return Math.max(rightHeight, leftHeight) + 1;
    }

    public static boolean checkOrderingOfTree(AVLTree.IAVLNode current) {
    	if(current == null) {
    		return true;
    	}
        if (current.getLeft().isRealNode()) {
            if (Integer.parseInt(current.getLeft().getValue()) > Integer.parseInt(current.getValue()))
                return false;
            else
                return checkOrderingOfTree(current.getLeft());
        } else if (current.getRight().isRealNode()) {
            if (Integer.parseInt(current.getRight().getValue()) < Integer.parseInt(current.getValue()))
                return false;
            else
                return checkOrderingOfTree(current.getRight());
        } else if (!current.getLeft().isRealNode() && !current.getRight().isRealNode())
            return true;

        return true;
    }
}
