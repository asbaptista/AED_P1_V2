package dataStructures_2ndPhase;
/**
 * Array Iterator
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
class ArrayIterator<E> implements Iterator<E> {
    private E[] elems;
    private int counter;
    private int current;
    
    public ArrayIterator(E[] elems, int counter) {
        this.elems = elems;
        this.counter = counter;
        rewind();
    }
    
    @Override
    public void rewind() {
        //TODO: Left as an exercise.
    }

    @Override
    public boolean hasNext() {
	//TODO: Left as an exercise.
        return false;
    }

    @Override
    public E next() {
	//TODO: Left as an exercise.
        return null;
    }

}
