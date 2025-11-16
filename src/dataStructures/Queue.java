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
     * @return
     */
    boolean isEmpty( );

    /**
     * Returns the number of elements in the queue.
     * @return
     */
    int size( );

    /**
     * Inserts the specified element at the rear of the queue.
     * @param element
     */
    void enqueue( E element );

    /**
     * Returns the element at the front of the queue.
     * @return
     * @throws EmptyQueueException
     */
    E peek();
    /**
     * Removes and returns the element at the front of the queue.
     * @return
     * @throws EmptyQueueException
     */
    E dequeue( );
}
