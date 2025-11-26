package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * An iterator that wraps another iterator and filters its elements.
 * This iterator only returns elements that satisfy a given {@link Predicate}.
 * It uses a "look-ahead" mechanism to find the next valid element.
 *
 * @author AED Team
 * @version 1.0
 * @param <E> Generic Element
 */
public class FilterIterator<E> implements Iterator<E> {

    // --- Fields ---

    /**
     * The underlying (wrapped) iterator that provides the original elements.
     */
    private Iterator<E> iterator;

    /**
     * The condition (filter) that each element must satisfy to be returned.
     */
    private Predicate<E> filter;

    /**
     * The "look-ahead" element. Stores the next element that matches
     * the filter, or null if the end of the iteration is reached or
     * no matching element has been found yet.
     */
    private E nextToReturn;

    // --- Constructor ---

    /**
     * Creates a new FilterIterator.
     * <p>
     * This constructor "primes" the iterator by immediately calling
     * {@link #advanceNext()} to find the first valid element.
     *
     * @apiNote Time Complexity: Best-Case O(1) (if the first element matches);
     * Worst-Case O(N) (if no elements match, or only the last element matches),
     * where N is the total number of elements in the wrapped iterator.
     *
     * @param list The iterator to be wrapped and filtered.
     * @param filter The {@link Predicate} to apply.
     */
    public FilterIterator(Iterator<E> list, Predicate<E> filter) {
        this.iterator = list;
        this.filter = filter;
        advanceNext();
        //TODO: Left as an exercise.//done
    }

    // --- Public Methods (from Iterator interface) ---

    /**
     * Returns true if the iteration has more elements that match the filter.
     *
     * @apiNote Time Complexity: O(1)
     * @return {@code true} if a matching element is ready to be returned,
     * {@code false} otherwise.
     */
    public boolean hasNext() {
        return nextToReturn != null;
        //TODO: Left as an exercise.
    }

    /**
     * Returns the next element in the iteration that matches the filter.
     * <p>
     * This method returns the pre-fetched element and then calls
     * {@link #advanceNext()} to find the next valid element for the
     * subsequent call.
     *
     * @apiNote Time Complexity: Best-Case O(1) (if the very next element in the
     * wrapped iterator matches); Worst-Case O(N) (if no more elements match),
     * where N is the *remaining* number of elements in the wrapped iterator.
     *
     * @return The next element in the iteration that satisfies the filter.
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     * (i.e., if {@link #hasNext()} is false).
     */
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        E element = nextToReturn;
        advanceNext(); // Find the next element for the *next* call
        return element;
        //TODO: Left as an exercise.//done
    }

    /**
     * Restarts the iteration.
     * This rewinds the wrapped iterator and then searches for the
     * first element that matches the filter.
     *
     * @apiNote Time Complexity: Best-Case O(1) (if the first element matches);
     * Worst-Case O(N) (if no elements match), where N is the total
     * number of elements in the wrapped iterator.
     */
    public void rewind() {
        iterator.rewind();
        advanceNext();
        //TODO: Left as an exercise.//done
    }

    // --- Private Helper Methods ---

    /**
     * A private helper method to find the next element that
     * satisfies the filter and store it in {@code nextToReturn}.
     * <p>
     * It scans the wrapped iterator until a match is found or
     * the iterator is exhausted.
     */
    private void advanceNext(){
        nextToReturn = null;
        while (iterator.hasNext() && nextToReturn == null) {
            E element = iterator.next();
            if(filter.check(element)){
                nextToReturn = element;
                break; // Found a matching element
            }
        }
        // If loop finishes, nextToReturn is either the matched element or null
    }

}