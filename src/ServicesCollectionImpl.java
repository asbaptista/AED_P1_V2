import Services.ByStarsComparator;
import Services.Service;
import dataStructures_1stPhase.*;

import java.io.*;

/**
 * Implements the {@link ServiceCollection} interface.
 * <p>
 * This class manages all {@link Service} objects for an {@link Area}.
 * It maintains two internal lists:
 * 1. A {@link TwoWayList} (`services`) to store services in their original **insertion order**.
 * 2. A {@link SortedList} (`rankingByStars`) to store services sorted by their
 * **average star rating** (descending).
 * <p>
 * This class is serializable and uses custom `writeObject` and `readObject`
 * methods to ensure the `rankingByStars` list is correctly rebuilt upon deserialization.
 */
public class ServicesCollectionImpl implements ServiceCollection, Serializable {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * List of services, maintained in their original insertion order.
     */
    TwoWayList<Service> services;

    /**
     * List of services, automatically sorted by average star rating (descending)
     * using the {@link ByStarsComparator}.
     */
    SortedList<Service> rankingByStars;

    // --- Constructor ---

    /**
     * Constructs a new, empty service collection.
     * Initializes the insertion-order list and the sorted star-ranking list.
     */
    public ServicesCollectionImpl() {
        services = new DoublyLinkedList<>();
        rankingByStars = new SortedDoublyLinkedList<>(new ByStarsComparator());
    }

    // --- State Modifiers ---

    /**
     * Adds a new service to the collection.
     * The service is added to the end of the insertion-order list (`services`)
     * and also added in its correct sorted position in the star-ranking
     * list (`rankingByStars`).
     *
     * @param service The {@link Service} to add.
     */
    @Override
    public void add(Service service) {
        services.addLast(service);
        rankingByStars.add(service);
    }

    /**
     * Updates a service's position in the star-ranked list.
     * <p>
     * This is achieved by removing and re-adding the service to the
     * {@code SortedList}. This correctly re-sorts the service and, crucially,
     * places it at the end of any group of services with the same star rating.
     * This behavior is essential for the `ranking` and `ranked` commands'
     * tie-breaking logic.
     *
     * @param service The service whose star rating has been updated.
     */
    @Override
    public void updateRankingByStars(Service service) {
        rankingByStars.remove(service);
        rankingByStars.add(service);
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
        Iterator<Service> it = services.iterator();
        while (it.hasNext()) {
            Service service = it.next();
            if (service.getName().equalsIgnoreCase(name)) {
                return service;
            }
        }
        return null;
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
        return services.size();
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
        return services.iterator();
    }

    /**
     * Gets an iterator over all services, sorted by their average star rating
     * in descending order (highest stars first).
     *
     * @return A sorted {@link Iterator} of services.
     */
    @Override
    public Iterator<Service> getServicesByStars() {
        return rankingByStars.iterator();
    }

    /**
     * Gets the raw list of services in their insertion order.
     *
     * @return The {@link DoublyLinkedList} containing all services in insertion order.
     */
    @Override
    public DoublyLinkedList<Service> getServicesByInsertion() {
        return (DoublyLinkedList<Service>) services;
    }

    // --- Serialization Methods ---

    /**
     * Custom serialization method.
     * <p>
     * Saves the total size and then each service from the `services`
     * (insertion-order) list. The `rankingByStars` list is **not** saved,
     * as it will be rebuilt during deserialization.
     *
     * @param oos The ObjectOutputStream to write to.
     * @throws IOException If an I/O error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(services.size());

        Iterator<Service> it = services.iterator();

        while (it.hasNext()) {
            oos.writeObject(it.next());
        }
    }

    /**
     * Custom deserialization method.
     * <p>
     * Reads the services in their original insertion order and re-adds
     * them using the {@link #add(Service)} method.
     * This single call correctly rebuilds *both* the `services` list
     * and the `rankingByStars` list, ensuring the system state is consistent.
     *
     * @param ois The ObjectInputStream to read from.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        // Initialize transient fields
        this.services = new DoublyLinkedList<>();
        this.rankingByStars = new SortedDoublyLinkedList<>(new ByStarsComparator());

        // Read services and rebuild both lists by calling add()
        int size = ois.readInt();
        for (int i = 0; i < size; i++) {
            Service service = (Service) ois.readObject();
            this.add(service);
        }
    }
}