package dataStructures;

/**
 * Iterator Abstract Data Type with Filter
 * Includes description of general methods for one way iterator.
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public interface Predicate<E> {
    /**
     *  Filter that an element needs to check
     * @apiNote Time Complexity: Implementation-dependent
     * @param elem - element to check
     * @return true if element satisfies the predicate
     */
    boolean check(E elem);
}
