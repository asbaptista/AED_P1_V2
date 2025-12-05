package dataStructures;

import java.io.*;

/**
 * AVL Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class AVLSortedMap <K extends Comparable<K>,V> extends AdvancedBSTree<K,V> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {
        if (isEmpty()) {
            root = createNode(new Entry<>(key, value), null);
            currentSize++;
            return null;
        }

        BTNode<Entry<K, V>> node = findNode(key);
        int cmp = key.compareTo(node.getElement().key());

        if (cmp == 0) {
            V oldValue = node.getElement().value();
            node.setElement(new Entry<>(key, value));
            return oldValue;
        }
        AVLNode<Entry<K, V>> newNode = createNode(new Entry<>(key, value), node);
        if (cmp < 0) {
            node.setLeftChild(newNode);
        } else {
            node.setRightChild(newNode);
        }
        currentSize++;

        organizeTree(newNode);

        return null;

    }

    /**
     * @param key whose entry is to be removed from the map
     * @return
     */
    public V remove(K key) {
        BTNode<Entry<K, V>> nodeToRemove = findNode(key);
        if (nodeToRemove == null || nodeToRemove.getElement().key().compareTo(key) != 0) {
            return null;
        }
        V returnValue = nodeToRemove.getElement().value();
        AVLNode<Entry<K, V>> restructureStart = removeNode(nodeToRemove);
        currentSize--;

        if (restructureStart != null) {
            organizeTree(restructureStart);
        }
        return returnValue;
    }


    private AVLNode<Entry<K, V>> removeNode(BTNode<Entry<K, V>> nodeToRemove) {

        if (nodeToRemove.isLeaf()) {  //caso1
            AVLNode<Entry<K, V>> parent = (AVLNode<Entry<K, V>>) nodeToRemove.getParent();
            if (nodeToRemove.isRoot()) {
                root = null;
            } else {
                if (parent.getLeftChild() == nodeToRemove) {
                    parent.setLeftChild(null);
                } else {
                    parent.setRightChild(null);
                }
            }
            return parent;
        }
        else if (nodeToRemove.getLeftChild() == null || nodeToRemove.getRightChild() == null) {

            BTNode<Entry<K, V>> child = (nodeToRemove.getLeftChild() != null) ?
                    (BTNode<Entry<K, V>>) nodeToRemove.getLeftChild() :
                    (BTNode<Entry<K, V>>) nodeToRemove.getRightChild();

            AVLNode<Entry<K, V>> parent = (AVLNode<Entry<K, V>>) nodeToRemove.getParent();

            if (nodeToRemove.isRoot()) {
                root = child;
                ( child).setParent(null);
            } else {
                ( child).setParent(parent);
                if (parent.getLeftChild() == nodeToRemove) {
                    parent.setLeftChild(child);
                } else {
                    parent.setRightChild(child);
                }
            }
            return parent;
        }
        else {
            BTNode<Entry<K, V>> successor = ((BTNode<Entry<K, V>>) nodeToRemove.getRightChild()).furtherLeftElement();
            nodeToRemove.setElement(successor.getElement());
            return removeNode(successor);
        }

    }

    private void organizeTree(AVLNode<Entry<K, V>> node) {
        AVLNode<Entry<K, V>> current = node;
        while (current != null) {
            current.updateHeight();
            if (!current.isBalanced()) {
                AVLNode<Entry<K, V>> y = getTallerChild(current);
                if (y != null) {
                    AVLNode<Entry<K, V>> x = getTallerChild(y);
                    if (x != null) {
                        current = (AVLNode<Entry<K, V>>) restructure(x);

                        if (current != null) {
                            current.updateHeight();
                        }
                    }
                }
            }
            if (current != null) {
                current = (AVLNode<Entry<K, V>>) current.getParent();
            }

        }
    }

    private AVLNode<Entry<K, V>> getTallerChild(AVLNode<Entry<K, V>> node) {
        AVLNode<Entry<K, V>> left = (AVLNode<Entry<K, V>>) node.getLeftChild();
        AVLNode<Entry<K, V>> right = (AVLNode<Entry<K, V>>) node.getRightChild();
        int leftH = (left == null) ? -1 : left.getHeight();
        int rightH = (right == null) ? -1 : right.getHeight();
        if (leftH > rightH) {
            return left;
        } else if (leftH < rightH) {
            return right;
        }

        if (node.getParent() == null) { // ainda nao sei se é preciso
            return left != null ? left : right;
        }
        if (((BTNode<Entry<K, V>>) node.getParent()).getLeftChild() == node) {
            return left;
        } else {
            return right;
        }
    }

    /**
     * Creates a new AVL node.
     * @apiNote Time Complexity: O(1)
     * @param entry - the entry to store in the node
     * @param parent - the parent node
     * @return the newly created AVL node
     */
    private AVLNode<Entry<K, V>> createNode(Entry<K, V> entry, BTNode<Entry<K, V>> parent) {
        return new AVLNode<>(entry, (AVLNode<Entry<K, V>>) parent);
    }

    /**
     * Finds the node with the given key or the parent where it should be inserted.
     * @apiNote Time Complexity: O(log N)
     * @param key - the key to search for
     * @return the node with the key or its would-be parent
     */
    private BTNode<Entry<K, V>> findNode(K key) {
        BTNode<Entry<K, V>> current = (BTNode<Entry<K, V>>) root;
        BTNode<Entry<K, V>> parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = (BTNode<Entry<K, V>>) current.getLeftChild();
            } else {
                current = (BTNode<Entry<K, V>>) current.getRightChild();
            }
        }
        return parent;
    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(currentSize);
        Iterator<Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            oos.writeObject(entry.key());
            oos.writeObject(entry.value());
        }
        oos.flush();
    }


    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        int size = ois.readInt();

        this.root = null;
        this.currentSize = 0;

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            K key = (K) ois.readObject();
            @SuppressWarnings("unchecked")
            V value = (V) ois.readObject();
            put(key, value);
        }
    }
}
