import Students.Student;
import dataStructures.Comparator;

import java.io.Serializable;

/**
 * A comparator for {@link Student} objects that sorts them alphabetically
 * by their name, ignoring case.
 * <p>
 * This class is serializable.
 */
class StudentNameComparator implements Comparator<Student>, Serializable {

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Compares two {@link Student} objects for order based on their names.
     * <p>
     * The comparison uses {@link String#compareToIgnoreCase(String)} to ensure
     * that the sorting is alphabetical and case-insensitive
     * (e.g., "Neil Perry" and "neil perry" are treated as equal).
     *
     * @param s1 The first student to be compared.
     * @param s2 The second student to be compared.
     * @return A negative integer, zero, or a positive integer as the
     * first student's name is lexicographically less than, equal to,
     * or greater than the second student's name, ignoring case.
     */
    @Override
    public int compare(Student s1, Student s2) {
        return s1.getName().compareToIgnoreCase(s2.getName());
    }
}