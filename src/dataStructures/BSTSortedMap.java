package dataStructures;

import dataStructures.exceptions.EmptyMapException;
/**
 * Binary Search Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class BSTSortedMap<K extends Comparable<K>,V> extends BTree<Map.Entry<K,V>> implements SortedMap<K,V>{

    /**
     * Constructor
     */
    public BSTSortedMap(){
        super();
    }
    /**
     * Returns the entry with the smallest key in the dictionary.
     *
     * @return
     * @throws EmptyMapException
     */
    @Override
    public Entry<K, V> minEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherLeftElement().getElement();
    }

    /**
     * Returns the entry with the largest key in the dictionary.
     *
     * @return
     * @throws EmptyMapException
     */
    @Override
    public Entry<K, V> maxEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherRightElement().getElement();
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
        Node<Entry<K,V>> node=getNode(key);
        if (node!=null)
            return node.getElement().value();
        return null;
    }

    private BTNode<Entry<K,V>> getNode(K key) {
        BTNode<Entry<K,V>> current = (BTNode<Entry<K,V>>) root;

        while (current != null) {
            int cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = (BTNode<Entry<K,V>>) current.getLeftChild();
            } else {
                current = (BTNode<Entry<K,V>>) current.getRightChild();
            }
        }
        return null;
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
    @Override
    public V put(K key, V value) {

        Entry<K,V> newEntry=new Entry<>(key,value);
        if (isEmpty()) {
            root = new BTNode<>(newEntry);
            currentSize++;
            return null;
        }
        BTNode<Entry<K,V>> current=(BTNode<Entry<K,V>>) root;
        BTNode<Entry<K,V>> parent=null;
        int cmp = 0;
        while(current!=null){
            cmp = key.compareTo(current.getElement().key());
            parent=current;
            if(cmp==0){
                V oldValue=current.getElement().value();
                current.setElement(newEntry);
                return oldValue;
            } else if(cmp<0){
                current=(BTNode<Entry<K,V>>) current.getLeftChild();
            } else{
                current=(BTNode<Entry<K,V>>) current.getRightChild();
            }
        }
        BTNode<Entry<K,V>> newNode=new BTNode<>(newEntry,parent);
        if(cmp<0){
            parent.setLeftChild(newNode);
        } else{
            parent.setRightChild(newNode);
        }
        currentSize++;
        return null;

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
    @Override
    public V remove(K key) {
        BTNode<Entry<K,V>> nodeToRemove = getNode(key);
        if (nodeToRemove!=null) {
            V returnValue = nodeToRemove.getElement().value();
            removeNode(nodeToRemove);
            currentSize--;
            return returnValue;
        }
        //TODO: Left as an exercise.

        return null;
    }

    private void removeNode(BTNode<Entry<K,V>> nodeToRemove) {
        if (nodeToRemove.isLeaf()){  //caso1
            if(nodeToRemove.isRoot()){
                root = null;
            } else {
                BTNode<Entry<K,V>> parent = (BTNode<Entry<K,V>>) nodeToRemove.getParent();
                if (parent.getLeftChild() == nodeToRemove) {
                    parent.setLeftChild(null);
                } else {
                    parent.setRightChild(null);
                }
            }
        } // caso2
        else  if ( nodeToRemove.getLeftChild() == null || nodeToRemove.getRightChild() == null) {
            BTNode<Entry<K,V>> child = (nodeToRemove.getLeftChild() != null) ?
                    (BTNode<Entry<K,V>>) nodeToRemove.getLeftChild() :
                    (BTNode<Entry<K,V>>) nodeToRemove.getRightChild();

            if (nodeToRemove.isRoot()){
                root = child;
                child.setParent(null);
            } else {
                BTNode<Entry<K,V>> parent = (BTNode<Entry<K,V>>) nodeToRemove.getParent();
                child.setParent(parent);
                if (parent.getLeftChild() == nodeToRemove) {
                    parent.setLeftChild(child);
                } else {
                    parent.setRightChild(child);
                }
            }
        } // caso 3
        else  {
            BTNode<Entry<K,V>> successor = ((BTNode<Entry<K,V>>) nodeToRemove.getRightChild()).furtherLeftElement();
            nodeToRemove.setElement(successor.getElement());
            removeNode(successor);
        }

    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new InOrderIterator<>((BTNode<Entry<K,V>>) root);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     *
     * @return iterator of the values in the dictionary
     */
    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     *
     * @return iterator of the keys in the dictionary
     */
    @Override
    @SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }
}
