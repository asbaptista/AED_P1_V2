package dataStructures;

import java.io.Serializable;

/**
 * Comparator interface.
 * Defines a method for comparing two objects of the same type.
 * Implementing classes can be used to define custom sorting logic.
 *
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
public interface Comparator<E>  {

    /**
     * Compares its two arguments for order.
     * Returns a negative integer, zero, or a positive integer as the first argument
     * is less than, equal to, or greater than the second.
     * <p>
     * Must ensure that:
     * <ul>
     * <li>signum(compare(x, y)) == -signum(compare(y, x)) for all x and y.</li>
     * <li>relation is transitive: ((compare(x, y)>0) && (compare(y, z)>0)) implies compare(x, z)>0.</li>
     * <li>compare(x, y)==0 implies that signum(compare(x, z))==signum(compare(y, z)) for all z.</li>
     * </ul>
     *
     * @apiNote Time Complexity: Implementation-dependent.
     * Typically O(1) for simple numeric comparisons or O(L) for
     * comparisons of objects with L elements (e.g., Strings of length L).
     *
     * @param x The first object to be compared.
     * @param y The second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the second.
     */
    int compare(E x, E y);
}