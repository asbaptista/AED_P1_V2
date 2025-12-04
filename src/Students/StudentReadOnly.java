package Students;

import Services.LodgingReadOnly;
import Services.ServiceReadOnly;
import dataStructures.Iterator;

/**
 * Read-only interface for a Student.
 * Defines the contract for querying student properties
 * without allowing modifications.
 */
public interface StudentReadOnly {

    /**
     * Gets the student's full name.
     *
     * @return The student's name.
     */
    String getName();

    /**
     * Gets the student's country of origin.
     *
     * @return The student's country.
     */
    String getCountry();

    /**
     * Gets the student's designated home (residence).
     *
     * @return The {@link LodgingReadOnly} service set as home.
     */
    LodgingReadOnly getHome();

    /**
     * Gets the service the student is currently visiting.
     *
     * @return The {@link ServiceReadOnly} of the student's current location.
     */
    ServiceReadOnly getCurrent();

    /**
     * Gets the student's concrete type (BOOKISH, OUTGOING, THRIFTY).
     *
     * @return The {@link StudentType} enum.
     */
    StudentType getType();

    /**
     * Gets an iterator over the services this student has visited and stored.
     *
     * @return An {@link Iterator} of {@link ServiceReadOnly} objects.
     */
    Iterator<?  extends ServiceReadOnly> getVisitedIterator();

}
