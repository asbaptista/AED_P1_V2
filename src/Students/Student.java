package Students;

import Exceptions.*;
import Services.Lodging;
import Services.Service;
import dataStructures.Iterator;

/**
 * Interface defining the contract for all Student types in the system.
 * It outlines the core functionalities, including retrieving student details,
 * managing their location, handling visits, and determining service preferences.
 * Extends {@link StudentReadOnly} to provide mutable operations.
 */
public interface Student extends StudentReadOnly {

    /**
     * Gets the student's designated home (residence).
     * Overridden to return mutable Lodging type for internal operations.
     *
     * @return The {@link Lodging} service set as home.
     */
    @Override
    Lodging getHome();

    /**
     * Gets the service the student is currently visiting.
     * Overridden to return mutable Service type for internal operations.
     *
     * @return The {@link Service} of the student's current location.
     */
    @Override
    Service getCurrent();

    /**
     * Moves the student to a new service location (e.g., Eating or Leisure).
     * This method is responsible for updating the student's current location
     * and triggering visit registration if applicable.
     *
     * @param service The {@link Service} the student is moving to.
     * @throws AlreadyThereException if the student is already at the service.
     * @throws NotValidServiceException if the service type is not valid for movement.
     * @throws EatingIsFullException if the service is an Eating service at capacity.
     */
    void goToLocation(Service service) throws AlreadyThereException, NotValidServiceException, EatingIsFullException;

    /**
     * Changes the student's designated home to a new lodging.
     * This also moves the student to the new home location.
     *
     * @param newHome The new {@link Lodging} service to set as home.
     * @throws AlreadyStudentHomeException if the student already lives there.
     * @throws LodgingIsFullException if the new lodging is at capacity.
     * @throws StudentIsThriftyException if a Thrifty student tries to move to a more expensive lodging.
     */
    void moveHome(Lodging newHome) throws AlreadyStudentHomeException, LodgingIsFullException, StudentIsThriftyException;

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

    /**
     * Gets an iterator over the services this student has visited and stored.
     * Overridden to return mutable Service type for internal operations.
     *
     * @return An {@link Iterator} of {@link Service} objects.
     */
    @Override
    Iterator<Service> getVisitedIterator();
}