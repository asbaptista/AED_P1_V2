package SystemManager;

import Services.ServiceReadOnly;
import Students.StudentReadOnly;
import dataStructures.Iterator;
import dataStructures.TwoWayIterator;
import Services.ServiceType;

/**
 * Read-only interface for a geographic SystemManager.SystemManager.Area.
 * Defines the contract for querying area properties, services, and students
 * without allowing modifications.
 */
public interface AreaReadOnly {

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
     * Gets the total number of services in the area.
     *
     * @return The count of services.
     */
    int getNumberOfServices();

    /**
     * Checks if a coordinate is within the area's bounding box.
     *
     * @param lat The latitude to check.
     * @param lon The longitude to check.
     * @return true if within bounds, false otherwise.
     */
    boolean isWithinBounds(long lat, long lon);

    /**
     * Checks if a service with the given name exists.
     *
     * @param name The name to check.
     * @return true if the service exists, false otherwise.
     */
    boolean containsService(String name);

    /**
     * Gets a service by name.
     *
     * @param name The name of the service to find.
     * @return The {@link ServiceReadOnly} object, or null if not found.
     */
    ServiceReadOnly getService(String name);

    /**
     * Gets an iterator over all services in insertion order.
     *
     * @return An {@link Iterator} of {@link ServiceReadOnly}s.
     */
    Iterator<? extends ServiceReadOnly> getServices();

    /**
     * Gets an iterator over all services sorted by star rating.
     *
     * @return A sorted {@link Iterator} of {@link ServiceReadOnly}s.
     */
    Iterator<? extends ServiceReadOnly> getRankedServices();

    /**
     * Gets services of a specific type with a specific star rating.
     *
     * @param type The service type to filter by.
     * @param stars The star rating to filter by.
     * @return An {@link Iterator} of {@link ServiceReadOnly}s.
     */
    Iterator<? extends  ServiceReadOnly> getServicesByTypeAndStars(ServiceType type, int stars);

    /**
     * Gets services of a specific type ordered by stars.
     *
     * @param type The service type to filter by.
     * @return An {@link Iterator} of {@link ServiceReadOnly}s.
     */
    Iterator<? extends ServiceReadOnly> getServicesByTypeOrderedByStars(ServiceType type);

    /**
     * Checks if the area has services of a specific type.
     *
     * @param type The service type to check.
     * @return true if services of this type exist, false otherwise.
     */
    boolean hasServicesOfType(ServiceType type);

    /**
     * Checks if a student with the given name exists.
     *
     * @param name The name to check.
     * @return true if the student exists, false otherwise.
     */
    boolean containsStudent(String name);

    /**
     * Gets a student by name.
     *
     * @param name The name of the student to find.
     * @return The {@link StudentReadOnly} object, or null if not found.
     */
    StudentReadOnly getStudent(String name);

    /**
     * Lists all students alphabetically by name.
     *
     * @return An {@link Iterator} of {@link StudentReadOnly}s.
     */
    Iterator<? extends StudentReadOnly> listAllStudents();

    /**
     * Lists students from a specific country in registration order.
     *
     * @param country The country to filter by.
     * @return An {@link Iterator} of {@link StudentReadOnly}s.
     */
    Iterator<? extends StudentReadOnly> listStudentsByCountry(String country);


    /**
     * Gets the current location of a student.
     *
     * @param studentName The name of the student.
     * @return The service where the student is currently located.
     */
    ServiceReadOnly getStudentCurrentLocation(String studentName);

    /**
     * Gets the current location of a student.
     *
     * @param student The student object.
     * @return The service where the student is currently located.
     */
    ServiceReadOnly getStudentCurrentLocation(StudentReadOnly student);

    /**
     * Checks if a thrifty student is distracted at an eating service.
     *
     * @param studentName The name of the student.
     * @param serviceName The name of the service.
     * @return true if the student is thrifty and distracted at that eating service.
     */
    boolean isStudentDistracted(String studentName, String serviceName);

    /**
     * Gets an iterator over the services a student has visited.
     *
     * @param studentName The name of the student.
     * @return An iterator of visited services.
     */
    Iterator<? extends ServiceReadOnly> getStudentVisitedLocations(String studentName);

    /**
     * Gets an iterator over occupants of a service (Eating or Lodging).
     *
     * @param serviceName The name of the service.
     * @return A two-way iterator over students in that service.
     */
    TwoWayIterator<? extends StudentReadOnly> getServiceOccupants(String serviceName);

    /**
     * Finds the most relevant service for a student based on their criteria.
     *
     * @param studentName The name of the student.
     * @param serviceType The type of service to search for.
     * @return The most relevant service for that student.
     */
    ServiceReadOnly findRelevantServiceForStudent(String studentName, ServiceType serviceType);

    /**
     * Gets the closest services to a student's current location
     * that match a specific type and star rating.
     *
     * @param studentName The name of the student.
     * @param type The service type.
     * @param stars The star rating.
     * @return An iterator of the closest matching services.
     */
    Iterator<? extends ServiceReadOnly> getClosestServicesByTypeAndStars(String studentName, ServiceType type, int stars);

    /**
     * Checks if a service is a lodging service.
     *
     * @param serviceName The name of the service.
     * @return true if the service is a lodging, false otherwise.
     */
    boolean isLodgingService(String serviceName);

    /**
     * Checks if a service controls entry/exit (Eating or Lodging).
     *
     * @param serviceName The name of the service.
     * @return true if the service is Eating or Lodging, false otherwise.
     */
    boolean isServiceWithOccupancy(String serviceName);

    /**
     * Checks if a student is of type Thrifty.
     *
     * @param studentName The name of the student.
     * @return true if the student is Thrifty, false otherwise.
     */
    boolean isStudentThrifty(String studentName);
}
