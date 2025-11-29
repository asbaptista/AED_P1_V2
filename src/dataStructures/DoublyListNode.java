package dataStructures;

import java.io.Serializable;

/**
 * Double List Node Implementation.
 * A package-private helper class for {@link DoublyLinkedList} and
 * {@link SortedDoublyLinkedList}.
 * <p>
 * This node stores the element and references to the
 * previous and next nodes in the list.
 * Implements {@link Serializable} to be used in serializable collections.
 *
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
class DoublyListNode<E> implements Serializable {
    private static final long serialVersionUID = 1L;

        // --- Fields ---

        /**
         * Element stored in the node.
         * Marked as transient because the containing List (e.g., DoublyLinkedList)
         * is responsible for custom serialization of its elements, not the nodes.
         */
        private transient E element;

        /**
         * (Pointer to) the previous node.
         * Marked as transient.
         */
        private transient DoublyListNode<E> previous;

        /**
         * (Pointer to) the next node.
         * Marked as transient.
         */
        private transient DoublyListNode<E> next;

        // --- Constructors ---

        /**
         * Constructs a new node with all references specified.
         *
         * @apiNote Time Complexity: O(1)
         * @param theElement - The element to be contained in the node.
         * @param thePrevious - The previous node.
         * @param theNext - The next node.
         */
        public DoublyListNode(E theElement, DoublyListNode<E> thePrevious,
                              DoublyListNode<E> theNext ) {
                this.element = theElement;
                this.previous = thePrevious;
                this.next = theNext;
                //TODO: Left as an exercise.// done
        }

        /**
         * Constructs a new node with null previous and next references.
         *
         * @apiNote Time Complexity: O(1)
         * @param theElement to be contained in the node.
         */
        public DoublyListNode(E theElement ) {
                this(theElement, null, null);
                //TODO: Left as an exercise.// done
        }

        // --- Getters ---

        /**
         * Returns the element contained in the node.
         *
         * @apiNote Time Complexity: O(1)
         * @return The element.
         */
        public E getElement( ) {
                return element;
        }

        /**
         * Returns the previous node in the list.
         *
         * @apiNote Time Complexity: O(1)
         * @return The previous node.
         */
        public DoublyListNode<E> getPrevious( ) {
                return previous;
        }

        /**
         * Returns the next node in the list.
         *
         * @apiNote Time Complexity: O(1)
         * @return The next node.
         */
        public DoublyListNode<E> getNext( ) {
                return next;
        }

        // --- Setters ---

        /**
         * Replaces the element currently stored in the node.
         *
         * @apiNote Time Complexity: O(1)
         * @param newElement - New element to replace the current element.
         */
        public void setElement( E newElement ) {
                this.element = newElement;
                //TODO: Left as an exercise //done
        }

        /**
         * Updates the reference to the previous node.
         *
         * @apiNote Time Complexity: O(1)
         * @param newPrevious - Node to replace the current previous node.
         */
        public void setPrevious( DoublyListNode<E> newPrevious ) {
                this.previous = newPrevious;
                //TODO: Left as an exercise.// done
        }

        /**
         * Updates the reference to the next node.
         *
         * @apiNote Time Complexity: O(1)
         * @param newNext - Node to replace the next node.
         */
        public void setNext( DoublyListNode<E> newNext ) {
                this.next = newNext;
                //TODO: Left as an exercise.// done
        }


}