import Exceptions.*;
import Services.Service;
import Services.ServiceType;
import Students.Student;
import Students.StudentType;
import dataStructures.Iterator;
import dataStructures.TwoWayIterator;

/**
 * Main interface for the 'Home Away From Home' system.
 * <p>
 * This interface acts as a **Facade** [a design pattern], defining the complete
 * set of operations the application can perform. It manages the lifecycle
 * of geographic areas, services, and students, and handles all interactions
 * between them. It defines the contract for all business logic, including
 * the exceptions that can be thrown by each operation.
 */
public interface SystemManager {

    // --- Area Lifecycle Management ---

    /**
     * Creates a new geographic area with the specified boundaries.
     * If an area is currently loaded, it is saved first.
     *
     * @param name      The name for the new area.
     * @param topLat    The top latitude of the bounding box.
     * @param leftLong  The left longitude of the bounding box.
     * @param bottomLat The bottom latitude of the bounding box.
     * @param rightLong The right longitude of the bounding box.
     * @throws InvalidBoundsException      if the coordinates do not form a valid rectangle.
     * @throws BoundsAlreadyExistsException if an area with this name or these exact bounds
     * already exists.
     */
    void createArea(String name, long topLat, long leftLong, long bottomLat, long rightLong)
            throws InvalidBoundsException, BoundsAlreadyExistsException;

    /**
     * Loads a previously saved area from persistent storage.
     * If an area is currently loaded, it is saved first.
     *
     * @param name The name of the area to load.
     * @throws BoundsNotFoundException if no saved area with the given name is found.
     */
    void loadArea(String name) throws BoundsNotFoundException;

    /**
     * Saves the currently loaded area to persistent storage.
     *
     * @throws NoAreaLoadedException if there is no area currently loaded to save.
     */
    void saveArea() throws NoAreaLoadedException;

    /**
     * Gets the currently active area.
     *
     * @return The currently loaded {@link Area} object.
     * @throws NoAreaLoadedException if no area is currently loaded.
     */
    Area getCurrentArea() throws NoAreaLoadedException;

    /**
     * Checks if the given bounds match the currently loaded area's bounds.
     *
     * @param topLat    The top latitude to check.
     * @param leftLong  The left longitude to check.
     * @param bottomLat The bottom latitude to check.
     * @param rightLong The right longitude to check.
     * @return {@code true} if the bounds are identical, {@code false} otherwise.
     */
    boolean equalBounds(long topLat, long leftLong, long bottomLat, long rightLong);


    // --- Service Management ---

    /**
     * Adds a new service to the currently loaded area.
     *
     * @param type  The {@link ServiceType} (EATING, LODGING, LEISURE).
     * @param name  The name of the new service.
     * @param lat   The latitude of the service.
     * @param lon   The longitude of the service.
     * @param price The price (menu, room, or ticket).
     * @param value The value (capacity or discount).
     * @throws InvalidServiceTypeException     if the type is not valid.
     * @throws InvalidLocationException        if the coordinates are outside the area bounds.
     * @throws SystemBoundsNotDefinedException (This should be checked by the caller via `getCurrentArea`).
     * @throws InvalidMenuPriceException       if price <= 0 for Eating.
     * @throws InvalidRoomPriceException       if price <= 0 for Lodging.
     * @throws InvalidTicketPriceException     if price <= 0 for Leisure.
     * @throws InvalidDiscountPriceException   if discount is not 0-100 for Leisure.
     * @throws InvalidCapacityException        if capacity <= 0 for Eating or Lodging.
     * @throws ServiceAlreadyExistsException   if a service with this name already exists.
     */
    void addService(ServiceType type, String name, long lat, long lon, int price, int value)
            throws InvalidServiceTypeException, InvalidLocationException,
            InvalidBoundsException, SystemBoundsNotDefinedException,
            InvalidMenuPriceException, InvalidRoomPriceException,
            InvalidTicketPriceException, InvalidDiscountPriceException,
            InvalidCapacityException, ServiceAlreadyExistsException;

    /**
     * Gets an iterator for all services in the current area, in insertion order
     *.
     *
     * @return An {@link Iterator} of {@link Service}s.
     * @throws NoServicesException if there are no services in the area.
     */
    Iterator<Service> listServices() throws NoServicesException;

    /**
     * Adds a user review (rating and comment) to a specific service.
     * This will trigger an update of the service's average star rating.
     *
     * @param serviceName The name of the service to review.
     * @param rating      The star rating (1-5).
     * @param comment     The text description of the review.
     * @throws ServiceNotFoundException if the service does not exist.
     * @throws InvalidStarsException    if the rating is not between 1 and 5.
     */
    void addReviewToService(String serviceName, int rating, String comment)
            throws ServiceNotFoundException, InvalidStarsException;

    /**
     * Gets an iterator for all services, sorted by star rating (descending)
     *.
     *
     * @return A sorted {@link Iterator} of {@link Service}s.
     */
    Iterator<Service> getRankedServices();

    /**
     * Gets the total number of services in the current area.
     *
     * @return The count of services.
     */
    int getNumberOfServices();

    /**
     * Finds a service by its name.
     *
     * @param name The name of the service to find (case-insensitive).
     * @return The {@link Service} object, or {@code null} if not found.
     */
    Service getServiceByName(String name);


    // --- Student Management ---

    /**
     * Adds a new student to the currently loaded area.
     *
     * @param type        The {@link StudentType} (BOOKISH, OUTGOING, THRIFTY).
     * @param name        The name of the new student.
     * @param country     The student's country of origin.
     * @param lodgingName The name of the lodging service where the student will live.
     * @throws SystemBoundsNotDefinedException (This should be checked by the caller).
     * @throws InvalidStudentTypeException     if the type is not valid.
     * @throws LodgingNotFoundException        if the specified lodging does not exist.
     * @throws StudentAlreadyExistsException   if a student with this name already exists.
     * @throws LodgingIsFullException          if the specified lodging is at capacity.
     */
    void addStudent(StudentType type, String name, String country, String lodgingName)
            throws SystemBoundsNotDefinedException, InvalidStudentTypeException,
            LodgingNotFoundException, StudentAlreadyExistsException, LodgingIsFullException;

    /**
     * Lists students based on a filter.
     * <ul>
     * <li>If filter is "all", lists all students alphabetically.</li>
     * <li>If filter is a country name, lists students from that country
     * by registration order.</li>
     * </ul>
     *
     * @param filter The filter string ("all" or a country name).
     * @return An {@link Iterator} of {@link Student}s.
     */
    Iterator<Student> listStudents(String filter);

    /**
     * Removes a student from the system.
     *
     * @param name The name of the student to remove.
     * @throws StudentNotFoundException if the student does not exist.
     */
    void removeStudent(String name) throws StudentNotFoundException;

    /**
     * Finds a student by their name.
     *
     * @param name The name of the student to find (case-insensitive).
     * @return The {@link Student} object, or {@code null} if not found.
     */
    Student getStudentByName(String name);


    // --- Student & Service Interaction (Actions) ---

    /**
     * Moves a student to a new location (an Eating or Leisure service).
     *
     * @param studentName The name of the student to move.
     * @param serviceName The name of the destination service.
     * @throws StudentNotFoundException   if the student does not exist.
     * @throws ServiceNotFoundException   if the service does not exist.
     * @throws AlreadyThereException      if the student is already at that service.
     * @throws EatingIsFullException      if the service is an Eating service at capacity.
     * @throws NotValidServiceException   if the service is not Eating or Leisure (e.g., Lodging).
     */
    void goToLocation(String studentName, String serviceName)
            throws StudentNotFoundException, ServiceNotFoundException,
            AlreadyThereException, EatingIsFullException, NotValidServiceException;

    /**
     * Changes a student's designated home to a new lodging.
     * This also moves the student to the new home.
     *
     * @param studentName The name of the student.
     * @param lodgingName The name of the new lodging service.
     * @throws StudentNotFoundException   if the student does not exist.
     * @throws LodgingNotFoundException   if the lodging service does not exist.
     * @throws LodgingIsFullException     if the new lodging is at capacity.
     * @throws StudentIsThriftyException  if a Thrifty student tries to move to a
     * more expensive lodging.
     * @throws AlreadyStudentHomeException if the student already lives there.
     */
    void moveStudentHome(String studentName, String lodgingName)
            throws StudentNotFoundException, LodgingNotFoundException,
            LodgingIsFullException, StudentIsThriftyException, AlreadyStudentHomeException;


    // --- System Queries (Reports) ---

    /**
     * Gets the current location (Service) of a specific student.
     *
     * @param studentName The name of the student.
     * @return The {@link Service} where the student is currently located.
     * @throws StudentNotFoundException if the student does not exist.
     */
    Service whereIsStudent(String studentName) throws StudentNotFoundException;

    /**
     * Checks if a student (assumed Thrifty) is "distracted" by visiting
     * an Eating service that is more expensive than their known cheapest
     *.
     *
     * @param studentName The name of the student.
     * @param serviceName The name of the Eating service.
     * @return {@code true} if the student is Thrifty and the service is
     * more expensive than their cheapest, {@code false} otherwise.
     */
    boolean isStudentDistracted(String studentName, String serviceName);

    /**
     * Lists the locations stored as "visited" by a student.
     *
     * @param studentName The name of the student.
     * @return An {@link Iterator} of visited {@link Service}s.
     * @throws StudentNotFoundException    if the student does not exist.
     * @throws StudentIsThriftyException   if the student is Thrifty (they don't
     * store locations).
     * @throws NoVisitedLocationsException if the student has not stored any visits.
     */
    Iterator<Service> listVisitedLocations(String studentName)
            throws StudentNotFoundException, StudentIsThriftyException,
            NoVisitedLocationsException;

    /**
     * Lists all students currently at a specific service (Eating or Lodging).
     *
     * @param order       The sort order: ">" (oldest to newest) or "<" (newest to oldest)
     * based on insertion.
     * @param serviceName The name of the service.
     * @return A {@link TwoWayIterator} of {@link Student}s.
     * @throws InvalidOrderException                  if the order string is not ">" or "<".
     * @throws ServiceNotFoundException               if the service does not exist.
     * @throws ServiceDoesNotControlEntryExitException if the service is not Eating or Lodging.
     */
    TwoWayIterator<Student> listUsersInService(String order, String serviceName)
            throws InvalidOrderException, ServiceNotFoundException,
            ServiceDoesNotControlEntryExitException;

    /**
     * Lists all services that have at least one review containing the specified tag (word).
     *
     * @param tag The tag to search for (case-insensitive).
     * @return An {@link Iterator} of {@link Service}s that have the tag.
     */
    Iterator<Service> listServicesWithTag(String tag);

    /**
     * Finds the service(s) of a given type and star rating that are closest
     * to a student's current location, using Manhattan distance
     *.
     *
     * @param type        The {@link ServiceType} to filter by.
     * @param stars       The average star rating to filter by.
     * @param studentName The name of the student (for their location).
     * @return An {@link Iterator} of the closest matching {@link Service}s.
     * @throws InvalidStarsException           if stars are not 1-5.
     * @throws StudentNotFoundException        if the student does not exist.
     * @throws NoTypeServicesWithStarsException if no services match type and stars.
     * @throws InvalidServiceTypeException     (Should not be thrown if using Enum).
     * @throws NoServicesOfThisTypeException   if no services match the type.
     */
    Iterator<Service> getRankedServicesByTypeAndStars(ServiceType type, int stars, String studentName)
            throws InvalidStarsException, StudentNotFoundException,
            NoTypeServicesWithStarsException, InvalidServiceTypeException, NoServicesOfThisTypeException;

    /**
     * Finds the "most relevant" service of a given type for a specific student.
     * <ul>
     * <li>For Thrifty students: cheapest price.</li>
     * <li>For Bookish/Outgoing: highest star rating.</li>
     * </ul>
     *
     * @param studentName The name of the student.
     * @param serviceType The desired {@link ServiceType}.
     * @return The single most relevant {@link Service}.
     * @throws StudentNotFoundException      if the student does not exist.
     * @throws InvalidServiceTypeException   (Should not be thrown if using Enum).
     * @throws NoServicesOfThisTypeException if no services of that type exist.
     */
    Service findRelevantServiceForStudent(String studentName, ServiceType serviceType)
            throws StudentNotFoundException, InvalidServiceTypeException, NoServicesOfThisTypeException;


    // --- Property Getters (Convenience) ---

    /**
     * Gets the top latitude of a given area.
     * @param area The area.
     * @return The top latitude.
     */
    long getTopLat(Area area);
    /**
     * Gets the left longitude of a given area.
     * @param area The area.
     * @return The left longitude.
     */
    long getLeftLong(Area area);
    /**
     * Gets the bottom latitude of a given area.
     * @param area The area.
     * @return The bottom latitude.
     */
    long getBottomLat(Area area);
    /**
     * Gets the right longitude of a given area.
     * @param area The area.
     * @return The right longitude.
     */
    long getRightLong(Area area);
    /**
     * Gets the name of a given area.
     * @param area The area.
     * @return The area's name.
     */
    String getName(Area area);

    /**
     * Gets the name of a given service.
     * @param service The service.
     * @return The service's name.
     */
    String getServiceName(Service service);
    /**
     * Gets the type of a given service.
     * @param service The service.
     * @return The service's {@link ServiceType}.
     */
    ServiceType getServiceType(Service service);
    /**
     * Gets the latitude of a given service.
     * @param service The service.
     * @return The service's latitude.
     */
    long getServiceLatitude(Service service);
    /**
     * Gets the longitude of a given service.
     * @param service The service.
     * @return The service's longitude.
     */
    long getServiceLongitude(Service service);

    /**
     * Gets the name of a given student.
     * @param student The student.
     * @return The student's name.
     */
    String getStudentName(Student student);
    /**
     * Gets the type of a given student.
     * @param student The student.
     * @return The student's {@link StudentType}.
     */
    StudentType getStudentType(Student student);
    /**
     * Gets the current location of a given student.
     * @param student The student.
     * @return The {@link Service} where the student is.
     */
    Service getStudentCurrentLocation(Student student);


    // --- Utility Methods ---

    /**
     * Calculates the Manhattan distance between two sets of coordinates
     *.
     *
     * @param lat1 Latitude of point 1.
     * @param lon1 Longitude of point 1.
     * @param lat2 Latitude of point 2.
     * @param lon2 Longitude of point 2.
     * @return The total distance: |lat1 - lat2| + |lon1 - lon2|.
     */
    long manhattanDistance(long lat1, long lon1, long lat2, long lon2);

}