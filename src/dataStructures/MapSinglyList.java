package dataStructures;

import java.io.*;

/**
 * Map with a singly linked list with head and size
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
class MapSinglyList<K,V> implements Map<K, V> , Serializable {

    private static final long serialVersionUID = 1L;


    private transient SinglyListNode<Entry<K,V>> head;

    private transient int size;

    public MapSinglyList() {
        head=null;
        size=0;
        //TODO: Left as exercise//done
    }

    /**
     * Returns true iff the dictionary contains no entries.
     *
     * @return true if dictionary is empty
     */
  
    public boolean isEmpty() {
        return size==0;
	//TODO: Left as exercise//done
    }

    /**
     * Returns the number of entries in the dictionary.
     *
     * @return number of elements in the dictionary
     */
    @Override
    public int size() {
	//TODO: Left as exercise//done
        return size;
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
        SinglyListNode<Entry<K,V>> temp = findNode(key);
        if (temp!=null) {
            return temp.getElement().value();
        }
        //TODO: Left as exercise//done
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

        //TODO: Left as an exercise.//done
        return null;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SinglyIterator<>(head);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     *
     * @return iterator of the values in the dictionary
     */
@SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     *
     * @return iterator of the keys in the dictionary
     */
@SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }


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

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Grava a 'list' automaticamente
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Restaura a 'list'
    }

}
