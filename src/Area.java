

import Services.*;

import Students.*;

import dataStructures.*;


/**
 * Interface for a geographic Area.
 * Defines the contract for managing all services and students
 * within a specific set of geographic boundaries.
 */
public interface Area {

    // --- Area Details & Bounds ---

    /**
     * Gets the name of the area.
     *
     * @return The area's name.
     */
    String getName();

    /**
     * Gets the top latitude of the area's bounding box.
     *
     * @return The top latitude coordinate.
     */
    long getTopLat();

    /**
     * Gets the left longitude of the area's bounding box.
     *
     * @return The left longitude coordinate.
     */
    long getLeftLong();

    /**
     * Gets the bottom latitude of the area's bounding box.
     *
     * @return The bottom latitude coordinate.
     */
    long getBottomLat();

    /**
     * Gets the right longitude of the area's bounding box.
     *
     * @return The right longitude coordinate.
     */
    long getRightLong();

    /**
     * Checks if a given coordinate (latitude, longitude) is
     * inside the area's defined bounding box.
     *
     * @param lat The latitude to check.
     * @param lon The longitude to check.
     * @return true if the coordinate is within bounds, false otherwise.
     */
    boolean isWithinBounds(long lat, long lon);


    // --- Service Management ---

    /**
     * Adds a new service to the area.
     *
     * @param service The {@link Service} to add.
     */
    void addService(Service service);

    /**
     * Finds and returns a service by its name.
     *
     * @param name The name of the service to find (case-insensitive).
     * @return The {@link Service} object, or null if not found.
     */
    Service getService(String name);

    /**
     * Checks if a service with the given name already exists in the area.
     *
     * @param name The name to check (case-insensitive).
     * @return true if the service exists, false otherwise.
     */
    boolean containsService(String name);

    /**
     * Gets an iterator over all services in the area,
     * typically in order of insertion.
     *
     * @return An {@link Iterator} of {@link Service}s.
     */
    Iterator<Service> getServices();

    /**
     * Gets an iterator over all services, sorted by their
     * average star rating in descending order.
     *
     * @return A sorted {@link Iterator} of {@link Service}s.
     */
    Iterator<Service> getRankedServices();

    /**
     * Updates the position of a service within the star-based ranking.
     * This should be called after a service's star rating changes.
     *
     * @param service The service whose ranking needs to be updated.
     * @param oldStars The previous star rating before the update.
     */
    void updateRankingByStars(Service service, int oldStars);

    /**
     * Gets an iterator over services of a specific type with a specific star rating.
     * This is an optimized query using indexed data structures.
     *
     * @param type The service type to filter by.
     * @param stars The star rating to filter by.
     * @return An {@link Iterator} of {@link Service}s matching the criteria.
     */
    Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars);

    /**
     * Gets the total number of services registered in the area.
     *
     * @return The count of services.
     */
    int getNumberOfServices();

    /**
     * Gets the internal service collection for direct access.
     * Used for advanced operations like tag indexing.
     *
     * @return The {@link ServiceCollection} instance.
     */
    ServiceCollection getServicesCollection();


    // --- Student Management ---

    /**
     * Adds a new student to the area.
     *
     * @param student The {@link Student} to add.
     */
    void addStudent(Student student);

    /**
     * Finds and returns a student by their name.
     *
     * @param name The name of the student to find (case-insensitive).
     * @return The {@link Student} object, or null if not found.
     */
    Student getStudent(String name);

    /**
     * Removes a student from the area.
     *
     * @param name The name of the student to remove.
     */
    void removeStudent(String name);

    /**
     * Gets an iterator over all students in the area,
     * sorted alphabetically by name.
     *
     * @return A sorted {@link Iterator} of {@link Student}s.
     */
    Iterator<Student> listAllStudents();

    /**
     * Gets an iterator over students from a specific country,
     * in order of registration.
     *
     * @param filter The country name to filter by.
     * @return An {@link Iterator} of {@link Student}s from that country.
     */
    Iterator<Student> listStudentsByCountry(String filter);


    Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type);
}