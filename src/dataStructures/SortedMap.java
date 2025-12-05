package dataStructures;

import dataStructures.exceptions.EmptyMapException;

/**
 * Ordered Dictionary interface
 *
 * @author AED team
 * @version 1.0
 * 
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public interface SortedMap<K extends Comparable<K>, V>
        extends Map<K,V> {

    /**
     * Returns the entry with the smallest key in the dictionary.
     *
     * @apiNote Time Complexity: O(log N) for balanced trees, O(N) worst-case for unbalanced trees
     * @return entry with smallest key
     * @throws EmptyMapException when the map is empty
     */
    Entry<K,V> minEntry( );

    /**
     * Returns the entry with the largest key in the dictionary.
     *
     * @apiNote Time Complexity: O(log N) for balanced trees, O(N) worst-case for unbalanced trees
     * @return entry with largest key
     * @throws EmptyMapException when the map is empty
     */
    Entry<K,V> maxEntry( );

} 

