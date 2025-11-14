package dataStructures_2ndPhase;

import java.io.Serializable;

/**
     * Double List Node Implementation
     * @author AED  Team
     * @version 1.0
     * @param <E> Generic Element
     * 
     */
class DoublyListNode<E> implements Serializable {
    	/**
    	 * Serial Version UID of the Class
    	 */
        static final long serialVersionUID = 0L;
        
    	/**
         * Element stored in the node.
         */
        private E element;

        /**
         * (Pointer to) the previous node.
         */
        private DoublyListNode<E> previous;

        /**
         * (Pointer to) the next node.
         */
        private DoublyListNode<E> next;

        /**
         * 
         * @param theElement - The element to be contained in the node
         * @param thePrevious - the previous node
         * @param theNext - the next node
         */
        public DoublyListNode(E theElement, DoublyListNode<E> thePrevious,
                              DoublyListNode<E> theNext ) {
            //TODO: Left as an exercise.

        }

        /**
         * 
         * @param theElement to be contained in the node
         */
        public DoublyListNode(E theElement ) {
            //TODO: Left as an exercise.

        }

        /**
         * 
         * @return the element contained in the node
         */
        public E getElement( ) {
            return element;
        }

        /**
         * 
         * @return the previous node
         */
        public DoublyListNode<E> getPrevious( ) {
            return previous;
        }

        /**
         * 
         * @return the next node
         */
        public DoublyListNode<E> getNext( ) {
            return next;
        }

        /**
         * 
         * @param newElement - New element to replace the current element
         */
        public void setElement( E newElement ) {
            //TODO: Left as an exercise.

        }

        /**
         * 
         * @param newPrevious - node to replace the current previous node
         */
        public void setPrevious( DoublyListNode<E> newPrevious ) {
            //TODO: Left as an exercise.

        }

        /**
         * 
         * @param newNext - node to replace the next node
         */
        public void setNext( DoublyListNode<E> newNext ) {
            //TODO: Left as an exercise.

        }
    }