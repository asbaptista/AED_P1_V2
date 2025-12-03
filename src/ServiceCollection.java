import Services.Service;
import Services.ServiceType;
import dataStructures.DoublyLinkedList;
import dataStructures.Iterator;

/**
 * Interface for a collection that manages all {@link Service} objects.
 * <p>
 * This contract defines the methods required to add, find, and list services.
 * It is responsible for maintaining services in at least two orders:
 * 1. Order of registration (insertion order).
 * 2. Order of average star rating (ranking).
 */
public interface ServiceCollection {

    /**
     * Adds a new service to the collection.
     * The implementation is responsible for adding the service to
     * all relevant internal lists (e.g., insertion list and ranked list).
     *
     * @param service The {@link Service} to add.
     */
    void add(Service service);

    /**
     * Updates the position of a service within the star-based ranking.
     * This method should be called after a service's {@code avgStar} value changes
     * to ensure the ranked list remains correctly sorted.
     *
     * @param service The service whose ranking needs to be updated.
     */


    void updateRankingByStars(Service service, int oldStars);

    /**
     * Finds and returns a service by its name.
     * The search is typically case-insensitive.
     *
     * @param name The name of the service to find.
     * @return The {@link Service} object, or {@code null} if not found.
     */
    Service findByName(String name);

    /**
     * Checks if a service with the given name already exists in the collection.
     *
     * @param name The name of the service to check (case-insensitive).
     * @return {@code true} if the service exists, {@code false} otherwise.
     */
    boolean contains(String name);

    /**
     * Gets the total number of services in the collection.
     *
     * @return The total count of services.
     */
    int size();


    /**
     * Gets an iterator over all services, in their original order of registration
     * (insertion order).
     *
     * @return An {@link Iterator} of services in insertion order.
     */
    Iterator<Service> listServices();

    /**
     * Gets an iterator over all services, sorted by their average star rating
     * in descending order (highest stars first).
     *
     * @return An {@link Iterator} of services sorted by stars.
     */
    Iterator<Service> getServicesByStars();

    Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars);

    /**
     * Gets an iterator over all services that have the specified tag.
     * Filters services by checking if they have an evaluation containing the tag.
     *
     * @param tag The tag to search for (case-insensitive).
     * @return An {@link Iterator} of services that have this tag, in insertion order.
     */
    Iterator<Service> getServicesByTag(String tag);

    Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type);
}