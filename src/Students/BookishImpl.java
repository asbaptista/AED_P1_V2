package Students;

import Exceptions.LodgingIsFullException;
import Services.Leisure;
import Services.Lodging;
import Services.Service;

/**
 * Implementation of the {@link Bookish} student type.
 * <p>
 * A Bookish student is primarily concerned with studying and visiting Leisure sites
 *. This class implements the specific behavior for
 * registering visited locations, which only stores services of type {@link Leisure}
 *. This class is serializable.
 */
public class BookishImpl extends StudentAbs implements Bookish {
    /**
     * Constructs a new Bookish student.
     *
     * @param name    The student's name.
     * @param country The student's country of origin.
     * @param home    The {@link Lodging} service where the student resides.
     */
    public BookishImpl(String name, String country, Lodging home) throws LodgingIsFullException {
        super(name, country, home, StudentType.BOOKISH);
    }

    /**
     * Registers a service as visited, but only if it is a {@link Leisure} service.
     * The service is only added if it hasn't been visited before (no duplicates).
     *
     * @param service The service the student has just visited.
     */
    @Override
    public void registerVisit(Service service) {
        if (service instanceof Leisure && visitedServicesSet.get(service) == null) {
                visitedServices.addLast(service);
                visitedServicesSet. put(service, true);
            }

    }

}