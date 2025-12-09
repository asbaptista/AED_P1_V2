package dataStructures;
import java.io.Serializable;

/**
 * Dictionary Abstract Data Type 
 * Includes description of general methods to be implemented by dictionaries.
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value 
 */

public interface Map<K,V>  {

    record Entry<K,V>(K key,V value) implements Serializable {



        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {

            if (this==obj) return true;
            if (obj==null) return false;

            if (obj instanceof Entry) {
                Entry<K,V> other = (Entry<K,V>) obj;
                return (this.key.equals(other.key) && this.value.equals(other.value));
            }
            return false;

        }
    }
    /**
     * Returns true iff the dictionary contains no entries.
     * @apiNote Time Complexity: O(1)
     * @return true if dictionary is empty
     */
    boolean isEmpty( );

    /**
     * Returns the number of entries in the dictionary.
     * @apiNote Time Complexity: O(1)
     * @return number of elements in the dictionary
     */
    int size( );

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     * @apiNote Time Complexity: Implementation-dependent.
     * O(N) for list-based, O(1) average for hash tables, O(log N) for tree-based.
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key, 
     * or null if the dictionary does not have an entry with that key
     */
    V get( K key );

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * replaces its value by the specified value and returns the old value;
     * otherwise, inserts the entry (key, value) and returns null.
     * @apiNote Time Complexity: Implementation-dependent.
     * O(N) for list-based, O(1) average for hash tables, O(log N) for tree-based.
     * @param key with which the specified value is to be associated
     * @param value to be associated with the specified key
     * @return previous value associated with key, 
     * or null if the dictionary does not have an entry with that key
     */
    V put( K key, V value );

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * removes it from the dictionary and returns its value;
     * otherwise, returns null.
     * @apiNote Time Complexity: Implementation-dependent.
     * O(N) for list-based, O(1) average for hash tables, O(log N) for tree-based.
     * @param key whose entry is to be removed from the map
     * @return previous value associated with key, 
     * or null if the dictionary does not an entry with that key
     */
    V remove( K key );

    /**
     * Returns an iterator of the entries in the dictionary.
     * @apiNote Time Complexity: O(1) to create the iterator
     * @return  iterator of the entries in the dictionary
     */
    Iterator<Entry<K,V>> iterator( );

    /**
     * Returns an iterator of the values in the dictionary.
     * @apiNote Time Complexity: O(1) to create the iterator
     * @return  iterator of the values in the dictionary
     */
    Iterator<V> values( );

    /**
     * Returns an iterator of the keys in the dictionary.
     * @apiNote Time Complexity: O(1) to create the iterator
     * @return  iterator of the keys in the dictionary
     */
    Iterator<K> keys( );

} 


