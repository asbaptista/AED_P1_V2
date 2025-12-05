package Students;

import Services.Service;

/**
 * A marker interface for "Bookish" students.
 * <p>
 * This interface extends {@link Student} and is used to identify students
 * who are primarily concerned with studying and visiting/attending Leisure sites.
 * A Bookish student collects information about the leisure places they have visited.
 */
public interface Bookish extends Student {
    /**
     * Registers a service as visited (only stores Leisure services).
     *
     * @param service The service the student has visited.
     */
    void registerVisit(Service service);
}