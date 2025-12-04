package Services;

import Exceptions.EatingIsFullException;
import Students.Student;
import dataStructures.TwoWayIterator;

/**
 * Interface defining the specific contract for Eating services.
 * It extends the base {@link Service} interface, adding functionalities
 * for managing student capacity and tracking current occupants (e.g., in a canteen).
 */
public interface Eating extends EatingReadOnly,Service {


    /**
     * Adds a student to the list of current occupants.
     * This method assumes a check for {@link #hasCapacity()} has been made externally.
     *
     *
     * @param student The {@link Student} to be added as an occupant.
     */
    void addOccupant(Student student) throws EatingIsFullException;

    /**
     * Removes a student from the list of current occupants.
     *
     * @param student The {@link Student} to be removed from the occupants list.
     */
    void removeOccupant(Student student);

    /**
     * Gets the total capacity (number of seats) of the eating service.
     *
     * @return The total seating capacity.
     */
    int getCapacity();

    /**
     * Gets a two-way iterator over the list of current occupants.
     * This allows for iterating both forwards and backwards through the list of students.
     *
     * @return A {@link TwoWayIterator} of {@link Student}s.
     *
     */
    @Override
    TwoWayIterator<Student> getOccupantsIterator();
}