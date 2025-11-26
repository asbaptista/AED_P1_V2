package dataStructures;

import java.io.Serializable;
/**
 * Node Interface
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic element
 */

interface Node<E> {
    /**
     *  Returns the element of the node
     */
    E getElement();

    /**
     * Update the element
     * @param elem
     */
    void setElement(E elem);
}
