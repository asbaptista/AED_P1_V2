package dataStructures;

import dataStructures.exceptions.*;

/**
 * Array Iterator.
 * Implements the {@link Iterator} interface for a fixed-size array.
 * This iterator is "fail-fast" in the sense that it iterates over a
 * snapshot of the array's state at the time of its creation and
 * does not reflect subsequent modifications to the underlying array.
 *
 * @author AED Team
 * @version 1.0
 * @param <E> Generic Element
 */
class ArrayIterator<E> implements Iterator<E> {

    // --- Fields ---

    /**
     * The underlying array of elements to iterate over.
     */
    private E[] elems;

    /**
     * The total number of elements in the array (array's logical size).
     */
    private int counter;

    /**
     * The index of the next element to be returned by {@link #next()}.
     */
    private int current;

    // --- Constructor ---

    /**
     * Creates a new iterator for the given array.
     *
     * @apiNote Time Complexity: O(1)
     * @param elems The array to iterate over.
     * @param counter The number of elements in the array to iterate.
     */
    public ArrayIterator(E[] elems, int counter) {
        this.elems = elems;
        this.counter = counter;
        rewind();
    }

    // --- Public Methods ---

    /**
     * Resets the iterator to the beginning of the array.
     *
     * @apiNote Time Complexity: O(1)
     */
    @Override
    public void rewind() {
        current = 0;
    }

    /**
     * Checks if the iteration has more elements.
     *
     * @apiNote Time Complexity: O(1)
     * @return {@code true} if the iterator has more elements, {@code false} otherwise.
     */
    @Override
    public boolean hasNext() {
        return current < counter;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @apiNote Time Complexity: O(1)
     * @return The next element in the iteration.
     * @throws NoSuchElementException if the iteration has no more elements.
     */
    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return elems[current++];
    }

}