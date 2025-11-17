package dataStructures;
/**
 * Binary Tree
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
abstract class BTree<E> extends Tree<E> {

    /**
     * Returns the height of the tree.
     */
    public int getHeight() {
        if(isEmpty())
            return 0;
        return ((BTNode<E>)root).getHeight();
    }

    /**
     * Return the further left node of the tree
     * @return
     */
    BTNode<E> furtherLeftElement() {
        if (isEmpty()){
            return null;
        }
        return ((BTNode<E>)root).furtherLeftElement();
        //TODO: Left as an exercise.//done

    }

    /**
     * Return the further right node of the tree
     * @return
     */
    BTNode<E> furtherRightElement() {
        if (isEmpty()){
            return null;
        }
        return ((BTNode<E>)root).furtherRightElement();
        //TODO: Left as an exercise.//done

    }

    //new methods: Left as an exercise.
}
