package Students;

import Exceptions.*;
import Services.*;
import dataStructures.*;

import java.io.*;

/**
 * Abstract base class implementing the {@link Student} interface.
 * <p>
 * This class provides the core, shared functionality for all student types,
 * including storing personal details (name, country), managing location (home, current),
 * and handling a list of visited services.
 * <p>
 * It provides a default implementation for `findMostRelevant` (based on star rating)
 * and a "hook" method (`registerVisit`) for subclasses to implement custom
 * visit-tracking logic. This class is serializable.
 */
public abstract class StudentAbs implements Student, Serializable {

    /**
     * The student's full name.
     */
    protected String name;

    /**
     * The student's country of origin.
     */
    protected String country;

    /**
     * The {@link Lodging} service designated as the student's permanent home.
     */
    protected Lodging home;

    /**
     * The {@link Service} where the student is currently located.
     */
    protected Service current;

    /**
     * A list of services this student has visited and stored,
     * as per their type-specific rules.
     */
    protected TwoWayList<Service> visitedServices;

    /**
     * A set for quick lookup of visited services to avoid duplicates.
     */
    protected Map<Service, Boolean> visitedServicesSet;

    /**
     * The type of the student (BOOKISH, OUTGOING, or THRIFTY).
     */
    protected StudentType type;


    /**
     * Constructs a new abstract student.
     * <p>
     * Initializes the student's details, sets their home and current location
     * to the provided {@link Lodging}, and adds them as an occupant to that lodging.
     *
     * @param name    The student's name.
     * @param country The student's country of origin.
     * @param home    The {@link Lodging} service where the student will reside.
     * @param type    The {@link StudentType} (BOOKISH, OUTGOING, or THRIFTY).
     */
    public StudentAbs(String name, String country, Lodging home, StudentType type) throws LodgingIsFullException {
        this.name = name;
        this.country = country;
        this.home = home;
        this.current = home;
        this.type = type;
        this.visitedServices = new DoublyLinkedList<>();
        this.visitedServicesSet = new ClosedHashTable<>();
        home.addOccupant(this);
    }


    /**
     * Gets the student's concrete type (BOOKISH, OUTGOING, THRIFTY).
     *
     * @return The {@link StudentType} enum.
     */
    @Override
    public StudentType getType() {
        return type;
    }

    /**
     * Gets the student's full name.
     *
     * @return The student's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the student's country of origin.
     *
     * @return The student's country.
     */
    @Override
    public String getCountry() {
        return country;
    }

    /**
     * Gets the student's designated home (residence).
     *
     * @return The {@link Lodging} service set as home.
     */
    @Override
    public Lodging getHome() {
        return home;
    }

    /**
     * Gets the service the student is currently visiting.
     *
     * @return The {@link Service} of the student's current location.
     */
    @Override
    public Service getCurrent() {
        return current;
    }

    /**
     * Gets an iterator over the list of services this student has visited and stored.
     *
     * @return An {@link Iterator} of {@link Service} objects.
     */
    @Override
    public Iterator<Service> getVisitedIterator() {
        return visitedServices.iterator();
    }


    /**
     * Moves the student to a new {@link Service} location.
     * Handles updating occupancy lists for Eating services
     * and invokes type-specific visit registration.
     *
     * @param service The new {@link Service} to move to.
     * @throws AlreadyThereException       if the student is already at that service.
     * @throws NotValidServiceException    if the service is not valid for visiting (e.g., Lodging).
     * @throws EatingIsFullException       if the Eating service is at capacity.
     */
    @Override
    public void goToLocation(Service service) throws AlreadyThereException, NotValidServiceException, EatingIsFullException {

        if (service instanceof Lodging) {
            throw new NotValidServiceException();
        }
        if (current.getName().equalsIgnoreCase(service.getName())) {
            throw new AlreadyThereException();
        }

        updateOccupancy(current, false);
        current = service;
        updateOccupancy(service, true);

        if (current instanceof Eating eating && this instanceof Thrifty thrifty) {
            thrifty.visitEating(eating);
        }

        registerVisit(service);
    }

    /**
     * Changes the student's permanent home to a new {@link Lodging}.
     * Handles removing the student from their old home's occupant list,
     * moving them to the new home, and adding them as an occupant.
     *
     * @param newHome The new {@link Lodging} service to set as home.
     * @throws AlreadyStudentHomeException if the student already lives there.
     * @throws LodgingIsFullException if the new lodging is at capacity.
     * @throws StudentIsThriftyException if a Thrifty student tries to move to a more expensive lodging.
     */
    @Override
    public void moveHome(Lodging newHome) throws AlreadyStudentHomeException, LodgingIsFullException, StudentIsThriftyException {

        if (home == newHome) {
            throw new AlreadyStudentHomeException();
        }
        if (newHome.isFull()) {
            throw new LodgingIsFullException();
        }

        if (this instanceof Thrifty thrifty && !thrifty.canMoveTo(newHome)) {
                throw new StudentIsThriftyException();
            }


        home.removeOccupant(this);
        home = newHome;

        if (current != home) {
            if (current instanceof Eating eating) {
                eating.removeOccupant(this);
            }
            current = home;
        }

        newHome.addOccupant(this);

        if (this instanceof Thrifty thrifty) {
            thrifty.updateCheapestLodging(newHome);
        } else {
            registerVisit(newHome);
        }
    }


    /**
     * Finds the most relevant service based on the *default* criteria:
     * the highest average star rating.
     * <p>
     * This implementation is used by {@link Bookish} and {@link Outgoing} students
     *.
     * The {@link Thrifty} student type overrides this method for a price-based comparison.
     *
     * @param services An iterator of services (pre-filtered by type) to evaluate.
     * @return The service with the highest `avgStar`, or the first one in case of a tie.
     */
    @Override
    public Service findMostRelevant(Iterator<Service> services) {
        Service bestService = null;

        while (services.hasNext()) {
            Service current = services.next();

            if (bestService == null) {
                bestService = current;
            } else if (current.getAvgStar() > bestService.getAvgStar()) {
                bestService = current;
            }
        }
        return bestService;
    }


    /**
     * Helper method to manage adding/removing this student from the
     * occupant list of {@link Eating} services.
     * Occupancy for {@link Lodging} is handled separately in moveHome.
     *
     * @param service The service to update.
     * @param add true to add the student, false to remove them.
     * @throws EatingIsFullException if adding to a full Eating service.
     */
    private void updateOccupancy(Service service, boolean add) throws EatingIsFullException {
        if (service instanceof Eating eating) {
            if (add) {
                eating.addOccupant(this);
            } else {
                eating.removeOccupant(this);
            }
        }
    }

    /**
     * Registers a service as visited according to the student type's rules.
     * This is a template method that each concrete student type must implement.
     *
     * @param service The service the student has visited.
     */
    protected abstract void registerVisit(Service service);
}