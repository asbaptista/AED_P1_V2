package Students;

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
public class StudentAbs implements Student, Serializable {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

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

    protected Map<Service, Boolean> visitedServicesSet;


    // --- Constructor ---

    /**
     * Constructs a new abstract student.
     * <p>
     * Initializes the student's details, sets their home and current location
     * to the provided {@link Lodging}, and adds them as an occupant to that lodging.
     *
     * @param name    The student's name.
     * @param country The student's country of origin.
     * @param home    The {@link Lodging} service where the student will reside.
     */
    public StudentAbs(String name, String country, Lodging home) {
        this.name = name;
        this.country = country;
        this.home = home;
        this.current = home;
        this.visitedServices = new DoublyLinkedList<>();
        this.visitedServicesSet = new ClosedHashTable<>();


        home.addOccupant(this);
    }

    // --- Getters (from Student interface) ---

    /**
     * Gets the student's concrete type (BOOKISH, OUTGOING, THRIFTY)
     * by checking the instance's class.
     *
     * @return The {@link StudentType} enum.
     */
    @Override
    public StudentType getType() {
        if (this instanceof Bookish) {
            return StudentType.BOOKISH;
        } else if (this instanceof Outgoing) {
            return StudentType.OUTGOING;
        } else {
            return StudentType.THRIFTY;
        }
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

    // --- Actions (from Student interface) ---

    /**
     * Moves the student to a new service location.
     * <p>
     * This method manages occupant lists for Eating services, updates the
     * student's `current` location, and notifies {@link Thrifty} students
     * when they visit an {@link Eating} service.
     * Finally, it calls the polymorphic {@link #registerVisit(Service)} method.
     *
     * @param service The {@link Service} the student is moving to.
     */
    @Override
    public void goToLocation(Service service) {
        // Remove from current location if it's an Eating service
        updateOccupancy(current, false);

        // Update current location
        current = service;

        // Add to new location if it's an Eating service
        updateOccupancy(service, true);

        // Special check for Thrifty students visiting Eating services
        if (current instanceof Eating && this instanceof Thrifty) {
            ((Thrifty) this).visitEating((Eating) current);
        }

        // Call the polymorphic hook method to register the visit
        // (Subclasses like Bookish and Outgoing will override this)
        this.registerVisit(service);
    }

    /**
     * Changes the student's permanent home to a new {@link Lodging}.
     * <p>
     * This method handles removing the student from their old home's occupant list.
     * For {@link Thrifty} students, it first checks if the move is permissible
     * (i.e., if the new home is cheaper).
     * If the move is successful, the student is also moved to the new home
     * and added as an occupant.
     *
     * @param newHome The new {@link Lodging} service to set as home.
     */
    @Override
    public void moveHome(Lodging newHome) {
        // Remove from old home's occupant list
        if (home != null) {
            home.removeOccupant(this);
        }

        // Check rules for different student types
        if (this instanceof Thrifty) {
            // Thrifty students only move if the new home is cheaper
            if (((Thrifty) this).canMoveTo(newHome)) {
                home = newHome;
                ((Thrifty) this).updateCheapestLodging(newHome);
            }
        } else {
            // Other students (Bookish, Outgoing) move unconditionally
            home = newHome;
        }

        // If the student is not already at their new home, move them there.
        if (current != home) {
            goToLocation(newHome);
        }

        // Add to the new home's occupant list
        newHome.addOccupant(this);
    }


    // --- Helper Methods (from Student interface) ---

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
                // Found a service with a better star rating
                bestService = current;
            }
        }
        return bestService;
    }


    // --- Protected Methods (Hooks for Subclasses) ---

    /**
     * A "hook" method for subclasses to implement visit registration logic.
     * <p>
     * The base implementation (used by {@link Thrifty}) does nothing.
     * This method is overridden by {@link BookishImpl} and {@link OutgoingImpl}
     * to store visits according to their specific rules.
     *
     * @param service The service that was just visited.
     */
    protected void registerVisit(Service service) {
        // Default implementation: do nothing.
    }

    // --- Private Helper Methods ---

    /**
     * A private helper to manage adding/removing this student from the
     * occupant list of {@link Eating} services.
     * <p>
     * Occupancy for {@link Lodging} is handled separately in `moveHome`.
     *
     * @param service The service to update.
     * @param add     true to add the student, false to remove them.
     */
    private void updateOccupancy(Service service, boolean add) {
        if (service instanceof Eating) {
            if (add) {
                ((Eating) service).addOccupant(this);
            } else {
                ((Eating) service).removeOccupant(this);
            }
        }
    }


    // --- Serialization Methods ---

    /**
     * Custom serialization method to write the object's state.
     * Uses default serialization, which is sufficient as all fields
     * (including data structures) are either serializable or handle
     * their own custom serialization.
     *
     * @param oos The ObjectOutputStream to write to.
     * @throws IOException If an I/O error occurs.
     */
    @Serial
    private void writeObject(java.io.ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Custom serialization method to read the object's state.
     * Uses default deserialization.
     *
     * @param ois The ObjectInputStream to read from.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    @Serial
    private void readObject(java.io.ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}