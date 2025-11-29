package dataStructures;

import dataStructures.exceptions.*;
import java.io.*;

public class SinglyLinkedList<E> implements List<E> , Serializable{

    private static final long serialVersionUID = 1L;
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
        //TODO: Left as an exercise.
        return null;
    }

    /**
     * Returns the last element of the list.
     *
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    @Override
    public E getLast() {
        //TODO: Left as an exercise.
        return null;
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
        //TODO: Left as an exercise.
        return null;
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
       
        //TODO: Left as an exercise.
       
        return 0;
    }

    /**
     * Inserts the specified element at the first position in the list.
     *
     * @param element to be inserted
     */
    @Override
    public void addFirst(E element) {
        //TODO: Left as an exercise.
        
    }

    /**
     * Inserts the specified element at the last position in the list.
     *
     * @param element to be inserted
     */
    @Override
    public void addLast(E element) {
        //TODO: Left as an exercise.
        
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
        //TODO: Left as an exercise.
        
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
        //TODO: Left as an exercise.
        return null;
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
        //TODO: Left as an exercise.
        return null;
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
       //TODO: Left as an exercise.
        return null;
    }


    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(currentSize);
        SinglyListNode<E> current = head;
        while (current != null) {
            out.writeObject(current.getElement());
            current = current.getNext();
        }
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int size = in.readInt();
        this.head = null;
        this.tail = null;
        this.currentSize = 0;
        for (int i = 0; i < size; i++) {
            addLast((E) in.readObject()); // Efficient O(1) per element
        }
    }

}
