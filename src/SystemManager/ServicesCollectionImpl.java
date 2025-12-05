package SystemManager;

import Exceptions.ServiceAlreadyExistsException;
import Services.Service;
import Services.ServiceType;
import dataStructures.*;

import java.io.*;

/**
 * Implements the {@link ServiceCollection} interface.
 * <p>
 * This class manages all {@link Service} objects for an {@link Area}.
 * It maintains two internal data structures:
 * 1. A {@link TwoWayList} (`servicesByInsertion`) to store services in their original **insertion order**.
 * 2. A {@link Map} (`rankingByStars`) to store services grouped by their **average star rating** (0-5).
 *    Each bucket contains a list of services with that rating, allowing O(1) lookups and updates.
 * <p>
 * This class is serializable and uses custom `writeObject` and `readObject`
 * methods to ensure the `rankingByStars` map is correctly rebuilt upon deserialization.
 */
public class ServicesCollectionImpl implements ServiceCollection, Serializable {

    /**
     * List of services, maintained in their original insertion order.
     */
    private final List<Service> servicesByInsertion;

    /**
     * Map for fast name-based lookups.
     * Key: service name (lowercase), Value: Service object.
     */
    private final Map<String, Service> servicesByName;

    /**
     * Array of services grouped by average star rating.
     * Index 0 = 1 star, Index 1 = 2 stars, ..., Index 4 = 5 stars.
     * Each index contains a list of services with that rating.
     */
    private final List<Service>[] rankingByStars;

    /**
     * Map grouping services by type and star rating.
     * Key: ServiceType, Value: Array of lists (same structure as rankingByStars).
     */
    private final Map<ServiceType, List<Service>[]> servicesByTypeAndStars;


    /**
     * Constructs a new, empty service collection.
     * Initializes the insertion-order list and the star-ranking map with buckets for each rating (0-5).
     */
    public ServicesCollectionImpl() {
        this.servicesByInsertion = new DoublyLinkedList<>();
        this.servicesByName = new ClosedHashTable<>();
        this.rankingByStars = createStarsArray();
        this.servicesByTypeAndStars = new SepChainHashTable<>();
    }

    /**
     * Helper method to create and initialize a stars array.
     *
     * @return A new array of lists for star ratings (1-5).
     */
    private List<Service>[] createStarsArray() {
        @SuppressWarnings("unchecked")
        List<Service>[] array = (List<Service>[]) new List[5];
        for (int i = 0; i < 5; i++) {
            array[i] = new DoublyLinkedList<>();
        }
        return array;
    }


    /**
     * Adds a new service to the collection.
     * The service is added to the end of the insertion-order list
     * and also added to the appropriate star-rating bucket in the ranking map.
     *
     * @param service The {@link Service} to add.
     */
    @Override
    public void add(Service service)throws ServiceAlreadyExistsException {
        if (contains(service.getName())) {
            throw new ServiceAlreadyExistsException();
        }
        servicesByInsertion.addLast(service);
        servicesByName.put(service.getName().toLowerCase(),service);
        addServiceToRankingByStars(service);
        addServiceToTypeStarsMap(service);
    }

    /**
     * Updates a service's position in the star-ranked map.
     * <p>
     * Removes the service from its old star rating bucket and adds it to the new one.
     * This is O(1) for hash lookup + O(k) where k is services in that bucket,
     * much more efficient than the previous O(n) SortedList approach.
     *
     * @param service The service whose star rating has been updated.
     * @param oldStars The previous star rating before the update.
     */
    @Override
    public void updateRankingByStars(Service service, int oldStars) {
        int newStars = service.getAvgStar();

        if (newStars == oldStars) {
            return;
        }

        removeServiceFromList(rankingByStars[oldStars - 1], service);
        addServiceToRankingByStars(service);

        ServiceType type = service.getType();
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);

        if (starsArray != null) {
            removeServiceFromList(starsArray[oldStars - 1], service);
        }
        addServiceToTypeStarsMap(service);
    }

    /**
     * Helper method to remove a service from a list if it exists.
     *
     * @param list The list to remove from.
     * @param service The service to remove.
     */
    private void removeServiceFromList(List<Service> list, Service service) {
        if (list != null) {
            int index = list.indexOf(service);
            if (index != -1) {
                list.remove(index);
            }
        }
    }

    /**
     * Helper method to add a service to the rankingByStars map.
     * Gets or creates the list for the service's star rating bucket.
     *
     * @param service The service to add.
     */
    private void addServiceToRankingByStars(Service service) {
        int stars = service.getAvgStar();
        rankingByStars[stars - 1].addLast(service);

    }

    /**
     * Helper method to add a service to the servicesByTypeAndStars map.
     * Gets or creates the star rating array for the service's type,
     * then adds the service to the appropriate star bucket.
     *
     * @param service The service to add.
     */
    private void addServiceToTypeStarsMap(Service service) {
        ServiceType type = service.getType();
        int stars = service.getAvgStar();

        List<Service>[] starsArray = servicesByTypeAndStars.get(type);
        if (starsArray == null) {
            starsArray = createStarsArray();
            servicesByTypeAndStars.put(type, starsArray);
        }

        starsArray[stars - 1].addLast(service);
    }


    /**
     * Finds a service by its name using a case-insensitive linear search.
     * <p>
     * This search is performed on the insertion-order list.
     *
     * @param name The name of the service to find.
     * @return The {@link Service} object, or {@code null} if not found.
     */
    @Override
    public Service findByName(String name) {
        return servicesByName.get(name.toLowerCase());
    }

    /**
     * Checks if a service with the given name already exists in the collection.
     *
     * @param name The name of the service to check (case-insensitive).
     * @return {@code true} if the service exists, {@code false} otherwise.
     */
    @Override
    public boolean contains(String name) {
        return findByName(name) != null;
    }

    /**
     * Gets the total number of services in the collection.
     *
     * @return The total count of services.
     */
    @Override
    public int size() {
        return servicesByInsertion.size();
    }


    /**
     * Gets an iterator over all services, in their original order of registration
     * (insertion order).
     *
     * @return An {@link Iterator} of services in insertion order.
     */
    @Override
    public Iterator<Service> listServices() {
        return servicesByInsertion.iterator();
    }

    /**
     * Gets an iterator over all services, sorted by their average star rating
     * in descending order (highest stars first).
     * <p>
     * This creates a temporary list by iterating through star buckets from 5 to 0.
     *
     * @return A sorted {@link Iterator} of services.
     */
    @Override
    public Iterator<Service> getServicesByStars() {
        return new ListsIterator<>(rankingByStars);
    }

    /**
     * Gets an iterator over services of a specific type, sorted by their average star rating
     * in descending order (highest stars first).
     *
     * @param type The service type to filter by.
     * @return An {@link Iterator} of services of the given type, ordered by stars.
     */
    @Override
    public Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type) {
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);

        return new ListsIterator<>(starsArray);
    }

    /**
     * Checks if there are any services of a specific type in the collection.
     *
     * @param type The service type to check.
     * @return {@code true} if at least one service of this type exists, {@code false} otherwise.
     */
    @Override
    public boolean hasServicesOfType(ServiceType type) {
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);
        if (starsArray == null) {
            return false;
        }
        for (List<Service> list : starsArray) {
            if (list != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an iterator over services of a specific type with a specific star rating.
     *
     * @param type The service type to filter by.
     * @param stars The star rating to filter by (1-5).
     * @return An {@link Iterator} of matching services.
     */
    @Override
    public Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars) {
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);

        if (starsArray != null && starsArray[stars - 1] != null) {
            return starsArray[stars - 1].iterator();
        }

        return new DoublyLinkedList<Service>().iterator();
    }

    /**
     * Gets an iterator over all services that have the specified tag.
     * Filters services by checking if they have an evaluation containing the tag.
     *
     * @param tag The tag to search for (case-insensitive).
     * @return An {@link Iterator} of services that have this tag, in insertion order.
     */
    @Override
    public Iterator<Service> getServicesByTag(String tag) {
        return new FilterIterator<>(servicesByInsertion.iterator(),
            service -> service.hasEvaluationWithTag(tag));
    }


}

