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

    //TODO: Left as exercise

    public SepChainHashTableIterator(Map<K,V>[] table) {
        this.table = table;
        this.rewind();
        //TODO: Left as exercise//done. talvez de so com o rewind, verificar dps se faciliia assim
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
	//TODO: Left as exercise//done ?
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
        //TODO: Left as exercise//done/

    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        this.currentBucketIndex = -1;
        this.currentBucketIterator = null;
        findNextToReturn();
        //TODO: Left as exercise//done
    }

    private void findNextToReturn() {
        if(currentBucketIterator != null && currentBucketIterator.hasNext()) {
            return;
            //basicamente se o anterior ainda tiver p devolver
        }
        currentBucketIndex++;

        while(currentBucketIndex < table.length) {
            if(!table[currentBucketIndex].isEmpty()) {
                currentBucketIterator = table[currentBucketIndex].iterator();
                return;
            }
            currentBucketIndex++;
        }
        currentBucketIterator = null; //n ha mais na table

    }
}

