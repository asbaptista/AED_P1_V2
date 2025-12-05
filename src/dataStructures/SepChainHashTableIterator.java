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
    /**
     * Constructor
     * @apiNote Time Complexity: O(M) worst case where M is table size (finds first non-empty bucket)
     * @param table - hash table array
     */
    public SepChainHashTableIterator(Map<K,V>[] table) {
        this.table = table;
        this.rewind();
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @apiNote Time Complexity: O(1)
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
        return currentBucketIterator != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @apiNote Time Complexity: O(M) worst case where M is table size (may need to find next non-empty bucket), amortized O(1)
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
     * @apiNote Time Complexity: O(M) where M is table size (finds first non-empty bucket)
     */
    public void rewind() {
        this.currentBucketIndex = -1;
        this.currentBucketIterator = null;
        findNextToReturn();
    }

    /**
     * Helper method to find the next non-empty bucket.
     * @apiNote Time Complexity: O(M) worst case where M is table size
     */
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

