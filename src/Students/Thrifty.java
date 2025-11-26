package Students;

import Services.Eating;
import Services.Lodging;

/**
 * Interface defining the specific contract for "Thrifty" students.
 * <p>
 * This interface extends {@link Student} and adds functionalities
 * specific to a student whose main concern is saving money
 *. This includes tracking the cheapest
 * services and making decisions based on price.
 */
public interface Thrifty extends Student {

    // --- Price/Move Checks ---

    /**
     * Checks if a move to a new home (lodging) is acceptable.
     * A Thrifty student will only move if the new lodging is cheaper than
     * their current one.
     *
     * @param newHome The potential new {@link Lodging} service.
     * @return true if the new home is cheaper, false otherwise.
     */
    boolean canMoveTo(Lodging newHome);

    /**
     * Checks if the student is "distracted" by the price of an eating service.
     * This happens if the student visits an eating service that is more
     * expensive than the cheapest one they have visited so far
     *.
     *
     * @param eating The {@link Eating} service the student is visiting.
     * @return true if the service is more expensive than the known cheapest, false otherwise.
     */
    boolean isDistracted(Eating eating);


    // --- State Updaters ---

    /**
     * Called when the student visits an {@link Eating} service.
     * Used to track and update the cheapest eating service known to the student.
     *
     * @param eating The {@link Eating} service being visited.
     */
    void visitEating(Eating eating);

    /**
     * Updates the student's internal record of the cheapest lodging.
     * This is typically called after a successful move to a new, cheaper home.
     *
     * @param lodging The new cheapest {@link Lodging} service.
     */
    void updateCheapestLodging(Lodging lodging);


    // --- Getters ---

    /**
     * Gets the cheapest {@link Eating} service this student has visited so far.
     *
     * @return The cheapest {@link Eating} service, or null if none visited.
     */
    Eating getCheapestEating();

    /**
     * Gets the cheapest {@link Lodging} service this student is aware of
     * (usually their current home).
     *
     * @return The cheapest {@link Lodging} service known.
     */
    Lodging getCheapestLodging();
}