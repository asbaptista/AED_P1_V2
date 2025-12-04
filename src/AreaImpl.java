import Exceptions.ServiceAlreadyExistsException;
import Exceptions.StudentAlreadyExistsException;
import Services.Service;
import Services.ServiceReadOnly;
import Services.ServiceType;
import Students.Student;
import dataStructures.*;

import java.io.*;

/**
 * Implements the {@link Area} interface.
 * This class represents a specific geographic area defined by a bounding box
 * and manages the collections of all {@link Student}s and {@link Service}s
 * within that area. This class is serializable.
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
     * Constructs a new Area.
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
     * Removes a student from the area's student collection.
     * <p>
     * Note: The {@code SystemManager} is responsible for handling any
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


    @Override
    public Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type) {
        return services.getServicesByTypeOrderedByStars(type);
    }

    @Override
    public boolean hasServicesOfType(ServiceType type) {
        return services.hasServicesOfType(type);    }

}