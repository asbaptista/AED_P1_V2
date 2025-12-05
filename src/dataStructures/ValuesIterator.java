package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
/**
 * Iterator of values
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic element
 */
class ValuesIterator<E> implements Iterator<E> {
    private Iterator<Map.Entry<?,E>> entryValuesIterator;
    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     * @param it - iterator of entries
     */
    public ValuesIterator(Iterator<Map.Entry<?,E>> it) {
        this.entryValuesIterator = it;
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @apiNote Time Complexity: O(1)
     * @return true iff the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return entryValuesIterator.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @apiNote Time Complexity: O(1)
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    @Override
    public E next() {
        if(!entryValuesIterator.hasNext()) {
            throw new NoSuchElementException();
        }
        return entryValuesIterator.next().value();
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     * @apiNote Time Complexity: O(1)
     */
    @Override
    public void rewind() {
        entryValuesIterator.rewind();
    }
}
