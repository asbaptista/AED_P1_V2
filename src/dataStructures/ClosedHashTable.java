package dataStructures;

import java.io.Serializable;

/**
 * Closed Hash Table
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class ClosedHashTable<K,V> extends HashTable<K,V> implements Serializable {

    private static final long serialVersionUID = 1L; // <--- ADICIONAR
    //Load factors
    static final float IDEAL_LOAD_FACTOR =0.5f;
    static final float MAX_LOAD_FACTOR =0.8f;
    static final int NOT_FOUND=-1;

    // removed cell
    static final Entry<?,?> REMOVED_CELL = new Entry<>(null,null);

    // The array of entries.
    private Entry<K,V>[] table;

    /**
     * Constructors
     */

    public ClosedHashTable( ){
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ClosedHashTable( int capacity ){
        super(capacity);
        int arraySize = HashTable.nextPrime((int) (capacity / IDEAL_LOAD_FACTOR));
        // Compiler gives a warning.
        table = (Entry<K,V>[]) new Entry[arraySize];
        for ( int i = 0; i < arraySize; i++ )
            table[i] = null;
        maxSize = (int)(arraySize * MAX_LOAD_FACTOR);
    }

    //Methods for handling collisions.
    // Returns the hash value of the specified key.
    int hash( K key, int i ){
        return Math.abs( key.hashCode() + i) % table.length;
    }
    /**
     * Linear Proving
     * @param key to search
     * @return the index of the table, where is the entry with the specified key, or null
     */
    int searchLinearProving(K key) {
        for (int i = 0; i < table.length; i++) {
            int index = hash(key, i);
            Entry<K,V> entry = table[index];
            if (entry == null) {
                return NOT_FOUND; // Key not found
            }
            if (entry != REMOVED_CELL && entry.key().equals(key)) {
                return index; // Key found
            }
        }
        //TODO: Left as an exercise.//done
        return NOT_FOUND; 
    }

    
    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    @Override
    public V get(K key) {
        int index = searchLinearProving(key);
        if (index != NOT_FOUND) {
            return table[index].value();
        }
        //TODO: Left as an exercise.//done
        
        return null;
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
    @Override
    public V put(K key, V value) {
        if (isFull())
            rehash();

        int index = searchLinearProving(key);
        if (index != NOT_FOUND) {
            V oldValue = table[index].value();
            table[index] = new Entry<>(key, value);
            return oldValue;
        }
        int insertionIdx = -1;
        for (int i = 0; i < table.length; i++) {
            int idx = hash(key, i);
            if (table[idx] == REMOVED_CELL) {
                if (insertionIdx == -1) {
                    insertionIdx = idx;
                }
            } else if (table[idx] == null) {
                if (insertionIdx == -1) {
                    insertionIdx = idx;
                }
                break;
            }
        }

        table[insertionIdx] = new Entry<>(key, value);
        currentSize++;
        //TODO: Left as an exercise.//done
        return null;
    }

     private void rehash(){
         Entry<K,V>[] oldTable = table;
         int newCapacity = HashTable.nextPrime(table.length * 2);
         table = (Entry<K,V>[]) new Entry[newCapacity]; // cast talvez nao necessario
         currentSize = 0;
         maxSize = (int)(newCapacity * MAX_LOAD_FACTOR);

         for (int i = 0; i < table.length; i++) {
             table[i] = null;
         }
         for (Entry<K,V> entry : oldTable) {
             if (entry != null && entry != REMOVED_CELL) {
                 put(entry.key(), entry.value());
             }
         }


 //TODO: Left as an exercise.
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
    @Override
    public V remove(K key) {
        int index = searchLinearProving(key);

        if (index == NOT_FOUND) {
            return null;
        }
        V oldValue = table[index].value();
        table[index] = (Entry<K,V>) REMOVED_CELL;
        currentSize--;

        return oldValue;

        //TODO: Left as an exercise.//done
        
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new FilterIterator<>(new ArrayIterator<>(table,table.length-1), m ->  m!=null && m!= REMOVED_CELL);
         //TODO: Left as an exercise.//done

    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws java.io.IOException {
        out.defaultWriteObject();
        out.writeInt(table.length);
        for (Entry<K,V> entry : table) {
            if (entry != null && entry != REMOVED_CELL) {
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
        this.table = (Entry<K, V>[]) new Entry[tableLength];
        for (int i = 0; i < tableLength; i++) {
            table[i] = null;
        }

        Object key;
        while ((key = in.readObject()) != null) {
            V value = (V) in.readObject();
            put((K) key, value);
        }
    }

}
