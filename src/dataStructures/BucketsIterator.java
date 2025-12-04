package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
import java.io. Serializable;


public class BucketsIterator<E> implements Iterator<E>, Serializable {

    private static final long serialVersionUID = 1L;

    private List<E>[] lists;

    private int currentListIndex;

    private Iterator<E> currentIterator;




    // "reverse" Buckets Iterator that goes from the last bucket to the first
    public BucketsIterator(List<E>[] lists) {
        this.lists = lists;
        this.currentListIndex = lists.length -1 ;
        this.currentIterator = null;
        advanceToNext();
    }


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

    @Override
    public boolean hasNext() {
        return currentIterator != null && currentIterator.hasNext();
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        E element = currentIterator. next();
        if (! currentIterator.hasNext()) {
            currentListIndex--;
            advanceToNext();
        }
        return element;
    }

    @Override
    public void rewind() {
        this.currentListIndex = lists.length - 1;
        this.currentIterator = null;
        advanceToNext();
    }
}