package Services;

import Students.StudentReadOnly;
import dataStructures.TwoWayIterator;

/**
 * Read-only interface for Lodging services.
 * Defines the contract for querying lodging service properties
 * without allowing modifications.
 */
public interface LodgingReadOnly extends ServiceReadOnly {

    /**
     * Checks if the lodging is at full capacity.
     *
     * @return true if full, false otherwise.
     */
    boolean isFull();

    /**
     * Gets a two-way iterator over the list of current occupants.
     *
     * @return A {@link TwoWayIterator} of {@link StudentReadOnly}s.
     */
    TwoWayIterator<?  extends StudentReadOnly> getOccupantsIterator();

}
