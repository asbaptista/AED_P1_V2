package Services;
import Students.StudentReadOnly;
import dataStructures.TwoWayIterator;

public interface EatingReadOnly extends ServiceReadOnly {

    boolean hasCapacity();
    int getCapacity();
    TwoWayIterator<? extends StudentReadOnly> getOccupantsIterator();


}
