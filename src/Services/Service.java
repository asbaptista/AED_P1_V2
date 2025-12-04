package Services;

/**
 * Interface defining the contract for all services in the system (e.g., Eating, Lodging, Leisure).
 * It outlines the core functionalities, including retrieving service details,
 * managing evaluations, and handling location data.
 */
public interface Service extends ServiceReadOnly {

    void addReview(int rating, String comment);

    /**
     * Updates the internal average star rating with a new rating value.
     *
     * @param stars The new star rating to include in the average calculation.
     */
    void updateStars(int stars);


}