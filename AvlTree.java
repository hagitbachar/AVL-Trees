package oop.ex4.data_structures;
/**
 * Created by hagitba on 5/9/17.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements an AVL tree
 */
public class AvlTree implements Iterable<Integer> {

    private Node root = null;
    private int numberOfElement = 0;
    private final int HEIGHT_OF_EMPTY_NODE = -1;
    private final int NOT_CONTAIN = -1;


    /* Constructors */

        /**
         * The default constructor.
         */
        public AvlTree() {
            this.numberOfElement = 0;
        }

        /**
         * A constructor that builds a new AVL tree containing all unique values in the input
         * array.
         *
         * @param data the values to add to tree.
         */
        public AvlTree(int[] data) {

            this();
            for (int item : data){
                this.add(item);
            }
        }

        /**
         * A copy constructor that creates a deep copy of the given oop.ex4.data_structures.AvlTree. The new tree
         * contains all the values of the given tree, but not necessarily in the same structure.
         *
         * @param avlTree an AVL tree.
         */
        public AvlTree(AvlTree avlTree) {

            this();
            Iterator<Integer> avlIterator = avlTree.iterator();
            while (avlIterator.hasNext()){
                int newNodeValue = avlIterator.next();
                this.add(newNodeValue);
            }
        }

    /* Public Methods */

        /*
        * Add a new node with the given key to the tree.
        *
        * @param newValue the value of the new node to add.
        * @return true if the value to add is not already in the tree and it was successfully added,
        * false otherwise.
        */
        public boolean add(int newValue) {

            String place = "";
            boolean isAdded = false;

            if (this.contains(newValue) != NOT_CONTAIN){
                return false;
            }

            if (root == null){
                root = new Node(newValue, null);
                numberOfElement++;
                isAdded = true;
            } else {

                Node node = root;
                Node tempNode = node;

                while (node != null){
                    tempNode = node;
                    if (newValue > node.getValue()){
                        node = node.getRightSon();
                        place = "right";
                    } else {
                        node = node.getLeftSon();
                        place = "left";
                    }
                }

                if (place == "right"){
                    tempNode.updateRightSon(newValue);
                    numberOfElement++;
                    isAdded = true;
                } else if (place == "left"){
                    tempNode.updateLeftSon(newValue);
                    numberOfElement++;
                    isAdded = true;
                }
                Node newNode = findNode(newValue);
                if (!isAvl(newNode)){
                    howToRotate(newNode.getFather().getFather());
                }

            }return isAdded;
        }


        /**
         * Check whether the tree contains the given input value.
         *
         * @param searchVal the value to search for.
         * @return the depth of the node (0 for the root) with the given value if it was found in
         * the tree, −1 otherwise.
         */ //
        public int contains(int searchVal) {

            Node nodeSearchVal = findNode(searchVal);
            if (nodeSearchVal == null){
                return NOT_CONTAIN;
            } else{
                return checkDepth(nodeSearchVal);
            }

        }

        /**
         * Removes the node with the given value from the tree, if it exists.
         *
         * @param toDelete the value to remove from the tree.
         * @return true if the given value was found and deleted, false otherwise.
         */
        public boolean delete(int toDelete) {

            boolean isDeleted = false;
            if (contains(toDelete) == NOT_CONTAIN){
                return isDeleted;
            }

            Node nodeToDelete = findNode(toDelete);
            if (nodeToDelete != null) {

                if (nodeToDelete.getRightSon() == null && nodeToDelete.getLeftSon() == null) {
                    nodeToDelete = null;
                    numberOfElement--;
                    isDeleted = true;
                } else if (nodeToDelete.getRightSon() == null && nodeToDelete.getLeftSon() != null) {
                    nodeToDelete.setValue(nodeToDelete.getLeftSon().getValue());
                    numberOfElement--;
                    isDeleted = true;

                } else if (nodeToDelete.getRightSon() != null && nodeToDelete.getLeftSon() == null) {
                    nodeToDelete.setValue(nodeToDelete.getRightSon().getValue());
                    numberOfElement--;
                    isDeleted = true;

                } else if (nodeToDelete.getRightSon() != null && nodeToDelete.getLeftSon() != null) {
                    int temp = nodeToDelete.getValue();
                    Node traceNode = findTrace(nodeToDelete);
                    nodeToDelete.setValue(traceNode.getValue());
                    traceNode.setValue(temp);
                    numberOfElement--;
                    isDeleted = true;

                    delete(traceNode.getValue());
                }
                if (!isAvl(nodeToDelete)) {
                    howToRotate(nodeToDelete);
                }
            }
            return isDeleted;
        }


        /**
         * @return the number of nodes in the tree.
         */
        public int size() {
            return numberOfElement;
        }


        /**
         * @return an iterator for the Avl Tree. The returned iterator iterates over the tree nodes
         * in an ascending order, and does NOT implement the remove() method.
         */
        public Iterator<Integer> iterator() {
            return new nestedClassIterator();
        }


    /**
     * class of iterator
     */
    class nestedClassIterator implements Iterator<Integer>{

            Node node = findSmallestElement();

            @Override
            public Integer next(){
                if (node == null){
                    throw new NoSuchElementException();
                }
                int theValue = node.getValue();
                node = findTrace(node);
                return theValue;
            }

            @Override
            public boolean hasNext(){
                return node != null;
            }
        }


    /* Static Methods */

        /**
         * Calculates the minimum number of nodes in an AVL tree of height h.
         *
         * @param h the height of the tree (a non−negative number) in question.
         * @return the minimum number of nodes in an AVL tree of the given height.
         */
        public static int findMinNodes(int h) {

            return (int)(Math.round(((Math.sqrt(5)+2)/ Math.sqrt(5))*Math.pow((1+ Math.sqrt(5))/2,h)-1));
        }

        /**
         * Calculates the maximum number of nodes in an AVL tree of height h.
         *
         * @param h the height of the tree (a non−negative number) in question.
         * @return the maximum number of nodes in an AVL tree of the given height.
         */
        public static int findMaxNodes(int h) {
            return (int)Math.pow(h + 1, 2) - 1;
        }


    /**
     * find the node with the given value
     * @param value to serch for
     * @return the node with the given value
     */
    private Node findNode(int value){
        Node node = root;

        while (node != null){
            if (value == node.getValue()) {
                return node;
            } else if (value > node.getValue()){
                node = node.getRightSon();
            } else {
                node = node.getLeftSon();
            }
        }return null;
    }

    /**
     * Return the depth of the given node
     * @param node to check for
     * @return the depth of the given node
     */
    private int checkDepth(Node node){

            if (node == null || node == root){
                return 0;
            } else {
               return 1 + checkDepth(node.getFather());
            }
    }


    /**
     * Return the height of the given node
     * @param node to check for
     * @return the height of the given node
     */
    private int checkHeight(Node node){
        if (node == null){
            return HEIGHT_OF_EMPTY_NODE;
        } else{
            return 1 + Math.max(checkHeight(node.getLeftSon()), checkHeight(node.getRightSon()));
        }
    }


    /**
     * checks if the tree need balance that will make him avl tree
     * @param node the root of the tree
     * @return true if the tree has the properties of avl tree, false otherwise
     */
    private boolean isAvl(Node node){

        while (node != null) {

            int rightHeight = checkHeight(node.getRightSon());
            int leftHeight = checkHeight(node.getLeftSon());

            if (Math.abs(rightHeight - leftHeight) > 1) {
                return false;
            } else {
                node = node.getFather();
            }
        }
        return true;
    }

    /**
     *balance the avl tree to left side
     * @param node to rotate him
     */
    private void balanceRight(Node node) {

        Node otherNode = node.getLeftSon();
        otherNode.setFather(node.getFather());
        node.setLeftSon(otherNode.getRightSon());
        otherNode.setRightSon(node);

        Node theFather = otherNode.getFather();
        if (theFather != null){
            if (theFather.getLeftSon() == node){
                theFather.setLeftSon(otherNode);
            }
            else {
                theFather.setRightSon(otherNode);
            }
        } else {
            root = otherNode;
        }
    }

    /**
     * balance the avl tree to left side
     * @param node to rotate him
     */
    private void balanceLeft(Node node){

        Node otherNode = node.getRightSon();
        otherNode.setFather(node.getFather());
        node.setRightSon(otherNode.getLeftSon());
        otherNode.setLeftSon(node);
        node.setFather(otherNode);

        Node theFather = otherNode.getFather();
        if (theFather != null){
            if (theFather.getRightSon() == node){
                theFather.setRightSon(otherNode);
            }
            else {
                theFather.setLeftSon(otherNode);
            }
        } else {
            root = otherNode;
        }
    }

    /**
     * chechs how to do rotation according to the balance factors
     * @param node to checks for
     */
    private void howToRotate(Node node){

        int rightBalanceFactor, leftBalanceFactor;

        int nodeBalanceFactor = checkHeight(node.getLeftSon()) - checkHeight(node.getRightSon());
        if (node.getRightSon() == null){
            rightBalanceFactor = -1;
        } else{
            rightBalanceFactor = checkHeight(node.getRightSon().getLeftSon()) -
                    checkHeight(node.getRightSon().getRightSon());
        }
        if (node.getLeftSon() == null){
            leftBalanceFactor = -1;
        } else {
            leftBalanceFactor = checkHeight(node.getLeftSon().getLeftSon()) -
                    checkHeight(node.getLeftSon().getRightSon());
        }

        if (nodeBalanceFactor == 2 && (leftBalanceFactor == 0 || leftBalanceFactor == 1)){
            balanceRight(node);
        } else if (nodeBalanceFactor == 2 && leftBalanceFactor == -1){
            balanceLeft(node.getLeftSon());
            balanceRight(node);
        } else if (nodeBalanceFactor == -2 && (rightBalanceFactor == 0 || rightBalanceFactor == -1)){
            balanceLeft(node);
        } else if (nodeBalanceFactor == -2 && rightBalanceFactor == 1){
            balanceRight(node.getRightSon());
            balanceLeft(node);
        }
    }


    /**
     * find the smallest element in the tree
     * @return the smallest element
     */
    private Node findSmallestElement(){
        Node node = root;
        Node temp = node;

        while (node != null){
            temp = node;
            node = node.getLeftSon();
        }
        return temp;
    }


    /**
     * find the trace node
     * @param node to find the trace of him
     * @return the trace
     */
    private Node findTrace(Node node){
        Node newNode = node.getRightSon();
        Node temp = newNode;

        while (newNode != null){
            temp = newNode;
            newNode = newNode.getLeftSon();
        }
        return temp;
    }

}

