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
    public V put(K key, V value) {
        if (isEmpty()) {
            root = createNode(new Entry<>(key, value), null);
            currentSize++;
            return null;
        }

        BTNode<Entry<K,V>> node = findNode(key);
        int cmp = key.compareTo(node.getElement().key());

        if (cmp == 0) {
            V oldValue = node.getElement().value();
            node.setElement(new Entry<>(key, value));
            return oldValue;
        }
        AVLNode<Entry<K,V>> newNode = createNode(new Entry<>(key, value), node);
        if (cmp < 0) {
            node.setLeftChild(newNode);
        } else {
            node.setRightChild(newNode);
        }
        currentSize++;

        organizeTree((AVLNode<Entry<K,V>>) newNode);

        return null;
        //TODO: Left as an exercise.// done ish
        // If exists a entry with this Key, update the node with new element
        // and return the old value of the entry
        // otherwise, insert the newNode, "rebalance" from the insertion position
        // and return value
        
    }

    /**
     *
     * @param key whose entry is to be removed from the map
     * @return
     */
    public V remove(K key) {
        BTNode<Entry<K,V>> nodeToRemove = findNode(key);
        if (nodeToRemove == null || nodeToRemove.getElement().key().compareTo(key) != 0) {
            return null;
        }
        V returnValue = nodeToRemove.getElement().value();
        AVLNode<Entry<K,V>> restructureStart = removeNode(nodeToRemove);
        currentSize--;

        if (restructureStart != null) {
            organizeTree(restructureStart);
        }
        return returnValue;


        //TODO: Left as an exercise.
        // If does not exist a entry with this Key, return null
        // otherwise, remove the node where is the element with this key,
        // "rebalance" from the removal position and return value
    }


    private AVLNode<Entry<K,V>> removeNode(BTNode<Entry<K,V>> nodeToRemove) {
        //AVLNode<Entry<K,V>> nodeToRemoveAVL = (AVLNode<Entry<K,V>>) nodeToRemove;

        if (nodeToRemove.isLeaf()){  //caso1
            AVLNode<Entry<K,V>> parent = (AVLNode<Entry<K,V>>) nodeToRemove.getParent();
            if(nodeToRemove.isRoot()){
                root = null;
            } else {
                if (parent.getLeftChild() == nodeToRemove) {
                    parent.setLeftChild(null);
                } else {
                    parent.setRightChild(null);
                }
            }
            return parent;
        } // caso2
        else  if ( nodeToRemove.getLeftChild() == null || nodeToRemove.getRightChild() == null) {

            BTNode<Entry<K,V>> child = (nodeToRemove.getLeftChild() != null) ?
                    (BTNode<Entry<K,V>>) nodeToRemove.getLeftChild() :
                    (BTNode<Entry<K,V>>) nodeToRemove.getRightChild();

            AVLNode<Entry<K,V>> parent = (AVLNode<Entry<K,V>>) nodeToRemove.getParent();

            if (nodeToRemove.isRoot()){
                root = child;
                ((BTNode<Entry<K,V>>) child).setParent(null);
            } else {
                ((BTNode<Entry<K,V>>) child).setParent(parent);
                if (parent.getLeftChild() == nodeToRemove) {
                    parent.setLeftChild(child);
                } else {
                    parent.setRightChild(child);
                }
            }
            return parent;
        } // caso 3
        else  {
            BTNode<Entry<K,V>> successor = ((BTNode<Entry<K,V>>) nodeToRemove.getRightChild()).furtherLeftElement();
            nodeToRemove.setElement(successor.getElement());
            return removeNode(successor);
        }

    }

    private void organizeTree(AVLNode<Entry<K,V>> node) {
        AVLNode<Entry<K,V>> current = node;
        while(current!=null){
            current.updateHeight();
            if(!current.isBalanced()){
                AVLNode<Entry<K,V>> y = getTallerChild(current);
                if (y != null) {
                    AVLNode<Entry<K,V>> x = getTallerChild(y);
                    if (x != null) {
                        current = (AVLNode<Entry<K,V>>) restructure(x);

                        if (current != null) {
                            current.updateHeight();
                        }
                    }
                }
            }
            if (current != null) {
                current = (AVLNode<Entry<K,V>>) current.getParent();
            }

        }
    }

    private AVLNode<Entry<K,V>> getTallerChild(AVLNode<Entry<K,V>> node) {
        AVLNode<Entry<K,V>> left = (AVLNode<Entry<K,V>>) node.getLeftChild();
        AVLNode<Entry<K,V>> right = (AVLNode<Entry<K,V>>) node.getRightChild();
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
        if (((BTNode<Entry<K,V>>)node.getParent()).getLeftChild()==node) {
            return left;
        } else {
            return right;
        }
    }

    private AVLNode<Entry<K,V>> createNode(Entry<K,V> entry, BTNode<Entry<K,V>> parent) {
        return new AVLNode<>(entry, (AVLNode<Entry<K,V>>) parent);
    }

    private BTNode<Entry<K,V>> findNode(K key) {
        BTNode<Entry<K,V>> current = (BTNode<Entry<K,V>>) root;
        BTNode<Entry<K,V>> parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = (BTNode<Entry<K,V>>) current.getLeftChild();
            } else {
                current = (BTNode<Entry<K,V>>) current.getRightChild();
            }
        }
        return parent;
    }
}
