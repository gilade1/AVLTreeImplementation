package mavnat1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mavnat1.AVLTree.AVLNode;

import java.util.*;

import java.util.*;

public class Tester {

    public static void main(String[] args) {
        //insertDeleteTester(11, 10,100 );
        joinSpliteTester(3 , 10000);
    }







    private static void joinSpliteTester(int s, int reptitions) {
        int size = (int) Math.pow(3,s);
        boolean error = false;

        Random rand = new Random();
        int cnt = 0 ;
        while ( !error && (cnt < reptitions)){
            ArrayList<Integer> numToInsert = new ArrayList<>();
            ArrayList<Integer> numToInsertLeft = new ArrayList<>();
            ArrayList<Integer> numToInsertRight = new ArrayList<>();

            int intSplit = 1 + rand.nextInt(size);           // a random node X s.t. left.keys < X < right.keys for split & join
            if (cnt == 0){ // special case - no left tree
                intSplit = 1;}
            if (cnt == 1){ // special case - no right tree
                intSplit = size; }



            for (int i = 0; i < size ; i++) {                 // insert all the numbers in to array-list
                numToInsert.add(i + 1);

                if (i+1 < intSplit ){                      // insert the low keys here
                    numToInsertLeft.add( i+1 );}
                if (intSplit < i +1 ){                     // intsplite stay in the side
                    numToInsertRight.add( i+1 );}           // insert the high key here

            }
            // mixing the insert order
            Collections.shuffle(numToInsertLeft);
            Collections.shuffle(numToInsertRight);
            Collections.shuffle(numToInsert);

            AVLTree left = new AVLTree();
            AVLTree right = new AVLTree();
            AVLTree treeInCheck = null;
            for (int i : numToInsertLeft) { // creating left tree
                left.insert(i, ""+i);}

            for (int i : numToInsertRight) { // creating right tree
                right.insert(i, ""+i);}

            //System.out.println("----- Begin----");
            //System.out.println("inorder left:" + Arrays.asList(left.infoToArray()));
            System.out.println("the splitter / join index is " + intSplit);
            //System.out.println("inorder right:" + Arrays.asList(right.infoToArray()));

            AVLTree.IAVLNode X = new AVLTree.AVLNode(intSplit , ""+intSplit);
            double coin = rand.nextFloat();
            if (coin < 0.5){
                left.join(X , right);
                treeInCheck = left; }
            else {
                right.join(X , left);
                treeInCheck = right ; }

            //System.out.println("inorder insert:" + Arrays.asList(treeInCheck.infoToArray()));

            if (!Tester.checkBalanceOfTree(treeInCheck.getRoot())) {          // checking if the tree is balanced
                System.out.println("error in join - in balanced" );
                error = true;
            }
            if (!Tester.checkOrderingOfTree(treeInCheck.getRoot())){          // checking if the tree is a BST
                System.out.println("error in join - in order" );
                error = true;}

            if (!(treeInCheck.min().equals("1")) ){
                System.out.println("error in join - in minimum");
                error = true;}
            if (!(treeInCheck.max().equals("" + size)) ){
                System.out.println("error in join - in maximum");
                error = true;}
            if ( !(treeInCheck.size() == size)){
                System.out.println("error in join - in maximum");
                error = true;}


            // test split
            AVLTree mainTree = new AVLTree();
//        System.out.println(numToInsert);
            for (int i : numToInsert) { // creating left tree
                mainTree.insert(i, ""+i);}

            AVLTree[] arr = mainTree.split(intSplit); // lets split
            AVLTree leftTree = arr[0];
            AVLTree rightTree = arr[1];
            if (cnt == 0){
                if ( !leftTree.empty()){
                    System.out.println("left tree in after split should be empty");}}
            if (cnt == 1){
                if ( !rightTree.empty()){
                    System.out.println("right tree in after split should be empty");}}


            if (leftTree.getRoot() != null && !Tester.checkBalanceOfTree(leftTree.getRoot())) {          // checking if the tree is balanced
                System.out.println("error in left tree split - in balanced" );
                error = true;
            }
            if (leftTree.getRoot() != null && !Tester.checkOrderingOfTree(leftTree.getRoot())){          // checking if the tree is a BST
                System.out.println("error in left tree split - in order" );
                error = true;}

            if (leftTree.getRoot() != null && !(leftTree.min().equals("1")) ){
                System.out.println("the current minimum is " + leftTree.min());
                System.out.println("error in left tree in split - in minimum");
                error = true;}
            if (leftTree.getRoot() != null && !(leftTree.max().equals("" + (intSplit - 1))) ){
                System.out.println("error in left tree in split - in maximum");
                error = true;}
            if (leftTree.getRoot() != null && !(leftTree.size() == (intSplit - 1))){
                System.out.println("error in left tree in split - in size");
                error = true;}


            if (rightTree.getRoot() != null && !Tester.checkBalanceOfTree(rightTree.getRoot())) {          // checking if the tree is balanced
                System.out.println("error in right tree split - in balanced" );
                error = true;
            }
            if (rightTree.getRoot() != null && !Tester.checkOrderingOfTree(rightTree.getRoot())){          // checking if the tree is a BST
                System.out.println("error in right tree split - in order" );
                error = true;}

            if (rightTree.getRoot() != null && !(rightTree.min().equals(("" + (intSplit + 1)))) ){
                System.out.println("the current minimum is " + rightTree.min());
                System.out.println("error in right tree in split - in minimum");
                error = true;}
            if (rightTree.getRoot() != null && !(rightTree.max().equals("" + size) )){
                System.out.println("error in right tree in split - in maximum");
                error = true;}
            if (rightTree.getRoot() != null && !(rightTree.size() == (size - intSplit))){
                System.out.println("error in right tree split - in size");
                error = true;}

            cnt++;
        }}



    public static void  insertDeleteTester(int s , int delete , int reptitions){

        boolean errorDelete = false;
        boolean emptyCheck = false;
        int rep_counter = 0;

        System.out.println("checking if your tree knows how to be an empty tree");
        AVLTree empty = new AVLTree();

        if (!(empty.min() == null )){
            System.out.println("error in empty tree  - in minimum");
            emptyCheck = true;}
        if (!(empty.max() == null )){
            System.out.println("error in empty tree - in maximum");
            emptyCheck = true;}
        if (!(empty.size() == 0)){
            System.out.println("error in empty tree - in size");
            emptyCheck = true;}
        if ((!(empty.keysToArray().length == 0))){
            System.out.println("error in empty tree - in keysToArray");
            emptyCheck = true;}

        if (!emptyCheck){ System.out.println("nice! your tree is empty Properly");}

        System.out.println("LETS PLAY WITH YOUR TREE!");

        while (!errorDelete && rep_counter < reptitions   ){                               // while there is no error - keep testing
            int size =(int) Math.pow(2,s);                     // set the size of your tree
            int numberOfDelete = (int) Math.pow(2,delete) -1;         // set the number of delete
            ArrayList<Integer> numToInsert = new ArrayList<>();
            int max = 0;
            int min = size;
            int sizeInCheck = 0;
            for (int i = 0; i < size ; i++) {                 // insert all the numbers in to array-list
                numToInsert.add(i + 1);

            }
            //       numToInsert.add(size - 1);                           // double insert
            Collections.shuffle(numToInsert);                    // make the list in a random order
            //      System.out.println(numToInsert);                  // print the list if you want

            AVLTree testing = new AVLTree();

            for (int i : numToInsert) {
                testing.insert(i, ""+i);
                max = Math.max(i,max);
                min = Math.min(i,min);
                sizeInCheck++;
                if (testing.getRoot() != null && !Tester.checkBalanceOfTree(testing.getRoot())) {          // checking if the tree is balanced
                    System.out.println("error in insert - in balanced" );
                    errorDelete = true;
                }
                if (testing.getRoot() != null && !Tester.checkOrderingOfTree(testing.getRoot())){          // checking if the tree is a BST
                    System.out.println("error in insert - in order" );
                    errorDelete = true;}

                if (testing.getRoot() != null && !(testing.min().equals(("" + min))) ){
                    System.out.println("error in insert  - in minimum");
                    errorDelete = true;}
                if (testing.getRoot() != null && !(testing.max().equals("" + max) )){
                    System.out.println("error in insert - in maximum");
                    errorDelete = true;}
                if (testing.getRoot() != null && !(testing.size() == (sizeInCheck))){
                    System.out.println("error in insert - in size");
                    errorDelete = true;}}


//        if (testing.getRoot() != null && !SelfTester.checkBalanceOfTree(testing.getRoot())) {          // checking if the tree is balanced
//            System.out.println("error in insert - in balanced" );}
//        if (testing.getRoot() != null && !SelfTester.checkOrderingOfTree(testing.getRoot())){          // checking if the tree is a BST
//            System.out.println("error in insert - in order" );}


//            TreePrinter.print(testing.getRoot());
            if (!errorDelete){
                System.out.println("insert work");}

// keys to array check after insert

            int[] treeArr = testing.keysToArray();
            Collections.sort(numToInsert);
            int[] compare = new int[numToInsert.size()];
            for (int i : numToInsert) {
                compare[i-1] = i;}
            if (!(Arrays.equals(treeArr, compare))){
                System.out.println("problem in keys to array after insert");
                errorDelete = true;}








            Collections.shuffle(numToInsert);                                 // shuffle the list again for random order in delete
            int[] numToDelete = {13,11};
            int cnt = 0;
            ArrayList<Integer> copy = new ArrayList<>(numToInsert);
            for (int i : numToInsert) {
                //      System.out.println(i);                                         // if you want to see the number that is being deleted
                testing.delete(i);
                cnt++;
                sizeInCheck--;


                copy.remove(copy.indexOf(i));
                if (copy.size() > 0){
                    max = Collections.max(copy);
                    min = Collections.min(copy);}
                if (testing.getRoot() != null && !(testing.size() == (sizeInCheck))){
                    System.out.println("error in delete - in size");
                    errorDelete = true;}

                if (testing.size() > 0){

                    if (testing.getRoot() != null && !Tester.checkBalanceOfTree(testing.getRoot())) {          // checking if the tree is balanced
                        System.out.println("error in delete - in balanced" );
                        errorDelete = true;}

                    if (testing.getRoot() != null && !Tester.checkOrderingOfTree(testing.getRoot())){          // checking if the tree is a BST
                        System.out.println("error in delete - in order" );
                        errorDelete = true;}

                    if (testing.getRoot() != null && !(testing.min().equals(("" + min))) ){
                        System.out.println("error in delete - in minimum");
                        errorDelete = true;}

                    if (testing.getRoot() != null && !(testing.max().equals("" + max) )){
                        System.out.println("error in delete - in maximum");
                        errorDelete = true;}
                }



                if (cnt > numberOfDelete ){
                    break;}
            }
            //check keys to array after delete
            int[] treeArrD = testing.keysToArray();
            Collections.sort(copy);
            int[] compareD = new int[copy.size()];
            int j = 0;
            for (int i : copy) {
                compareD[j] = i;
                j++; }

            if (!(Arrays.equals(treeArrD, compareD))){
                System.out.println("problem in keys to array after delete");
                errorDelete = true;}


            if (!errorDelete){
                System.out.println("delete work");}
            else{
                System.out.println("delete failed");
            }

            rep_counter++;
        }}




    public static  boolean checkBalanceOfTree(AVLTree.IAVLNode current) {
        boolean balancedRight = true, balancedLeft = true;
        int leftHeight = 0, rightHeight = 0;
        int leftHeightGilad = -1, rightHeightGilad = -1;
        if (current.getRight() != null) {
            balancedRight = checkBalanceOfTree(current.getRight());
            rightHeight = getDepth(current.getRight());
            rightHeightGilad = current.getRight().getHeight();
        }
        if (current.getLeft() != null) {
            balancedLeft = checkBalanceOfTree(current.getLeft());
            leftHeight = getDepth(current.getLeft());
            leftHeightGilad = current.getLeft().getHeight();
        }
        boolean legalLeft = current.getHeight() - leftHeightGilad <= 2;
        boolean legalRight = current.getHeight() - rightHeightGilad <= 2;
        boolean isLegalWAVL = current.getHeight() - leftHeightGilad == 2 //is 2,2 node
        		&& current.getHeight() - rightHeightGilad == 2;
        boolean isIlegal = !(Math.abs(leftHeightGilad - rightHeightGilad) < 2) || !legalLeft || !legalRight || isLegalWAVL;
        if(isIlegal) { //|| !(Math.abs(leftHeight - rightHeight) < 2)) {
            System.out.println("   " + current.getKey() + " \r\n" +
                    "  / \\   \r\n " +
                    current.getLeft().getKey() + "  " + current.getRight().getKey());
            System.out.println("current node is t: " + (current.getHeight() - current.getLeft().getHeight()) + ", " +
                    (current.getHeight() - current.getRight().getHeight()));
        }
        return balancedLeft && balancedRight && !isIlegal;//(Math.abs(leftHeight - rightHeight) < 2);
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
