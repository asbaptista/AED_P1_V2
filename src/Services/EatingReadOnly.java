package Services;
import Students.StudentReadOnly;
import dataStructures.TwoWayIterator;

/**
 * Read-only interface for Eating services.
 * Defines the contract for querying eating service properties
 * without allowing modifications.
 */
public interface EatingReadOnly extends ServiceReadOnly {

    /**
     * Checks if the eating service currently has space for more occupants.
     *
     * @return true if not at capacity, false otherwise.
     */
    boolean hasCapacity();

    /**
     * Gets the total capacity (number of seats) of the eating service.
     *
     * @return The total seating capacity.
     */
    int getCapacity();

    /**
     * Gets a two-way iterator over the list of current occupants.
     *
     * @return A {@link TwoWayIterator} of {@link StudentReadOnly}s.
     */
    TwoWayIterator<? extends StudentReadOnly> getOccupantsIterator();


}
