package Students;

import Services.Lodging;
import Services.Service;

import java.io.Serializable;

/**
 * Implementation of the {@link Outgoing} student type.
 * <p>
 * An Outgoing student is primarily concerned with eating out and visiting the town
 *. This class implements the specific behavior for
 * registering visited locations, which stores **all** services visited,
 * with no restrictions. This class is serializable.
 */
public class OutgoingImpl extends StudentAbs implements Outgoing, Serializable {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    // --- Constructor ---

    /**
     * Constructs a new Outgoing student.
     * <p>
     * As per the {@link StudentAbs} refactoring, the Outgoing student's
     * home lodging is immediately added as their first "visited" location.
     *
     * @param name    The student's name.
     * @param country The student's country of origin.
     * @param home    The {@link Lodging} service where the student resides.
     */
    public OutgoingImpl(String name, String country, Lodging home) {
        super(name, country, home);
        // Outgoing students register their home as the first visited location.
        this.visitedServices.addLast(home);
    }

    // --- Overridden Protected Methods ---

    /**
     * Registers any service as visited.
     * <p>
     * This overrides the default behavior from {@link StudentAbs} to implement
     * the specific rule for Outgoing students: they store every service they visit
     *.
     * The service is only added if it hasn't been visited before (no duplicates).
     *
     * @param service The service the student has just visited.
     */
    @Override
    protected void registerVisit(Service service) {
        if (visitedServicesSet. get(service) == null) {
            visitedServices. addLast(service);
            visitedServicesSet.put(service, true);
        }
    }
}