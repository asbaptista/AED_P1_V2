package dataStructures;
import dataStructures.exceptions.*;
/**
 * Stack Abstract Data Type
 * Includes description of general methods for the Stack with the LIFO discipline.
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public interface Stack<E> {

    /**
     *  Returns true iff the stack contains no
     *  elements.
     * @apiNote Time Complexity: O(1)
     * @return true iff the stack contains no
     *         elements, false otherwise
     */
    boolean isEmpty( );

    /**
     *  Returns the number of elements in the stack.
     * @apiNote Time Complexity: O(1)
     * @return number of elements in the stack
     */
    int size( );

    /**
     *  Returns the element at the top of the stack.
     *  Requires
     * @apiNote Time Complexity: O(1)
     * @return element at top of stack
     * @throws EmptyStackException when size = 0
     */
    E top( );

    /**
     *  Inserts the specified <code>element</code> onto
     *  the top of the stack.
     * @apiNote Time Complexity: O(1) (amortized for array-based implementations)
     * @param element element to be inserted onto the stack
     */
    void push( E element );

    /**
     *  Removes and returns the element at the top of the
     *  stack.
     * @apiNote Time Complexity: O(1)
     * @return element removed from top of stack
     * @throws EmptyStackException when size = 0
     */
    E pop( );
}
