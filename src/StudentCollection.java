import Students.Student;
import dataStructures.Iterator;

import java.io.Serializable;

/**
 * Interface for a collection that manages all {@link Student} objects.
 * <p>
 * This contract defines the methods required to add, remove, find, and list students.
 * It is responsible for maintaining students in at least two orders:
 * 1. Order of registration (insertion order), for country-based filtering.
 * 2. Alphabetical order by name, for listing all students.
 */
public interface StudentCollection  {

    // --- State Modifiers ---

    /**
     * Adds a new student to the collection.
     * The implementation is responsible for adding the student to
     * all relevant internal lists (e.g., insertion list and sorted name list).
     *
     * @param student The {@link Student} to add.
     */
    void addStudent(Student student);

    /**
     * Removes a student from the collection, identified by their name.
     * The implementation must remove the student from all internal lists.
     *
     * @param name The name of the student to remove.
     */
    void removeStudent(String name);


    // --- Querying & Searching ---

    /**
     * Finds and returns a student by their name.
     * The search is typically case-insensitive.
     *
     * @param name The name of the student to find.
     * @return The {@link Student} object, or {@code null} if not found.
     */
    Student findByName(String name);


    // --- Iterators & Retrieval ---

    /**
     * Gets an iterator over all students in the collection,
     * sorted alphabetically by name.
     *
     * @return A sorted {@link Iterator} of all {@link Student}s.
     */
    Iterator<Student> listAllStudents();

    /**
     * Gets an iterator over all students from a specific country,
     * in their original order of registration (insertion order)
     *.
     *
     * @param country The country name to filter by.
     * @return An {@link Iterator} of {@link Student}s from that country.
     */
    Iterator<Student> listStudentsByCountry(String country);
}