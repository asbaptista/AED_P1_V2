package SystemManager;

import Exceptions.*;
import Services.*;
import Students.*;
import dataStructures.*;


import java.io.*;

/**
 * Implements the {@link Area} interface.
 * This class represents a specific geographic area defined by a bounding box
 * and manages the collections of all {@link Student}s and {@link Service}s
 * within that area. This class is serializable and handles student/service operations.
 */
public class AreaImpl implements Area, Serializable {
    /**
     * The name of the geographic area.
     */
    String name;

    /**
     * The top latitude coordinate of the bounding box.
     */
    long topLat;

    /**
     * The bottom latitude coordinate of the bounding box.
     */
    long bottomLat;

    /**
     * The left longitude coordinate of the bounding box.
     */
    long leftLong;

    /**
     * The right longitude coordinate of the bounding box.
     */
    long rightLong;

    /**
     * Collection responsible for managing all students in this area.
     */
    StudentsCollectionImpl students;

    /**
     * Collection responsible for managing all services in this area.
     */
    ServicesCollectionImpl services;


    /**
     * Constructs a new SystemManager.SystemManager.Area.
     *
     * @param name      The name for the area.
     * @param topLat    The top latitude coordinate.
     * @param leftLong  The left longitude coordinate.
     * @param bottomLat The bottom latitude coordinate.
     * @param rightLong The right longitude coordinate.
     */
    public AreaImpl(String name, long topLat, long leftLong, long bottomLat, long rightLong) {
        this.name = name;
        this.topLat = topLat;
        this.leftLong = leftLong;
        this.bottomLat = bottomLat;
        this.rightLong = rightLong;
        this.students = new StudentsCollectionImpl();
        this.services = new ServicesCollectionImpl();
    }


    /**
     * Gets the name of the area.
     *
     * @return The area's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the top latitude of the area's bounding box.
     *
     * @return The top latitude coordinate.
     */
    @Override
    public long getTopLat() {
        return topLat;
    }

    /**
     * Gets the left longitude of the area's bounding box.
     *
     * @return The left longitude coordinate.
     */
    @Override
    public long getLeftLong() {
        return leftLong;
    }

    /**
     * Gets the bottom latitude of the area's bounding box.
     *
     * @return The bottom latitude coordinate.
     */
    @Override
    public long getBottomLat() {
        return bottomLat;
    }

    /**
     * Gets the right longitude of the area's bounding box.
     *
     * @return The right longitude coordinate.
     */
    @Override
    public long getRightLong() {
        return rightLong;
    }

    /**
     * Checks if a given coordinate (latitude, longitude) is
     * inside the area's defined bounding box.
     *
     * @param lat The latitude to check.
     * @param lon The longitude to check.
     * @return true if the coordinate is within bounds, false otherwise.
     */
    @Override
    public boolean isWithinBounds(long lat, long lon) {
        return lat <= topLat && lat >= bottomLat &&
                lon >= leftLong && lon <= rightLong;
    }


    /**
     * Adds a new service to the area's service collection.
     *
     * @param service The {@link Service} to add.
     */
    @Override
    public void addService(Service service)throws ServiceAlreadyExistsException {
        services.add(service);
    }

    /**
     * Gets an iterator over all services in the area, in order of insertion
     *.
     *
     * @return An {@link Iterator} of {@link Service}s.
     */
    @Override
    public Iterator<Service> getServices() {
        return services.listServices();
    }

    /**
     * Checks if a service with the given name already exists in the area.
     *
     * @param name The name to check (case-insensitive).
     * @return true if the service exists, false otherwise.
     */
    @Override
    public boolean containsService(String name) {
        return services.contains(name);
    }

    /**
     * Finds and returns a service by its name.
     *
     * @param name The name of the service to find (case-insensitive).
     * @return The {@link Service} object, or null if not found.
     */
    @Override
    public Service getService(String name) {
        return services.findByName(name);
    }

    /**
     * Gets the total number of services registered in the area.
     *
     * @return The count of services.
     */
    @Override
    public int getNumberOfServices() {
        return services.size();
    }

    /**
     * Gets the internal service collection for direct access.
     * Used for advanced operations like tag indexing.
     *
     * @return The {@link ServiceCollection} instance.
     */
    @Override
    public ServiceCollection getServicesCollection() {
        return services;
    }

    /**
     * Gets an iterator over all services, sorted by their
     * average star rating in descending order.
     *
     * @return A sorted {@link Iterator} of {@link Service}s.
     */
    @Override
    public Iterator<Service> getRankedServices() {
        return services.getServicesByStars();
    }

    /**
     * Notifies the service collection that a service's star rating has changed,
     * so its position in the ranked list can be updated.
     *
     * @param service The service whose ranking needs to be updated.
     * @param oldStars The previous star rating before the update.
     */
    @Override
    public void updateRankingByStars(Service service, int oldStars) {
        services.updateRankingByStars(service, oldStars);
    }

    /**
     * Gets an iterator over services of a specific type with a specific star rating.
     * Delegates to the services collection's optimized index.
     *
     * @param type The service type to filter by.
     * @param stars The star rating to filter by.
     * @return An {@link Iterator} of {@link Service}s matching the criteria.
     */
    @Override
    public Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars) {
        return services.getServicesByTypeAndStars(type, stars);
    }

    /**
     * Adds a new student to the area's student collection.
     *
     * @param student The {@link Student} to add.
     */
    @Override
    public void addStudent(Student student)throws StudentAlreadyExistsException {
        students.addStudent(student);
    }

    /**
     * Creates and adds a new service to the area.
     *
     * @param type  The type of service to create.
     * @param name  The name of the service.
     * @param lat   The latitude.
     * @param lon   The longitude.
     * @param price The price.
     * @param value The value (capacity or discount).
     * @throws ServiceAlreadyExistsException     if a service with this name already exists.
     * @throws InvalidMenuPriceException     if the menu price is invalid.
     * @throws InvalidRoomPriceException     if the room price is invalid.
     * @throws InvalidTicketPriceException   if the ticket price is invalid.
     * @throws InvalidDiscountPriceException if the discount price is invalid.
     * @throws InvalidCapacityException      if the capacity is invalid.
     */
    @Override
    public void createAndAddService(ServiceType type, String name, long lat, long lon, int price, int value)
            throws ServiceAlreadyExistsException, InvalidMenuPriceException,
            InvalidRoomPriceException, InvalidTicketPriceException,
            InvalidDiscountPriceException, InvalidCapacityException {
        Service service = createService(name, lat, lon, price, type, value);
        addService(service);
    }

    /**
     * Factory method to create a new {@link Service} instance based on its type.
     *
     * @param name  The name of the service.
     * @param lat   The latitude.
     * @param lon   The longitude.
     * @param price The price.
     * @param type  The {@link ServiceType} enum.
     * @param value The value (capacity or discount).
     * @return A new {@link Service} (e.g., EatingImpl, LodgingImpl, LeisureImpl).
     * @throws InvalidMenuPriceException     if the menu price is invalid.
     * @throws InvalidRoomPriceException     if the room price is invalid.
     * @throws InvalidTicketPriceException   if the ticket price is invalid.
     * @throws InvalidCapacityException      if the capacity is invalid.
     * @throws InvalidDiscountPriceException if the discount price is invalid.
     */
    private Service createService(String name, long lat, long lon, int price, ServiceType type, int value)
            throws InvalidMenuPriceException, InvalidRoomPriceException,
            InvalidTicketPriceException, InvalidCapacityException,
            InvalidDiscountPriceException {
        return switch (type) {
            case EATING -> new EatingImpl(name, lat, lon, price, value);
            case LODGING -> new LodgingImpl(name, lat, lon, price, value);
            case LEISURE -> new LeisureImpl(name, lat, lon, price, value);
        };
    }

    /**
     * Finds and returns a student by their name.
     *
     * @param name The name of the student to find (case-insensitive).
     * @return The {@link Student} object, or null if not found.
     */
    @Override
    public Student getStudent(String name) {
        return students.findByName(name);
    }

    /**
     * Checks if a student with the given name exists in the area.
     *
     * @param name The name to check (case-insensitive).
     * @return true if the student exists, false otherwise.
     */
    @Override
    public boolean containsStudent(String name) {
        return students.findByName(name) != null;
    }

    /**
     * Removes a student from the area's student collection.
     * <p>
     * Note: The {@code SystemManager.SystemManager} is responsible for handling any
     * side effects *before* calling this method (e.g., removing the
     * student from their current service's occupant list).
     *
     * @param name The name of the student to remove.
     */
    @Override
    public void removeStudent(String name) {
        students.removeStudent(name);
    }

    /**
     * Gets an iterator over all students in the area,
     * sorted alphabetically by name.
     *
     * @return A sorted {@link Iterator} of {@link Student}s.
     */
    @Override
    public Iterator<Student> listAllStudents() {
        return students.listAllStudents();
    }

    /**
     * Gets an iterator over students from a specific country,
     * in order of registration.
     *
     * @param filter The country name to filter by.
     * @return An {@link Iterator} of {@link Student}s from that country.
     */
    @Override
    public Iterator<Student> listStudentsByCountry(String filter) {
        return students.listStudentsByCountry(filter);
    }

    /**
     * Gets an iterator over services of a specific type ordered by star rating.
     *
     * @param type The service type to filter by.
     * @return An {@link Iterator} of {@link Service}s sorted by stars.
     */
    @Override
    public Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type) {
        return services.getServicesByTypeOrderedByStars(type);
    }

    /**
     * Checks if there are any services of a specific type in the area.
     *
     * @param type The service type to check for.
     * @return true if there are services of that type, false otherwise.
     */
    @Override
    public boolean hasServicesOfType(ServiceType type) {
        return services.hasServicesOfType(type);
    }

    /**
     * Adds a review to a service and updates its ranking.
     *
     * @param serviceName The name of the service.
     * @param rating      The star rating (1-5).
     * @param comment     The review comment.
     */
    @Override
    public void addReviewToService(String serviceName, int rating, String comment) {
        Service service = getService(serviceName);
        int oldAvgStar = service.getAvgStar();
        service.addReview(rating, comment);
        int newAvgStar = service.getAvgStar();
        if (newAvgStar != oldAvgStar) {
            updateRankingByStars(service, oldAvgStar);
        }
    }

    /**
     * Removes a student from the area, handling all cleanup
     * (removing from current service occupancy, etc.).
     *
     * @param studentName The name of the student to remove.
     */
    @Override
    public void removeStudentWithCleanup(String studentName) {
        Student student = getStudent(studentName);
        Service current = student.getCurrent();
        if (current instanceof Eating eating) {
            eating.removeOccupant(student);
        } else if (current instanceof Lodging lodging) {
            lodging.removeOccupant(student);
        }
        removeStudent(studentName);
    }

    /**
     * Moves a student to a new service location.
     *
     * @param studentName The name of the student.
     * @param serviceName The name of the service to move to.
     */
    @Override
    public void moveStudentToService(String studentName, String serviceName)
            throws AlreadyThereException, NotValidServiceException, EatingIsFullException {
        Student student = getStudent(studentName);
        Service service = getService(serviceName);
        student.goToLocation(service);
    }

    /**
     * Moves a student's home to a new lodging.
     *
     * @param studentName The name of the student.
     * @param lodgingName The name of the new lodging.
     */
    @Override
    public void moveStudentHome(String studentName, String lodgingName)
            throws AlreadyStudentHomeException, LodgingIsFullException, StudentIsThriftyException {
        Student student = getStudent(studentName);
        Service service = getService(lodgingName);
        student.moveHome(((Lodging) service));
    }

    /**
     * Gets the current location of a student.
     *
     * @param studentName The name of the student.
     * @return The service where the student is currently located.
     */
    @Override
    public Service getStudentCurrentLocation(String studentName) {
        Student student = getStudent(studentName);
        return student.getCurrent();
    }

    /**
     * Gets the current location of a student.
     *
     * @param student The student object.
     * @return The service where the student is currently located.
     */
    @Override
    public Service getStudentCurrentLocation(StudentReadOnly student) {
        return getStudentCurrentLocation(student.getName());
    }

    /**
     * Checks if a thrifty student is distracted at an eating service.
     *
     * @param studentName The name of the student.
     * @param serviceName The name of the service.
     * @return true if the student is thrifty and distracted at that eating service.
     */
    @Override
    public boolean isStudentDistracted(String studentName, String serviceName) {
        Student student = getStudent(studentName);
        Service service = getService(serviceName);
        return student instanceof Thrifty thrifty
                && service instanceof Eating eating
                && thrifty.isDistracted(eating);
    }

    /**
     * Gets an iterator over the services a student has visited.
     *
     * @param studentName The name of the student.
     * @return An iterator of visited services.
     */
    @Override
    public Iterator<Service> getStudentVisitedLocations(String studentName) {
          return getStudent(studentName).getVisitedIterator();
    }

    /**
     * Gets an iterator over occupants of a service (Eating or Lodging).
     *
     * @param serviceName The name of the service.
     * @return A two-way iterator over students in that service.
     */
    @Override
    public TwoWayIterator<Student> getServiceOccupants(String serviceName) {
        Service service = getService(serviceName);
        if (service instanceof Eating eating) {
            return eating.getOccupantsIterator();
        } else if (service instanceof Lodging lodging) {
            return lodging.getOccupantsIterator();
        }
        return null;
    }

    /**
     * Finds the most relevant service for a student based on their criteria.
     *
     * @param studentName The name of the student.
     * @param serviceType The type of service to search for.
     * @return The most relevant service for that student.
     */
    @Override
    public Service findRelevantServiceForStudent(String studentName, ServiceType serviceType) {
        Student student = getStudent(studentName);
        Iterator<Service> typeServicesIterator = getServicesByTypeOrderedByStars(serviceType);
        return student.findMostRelevant(typeServicesIterator);
    }

    /**
     * Gets the closest services to a student's current location
     * that match a specific type and star rating.
     *
     * @param studentName The name of the student.
     * @param type        The service type.
     * @param stars       The star rating.
     * @return An iterator of the closest matching services.
     */
    @Override
    public Iterator<Service> getClosestServicesByTypeAndStars(String studentName, ServiceType type, int stars) {
        Student student = getStudent(studentName);

        Iterator<Service> filteredByTypeStars = getServicesByTypeAndStars(type, stars);
        List<Service> closestServices = new DoublyLinkedList<>();
        long minDistance = Long.MAX_VALUE;

        while (filteredByTypeStars.hasNext()) {
            Service service = filteredByTypeStars.next();
            long currentDistance = calculateManhattanDistance(
                    student.getCurrent().getLatitude(),
                    student.getCurrent().getLongitude(),
                    service.getLatitude(),
                    service.getLongitude());

            if (currentDistance < minDistance) {
                closestServices = new DoublyLinkedList<>();
                closestServices.addLast(service);
                minDistance = currentDistance;
            } else if (currentDistance == minDistance) {
                closestServices.addLast(service);
            }
        }

        return closestServices.iterator();
    }

    /**
     * Calculates the Manhattan distance between two coordinates.
     *
     * @param lat1 First latitude.
     * @param lon1 First longitude.
     * @param lat2 Second latitude.
     * @param lon2 Second longitude.
     * @return The Manhattan distance.
     */
    private long calculateManhattanDistance(long lat1, long lon1, long lat2, long lon2) {
        return Math.abs(lat1 - lat2) + Math.abs(lon1 - lon2);
    }

    /**
     * Checks if a service is a lodging service.
     *
     * @param serviceName The name of the service.
     * @return true if the service is a lodging, false otherwise.
     */
    @Override
    public boolean isLodgingService(String serviceName) {
        Service service = getService(serviceName);
        return service instanceof Lodging;
    }

    /**
     * Checks if a service controls entry/exit (Eating or Lodging).
     *
     * @param serviceName The name of the service.
     * @return true if the service is Eating or Lodging, false otherwise.
     */
    @Override
    public boolean isServiceWithOccupancy(String serviceName) {
        Service service = getService(serviceName);
        return service instanceof Eating || service instanceof Lodging;
    }

    /**
     * Checks if a student is of type Thrifty.
     *
     * @param studentName The name of the student.
     * @return true if the student is Thrifty, false otherwise.
     */
    @Override
    public boolean isStudentThrifty(String studentName) {
        Student student = getStudent(studentName);
        return student instanceof Thrifty;
    }

    /**
     * Adds a student to the area with the specified lodging as home.
     *
     * @param type        The type of student.
     * @param name        The name of the student.
     * @param country     The student's country.
     * @param lodgingName The name of the lodging service.
     */
    @Override
    public void addStudentWithLodging(StudentType type, String name, String country, String lodgingName)
            throws StudentAlreadyExistsException, LodgingIsFullException {
        Service service = getService(lodgingName);
        if (service instanceof Lodging lodging) {
            Student student = createStudentByType(type, name, country, lodging);
            addStudent(student);
        }
    }

    /**
     * Gets the name of a service.
     *
     * @param service The service object.
     * @return The service's name, or null if not found.
     */
    @Override
    public String getServiceNameProperty(ServiceReadOnly service) {
        return service.getName();
    }

    /**
     * Gets the type of a service.
     *
     * @param service The service object.
     * @return The service's type, or null if not found.
     */
    @Override
    public ServiceType getServiceTypeProperty(ServiceReadOnly service) {
        return service.getType();
    }

    /**
     * Gets the latitude of a service.
     *
     * @param service The service object.
     * @return The service's latitude, or 0 if not found.
     */
    @Override
    public long getServiceLatitudeProperty(ServiceReadOnly service) {
        return service.getLatitude();
    }

    /**
     * Gets the longitude of a service.
     *
     * @param service The service object.
     * @return The service's longitude, or 0 if not found.
     */
    @Override
    public long getServiceLongitudeProperty(ServiceReadOnly service) {
        return service.getLongitude();
    }

    /**
     * Gets the name of a student.
     *
     * @param student The student object.
     * @return The student's name, or null if not found.
     */
    @Override
    public String getStudentNameProperty(StudentReadOnly student) {
        return student.getName();
    }

    /**
     * Gets the type of a student.
     *
     * @param student The student object.
     * @return The student's type, or null if not found.
     */
    @Override
    public StudentType getStudentTypeProperty(StudentReadOnly student) {
        return student.getType();
    }

    /**
     * Factory method to create a new {@link Student} instance based on its type.
     *
     * @param type    The {@link StudentType} enum.
     * @param name    The name of the student.
     * @param country The student's country.
     * @param lodging The student's home lodging.
     * @return A new {@link Student} (e.g., BookishImpl, ThriftyImpl, OutgoingImpl).
     * @throws LodgingIsFullException if the lodging is at capacity.
     */
    private Student createStudentByType(StudentType type, String name, String country, Lodging lodging)
            throws LodgingIsFullException {
        return switch (type) {
            case BOOKISH -> new BookishImpl(name, country, lodging);
            case THRIFTY -> new ThriftyImpl(name, country, lodging);
            case OUTGOING -> new OutgoingImpl(name, country, lodging);
        };
    }

}

