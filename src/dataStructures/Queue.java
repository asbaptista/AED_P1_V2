package dataStructures;
import dataStructures.exceptions.*;
/**
 * Queue Abstract Data Type
 * Includes description of general methods for the Queue with the FIFO discipline.
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public interface Queue <E>{
    /**
     * Returns true iff the queue contains no elements.
     * @apiNote Time Complexity: O(1)
     * @return true iff queue is empty
     */
    boolean isEmpty( );

    /**
     * Returns the number of elements in the queue.
     * @apiNote Time Complexity: O(1)
     * @return number of elements in the queue
     */
    int size( );

    /**
     * Inserts the specified element at the rear of the queue.
     * @apiNote Time Complexity: O(1)
     * @param element - element to be inserted
     */
    void enqueue( E element );

    /**
     * Returns the element at the front of the queue.
     * @apiNote Time Complexity: O(1)
     * @return element at front of queue
     * @throws EmptyQueueException when size = 0
     */
    E peek();
    /**
     * Removes and returns the element at the front of the queue.
     * @apiNote Time Complexity: O(1)
     * @return element removed from front of queue
     * @throws EmptyQueueException when size = 0
     */
    E dequeue( );
}
