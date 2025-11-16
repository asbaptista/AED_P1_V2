package Students;

import Services.*;
import dataStructures.Iterator;

import java.io.Serializable;

/**
 * Implementation of the {@link Thrifty} student type.
 * <p>
 * A Thrifty student is primarily concerned with saving money and living on a budget
 *. This class implements the specific logic for
 * tracking the cheapest eating and lodging services
 * and making decisions based on price.
 * This class is serializable.
 */
public class ThriftyImpl extends StudentAbs implements Thrifty, Serializable {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Stores the cheapest {@link Eating} service this student has visited so far.
     */
    private Eating cheapestEating;

    /**
     * Stores the cheapest {@link Lodging} service this student is aware of
     * (initially their home).
     */
    private Lodging cheapestLodging;

    // --- Constructor ---

    /**
     * Constructs a new Thrifty student.
     * The student's home is set as the initial cheapest lodging.
     *
     * @param name    The student's name.
     * @param country The student's country of origin.
     * @param home    The {@link Lodging} service where the student resides.
     */
    public ThriftyImpl(String name, String country, Lodging home) {
        super(name, country, home);
        this.cheapestLodging = home;
        this.cheapestEating = null;
    }

    // --- Getters (from Thrifty interface) ---

    /**
     * Gets the cheapest {@link Eating} service this student has visited so far.
     *
     * @return The cheapest {@link Eating} service, or null if none have been visited.
     */
    @Override
    public Eating getCheapestEating() {
        return cheapestEating;
    }

    /**
     * Gets the cheapest {@link Lodging} service this student is aware of.
     *
     * @return The cheapest {@link Lodging} service known.
     */
    @Override
    public Lodging getCheapestLodging() {
        return cheapestLodging;
    }

    // --- State Updaters (from Thrifty interface) ---

    /**
     * Called when the student visits an {@link Eating} service.
     * Compares the service's price to the current cheapest and updates
     * the internal record if this one is cheaper.
     *
     * @param eating The {@link Eating} service being visited.
     */
    @Override
    public void visitEating(Eating eating) {
        if (cheapestEating == null || eating.getPrice() < cheapestEating.getPrice()) {
            cheapestEating = eating;
        }
    }

    /**
     * Updates the student's internal record of the cheapest lodging.
     * This is typically called by {@link StudentAbs#moveHome} after a
     * successful (cheaper) move.
     *
     * @param lodging The new cheapest {@link Lodging} service.
     */
    @Override
    public void updateCheapestLodging(Lodging lodging) {
        cheapestLodging = lodging;
    }

    // --- Business Logic / Checks (from Thrifty interface) ---

    /**
     * Checks if a move to a new home (lodging) is acceptable.
     * A Thrifty student will only move if the new lodging is
     * strictly cheaper than their current cheapest lodging
     *.
     *
     * @param newHome The potential new {@link Lodging} service.
     * @return true if the new home is cheaper, false otherwise.
     */
    @Override
    public boolean canMoveTo(Lodging newHome) {
        return newHome.getPrice() < cheapestLodging.getPrice();
    }

    /**
     * Checks if the student is "distracted" by the price of an eating service.
     * This occurs if the student visits an eating service that is more
     * expensive than the cheapest one they have visited so far
     *.
     *
     * @param eating The {@link Eating} service the student is visiting.
     * @return true if the service is more expensive than the known cheapest, false otherwise.
     */
    @Override
    public boolean isDistracted(Eating eating) {
        return cheapestEating != null && eating.getPrice() > cheapestEating.getPrice();
    }

    // --- Overridden Business Logic (from Student interface) ---

    /**
     * Finds the most relevant service for a Thrifty student: the one
     * with the lowest price.
     * <p>
     * This overrides the default implementation in {@link StudentAbs}
     * (which searches by star rating).
     *
     * @param services An iterator of services (pre-filtered by type) to evaluate.
     * @return The {@link Service} with the lowest price.
     */
    @Override
    public Service findMostRelevant(Iterator<Service> services) {
        Service cheapestService = null;

        while (services.hasNext()) {
            Service current = services.next();

            if (cheapestService == null) {
                cheapestService = current;
            } else if (current.getPrice() < cheapestService.getPrice()) {
                // Found a cheaper service
                cheapestService = current;
            }
        }
        return cheapestService;
    }
}