package dataStructures_2ndPhase;

import dataStructures_2ndPhase.exceptions.*;
import java.io.Serializable;

/**
 * List (sequence) Abstract Data Type 
 * Includes description of general methods to be implemented by lists.
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 * 
 */

public interface List<E> extends Serializable {
    int NOT_FOUND=-1;
    /**
     * Returns true iff the list contains no elements.
     * @return true if list is empty
     */
    boolean isEmpty( );

    /**
     * Returns the number of elements in the list.
     * @return number of elements in the list
     */
    int size( );

    /**
     *  Returns an iterator of the elements in the list (in proper sequence).
     * @return Iterator of the elements in the list
     */
    Iterator<E> iterator( );

    /**
     * Returns the first element of the list.
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    E getFirst( );

    /**
     * Returns the last element of the list.
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    E getLast( );
 
    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, get corresponds to getFirst. 
     * If the specified position is size()-1, get corresponds to getLast.    
     * @param position - position of element to be returned
     * @return element at position
     * @throws InvalidPositionException if position is not valid in the list
     */
    E get( int position );

    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     * @param element - element to be searched in list
     * @return position of the first occurrence of the element in the list (or -1)
     */
    int indexOf(E element );

    /**
     * Inserts the specified element at the first position in the list.
     * @param element to be inserted
     */
    void addFirst( E element );

    /**
     * Inserts the specified element at the last position in the list.
     * @param element to be inserted
     */
    void addLast( E element );

    /**
     * Inserts the specified element at the specified position in the list.
     * Range of valid positions: 0, ..., size(). 
     * If the specified position is 0, add corresponds to addFirst.
     * If the specified position is size(), add corresponds to addLast.
     * @param position - position where to insert element
     * @param element - element to be inserted
     * @throws InvalidPositionException - if position is not valid in the list
     */
    void add( int position, E element );

    /**
     * Removes and returns the element at the first position in the list.
     * @return element removed from the first position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    E removeFirst( );

    /**
     * Removes and returns the element at the last position in the list.
     * @return element removed from the last position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    E removeLast( );

    /**
     *  Removes and returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, remove corresponds to removeFirst. 
     * If the specified position is size()-1, remove corresponds to removeLast.
     * @param position - position of element to be removed
     * @return element removed at position 
     * @throws InvalidPositionException - if position is not valid in the list
     */
    E remove( int position );

}   

