package Services;

import Students.StudentReadOnly;
import dataStructures.TwoWayIterator;

public interface LodgingReadOnly extends ServiceReadOnly {
    boolean isFull();
    TwoWayIterator<?  extends StudentReadOnly> getOccupantsIterator();

}
