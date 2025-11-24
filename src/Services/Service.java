package Services;

/**
 * Interface defining the contract for all services in the system (e.g., Eating, Lodging, Leisure).
 * It outlines the core functionalities, including retrieving service details,
 * managing evaluations, and handling location data.
 */
public interface Service {

    // --- Getters ---

    /**
     * Gets the official name of the service.
     *
     * @return The service's name.
     */
    String getName();

    /**
     * Gets the geographic latitude of the service.
     *
     * @return The service's latitude coordinate.
     */
    long getLat();

    /**
     * Gets the geographic longitude of the service.
     *
     * @return The service's longitude coordinate.
     */
    long getLon();

    /**
     * Gets the geographic latitude of the service.
     * (Alias for {@link #getLat()}).
     *
     * @return The service's latitude coordinate.
     */
    long getLatitude();

    /**
     * Gets the geographic longitude of the service.
     * (Alias for {@link #getLon()}).
     *
     * @return The service's longitude coordinate.
     */
    long getLongitude();

    /**
     * Gets the base price of the service.
     *
     * @return The service's base price (e.g., menu, room, or ticket price).
     */
    int getPrice();

    /**
     * Gets the average star rating, rounded to the nearest integer.
     *
     * @return The rounded integer average star rating.
     */
    int getAvgStar();

    /**
     * Gets the total number of evaluations (reviews) this service has received.
     *
     * @return The total count of evaluations.
     */
    int getNEval();

    /**
     * Gets the type of the service.
     *
     * @return The {@link ServiceType} enum (EATING, LODGING, or LEISURE).
     */
    Services.ServiceType getType();

    /**
     * Gets an iterator over all evaluations for this service.
     * Used for tag re-indexing during deserialization.
     *
     * @return An iterator over all evaluations.
     */
    dataStructures.Iterator<Evaluation> getEvaluations();


    // --- State Updaters ---

    /**
     * Adds a new user evaluation (review) to this service.
     *
     * @param rating  The star rating (1-5).
     * @param comment The text comment for the review.
     */
    void addReview(int rating, String comment);

    /**
     * Updates the internal average star rating with a new rating value.
     *
     * @param stars The new star rating to include in the average calculation.
     */
    void updateStars(int stars);


    // --- Public Methods ---

    /**
     * Checks if any evaluation for this service contains a specific tag (word).
     * The search is case-insensitive.
     *
     * @param tag The tag (word) to search for in the evaluation descriptions.
     * @return true if at least one evaluation contains the tag, false otherwise.
     */
    boolean hasEvaluationWithTag(String tag);
}