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
     * @param elem
     * @return
     */
    boolean check(E elem);
}
