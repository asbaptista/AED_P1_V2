package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
/**
 * Iterator of keys
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic element
 */
class KeysIterator<E> implements Iterator<E> {

    private Iterator<Map.Entry<E,?>> entryIterator; // por verificar

    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     * @param it - iterator of entries
     */
    public KeysIterator(Iterator<Map.Entry<E,?>> it) {
        this.entryIterator = it;
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @apiNote Time Complexity: O(1)
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
        return entryIterator.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @apiNote Time Complexity: O(1)
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public E next() {
        if (!entryIterator.hasNext()) {
            throw new NoSuchElementException();
        }
        return entryIterator.next().key();
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     * @apiNote Time Complexity: O(1)
     */
    public void rewind() {
        entryIterator.rewind();
    }
}
