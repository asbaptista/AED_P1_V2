package dataStructures;
/**
 * Tree
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic element
 */
import java.io.Serializable;

abstract class Tree<E>  {

    /**
     * Root
     */
    protected transient Node<E> root;

    /**
     * Number of elements
     */
    protected transient int currentSize;

    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     */
    public Tree(){
        root=null;
        currentSize=0;
    }

    /**
     * Returns true iff the dictionary contains no entries.
     *
     * @apiNote Time Complexity: O(1)
     * @return true if dictionary is empty
     */
    public boolean isEmpty() {
        return currentSize==0;
    }

    /**
     * Returns the number of entries in the dictionary.
     *
     * @apiNote Time Complexity: O(1)
     * @return number of elements in the dictionary
     */
    public int size() {
        return currentSize;
    }


    /**
     * Return the root of the tree
     * @apiNote Time Complexity: O(1)
     * @return root node
     */
    Node<E> root(){ return root;}

}