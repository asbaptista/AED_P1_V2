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
    private static final long serialVersionUID = 1L;
//deve ter que se por isto tudo private
    /**
     * List of services, maintained in their original insertion order.
     */
    private List<Service> servicesByInsertion;

    private Map<String, Service> servicesByName;


    /**
     * Map of services grouped by average star rating.
     * Key: avgStars (0-5), Value: List of services with that rating.
     * This allows O(1) updates instead of O(n) with SortedList.
     */
    private Map<Integer, List<Service>> rankingByStars;

    private Map<ServiceType, Map<Integer, List<Service>>> servicesByTypeAndStars;

    private Map<String, Map<String, Service>> tagMap;





    // --- Constructor ---

    /**
     * Constructs a new, empty service collection.
     * Initializes the insertion-order list and the star-ranking map with buckets for each rating (0-5).
     */
    public ServicesCollectionImpl() {
        this.servicesByInsertion = new DoublyLinkedList<>();
        this.servicesByName = new ClosedHashTable<>(); // em principio closed
        this.rankingByStars = new ClosedHashTable<>(); // Map com buckets por estrelas
        this.servicesByTypeAndStars = new SepChainHashTable<>();
        this.tagMap = new SepChainHashTable<>();
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
        // Remove from old stars bucket
        List<Service> oldList = rankingByStars.get(oldStars);
        if (oldList != null) {
            int index = oldList.indexOf(service);
            if (index != -1) {
                oldList.remove(index);
            }
        }

        // Add to new stars bucket
        addServiceToRankingByStars(service);

        // Update the type-stars map
        ServiceType type = service.getType();
        Map<Integer, List<Service>> starsMap = servicesByTypeAndStars.get(type);
        if(starsMap != null){
            List<Service> oldTypeList = starsMap.get(oldStars);
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
        List<Service> list = rankingByStars.get(stars);
        if (list == null) {
            list = new DoublyLinkedList<>();
            rankingByStars.put(stars, list);
        }
        list.addLast(service);
    }

    private void addServiceToTypeStarsMap(Service service) {
        ServiceType type = service.getType();
        int stars = service.getAvgStar();

        Map<Integer, List<Service>> starsMap = servicesByTypeAndStars.get(type);
        if (starsMap == null) {
            starsMap = new SepChainHashTable<>();
            servicesByTypeAndStars.put(type, starsMap);
        }

        List<Service> list = starsMap.get(stars);
        if (list == null) {
            list = new DoublyLinkedList<>();
            starsMap.put(stars, list);
        }
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
        for (int stars = 5; stars >= 0; stars--) {
            List<Service> servicesWithStars = rankingByStars.get(stars);
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
        Map<Integer, List<Service>> starsMap = servicesByTypeAndStars.get(type);
        if (starsMap != null) {
            List<Service> list = starsMap.get(stars);
            if (list != null) {
                return list.iterator();
            }
        }
        return new DoublyLinkedList<Service>().iterator(); // Iterador vazio
    }

    /**
     * Adds a tag to the tag map, associating it with a specific service.
     * If the tag already exists, the service is added to its map.
     * If the service already exists for that tag, it won't be duplicated.
     *
     * @param tag     The tag (word) to index (case-insensitive).
     * @param service The service to associate with this tag.
     */
    @Override
    public void addTagToService(String tag, Service service) {
        String tagLower = tag.toLowerCase().trim();

        // Get or create the map for this tag
        Map<String, Service> servicesWithTag = tagMap.get(tagLower);
        if (servicesWithTag == null) {
            servicesWithTag = new SepChainHashTable<>();
            tagMap.put(tagLower, servicesWithTag);
        }

        // Add the service to the tag's map (using service name as key to avoid duplicates)
        servicesWithTag.put(service.getName().toLowerCase(), service);
    }

    /**
     * Gets an iterator over all services that have the specified tag.
     * Uses the tag map for O(1) lookup but returns services in insertion order.
     *
     * @param tag The tag to search for (case-insensitive).
     * @return An {@link Iterator} of services that have this tag, in insertion order.
     */
    @Override
    public Iterator<Service> getServicesByTag(String tag) {
        String tagLower = tag.toLowerCase().trim();
        Map<String, Service> servicesWithTag = tagMap.get(tagLower);

        if (servicesWithTag == null) {
            // No services with this tag, return empty iterator
            return new DoublyLinkedList<Service>().iterator();
        }

        // Iterate through services in insertion order and keep only those with the tag
        List<Service> servicesList = new DoublyLinkedList<>();
        Iterator<Service> insertionIt = servicesByInsertion.iterator();
        while (insertionIt.hasNext()) {
            Service service = insertionIt.next();
            // Check if this service has the tag (O(1) lookup in the inner map)
            if (servicesWithTag.get(service.getName().toLowerCase()) != null) {
                servicesList.addLast(service);
            }
        }
        return servicesList.iterator();
    }

    // --- Serialization Methods ---

    /**
     * Custom serialization method.
     * <p>
     * Saves the total size and then each service from the insertion-order list.
     * The `rankingByStars` map is **not** saved, as it will be rebuilt during deserialization.
     *
     * @param oos The ObjectOutputStream to write to.
     * @throws IOException If an I/O error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(servicesByInsertion.size());

        Iterator<Service> it = servicesByInsertion.iterator();

        while (it.hasNext()) {
            oos.writeObject(it.next());
        }
    }

    /**
     * Custom deserialization method.
     * <p>
     * Reads the services in their original insertion order and re-adds
     * them using the {@link #add(Service)} method.
     * This correctly rebuilds the insertion-order list, the
     * `rankingByStars` map, and the `tagMap`, ensuring the system state is consistent.
     *
     * @param ois The ObjectInputStream to read from.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        // Initialize fields that need rebuilding
        this.servicesByInsertion = new DoublyLinkedList<>();
        this.servicesByName = new ClosedHashTable<>();
        this.rankingByStars = new ClosedHashTable<>();
        this.servicesByTypeAndStars = new SepChainHashTable<>();

        // Always re-initialize tagMap to ensure it's empty before re-indexing
        this.tagMap = new SepChainHashTable<>();

        // Read services and rebuild all data structures including tagMap
        int size = ois.readInt();
        for (int i = 0; i < size; i++) {
            Service service = (Service) ois.readObject();
            this.add(service);

            // Re-index tags from all evaluations
            if (service instanceof Services.ServiceAbs serviceAbs) {
                dataStructures.Iterator<Services.Evaluation> evalIt = serviceAbs.getEvaluations();
                while (evalIt.hasNext()) {
                    Services.Evaluation eval = evalIt.next();
                    String desc = eval.getDescription();
                    if (desc != null && !desc.trim().isEmpty()) {
                        for (String word : desc.split("\\s+")) {
                            String cleanWord = word.trim();
                            if (!cleanWord.isEmpty()) {
                                addTagToService(cleanWord, service);
                            }
                        }
                    }
                }
            }
        }
    }
}