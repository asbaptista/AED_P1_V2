package dataStructures;

import dataStructures.exceptions.*;

/**
 * Implementation of Two Way Iterator for DLList 
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 * 
 */
class TwoWayDoublyIterator<E> extends DoublyIterator<E>
        implements TwoWayIterator<E> {

    /**
     * Node with the first element in the iteration.
     */
    private DoublyListNode<E> lastNode;
    /**
     * Node with the previous element in the iteration.
     */
    private DoublyListNode<E> prevToReturn;

    /**
     * DoublyLLIterator constructor
     *
     * @apiNote Time Complexity: O(1)
     * @param first - Node with the first element of the iteration
     * @param last  - Node with the last element of the iteration
     */
    public TwoWayDoublyIterator(DoublyListNode<E> first, DoublyListNode<E> last) {
        super(first);
        this.lastNode = last;
        this.prevToReturn = null;
    }

    /**
     * Returns true if previous would return an element
     * rather than throwing an exception.
     * @apiNote Time Complexity: O(1)
     * @return true iff the iteration has more elements in the reverse direction
     */
    public boolean hasPrevious( ) {
        return prevToReturn != null;
    }

    /**
     * Returns the next element in the iteration.
     * @apiNote Time Complexity: O(1)
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public E next( ){
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        E element = super.next();
        if(this.nextToReturn == null) {
            this.prevToReturn = lastNode;
        } else {
            this.prevToReturn = this.nextToReturn.getPrevious();

        }
        return element;
    }

    /**
     * Returns the previous element in the iteration.
     * @apiNote Time Complexity: O(1)
     * @return previous element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public E previous( ) {
        if(!hasPrevious()) {
            throw new NoSuchElementException();
        }
        E element = prevToReturn.getElement();
        this.nextToReturn = prevToReturn;
        this.prevToReturn = this.prevToReturn.getPrevious();
        return element;
    }

    /**
     * Restarts the iteration in the reverse direction.
     * After fullForward, if iteration is not empty,
     * previous will return the last element
     * @apiNote Time Complexity: O(N) (traverses all elements)
     */

    public void fullForward() {
        while (hasNext()) {
            next();
        }
        this.prevToReturn = lastNode;
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     * @apiNote Time Complexity: O(1)
     */
    public void rewind() {
        super.rewind();
        this.prevToReturn = null;
    }
}
