import Services.Evaluation;
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

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    /**
     * List of services, maintained in their original insertion order.
     */
    private  List<Service> servicesByInsertion;

    private  Map<String, Service> servicesByName;


    /**
     * Map of services grouped by average star rating.
     * Key: avgStars (0-5), Value: List of services with that rating.
     * This allows O(1) updates instead of O(n) with SortedList.
     */
    private  List<Service>[] rankingByStars;

    private  Map<ServiceType, List<Service>[]> servicesByTypeAndStars;






    // --- Constructor ---

    /**
     * Constructs a new, empty service collection.
     * Initializes the insertion-order list and the star-ranking map with buckets for each rating (0-5).
     */
    public ServicesCollectionImpl() {
        this.servicesByInsertion = new DoublyLinkedList<>();
        this.servicesByName = new ClosedHashTable<>(); // em principio closed
        this.rankingByStars = (List<Service>[]) new List[5];
        for (int i = 0; i < 5; i++) {
            this.rankingByStars[i] = new DoublyLinkedList<>();
        }
        this.servicesByTypeAndStars = new SepChainHashTable<>(); // verificar se é closed ou open
    }

    // --- State Modifiers ---

    /**
     * Adds a new service to the collection.
     * The service is added to the end of the insertion-order list
     * and also added to the appropriate star-rating bucket in the ranking map.
     *
     * @param service The {@link Service} to add.
     */
    @Override
    public void add(Service service) {
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
        List<Service> oldList = rankingByStars[oldStars-1];
        if (oldList != null) {
            int index = oldList.indexOf(service);
            if (index != -1) {
                oldList.remove(index);
            }
        }
        addServiceToRankingByStars(service);
        ServiceType type = service.getType();
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);

        if(starsArray != null){
            List<Service> oldTypeList = starsArray[oldStars-1];
            if(oldTypeList != null) {
                int index = oldTypeList.indexOf(service);
                if (index != -1) {
                    oldTypeList.remove(index);
                }
            }
        }
        addServiceToTypeStarsMap(service);

    }

    /**
     * Helper method to add a service to the rankingByStars map.
     * Gets or creates the list for the service's star rating bucket.
     *
     * @param service The service to add.
     */
    private void addServiceToRankingByStars(Service service) {
        int stars = service.getAvgStar();
        rankingByStars[stars-1].addLast(service);

    }

    private void addServiceToTypeStarsMap(Service service) {
        ServiceType type = service.getType();
        int stars = service.getAvgStar();

        List<Service>[] starsArray = servicesByTypeAndStars.get(type);
        if (starsArray == null) {
            starsArray = (List<Service>[]) new List[5];
            for (int i = 0; i < 5; i++) {
                starsArray[i] = new DoublyLinkedList<>();
            }
            servicesByTypeAndStars.put(type, starsArray);
        }

        List<Service> list = starsArray[stars-1];

        list.addLast(service);
    }



    // --- Querying & Searching ---

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
        return servicesByName.get(name.toLowerCase()); // confirmar os lowerCase, aqui e em cima
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

    // --- Iterators & Retrieval ---

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
        List<Service> sortedList = new DoublyLinkedList<>();
        // Iterate from 5 stars down to 0
        for (int stars = 4; stars >= 0; stars--) {
            List<Service> servicesWithStars = rankingByStars[stars];
            if (servicesWithStars != null) {
                Iterator<Service> it = servicesWithStars.iterator();
                while (it.hasNext()) {
                    sortedList.addLast(it.next());
                }
            }
        }
        return sortedList.iterator();
    }

    /**
     * Gets the raw list of services in their insertion order.
     *
     * @return The {@link DoublyLinkedList} containing all services in insertion order.
     */
    @Override
    public DoublyLinkedList<Service> getServicesByInsertion() {
        return (DoublyLinkedList<Service>) servicesByInsertion;
    }
    @Override
    public Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars) {
        List<Service>[] starsArray = servicesByTypeAndStars.get(type);
        if (starsArray != null) {
            List<Service> list = starsArray[stars-1];
            if (list != null) {
                return list.iterator();
            }
        }
        return new DoublyLinkedList<Service>().iterator(); // Iterador vazio
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

    @Override
    public Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type) {

        List<Service>[] starsArray = servicesByTypeAndStars.get(type);


        if (starsArray == null) {
            return new DoublyLinkedList<Service>().iterator();
        }

        DoublyLinkedList<Service> sortedServices = new DoublyLinkedList<>();

        for (int stars = 4; stars >= 0; stars--) {
            List<Service> list = starsArray[stars];
            if (list != null) {
                Iterator<Service> it = list.iterator();
                while (it.hasNext()) {
                    sortedServices.addLast(it.next());
                }
            }
        }

        return sortedServices.iterator();
    }

}

