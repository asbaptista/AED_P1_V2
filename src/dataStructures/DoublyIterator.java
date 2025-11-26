package dataStructures;
import dataStructures.exceptions.*;

/**
 * Implementation of a one-way {@link Iterator} for a Doubly Linked List.
 * Iterates from the head (first node) to the tail (last node).
 *
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
class DoublyIterator<E> implements Iterator<E> {

    // --- Fields ---

    /**
     * Node with the first element in the iteration.
     * This reference is final and used by {@link #rewind()}.
     */
    private final DoublyListNode<E> firstNode;

    /**
     * Node containing the next element to be returned by {@link #next()}.
     * This reference is advanced during iteration.
     */
    DoublyListNode<E> nextToReturn;

    // --- Constructor ---

    /**
     * DoublyIterator constructor.
     *
     * @apiNote Time Complexity: O(1)
     * @param first - Node with the first element of the iteration.
     */
    public DoublyIterator(DoublyListNode<E> first) {
        this.firstNode = first;
        this.nextToReturn = first;
        //TODO: Left as an exercise.//done
    }

    // --- Public Methods ---

    /**
     * Returns true if the iteration has more elements.
     *
     * @apiNote Time Complexity: O(1)
     * @return {@code true} iff the iteration has more elements.
     */
    public boolean hasNext( ) {
        return nextToReturn != null;
        //TODO: Left as an exercise.
    }

    /**
     * Returns the next element in the iteration and advances the iterator.
     *
     * @apiNote Time Complexity: O(1)
     * @return The next element in the iteration.
     * @throws NoSuchElementException - if call is made without verifying pre-condition.
     */
    public E next( ){
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        E element = nextToReturn.getElement();
        nextToReturn = nextToReturn.getNext();
        return element;
        //TODO: Left as an exercise.//done
    }

    /**
     * Restarts the iterator to the beginning of the list.
     * After rewind, if the iteration is not empty, next() will return the first element.
     *
     * @apiNote Time Complexity: O(1)
     */
    public void rewind(){
        this.nextToReturn = this.firstNode;
    }
    //TODO: Left as an exercise.
}