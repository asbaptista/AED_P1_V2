package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * In-order Binary Tree iterator
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
public class InOrderIterator<E> implements Iterator<E> {

    /**
     * Node with the current element
     */
    private BTNode<E> next;

    /**
     * Root Node
     */
    private BTNode<E> root;

    /**
     *
     * @param root
     */
    public  InOrderIterator(BTNode<E> root) {
        this.root=root;
        rewind();
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return next!=null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    @Override
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();
        E elem=next.getElement();
        advance();
        return elem;
    }

    private void advance() {
        if (next.getRightChild()!=null){
            next = ((BTNode<E>)next.getRightChild()).furtherLeftElement();
        }
        else {
            BTNode<E> current = next;
            BTNode<E> parent = (BTNode<E>) current.getParent();
            while (parent != null && current == parent.getRightChild()) {
                current = parent;
                parent = (BTNode<E>) parent.getParent();
            }
            next = parent;
        }
        //TODO: Left as an exercise.//done

    }


    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        if (root==null)
            next=null;
        else
            next=root.furtherLeftElement();
    }
}
