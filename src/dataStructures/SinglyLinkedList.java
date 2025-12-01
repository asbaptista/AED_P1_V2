package dataStructures;

import dataStructures.exceptions.*;
import java.io.*;

public class SinglyLinkedList<E> implements List<E>{

    /**
     *  Node at the head of the list.
     */
    private transient SinglyListNode<E> head;
    /**
     * Node at the tail of the list.
     */
    private transient SinglyListNode<E> tail;
    /**
     * Number of elements in the list.
     */
    private transient int currentSize;
    /**
     * Constructor of an empty singly linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public SinglyLinkedList( ) {
        head = null;
        tail = null;
        currentSize = 0;
    }

    /**
     * Returns true iff the list contains no elements.
     * @return true if list is empty
     */
    public boolean isEmpty() {
        return currentSize==0;
    }

    /**
     * Returns the number of elements in the list.
     * @return number of elements in the list
     */

    public int size() {
        return currentSize;
    }

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     * @return Iterator of the elements in the list
     */
    public Iterator<E> iterator() {
        return new SinglyIterator<>(head);
    }

    /**
     * Returns the first element of the list.
     *
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    @Override
    public E getFirst() {
        if (this.isEmpty())
            throw new NoSuchElementException();
        return head.getElement();
    }

    /**
     * Returns the last element of the list.
     *
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    @Override
    public E getLast() {
        if (this.isEmpty())
            throw new NoSuchElementException();
        return tail.getElement();
    }

    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, get corresponds to getFirst.
     * If the specified position is size()-1, get corresponds to getLast.
     *
     * @param position - position of element to be returned
     * @return element at position
     * @throws InvalidPositionException if position is not valid in the list
     */
    @Override
    public E get(int position) {
        if (position < 0 || position >= currentSize)
            throw new InvalidPositionException();

        SinglyListNode<E> current = head;
        for (int i = 0; i < position; i++)
            current = current.getNext();

        return current.getElement();
    }


    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     *
     * @param element - element to be searched in list
     * @return position of the first occurrence of the element in the list (or -1)
     */
    @Override
    public int indexOf(E element) {
        SinglyListNode<E> current = head;
        int position = 0;

        while (current != null) {
            if (current.getElement().equals(element))
                return position;
            current = current.getNext();
            position++;
        }

        return -1;
    }

    /**
     * Inserts the specified element at the first position in the list.
     *
     * @param element to be inserted
     */
    @Override
    public void addFirst(E element) {
        SinglyListNode<E> newNode = new SinglyListNode<>(element, head);
        head = newNode;
        if (tail == null)
            tail = head;
        currentSize++;
    }

    /**
     * Inserts the specified element at the last position in the list.
     *
     * @param element to be inserted
     */
    @Override
    public void addLast(E element) {
        SinglyListNode<E> newNode = new SinglyListNode<>(element, null);
        if (this.isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        currentSize++;
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Range of valid positions: 0, ..., size().
     * If the specified position is 0, add corresponds to addFirst.
     * If the specified position is size(), add corresponds to addLast.
     *
     * @param position - position where to insert element
     * @param element  - element to be inserted
     * @throws InvalidPositionException - if position is not valid in the list
     */
    @Override
    public void add(int position, E element) {
        if ( position < 0 || position > currentSize )
            throw new InvalidPositionException();

        if (position == 0)
            addFirst(element);
        else if (position == currentSize)
            addLast(element);
        else {
            SinglyListNode<E> previous = head;
            for (int i = 0; i < position - 1; i++)
                previous = previous.getNext();

            SinglyListNode<E> newNode = new SinglyListNode<>(element, previous.getNext());
            previous.setNext(newNode);
            currentSize++;
        }
    }


    /**
     * Removes and returns the element at the first position in the list.
     *
     * @return element removed from the first position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    @Override
    public E removeFirst() {
        if ( this.isEmpty() )
            throw new NoSuchElementException();

        E element = head.getElement();
        head = head.getNext();
        currentSize--;

        if (this.isEmpty())
            tail = null;

        return element;
    }

    /**
     * Removes and returns the element at the last position in the list.
     *
     * @return element removed from the last position of the list
     * @throws NoSuchElementException - if size() == 0
     */

    public E removeLast() {
        if ( this.isEmpty() )
            throw new NoSuchElementException();

        E element = tail.getElement();

        if (currentSize == 1) {
            head = null;
            tail = null;
        } else {
            SinglyListNode<E> previous = head;
            while (previous.getNext() != tail)
                previous = previous.getNext();

            previous.setNext(null);
            tail = previous;
        }

        currentSize--;
        return element;
    }

    /**
     * Removes and returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, remove corresponds to removeFirst.
     * If the specified position is size()-1, remove corresponds to removeLast.
     *
     * @param position - position of element to be removed
     * @return element removed at position
     * @throws InvalidPositionException - if position is not valid in the list
     */
    @Override
    public E remove(int position) {
        if ( position < 0 || position >= currentSize )
            throw new InvalidPositionException();

        if (position == 0)
            return removeFirst();
        else if (position == currentSize - 1)
            return removeLast();
        else {
            SinglyListNode<E> previous = head;
            for (int i = 0; i < position - 1; i++)
                previous = previous.getNext();

            E element = previous.getNext().getElement();
            previous.setNext(previous.getNext().getNext());
            currentSize--;
            return element;
        }
    }

}
