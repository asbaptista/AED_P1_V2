package dataStructures;

import java.io.*;

/**
 * Binary Tree Node
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
class BTNode<E> implements Node<E> {

    // Element stored in the node.
    private transient E element;

    // (Pointer to) the father.
    private transient Node<E> parent;

    // (Pointer to) the left child.
    private transient Node<E> leftChild;

    // (Pointer to) the right child.
    private transient Node<E> rightChild;

    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     * @param elem - element to store
     */
    BTNode(E elem){
        this(elem,null,null,null);
    }

    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     * @param elem - element to store
     * @param parent - parent node
     */
    BTNode(E elem, BTNode<E> parent) {
        this(elem,parent,null,null);
    }
    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     * @param elem - element to store
     * @param parent - parent node
     * @param leftChild - left child node
     * @param rightChild - right child node
     */
    BTNode(E elem, BTNode<E> parent, BTNode<E> leftChild, BTNode<E> rightChild){
        this.element = elem;
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        //TODO: Left as an exercise.//done
    }

    /**
     *  Returns the element of the node
     * @apiNote Time Complexity: O(1)
     * @return element stored in the node
     */
    public E getElement() {
        return element;
    }
    /**
     * Returns the left son of node
     * @apiNote Time Complexity: O(1)
     * @return left child node
     */
    public Node<E> getLeftChild(){
        return leftChild;
    }
    /**
     * Returns the right son of node
     * @apiNote Time Complexity: O(1)
     * @return right child node
     */
    public Node<E> getRightChild(){
        return rightChild;
    }
    /**
     * Returns the parent of node
     * @apiNote Time Complexity: O(1)
     * @return parent node
     */
    public Node<E> getParent(){
        return parent;
    }

    /**
     * Returns true if node n does not have any children.
     * @apiNote Time Complexity: O(1)
     * @return true if node is a leaf
     */
    boolean isLeaf() {
        return getLeftChild()== null && getRightChild()==null;
    }

    /**
     * Update the element
     * @apiNote Time Complexity: O(1)
     * @param elem - new element
     */
    public void setElement(E elem) {
        element=elem;
    }

    /**
     * Update the left child
     * @apiNote Time Complexity: O(1)
     * @param node - new left child
     */
    public void setLeftChild(Node<E> node) {
        leftChild=node;
    }

    /**
     * Update the right child
     * @apiNote Time Complexity: O(1)
     * @param node - new right child
     */
    public void setRightChild(Node<E> node) {
        rightChild=node;
    }

    /**
     * Update the parent
     * @apiNote Time Complexity: O(1)
     * @param node - new parent
     */
    public void setParent(Node<E> node) {
        parent=node;
    }

    /**
     * Returns true if is the root
     * @apiNote Time Complexity: O(1)
     * @return true if node is root
     */
    boolean isRoot() {
        return getParent()==null;
    }

    /**
     * Returns the height of the subtree rooted at this node.
     * @apiNote Time Complexity: O(N) where N is number of nodes in subtree (recursive computation)
     * @return height of subtree
     */

    public int getHeight() {
        int leftHeight = (leftChild != null) ? ((BTNode<E>) leftChild).getHeight() : -1;
        int rightHeight = (rightChild != null) ? ((BTNode<E>) rightChild).getHeight() : -1;
        return 1 + Math.max(leftHeight, rightHeight);
    }


    /**
     * Returns the leftmost node in the subtree rooted at this node.
     * @apiNote Time Complexity: O(h) where h is the height
     * @return leftmost node
     */
    BTNode<E> furtherLeftElement() {
        BTNode<E> node = this;
        while (node.getLeftChild() != null) {
            node = (BTNode<E>) node.getLeftChild();
        }
        return node;
    }

    /**
     * Returns the rightmost node in the subtree rooted at this node.
     * @apiNote Time Complexity: O(h) where h is the height
     * @return rightmost node
     */
    BTNode<E> furtherRightElement() {
        BTNode<E> node = this;
        while (node.getRightChild() != null) {
            node = (BTNode<E>) node.getRightChild();
        }
        return node;
    }

}
