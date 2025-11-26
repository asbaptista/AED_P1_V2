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

    //TODO: Left as an exercise.


    public KeysIterator(Iterator<Map.Entry<E,?>> it) {
        this.entryIterator = it;

        //TODO: Left as an exercise.//done
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
        return entryIterator.hasNext();
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public E next() {
        if (!entryIterator.hasNext()) {
            throw new NoSuchElementException();
        }
        return entryIterator.next().key();
        //TODO: Left as an exercise.//done
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        entryIterator.rewind();
        //TODO: Left as an exercise.//done
    }
}
