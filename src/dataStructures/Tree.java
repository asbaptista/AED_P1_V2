package dataStructures;
/**
 * Tree
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic element
 */
import java.io.Serializable;

abstract class Tree<E> implements Serializable {

    /**
     * Root
     */
    protected Node<E> root;

    /**
     * Number of elements
     */
    protected int currentSize;

    public Tree(){
        root=null;
        currentSize=0;
    }

    /**
     * Returns true iff the dictionary contains no entries.
     *
     * @return true if dictionary is empty
     */
    public boolean isEmpty() {
        return currentSize==0;
    }

    /**
     * Returns the number of entries in the dictionary.
     *
     * @return number of elements in the dictionary
     */
    public int size() {
        return currentSize;
    }


    /**
     * Return the root of the tree
     * @return
     */
    Node<E> root(){ return root;}

}