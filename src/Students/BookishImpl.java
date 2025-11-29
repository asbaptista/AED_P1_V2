package Students;

import Services.Leisure;
import Services.Lodging;
import Services.Service;
import java.io.*;

/**
 * Implementation of the {@link Bookish} student type.
 * <p>
 * A Bookish student is primarily concerned with studying and visiting Leisure sites
 *. This class implements the specific behavior for
 * registering visited locations, which only stores services of type {@link Leisure}
 *. This class is serializable.
 */
public class BookishImpl extends StudentAbs implements Bookish, Serializable {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    // --- Constructor ---

    /**
     * Constructs a new Bookish student.
     *
     * @param name    The student's name.
     * @param country The student's country of origin.
     * @param home    The {@link Lodging} service where the student resides.
     */
    public BookishImpl(String name, String country, Lodging home) {
        super(name, country, home);
    }

    // --- Overridden Protected Methods ---

    /**
     * Registers a service as visited, but only if it is a {@link Leisure} service.
     * <p>
     * This overrides the default behavior from {@link StudentAbs} to implement
     * the specific rule for Bookish students.
     * The service is only added if it hasn't been visited before (no duplicates).
     *
     * @param service The service the student has just visited.
     */
    @Override
    protected void registerVisit(Service service) {
        if (service instanceof Leisure) {
            if (visitedServicesSet.get(service) == null) {
                visitedServices.addLast(service);
                visitedServicesSet. put(service, true);
            }
        }
    }


    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}