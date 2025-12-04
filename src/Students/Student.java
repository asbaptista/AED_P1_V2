package Students;

import Exceptions.*;
import Services.Lodging;
import Services.Service;
import dataStructures.Iterator;

/**
 * Interface defining the contract for all Student types in the system.
 * It outlines the core functionalities, including retrieving student details,
 * managing their location, handling visits, and determining service preferences.
 */
public interface Student extends StudentReadOnly {

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to return mutable Lodging type for internal operations.
     */
    @Override
    Lodging getHome(); // n deve serprecido

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to return mutable Service type for internal operations.
     */
    @Override
    Service getCurrent();


    /**
     * Moves the student to a new service location (e.g., Eating or Leisure).
     * This method is responsible for updating the student's current location
     * and triggering visit registration if applicable.
     *
     * @param service The {@link Service} the student is moving to.
     */
    void goToLocation(Service service) throws AlreadyThereException, NotValidServiceException, EatingIsFullException;

    /**
     * Changes the student's designated home to a new lodging.
     * This also moves the student to the new home location.
     *
     * @param newHome The new {@link Lodging} service to set as home.
     */
    void moveHome(Lodging newHome)throws AlreadyStudentHomeException, LodgingIsFullException, StudentIsThriftyException;


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

    @Override
    Iterator<Service> getVisitedIterator();
}