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

    /**
     * Constructor with capacity
     * @apiNote Time Complexity: O(1) (array allocation is O(capacity), but constant relative to stack operations)
     * @param capacity - initial capacity of the stack
     */
    public StackWithListInArray( int capacity ) {
        array =  new ListInArray<>(capacity);
	this.capacity=capacity;
    }

    /**
     * Constructor with default capacity
     * @apiNote Time Complexity: O(1)
     */
    public StackWithListInArray( ) {
        this(DEFAULT_CAPACITY);
    }
    /**
     * Returns true iff the stack contains no
     * elements.
     *
     * @apiNote Time Complexity: O(1)
     * @return true iff the stack contains no
     * elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @apiNote Time Complexity: O(1)
     * @return number of elements in the stack
     */
    @Override
    public int size() {
        return array.size();
    }

    /**
     * Returns the element at the top of the stack.
     * Requires
     *
     * @apiNote Time Complexity: O(1)
     * @return element at top of stack
     * @throws EmptyStackException when size = 0
     */
    @Override
    public E top() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return array.get(array.size()-1);
    }

    /**
     * Inserts the specified <code>element</code> onto
     * the top of the stack.
     *
     * @apiNote Time Complexity: O(1) (amortized, O(N) if resize needed)
     * @param element element to be inserted onto the stack
     * @throws FullStackException when size = capacity
     */
    @Override
    public void push(E element) {
        if (size() == capacity) {
            throw new FullStackException();
        }
        array.addLast(element);
    }

    
    /**
     * Removes and returns the element at the top of the
     * stack.
     *
     * @apiNote Time Complexity: O(1)
     * @return element removed from top of stack
     * @throws EmptyStackException when size = 0
     */
    @Override
    public E pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return array.removeLast();
    }
}
