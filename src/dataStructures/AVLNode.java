package dataStructures;

import java.io.Serializable;

/**
 * AVL Tree Node
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
class AVLNode<E> extends BTNode<E> implements Serializable {

    private static final long serialVersionUID = 1L;
    // Height of the node
    protected transient int height;

    public AVLNode(E elem) {
        super(elem);
        height=0;
    }
    
    public AVLNode( E element, AVLNode<E> parent, AVLNode<E> left, AVLNode<E> right ){
        super(element, parent, left, right);
        updateHeight();
        //TODO: Left as an exercise.//done
    }
    public AVLNode( E element, AVLNode<E> parent){
        super(element, parent,null, null);
        height=0;
    }

    public void updateHeight() {
        int leftH = height((AVLNode<E>) getLeftChild());
        int rightH = height((AVLNode<E>) getRightChild());
        this.height = 1 + Math.max(leftH, rightH);
    }

    private int height(AVLNode<E> node) {
        if (node==null)	return -1;
        return node.getHeight();
    }
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Update the left child and height
     * @param node
     */
    public void setLeftChild(AVLNode<E> node) {
        super.setLeftChild(node);
        updateHeight();
        //TODO: Left as an exercise.//done
    }

    /**
     * Update the right child and height
     * @param node
     */
    public void setRightChild(AVLNode<E> node) {
        super.setRightChild(node);
        updateHeight();
        //TODO: Left as an exercise.//done
    }


    public int getBalanceFactor() {
        return height((AVLNode<E>) getLeftChild()) - height((AVLNode<E>) getRightChild());
    }

    public boolean isBalanced() {
        int balance = getBalanceFactor();
        return (balance >= -1) && (balance <= 1);
    }


// others public methods
//TODO: Left as an exercise.


}
