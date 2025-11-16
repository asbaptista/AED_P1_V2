package dataStructures;
/**
 * AVL Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class AVLSortedMap <K extends Comparable<K>,V> extends AdvancedBSTree<K,V>{
    /**
     * 
     * @param key
     * @param value
     * @return
     */
    public V insert(K key, V value) {

        //TODO: Left as an exercise.
        // If exists a entry with this Key, update the node with new element
        // and return the old value of the entry
        // otherwise, insert the newNode, "rebalance" from the insertion position
        // and return value
        
        return null;
    }

    /**
     *
     * @param key whose entry is to be removed from the map
     * @return
     */
    public V remove(K key) {
        //TODO: Left as an exercise.
        // If does not exist a entry with this Key, return null
        // otherwise, remove the node where is the element with this key,
        // "rebalance" from the removal position and return value
        return null;
    }


}
