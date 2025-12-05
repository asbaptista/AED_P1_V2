package dataStructures;

import java.io.*;

/**
 * Map with a singly linked list with head and size
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
class MapSinglyList<K,V> implements Map<K, V> {

    private transient SinglyListNode<Entry<K,V>> head;

    private transient int size;

    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     */
    public MapSinglyList() {
        head=null;
        size=0;
    }

    /**
     * Returns true iff the dictionary contains no entries.
     *
     * @apiNote Time Complexity: O(1)
     * @return true if dictionary is empty
     */
  
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * Returns the number of entries in the dictionary.
     *
     * @apiNote Time Complexity: O(1)
     * @return number of elements in the dictionary
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @apiNote Time Complexity: O(N) (linear search through the list)
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    @Override
    public V get(K key) {
        SinglyListNode<Entry<K,V>> temp = findNode(key);
        if (temp!=null) {
            return temp.getElement().value();
        }
        return null;
    }
    

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * replaces its value by the specified value and returns the old value;
     * otherwise, inserts the entry (key, value) and returns null.
     *
     * @apiNote Time Complexity: O(N) (linear search; insertion at head is O(1))
     * @param key   with which the specified value is to be associated
     * @param value to be associated with the specified key
     * @return previous value associated with key,
     * or null if the dictionary does not have an entry with that key
     */
    
    public V put(K key, V value) {
        SinglyListNode<Entry<K,V>> node = findNode(key);
        Entry<K,V> newValue = new Entry<>(key, value);
        if (node!=null) {
            V oldValue = node.getElement().value();
            node.setElement(newValue);
            return oldValue;
        }else {
            head = new SinglyListNode<>(newValue, head);
            size++;
            return null;
        }
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * removes it from the dictionary and returns its value;
     * otherwise, returns null.
     *
     * @apiNote Time Complexity: O(N) (linear search and removal)
     * @param key whose entry is to be removed from the map
     * @return previous value associated with key,
     * or null if the dictionary does not an entry with that key
     */
    public V remove(K key) {
        if (head==null) {
            return null;
        }
        if (head.getElement().key().equals(key)) {
            V valueToReturn = head.getElement().value();
            head = head.getNext();
            size--;
            return valueToReturn;
        }
        SinglyListNode<Entry<K,V>> current = head;
        while (current.getNext()!=null) {
            if (current.getNext().getElement().key().equals(key)) {
                V valueToReturn = current.getNext().getElement().value();
                current.setNext(current.getNext().getNext());
                size--;
                return valueToReturn;
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @apiNote Time Complexity: O(1)
     * @return iterator of the entries in the dictionary
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SinglyIterator<>(head);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     *
     * @apiNote Time Complexity: O(1)
     * @return iterator of the values in the dictionary
     */
@SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     *
     * @apiNote Time Complexity: O(1)
     * @return iterator of the keys in the dictionary
     */
@SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }

    /**
     * Helper method to find the node containing the given key.
     * @apiNote Time Complexity: O(N)
     * @param key - the key to search for
     * @return the node containing the key, or null if not found
     */
    private SinglyListNode<Entry<K,V>> findNode(K key) {
        SinglyListNode<Entry<K,V>> current = head;
        while (current != null) {
            if (current.getElement().key().equals(key)) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }

}
