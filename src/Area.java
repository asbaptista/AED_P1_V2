import Exceptions.ServiceAlreadyExistsException;
import Exceptions.StudentAlreadyExistsException;
import Services.*;
import Students.*;
import dataStructures.*;


/**
 * Interface for a geographic Area.
 * Defines the contract for managing all services and students
 * within a specific set of geographic boundaries.
 */
public interface Area extends AreaReadOnly{

    @Override
    Service getService(String name);

    @Override
    Iterator<Service> getServices();

    @Override
    Iterator<Service> getRankedServices();

    @Override
    Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars);

    @Override
    Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type);

    @Override
    Student getStudent(String name);

    @Override
    Iterator<Student> listAllStudents();

    @Override
    Iterator<Student> listStudentsByCountry(String country);

    void addService(Service service) throws ServiceAlreadyExistsException;

    void updateRankingByStars(Service service, int oldStars);

    ServiceCollection getServicesCollection();

    void addStudent(Student student) throws StudentAlreadyExistsException;


    void removeStudent(String name);

}