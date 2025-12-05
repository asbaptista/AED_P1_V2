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
     * @apiNote Time Complexity: O(1)
     * @return element stored in the node
     */
    E getElement();

    /**
     * Update the element
     * @apiNote Time Complexity: O(1)
     * @param elem - new element
     */
    void setElement(E elem);
}
