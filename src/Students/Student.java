package Students;

import Services.Lodging;
import Services.Service;
import dataStructures.Iterator;

/**
 * Interface defining the contract for all Student types in the system.
 * It outlines the core functionalities, including retrieving student details,
 * managing their location, handling visits, and determining service preferences.
 */
public interface Student {

    // --- Getters ---

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
     * Gets the student's current home (residence).
     *
     * @return The {@link Lodging} service designated as the student's home.
     */
    Lodging getHome();

    /**
     * Gets the service the student is currently at.
     *
     * @return The {@link Service} representing the student's current location.
     */
    Service getCurrent();

    /**
     * Gets the student's type.
     *
     * @return The {@link StudentType} enum (BOOKISH, OUTGOING, or THRIFTY).
     */
    StudentType getType();

    /**
     * Gets an iterator over the list of services this student has visited and stored.
     * <p>
     * The contents of this list depend on the student's type (e.g., Bookish only
     * stores Leisure, Thrifty stores none).
     *
     * @return An {@link Iterator} of {@link Service} objects.
     */
    Iterator<Service> getVisitedIterator();


    // --- Actions ---

    /**
     * Moves the student to a new service location (e.g., Eating or Leisure).
     * This method is responsible for updating the student's current location
     * and triggering visit registration if applicable.
     *
     * @param service The {@link Service} the student is moving to.
     */
    void goToLocation(Service service);

    /**
     * Changes the student's designated home to a new lodging.
     * This also moves the student to the new home location.
     *
     * @param newHome The new {@link Lodging} service to set as home.
     */
    void moveHome(Lodging newHome);


    // --- Helper Methods ---

    /**
     * Finds the "most relevant" service from a given list, based on this
     * student's specific criteria (polymorphic behavior).
     * <p>
     * - Thrifty students will find the cheapest service (by price).
     * - Bookish/Outgoing students will find the best-rated service (by avgStar).
     *
     * @param services An iterator of services (pre-filtered by type) to evaluate.
     * @return The most relevant {@link Service} according to the student's type.
     */
    Service findMostRelevant(Iterator<Service> services);
}