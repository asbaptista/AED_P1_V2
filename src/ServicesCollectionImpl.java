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
public class ServicesCollectionImpl implements ServiceCollection {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
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
        String tagLower = trimString(tag);

        Map<String, Service> servicesWithTag = tagMap.get(tagLower);
        if (servicesWithTag == null) {
            servicesWithTag = new SepChainHashTable<>();
            tagMap.put(tagLower, servicesWithTag);
        }

        servicesWithTag.put(service.getName().toLowerCase(), service);
    }

    private String trimString(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }

        int start = 0;
        int end = s.length();

        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        if (start == 0 && end == s.length()) {
            return s;
        }

        if (start >= end) {
            return "";
        }

        return extractWord(s, start, end);
    }


    private String extractWord(String comment, int start, int end) { // dps tratar de nao te codigo repetido
        char[] chars = new char[end-start];
        for (int i = 0; i < chars.length; i++) {
            char c = comment.charAt(start+i);
            chars[i] = Character.toLowerCase(c);
        }
        return new String(chars);
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
        String tagClean = trimString(tag);
        Map<String, Service> servicesWithTag = tagMap.get(tagClean);

        if (servicesWithTag == null || servicesWithTag.isEmpty()) {
            return new DoublyLinkedList<Service>().iterator();
        }
        return new FilterIterator<>(servicesByInsertion.iterator(), service -> servicesWithTag.get(service.getName().toLowerCase()) != null);
    }

}