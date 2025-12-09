package Students;

import Services.Service;

/**
 * A marker interface for "Outgoing" students.
 * <p>
 * This interface extends {@link Student} and is used to identify students
 * who are mainly concerned with eating out and visiting the town.
 * An Outgoing student collects information about all the locations and
 * services they have visited, with no restrictions.
 */
public interface Outgoing extends Student {
    /**
     * Registers any service as visited (stores all services).
     *
     * @param service The service the student has visited.
     */
    void registerVisit(Service service);
}