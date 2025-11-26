package dataStructures;

import dataStructures.exceptions.*;

/**
 * Stack in Array
 *
 * @author AED team
 * @version 1.0
 *
 * @param <E> Generic Element
 */
public class StackWithListInArray<E> implements Stack<E> {

    // Default capacity of the stack.
    static final int DEFAULT_CAPACITY = 1000;
    // Top of the empty stack.
    static final int EMPTY = -1;

    // Memory of the stack: a list in array.
    private List<E> array;
    // capacity
    private int capacity;

    public StackWithListInArray( int capacity ) {
        array =  new ListInArray<>(capacity);
	this.capacity=capacity;
    }

    public StackWithListInArray( ) {
        this(DEFAULT_CAPACITY);
    }
    /**
     * Returns true iff the stack contains no
     * elements.
     *
     * @return true iff the stack contains no
     * elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return array.isEmpty();
	//TODO: Left as an exercise.//done
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return number of elements in the stack
     */
    @Override
    public int size() {
        return array.size();
        //TODO: Left as an exercise.//done
    }

    /**
     * Returns the element at the top of the stack.
     * Requires
     *
     * @return element at top of stack
     * @throws EmptyStackException when size = 0
     */
    @Override
    public E top() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return array.get(array.size()-1);
       //TODO: Left as an exercise.//done
    }

    /**
     * Inserts the specified <code>element</code> onto
     * the top of the stack.
     *
     * @param element element to be inserted onto the stack
     * @throws FullStackException when size = capacity
     */
    @Override
    public void push(E element) {
        if (size() == capacity) {
            throw new FullStackException();
        }
        array.addLast(element);
        //TODO: Left as an exercise.//done
    }

    
    /**
     * Removes and returns the element at the top of the
     * stack.
     *
     * @return element removed from top of stack
     * @throws EmptyStackException when size = 0
     */
    @Override
    public E pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return array.removeLast();
       //TODO: Left as an exercise.//done
    }
}
