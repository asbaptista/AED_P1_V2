package Services;

/**
 * Read-only interface for a Service.
 * Defines the contract for querying service properties
 * without allowing modifications.
 */
public interface ServiceReadOnly {

    /**
     * Gets the name of the service.
     *
     * @return The service's name.
     */
    String getName();

    /**
     * Gets the latitude coordinate of the service.
     *
     * @return The service's latitude.
     */
    long getLatitude();

    /**
     * Gets the longitude coordinate of the service.
     *
     * @return The service's longitude.
     */
    long getLongitude();

    /**
     * Gets the base price of the service.
     *
     * @return The service's price.
     */
    int getPrice();

    /**
     * Gets the average star rating (rounded).
     *
     * @return The rounded integer average star rating.
     */
    int getAvgStar();

    /**
     * Gets the type of the service.
     *
     * @return The {@link ServiceType}.
     */
    ServiceType getType();

    /**
     * Checks if the service has an evaluation containing a specific tag.
     *
     * @param tag The tag to search for.
     * @return true if at least one evaluation contains the tag, false otherwise.
     */
    boolean hasEvaluationWithTag(String tag);

}
