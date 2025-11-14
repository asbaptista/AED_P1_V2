package dataStructures_2ndPhase;

import dataStructures_2ndPhase.exceptions.NoSuchElementException;
/**
 * Iterator of values
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic element
 */
class ValuesIterator<E> implements Iterator<E> {

    //TODO: Left as an exercise.
   

    public ValuesIterator(Iterator<Map.Entry<?,E>> it) {
        //TODO: Left as an exercise.
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    @Override
    public boolean hasNext() {
       //TODO: Left as an exercise.
	return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    @Override
    public E next() {
	//TODO: Left as an exercise.
        return null;
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    @Override
    public void rewind() {
       //TODO: Left as an exercise.
    }
}
