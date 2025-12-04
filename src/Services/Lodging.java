package Services;

import Exceptions.LodgingIsFullException;
import Students.Student;
import dataStructures.TwoWayIterator;

/**
 * Interface defining the specific contract for Lodging services.
 * It extends the base {@link Service} interface, adding functionalities
 * for managing student occupancy (e.g., in a student residence).
 */
public interface Lodging extends LodgingReadOnly, Service {


    /**
     * Adds a student to the list of occupants currently residing in this lodging.
     *
     * @param student The {@link Student} to be added as an occupant.
     */
    void addOccupant(Student student) throws LodgingIsFullException;

    /**
     * Removes a student from the list of occupants.
     *
     * @param student The {@link Student} to be removed from the occupants list.
     */
    void removeOccupant(Student student);

    /**
     * Gets a two-way iterator over the list of current occupants.
     * This allows for iterating both forwards and backwards through the list of students.
     *
     * @return A {@link TwoWayIterator} of {@link Student}s.
     */
    @Override
    TwoWayIterator<Student> getOccupantsIterator();

}