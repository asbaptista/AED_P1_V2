package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
import java.io.Serializable;


public class ListsIterator<E> implements Iterator<E> {

    private List<E>[] lists;

    private int currentListIndex;

    private Iterator<E> currentIterator;


    /**
     * Constructor
     * @apiNote Time Complexity: O(M) worst case where M is number of lists (finds first non-empty list)
     * @param lists - array of lists
     */
    public ListsIterator(List<E>[] lists) {
        this.lists = lists;
        this.currentListIndex = lists.length -1 ;
        this.currentIterator = null;
        advanceToNext();
    }

    /**
     * Advances to the next non-empty list.
     * @apiNote Time Complexity: O(M) worst case where M is number of lists
     */
    private void advanceToNext() {
        while (currentListIndex >= 0) {
            List<E> currentList = lists[currentListIndex];
            if (currentList != null) {
                currentIterator = currentList.iterator();
                if (currentIterator.hasNext()) {
                    return;
                }
            }
            currentListIndex--;
        }
        currentIterator = null;
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     * @apiNote Time Complexity: O(1)
     * @return true iff the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return currentIterator != null && currentIterator.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     * @apiNote Time Complexity: O(M) worst case where M is number of lists (may need to find next list), amortized O(1)
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        E element = currentIterator.next();
        if (!currentIterator.hasNext()) {
            currentListIndex--;
            advanceToNext();
        }
        return element;
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     * @apiNote Time Complexity: O(M) where M is number of lists (finds first non-empty list)
     */
    @Override
    public void rewind() {
        this.currentListIndex = lists.length - 1;
        this.currentIterator = null;
        advanceToNext();
    }
}

