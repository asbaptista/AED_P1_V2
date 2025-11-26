package dataStructures;

import dataStructures.exceptions.*;

public class QueueInList<E> implements Queue<E> {

    // Memory of the queue: a list.
    private List<E> list;

    public QueueInList( ){
        list = new SinglyLinkedList<E>();
    }

    /**
     * Returns true iff the queue contains no elements.
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        //TODO: Left as an exercise.//done
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return
     */
    @Override
    public int size() {
        //TODO: Left as an exercise.//done
        return list.size();
    }

    /**
     * Inserts the specified element at the rear of the queue.
     *
     * @param element
     */
    @Override
    public void enqueue(E element) {
        list.addLast(element);

        //TODO: Left as an exercise.//done
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return
     * @throws EmptyQueueException
     */
    @Override
    public E dequeue() {
        if (list.isEmpty()) {
            throw new EmptyQueueException();
        }
        return list.removeFirst();
        //TODO: Left as an exercise.//done
    }
    /**
     * Returns the element at the front of the queue.
     *
     * @return
     * @throws EmptyQueueException
     */
    @Override
    public E peek() {
        if (list.isEmpty()) {
            throw new EmptyQueueException();
        }
        return list.getFirst();
        //TODO: Left as an exercise.//done
    }
}
