package dataStructures;

import java.io.Serializable;

/**
 * SepChain Hash Table
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class SepChainHashTable<K,V> extends HashTable<K,V> implements Serializable {

    private static final long serialVersionUID = 1L;
    //Load factors
    static final float IDEAL_LOAD_FACTOR =0.75f;
    static final float MAX_LOAD_FACTOR =0.9f;

    // The array of Map with singly linked list.
    private transient Map<K,V>[] table;

    public SepChainHashTable( ){
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public SepChainHashTable( int capacity ){
        super(capacity);

        int arraySize = HashTable.nextPrime((int)(capacity/IDEAL_LOAD_FACTOR));
        this.table = (Map<K,V>[]) new MapSinglyList[arraySize];
        for (int i = 0; i < arraySize; i++){
            table[i] = new MapSinglyList<>();
        }
        this.maxSize = (int)(arraySize * MAX_LOAD_FACTOR);
       //TODO: Left as exercise//done
    }

    // Returns the hash value of the specified key.
    protected int hash( K key ){
        return Math.abs( key.hashCode() ) % table.length;
    }
    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    public V get(K key) {
        int index = hash(key);
        return table[index].get(key);
        //TODO: Left as an exercise.//done
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * replaces its value by the specified value and returns the old value;
     * otherwise, inserts the entry (key, value) and returns null.
     *
     * @param key   with which the specified value is to be associated
     * @param value to be associated with the specified key
     * @return previous value associated with key,
     * or null if the dictionary does not have an entry with that key
     */
    public V put(K key, V value) {
        if (isFull())
            rehash();

        int index = hash(key);
        Map<K,V> map = table[index];
        V oldValue = map.put(key, value);

        if (oldValue == null) {
            currentSize++;
        }
        return oldValue;

        //TODO: Left as an exercise.//done
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        Map<K,V>[] oldTable = table;
        int newArraySize = HashTable.nextPrime(2 * oldTable.length);
        table = (Map<K,V>[]) new MapSinglyList[newArraySize];
        for (int i = 0; i < newArraySize; i++) {
            table[i] = new MapSinglyList<>();
        }
        this.currentSize = 0;
        this.maxSize = (int)(newArraySize * IDEAL_LOAD_FACTOR);

        for (Map<K,V> map : oldTable) {
            Iterator<Entry<K,V>> it = map.iterator();
            while (it.hasNext()) {
                Entry<K,V> entry = it.next();
                this.put(entry.key(), entry.value());
            }
        }

        //TODO: Left as an exercise.//done
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * removes it from the dictionary and returns its value;
     * otherwise, returns null.
     *
     * @param key whose entry is to be removed from the map
     * @return previous value associated with key,
     * or null if the dictionary does not an entry with that key
     */
    public V remove(K key) {
        int index = hash(key);
        V oldValue = table[index].remove(key);
        if (oldValue != null) {
            currentSize--;
        }
        return oldValue;
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SepChainHashTableIterator<>(table);
    }


    private void writeObject(java.io.ObjectOutputStream out)
            throws java.io.IOException {
        out.defaultWriteObject();
        out.writeInt(table.length);
        for (Map<K,V> map : table) {
            Iterator<Entry<K,V>> it = map.iterator();
            while (it.hasNext()) {
                Entry<K,V> entry = it.next();
                out.writeObject(entry.key());
                out.writeObject(entry.value());
            }
        }
        out.writeObject(null); // End marker
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        int tableLength = in.readInt();
        this.table = (Map<K, V>[]) new MapSinglyList[tableLength];
        for (int i = 0; i < tableLength; i++) {
            table[i] = new MapSinglyList<>();
        }
    }


}
