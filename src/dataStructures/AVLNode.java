package dataStructures;

import java.io.Serial;
import java.io.Serializable;

/**
 * AVL Tree Node
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
class AVLNode<E> extends BTNode<E> {
    protected transient int height;

    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     * @param elem - element to store
     */
    public AVLNode(E elem) {
        super(elem);
        height=0;
    }
    
    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     * @param element - element to store
     * @param parent - parent node
     * @param left - left child
     * @param right - right child
     */
    public AVLNode( E element, AVLNode<E> parent, AVLNode<E> left, AVLNode<E> right ){
        super(element, parent, left, right);
        updateHeight();
    }

    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     * @param element - element to store
     * @param parent - parent node
     */
    public AVLNode( E element, AVLNode<E> parent){
        super(element, parent,null, null);
        height=0;
    }

    /**
     * Updates the height of this node based on children's heights.
     * @apiNote Time Complexity: O(1)
     */
    public void updateHeight() {
        int leftH = height((AVLNode<E>) getLeftChild());
        int rightH = height((AVLNode<E>) getRightChild());
        this.height = 1 + Math.max(leftH, rightH);
    }

    /**
     * Helper method to get height of a node.
     * @apiNote Time Complexity: O(1)
     * @param node - the node
     * @return height of the node
     */
    private int height(AVLNode<E> node) {
        if (node==null)	return -1;
        return node.getHeight();
    }

    /**
     * Returns the height of this node.
     * @apiNote Time Complexity: O(1) (height is cached)
     * @return height of the node
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Update the left child and height
     * @apiNote Time Complexity: O(1)
     * @param node - new left child
     */
    public void setLeftChild(AVLNode<E> node) {
        super.setLeftChild(node);
        updateHeight();
    }

    /**
     * Update the right child and height
     * @apiNote Time Complexity: O(1)
     * @param node - new right child
     */
    public void setRightChild(AVLNode<E> node) {
        super.setRightChild(node);
        updateHeight();
    }

    /**
     * Returns the balance factor of this node.
     * @apiNote Time Complexity: O(1)
     * @return balance factor (left height - right height)
     */
    public int getBalanceFactor() {
        return height((AVLNode<E>) getLeftChild()) - height((AVLNode<E>) getRightChild());
    }

    /**
     * Returns true if this node is balanced.
     * @apiNote Time Complexity: O(1)
     * @return true if node is balanced (balance factor in [-1, 1])
     */
    public boolean isBalanced() {
        int balance = getBalanceFactor();
        return (balance >= -1) && (balance <= 1);
    }


}
