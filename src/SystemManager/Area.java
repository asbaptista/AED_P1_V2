package SystemManager;

import Exceptions.*;
import Services.*;
import Students.*;
import dataStructures.*;


/**
 * Interface for a geographic SystemManager.SystemManager.Area.
 * Defines the contract for managing all services and students
 * within a specific set of geographic boundaries.
 * Extends {@link AreaReadOnly} to provide mutable operations.
 */
public interface Area extends AreaReadOnly {

    /**
     * Gets a service by name.
     * Overridden to return mutable Service type.
     *
     * @param name The name of the service to find.
     * @return The {@link Service} object, or null if not found.
     */
    @Override
    Service getService(String name);

    /**
     * Gets an iterator over all services in insertion order.
     * Overridden to return mutable Service type.
     *
     * @return An {@link Iterator} of {@link Service}s.
     */
    @Override
    Iterator<Service> getServices();

    /**
     * Gets an iterator over all services sorted by star rating.
     * Overridden to return mutable Service type.
     *
     * @return A sorted {@link Iterator} of {@link Service}s.
     */
    @Override
    Iterator<Service> getRankedServices();

    /**
     * Gets services of a specific type with a specific star rating.
     * Overridden to return mutable Service type.
     *
     * @param type The service type to filter by.
     * @param stars The star rating to filter by.
     * @return An {@link Iterator} of {@link Service}s.
     */
    @Override
    Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars);

    /**
     * Gets services of a specific type ordered by stars.
     * Overridden to return mutable Service type.
     *
     * @param type The service type to filter by.
     * @return An {@link Iterator} of {@link Service}s.
     */
    @Override
    Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type);

    /**
     * Gets a student by name.
     * Overridden to return mutable Student type.
     *
     * @param name The name of the student to find.
     * @return The {@link Student} object, or null if not found.
     */
    @Override
    Student getStudent(String name);

    /**
     * Lists all students alphabetically by name.
     * Overridden to return mutable Student type.
     *
     * @return An {@link Iterator} of {@link Student}s.
     */
    @Override
    Iterator<Student> listAllStudents();

    /**
     * Lists students from a specific country in registration order.
     * Overridden to return mutable Student type.
     *
     * @param country The country to filter by.
     * @return An {@link Iterator} of {@link Student}s.
     */
    @Override
    Iterator<Student> listStudentsByCountry(String country);

    /**
     * Adds a new service to the area.
     *
     * @param service The {@link Service} to add.
     * @throws ServiceAlreadyExistsException if a service with this name already exists.
     */
    void addService(Service service) throws ServiceAlreadyExistsException;

    /**
     * Creates and adds a new service to the area.
     *
     * @param type The type of service to create.
     * @param name The name of the service.
     * @param lat The latitude.
     * @param lon The longitude.
     * @param price The price.
     * @param value The value (capacity or discount).
     * @throws ServiceAlreadyExistsException if a service with this name already exists.
     * @throws InvalidMenuPriceException if the menu price is invalid.
     * @throws InvalidRoomPriceException if the room price is invalid.
     * @throws InvalidTicketPriceException if the ticket price is invalid.
     * @throws InvalidDiscountPriceException if the discount price is invalid.
     * @throws InvalidCapacityException if the capacity is invalid.
     */
    void createAndAddService(ServiceType type, String name, long lat, long lon, int price, int value)
            throws ServiceAlreadyExistsException, InvalidMenuPriceException,
            InvalidRoomPriceException, InvalidTicketPriceException,
            InvalidDiscountPriceException, InvalidCapacityException;

    /**
     * Updates a service's position in the star-based ranking.
     *
     * @param service The service whose ranking needs updating.
     * @param oldStars The previous star rating.
     */
    void updateRankingByStars(Service service, int oldStars);

    /**
     * Gets the internal services collection for direct access.
     *
     * @return The {@link ServiceCollection} instance.
     */
    ServiceCollection getServicesCollection();

    /**
     * Adds a new student to the area.
     *
     * @param student The {@link Student} to add.
     * @throws StudentAlreadyExistsException if a student with this name already exists.
     */
    void addStudent(Student student) throws StudentAlreadyExistsException;

    /**
     * Removes a student from the area.
     *
     * @param name The name of the student to remove.
     */
    void removeStudent(String name);

    /**
     * Adds a review to a service and updates its ranking.
     *
     * @param serviceName The name of the service.
     * @param rating The star rating (1-5).
     * @param comment The review comment.
     * @return true if the service's star rating changed, false otherwise.
     */
    void addReviewToService(String serviceName, int rating, String comment);

    /**
     * Removes a student from the area, handling all cleanup
     * (removing from current service occupancy, etc.).
     *
     * @param studentName The name of the student to remove.
     */
    void removeStudentWithCleanup(String studentName);

    /**
     * Moves a student to a new service location.
     *
     * @param studentName The name of the student.
     * @param serviceName The name of the service to move to.
     */
    void moveStudentToService(String studentName, String serviceName) throws Exceptions.AlreadyThereException, Exceptions.NotValidServiceException, Exceptions.EatingIsFullException;

    /**
     * Moves a student's home to a new lodging.
     *
     * @param studentName The name of the student.
     * @param lodgingName The name of the new lodging.
     */
    void moveStudentHome(String studentName, String lodgingName) throws Exceptions.AlreadyStudentHomeException, Exceptions.LodgingIsFullException, Exceptions.StudentIsThriftyException;

    /**
     * Gets the current location of a student.
     *
     * @param studentName The name of the student.
     * @return The service where the student is currently located.
     */
    Service getStudentCurrentLocation(String studentName);

    /**
     * Gets the current location of a student.
     *
     * @param student The student object.
     * @return The service where the student is currently located.
     */
    Service getStudentCurrentLocation(StudentReadOnly student);

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
    Iterator<Service> getStudentVisitedLocations(String studentName);

    /**
     * Gets an iterator over occupants of a service (Eating or Lodging).
     *
     * @param serviceName The name of the service.
     * @return A two-way iterator over students in that service.
     */
    TwoWayIterator<Student> getServiceOccupants(String serviceName);

    /**
     * Finds the most relevant service for a student based on their criteria.
     *
     * @param studentName The name of the student.
     * @param serviceType The type of service to search for.
     * @return The most relevant service for that student.
     */
    Service findRelevantServiceForStudent(String studentName, ServiceType serviceType);

    /**
     * Gets the closest services to a student's current location
     * that match a specific type and star rating.
     *
     * @param studentName The name of the student.
     * @param type The service type.
     * @param stars The star rating.
     * @return An iterator of the closest matching services.
     */
    Iterator<Service> getClosestServicesByTypeAndStars(String studentName, ServiceType type, int stars);

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

    /**
     * Adds a student to the area with the specified lodging as home.
     *
     * @param type The type of student.
     * @param name The name of the student.
     * @param country The student's country.
     * @param lodgingName The name of the lodging service.
     */
    void addStudentWithLodging(StudentType type, String name, String country, String lodgingName)
            throws StudentAlreadyExistsException, Exceptions.LodgingIsFullException;

    /**
     * Gets the name of a service.
     *
     * @param service The service object.
     * @return The service's name, or null if not found.
     */
    String getServiceNameProperty(ServiceReadOnly service);

    /**
     * Gets the type of a service.
     *
     * @param service The service object.
     * @return The service's type, or null if not found.
     */
    ServiceType getServiceTypeProperty(ServiceReadOnly service);

    /**
     * Gets the latitude of a service.
     *
     * @param service The service object.
     * @return The service's latitude, or 0 if not found.
     */
    long getServiceLatitudeProperty(ServiceReadOnly service);

    /**
     * Gets the longitude of a service.
     *
     * @param service The service object.
     * @return The service's longitude, or 0 if not found.
     */
    long getServiceLongitudeProperty(ServiceReadOnly service);

    /**
     * Gets the name of a student.
     *
     * @param student The student object.
     * @return The student's name, or null if not found.
     */
    String getStudentNameProperty(StudentReadOnly student);

    /**
     * Gets the type of a student.
     *
     * @param student The student object.
     * @return The student's type, or null if not found.
     */
    StudentType getStudentTypeProperty(StudentReadOnly student);

}