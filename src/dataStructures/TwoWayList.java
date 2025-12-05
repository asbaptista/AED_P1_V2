package dataStructures;
/**
 * Two-Way List
 *
 * @author AED team
 * @version 1.0
 *
 * @param <E> Generic Element
 */
public interface TwoWayList<E> extends List<E> {
    /**
     * Returns a two-way iterator of the elements in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return Two-Way Iterator of the elements in the list
     */
    TwoWayIterator<E> twoWayiterator();
}