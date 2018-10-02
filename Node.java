package oop.ex4.data_structures;

/**
 * Created by hagitba on 5/9/17.
 */

/**
 * create a node
 */
public class Node {

    public int value;
    private Node father, rightSon, leftSon;


    /**
     *constructor, create new node
     * @param value the value of the node
     * @param father the father of the node
     */
    public Node(int value, Node father){

        this.value = value;
        this.father = father;
    }


    /**
     * get the value of the node
     * @return the value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * set the value of the node
     * @param value the new value of the node
     */
    public void setValue(int value){
        this.value = value;
    }

    /**
     * get the father of the node
     * @return the father
     */
    public Node getFather() {
        return this.father;
    }

    /**
     * set the father of the node
     * @param father the new father
     */
    public void setFather(Node father) {
        this.father = father;
    }

    /**
     * get the left son of the node
     * @return the left son
     */
    public Node getLeftSon(){
        return leftSon;
    }

    /**
     * get the right son of the node
     * @return the right son
     */
    public Node getRightSon() {
        return rightSon;
    }

    /**
     * set the right son of the node
     * @param rightSon the new right son to update
     */
    public void setRightSon(Node rightSon) {
        this.rightSon = rightSon;
    }

    /**
     * set the left son of the node
     * @param leftSon the new left son to update
     */
    public void setLeftSon(Node leftSon) {
        this.leftSon = leftSon;
    }

    /**
     * update the right son of the node
     * @param rightSon the new right son to update
     */
    public void updateRightSon(int rightSon) {
        this.rightSon = new Node(rightSon, this);
    }

    /**
     * set the left son of the node
     * @param leftSon the new left son to update
     */
    public void updateLeftSon(int leftSon) {
        this.leftSon = new Node(leftSon, this);
    }

}

