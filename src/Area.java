import Exceptions.ServiceAlreadyExistsException;
import Exceptions.StudentAlreadyExistsException;
import Services.*;
import Students.*;
import dataStructures.*;


/**
 * Interface for a geographic Area.
 * Defines the contract for managing all services and students
 * within a specific set of geographic boundaries.
 * Extends {@link AreaReadOnly} to provide mutable operations.
 */
public interface Area extends AreaReadOnly{

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

}