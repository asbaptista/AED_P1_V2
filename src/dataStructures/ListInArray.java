package dataStructures;
import dataStructures.exceptions.*;

import java.io.Serializable;

/**
 * List in Array.
 * An implementation of the {@link List} interface using a dynamic, resizable array.
 * This class is serializable.
 *
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
public class ListInArray<E> implements List<E>, Serializable {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The growth factor for resizing the internal array.
     */
    private static final int FACTOR = 2;

    /**
     * Array of generic elements E.
     */
    private transient E[] elems;

    /**
     * Number of elements in array (the logical size of the list).
     */
    private transient int counter;

    // --- Constructor ---

    /**
     * Constructor with initial capacity.
     *
     * @apiNote Time Complexity: O(1) (Array allocation is O(dimension),
     * but relative to list size N=0, it's constant time).
     * @param dimension - initial capacity of array.
     */
    @SuppressWarnings("unchecked")
    public ListInArray(int dimension) {
        elems = (E[]) new Object[dimension];
        counter = 0;
    }

    // --- Status Checkers ---

    /**
     * Returns true iff the list contains no elements.
     *
     * @apiNote Time Complexity: O(1)
     * @return {@code true} if list is empty
     */
    public boolean isEmpty() {
        return counter == 0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return number of elements in the list
     */
    public int size() {
        return counter;
    }

    // --- Iterators ---

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     *
     * @apiNote Time Complexity: O(1)
     * @return Iterator of the elements in the list
     */
    public Iterator<E> iterator() {
        return new ArrayIterator<>(elems, counter);
    }

    // --- Get Operations ---

    /**
     * Returns the first element of the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return First element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elems[0];
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the last element of the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return Last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elems[counter - 1];
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     *
     * @apiNote Time Complexity: O(1)
     * @param position - position of element to be returned
     * @return Element at position
     * @throws InvalidPositionException if position is not valid in the list
     */
    public E get(int position) {
        if (position < 0 || position >= counter) {
            throw new InvalidPositionException();
        }
        return elems[position];
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     *
     * @apiNote Time Complexity: O(N) (linear search)
     * @param element - element to be searched in list
     * @return Position of the first occurrence of the element in the list (or -1)
     */
    public int indexOf(E element) {
        for (int i = 0; i < counter; i++) {
            if (elems[i].equals(element)) {
                return i;
            }
        }
        //TODO: Left as an exercise.
        return -1;
    }

    // --- Add Operations ---

    /**
     * Inserts the specified element at the first position in the list.
     * All existing elements are shifted one position to the right.
     *
     * @apiNote Time Complexity: O(N) (due to element shifting;
     * O(N) amortized if resize occurs).
     * @param element to be inserted
     */
    public void addFirst(E element) {
        if (counter == elems.length) {
            resize(elems.length * FACTOR);
        }
        for (int i = counter; i > 0; i--) {
            elems[i] = elems[i - 1];
        }
        elems[0] = element;
        counter++;
        //TODO: Left as an exercise.
    }

    /**
     * Inserts the specified element at the last position in the list.
     *
     * @apiNote Time Complexity: O(1) (Amortized)
     * (O(1) if no resize, O(N) if resize is needed).
     * @param element to be inserted
     */
    public void addLast(E element) {
        if (counter == elems.length) {
            resize(elems.length * FACTOR);
        }
        elems[counter] = element;
        counter++;
        //TODO: Left as an exercise.//done
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Elements from that position onwards are shifted to the right.
     * Range of valid positions: 0, ..., size().
     *
     * @apiNote Time Complexity: O(N)
     * (Best-case O(1) amortized if pos=size; Worst-case O(N) if pos=0).
     * @param position - position where to insert element
     * @param element  - element to be inserted
     * @throws InvalidPositionException - if position is not valid in the list
     */
    public void add(int position, E element) {
        if (position < 0 || position > counter) {
            throw new InvalidPositionException();
        }
        if (counter == elems.length) {
            resize(elems.length * FACTOR);
        }
        if(position==0) {
            addFirst(element);
        } else if (position==counter) {
            addLast(element);
        } else {
            for (int i = counter; i > position; i--) {
                elems[i] = elems[i - 1];
            }
            elems[position] = element;
            counter++;
        }
        //TODO: Left as an exercise.//done
    }

    // --- Remove Operations ---

    /**
     * Removes and returns the element at the first position in the list.
     * All subsequent elements are shifted one position to the left.
     *
     * @apiNote Time Complexity: O(N) (due to element shifting).
     * @return Element removed from the first position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E removedElement = elems[0];
        for (int i = 1; i < counter; i++) {
            elems[i - 1] = elems[i];
        }
        elems[--counter] = null; // Help garbage collector
        return removedElement;
        //TODO: Left as an exercise.//done
    }

    /**
     * Removes and returns the element at the last position in the list.
     *
     * @apiNote Time Complexity: O(1)
     * @return Element removed from the last position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E removedElement = elems[--counter];
        elems[counter] = null; // Help garbage collector
        return removedElement;
        //TODO: Left as an exercise.//done
    }

    /**
     * Removes and returns the element at the specified position in the list.
     * Elements after the position are shifted to the left.
     * Range of valid positions: 0, ..., size()-1.
     *
     * @apiNote Time Complexity: O(N)
     * (Best-case O(1) if pos=size-1; Worst-case O(N) if pos=0).
     * @param position - position of element to be removed
     * @return Element removed at position
     * @throws InvalidPositionException - if position is not valid in the list
     */
    public E remove(int position) {
        if (position < 0 || position >= counter) {
            throw new InvalidPositionException();
        }
        if (position == 0) {
            return removeFirst();
        } else if (position == counter - 1) {
            return removeLast();
        } else {
            E removedElement = elems[position];
            for (int i = position; i < counter - 1; i++) {
                elems[i] = elems[i + 1];
            }
            elems[--counter] = null; // Help garbage collector
            return removedElement;
            //TODO: Left as an exercise.
        }
    }

    // --- Private Helper Methods ---

    /**
     * Resizes the internal array to a new capacity.
     * All existing elements are copied to the new array.
     *
     * @apiNote Time Complexity: O(N) (where N = counter, the current size).
     * @param newCapacity The new capacity for the array.
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        E[] newArray = (E[]) new Object[newCapacity];
        for (int i = 0; i < counter; i++) {
            newArray[i] = elems[i];
        }
        elems = newArray;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        out.writeInt(elems.length);
        for (int i = 0; i < counter; i++) {
            out.writeObject(elems[i]);
        }
        out.flush();
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        int arrayLength = in.readInt();
        this.elems = (E[]) new Object[arrayLength];
        for (int i = 0; i < counter; i++) {
            elems[i] = (E) in.readObject();
        }
    }


}