package dataStructures;

import dataStructures.exceptions.*;

import java.io.*;
import java.util.Objects;


/**
 * Sorted Doubly linked list Implementation
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 * 
 */
public class SortedDoublyLinkedList<E> implements SortedList<E> {

    /**
     *  Node at the head of the list.
     */
    private transient DoublyListNode<E> head;
    /**
     * Node at the tail of the list.
     */
    private transient DoublyListNode<E> tail;
    /**
     * Number of elements in the list.
     */
    private transient int currentSize;
    /**
     * Comparator of elements.
     */
    private final Comparator<E> comparator;


    /**
     * Constructor of an empty sorted double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public SortedDoublyLinkedList(Comparator<E> comparator) {
        this.head = null;
        this.tail = null;
        this.currentSize = 0;
        //TODO: Left as an exercise.//done
        this.comparator = comparator;
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
        return new DoublyIterator<>(head);
    }

    /**
     * Returns the first element of the list.
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getMin( ) {
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        return head.getElement();
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the last element of the list.
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getMax( ) {
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        return tail.getElement();
        //TODO: Left as an exercise.//done
         }
    /**
     * Returns the first occurrence of the element equals to the given element in the list.
     * @return element in the list or null
     */
    public E get(E element) {
        DoublyListNode<E> current = head;
        while (current != null) {
            if (comparator.compare(current.getElement(), element) == 0) {
                return current.getElement();
            }
            current = current.getNext();
        }
        //TODO: Left as an exercise.//done
        return null;
    }

    /**
     * Returns true iff the element exists in the list.
     *
     * @param element to be found
     * @return true iff the element exists in the list.
     */
    public boolean contains(E element) {
        return get(element)!=null;
        //TODO: Left as an exercise.//done
    }

    /**
     * Inserts the specified element at the list, according to the natural order.
     * If there is an equal element, the new element is inserted after it.
     * @param element to be inserted
     */
    public void add(E element) {
        DoublyListNode<E> newNode = new DoublyListNode<>(element);

        if (isEmpty()) {
            head = tail = newNode;
            currentSize++;
            return;
        }

        // Encontra o primeiro nó com elemento > novo elemento.
        DoublyListNode<E> current = head;
        while (current != null && comparator.compare(current.getElement(), element) <= 0) {
            current = current.getNext();
        }

        if (current == head) {
            // Inserção no início (novo elemento é menor que o head atual)
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        } else if (current == null) {
            // Inserção no final (novo elemento é >= tail; em caso de igualdade, vai após o último igual)
            newNode.setPrevious(tail);
            tail.setNext(newNode);
            tail = newNode;
        } else {
            // Inserção antes de 'current' (após o último igual encontrado)
            DoublyListNode<E> prev = current.getPrevious();
            newNode.setPrevious(prev);
            newNode.setNext(current);
            prev.setNext(newNode);
            current.setPrevious(newNode);
        }

        currentSize++;
    }


    /**
     * Removes and returns the first occurrence of the element equals to the given element in the list.
     * @return element removed from the list or null if !belongs(element)
     */
    // java
    public E remove(E element) {
        DoublyListNode<E> current = head;
        while (current != null) {
            E currentElement = current.getElement();
            boolean equal = Objects.equals(element, currentElement);
            if (equal) {
                if (current == head) {
                    head = head.getNext();
                    if (head != null) head.setPrevious(null);
                    else tail = null;
                } else if (current == tail) {
                    tail = tail.getPrevious();
                    if (tail != null) tail.setNext(null);
                    else head = null;
                } else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                }
                currentSize--;
                return currentElement;
            }
            current = current.getNext();
        }
        return null;
    }



}
