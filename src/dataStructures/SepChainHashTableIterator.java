package dataStructures;
/**
 * SepChain Hash Table Iterator
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
import dataStructures.exceptions.NoSuchElementException;

class SepChainHashTableIterator<K,V> implements Iterator<Map.Entry<K,V>> {

    private Map<K,V>[] table;
    private int currentBucketIndex;
    private Iterator<Map.Entry<K,V>> currentBucketIterator;


    public SepChainHashTableIterator(Map<K,V>[] table) {
        this.table = table;
        this.rewind();
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
        return currentBucketIterator != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public Map.Entry<K,V> next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        Map.Entry<K,V> nextEntry = currentBucketIterator.next();
        findNextToReturn();

        return nextEntry;

    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        this.currentBucketIndex = -1;
        this.currentBucketIterator = null;
        findNextToReturn();
    }

    private void findNextToReturn() {
        if(currentBucketIterator != null && currentBucketIterator.hasNext()) {
            return;
        }
        currentBucketIndex++;

        while(currentBucketIndex < table.length) {
            if(!table[currentBucketIndex].isEmpty()) {
                currentBucketIterator = table[currentBucketIndex].iterator();
                return;
            }
            currentBucketIndex++;
        }
        currentBucketIterator = null;

    }
}

