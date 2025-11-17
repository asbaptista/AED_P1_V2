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
    //TODO: Left as an exercise./done


    public ValuesIterator(Iterator<Map.Entry<?,E>> it) {
        this.entryValuesIterator = it;
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return entryValuesIterator.hasNext();
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    @Override
    public E next() {
        if(!entryValuesIterator.hasNext()) {
            throw new NoSuchElementException();
        }
        return entryValuesIterator.next().value();
        //TODO: Left as an exercise.//done
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    @Override
    public void rewind() {
        entryValuesIterator.rewind();
        //TODO: Left as an exercise.//done
    }
}
