package dataStructures;

import dataStructures.exceptions.*;

public class QueueInList<E> implements Queue<E> {

    // Memory of the queue: a list.
    private List<E> list;

    /**
     * Constructor
     * @apiNote Time Complexity: O(1)
     */
    public QueueInList( ){
        list = new SinglyLinkedList<E>();
    }

    /**
     * Returns true iff the queue contains no elements.
     *
     * @apiNote Time Complexity: O(1)
     * @return true iff queue is empty
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @apiNote Time Complexity: O(1)
     * @return number of elements in the queue
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * Inserts the specified element at the rear of the queue.
     *
     * @apiNote Time Complexity: O(1)
     * @param element - element to be inserted
     */
    @Override
    public void enqueue(E element) {
        list.addLast(element);
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @apiNote Time Complexity: O(1)
     * @return element removed from front of queue
     * @throws EmptyQueueException when size = 0
     */
    @Override
    public E dequeue() {
        if (list.isEmpty()) {
            throw new EmptyQueueException();
        }
        return list.removeFirst();
    }
    /**
     * Returns the element at the front of the queue.
     *
     * @apiNote Time Complexity: O(1)
     * @return element at front of queue
     * @throws EmptyQueueException when size = 0
     */
    @Override
    public E peek() {
        if (list.isEmpty()) {
            throw new EmptyQueueException();
        }
        return list.getFirst();
    }
}
