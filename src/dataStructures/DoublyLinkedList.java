package dataStructures;

import dataStructures.exceptions.*;
import java.io.*;

/**
 * Doubly Linked List implementation.
 * Implements the {@link TwoWayList} interface, providing functionality
 * for a list accessible from both ends and supporting two-way iteration.
 * All fields are transient, and custom serialization is provided.
 *
 * @author AED team
 * @version 1.0
 * @param <E> Generic Element
 */
public class DoublyLinkedList<E> implements TwoWayList<E>, Serializable {

    private static final long serialVersionUID = 1L;
    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */

    /**
     * Node at the head (start) of the list.
     * Transient to support custom serialization.
     */
    protected transient DoublyListNode<E> head;

    /**
     * Node at the tail (end) of the list.
     * Transient to support custom serialization.
     */
    protected transient DoublyListNode<E> tail;

    /**
     * Number of elements currently in the list.
     * Transient to support custom serialization.
     */
    protected transient int currentSize;

    // --- Constructor ---

    /**
     * Constructor for an empty double linked list.
     * Head and tail are initialized as null.
     * CurrentSize is initialized as 0.
     *
     * @apiNote Time Complexity: O(1)
     */
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.currentSize = 0;
        //TODO: Left as an exercise.//done
    }

    // --- Status Checkers ---

    /**
     * Returns true iff the list contains no elements.
     *
     * @apiNote Time Complexity: O(1)
     * @return {@code true} if list is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return currentSize == 0;
        //TODO: Left as an exercise.// done
    }

    /**
     * Returns the number of elements in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return Number of elements in the list.
     */
    public int size() {
        return currentSize;
        //TODO: Left as an exercise.//done
    }

    // --- Iterators ---

    /**
     * Returns a two-way iterator of the elements in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return A {@link TwoWayIterator} of the elements in the list.
     */
    public TwoWayIterator<E> twoWayiterator() {
        return new TwoWayDoublyIterator<>(head, tail);
    }

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     *
     * @apiNote Time Complexity: O(1)
     * @return An {@link Iterator} of the elements in the list.
     */
    public Iterator<E> iterator() {
        return new DoublyIterator<>(head);
    }

    // --- Add Operations ---

    /**
     * Inserts the element at the first position in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @param element - Element to be inserted.
     */
    public void addFirst(E element) {
        DoublyListNode<E> newNode = new DoublyListNode<>(element);
        if (isEmpty()) {
            tail = newNode;
            head = newNode;
        } else {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }
        currentSize++;
        //TODO: Left as an exercise. //done
    }

    /**
     * Inserts the element at the last position in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @param element - Element to be inserted.
     */
    public void addLast(E element) {
        DoublyListNode<E> newNode = new DoublyListNode<>(element);
        if (isEmpty()) {
            tail = newNode;
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }
        currentSize++;
        //TODO: Left as an exercise.//done
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Range of valid positions: 0, ..., size().
     * If the specified position is 0, add corresponds to addFirst.
     * If the specified position is size(), add corresponds to addLast.
     *
     * @apiNote Time Complexity: O(N) (Worst case O(N) to find position; Best case O(1) for pos=0 or pos=N).
     * @param position - position where to insert element.
     * @param element  - element to be inserted.
     * @throws InvalidPositionException - if position is not valid in the list.
     */
    public void add(int position, E element) {
        if (position < 0 || position > currentSize) {
            throw new InvalidPositionException();
        }
        if (position == 0) {
            addFirst(element);
        } else if (position == currentSize) {
            addLast(element);
        } else {
            DoublyListNode<E> current = head;
            for (int i = 0; i < position; i++) {
                current = current.getNext();
            }
            DoublyListNode<E> newNode = new DoublyListNode<>(element);
            newNode.setPrevious(current.getPrevious());
            newNode.setNext(current);
            current.getPrevious().setNext(newNode);
            current.setPrevious(newNode);
            currentSize++;
        }
        //TODO: Left as an exercise.//done
    }

    // --- Get Operations ---

    /**
     * Returns the first element of the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return First element in the list.
     * @throws NoSuchElementException - if size() == 0.
     */
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return head.getElement();
        }
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the last element of the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return Last element in the list.
     * @throws NoSuchElementException - if size() == 0.
     */
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return tail.getElement();
        }
        //TODO: Left as an exercise.
    }

    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     *
     * @apiNote Time Complexity: O(N) (Worst case O(N) to find position; Best case O(1) for pos=0).
     * @param position - position of element to be returned.
     * @return Element at position.
     * @throws InvalidPositionException if position is not valid in the list.
     */
    public E get(int position) {
        if (position < 0 || position >= currentSize) {
            throw new InvalidPositionException();
        }
        DoublyListNode<E> current = head;
        for (int i = 0; i < position; i++) {
            current = current.getNext();
        }
        return current.getElement();
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     *
     * @apiNote Time Complexity: O(N) (Worst case O(N) to find element; Best case O(1) if at head).
     * @param element - element to be searched in list.
     * @return Position of the first occurrence of the element in the list (or -1).
     */
    public int indexOf(E element) {
        DoublyListNode<E> current = head;
        int index = 0;
        while (current != null) {
            if (current.getElement().equals(element)) {
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
        //TODO: Left as an exercise.//done
    }

    // --- Remove Operations ---

    /**
     * Removes and returns the element at the first position in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return Element removed from the first position of the list.
     * @throws NoSuchElementException - if size() == 0.
     */
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E element = head.getElement();
        if (currentSize == 1) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrevious(null);
        }
        currentSize--;
        return element;
        //TODO: Left as an exercise.//done
    }

    /**
     * Removes and returns the element at the last position in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return Element removed from the last position of the list.
     * @throws NoSuchElementException - if size() == 0.
     */
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E element = tail.getElement();
        if (currentSize == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevious();
            tail.setNext(null);
        }
        currentSize--;
        return element;
        //TODO: Left as an exercise.//done
    }

    /**
     * Removes and returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     *
     * @apiNote Time Complexity: O(N) (Worst case O(N) to find position; Best case O(1) for pos=0 or pos=N-1).
     * @param position - position of element to be removed.
     * @return Element removed at position.
     * @throws InvalidPositionException - if position is not valid in the list.
     */
    public E remove(int position) {
        if (position < 0 || position >= currentSize) {
            throw new InvalidPositionException();
        }
        if (position == 0) {
            return removeFirst();
        } else if (position == currentSize - 1) {
            return removeLast();
        } else {
            DoublyListNode<E> current = head;
            for (int i = 0; i < position; i++) {
                current = current.getNext();
            }
            E element = current.getElement();
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
            currentSize--;
            return element;
            //TODO: Left as an exercise.
        }
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(currentSize);
        DoublyListNode<E> current = head;
        while (current != null) {
            oos.writeObject(current.getElement());
            current = current.getNext();
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        int size = ois.readInt();
        this.head = null;
        this.tail = null;
        this.currentSize = 0;
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) ois.readObject();
            this.addLast(element);
        }
    }

}