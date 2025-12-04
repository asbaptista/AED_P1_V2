import Exceptions.StudentAlreadyExistsException;
import Students.Student;
import dataStructures.*;
import java.io.*;

/**
 * Implements the {@link StudentCollection} interface.
 * <p>
 * This class is responsible for managing all {@link Student} objects for an {@link Area}.
 * It maintains two internal lists to satisfy different retrieval requirements:
 * <ol>
 * <li>A {@link TwoWayList} (`studentsByInsertion`) to store students in their original
 * **registration order**.</li>
 * <li>A {@link SortedList} (`studentsByName`) to store students sorted
 *.</li>
 * </ol>
 * This class is serializable and uses custom `writeObject` and `readObject`
 * methods to ensure the sorted list is correctly rebuilt upon deserialization.
 */
public class StudentsCollectionImpl implements StudentCollection, Serializable {

    /**
     * Map of students, automatically sorted alphabetically by name.
     */
    private final SortedMap<String, Student> studentsByName;

    /**
     * Map of students grouped by country.
     * Key: country name (lowercase), Value: List of students from that country.
     */
    private final Map<String, List<Student>> studentsByCountry;


    /**
     * Constructs a new, empty student collection.
     * Initializes both the insertion-order list and the name-sorted list,
     */
    public StudentsCollectionImpl() {
        this.studentsByName = new AVLSortedMap<>();
        this.studentsByCountry = new SepChainHashTable<>();
    }

    /**
     * Adds a new student to the collection.
     * The student is added to the end of the insertion-order list (`studentsByInsertion`)
     * and also added in its correct sorted position in the name-sorted
     * list (`studentsByName`).
     *
     * @param student The {@link Student} to add.
     */
    @Override
    public void addStudent(Student student) throws StudentAlreadyExistsException {
        if (findByName(student.getName()) != null) {
            throw new StudentAlreadyExistsException();
        }

        studentsByName.put(student.getName().toLowerCase(), student);
        addStudentToCountryMap(student);
    }

    /**
     * Helper method to add a student to the country map.
     * Gets or creates the list for the student's country.
     *
     * @param student The student to add.
     */
    private void addStudentToCountryMap(Student student) {
        String country = student.getCountry().toLowerCase();
        List<Student> countryList = studentsByCountry.get(country);

        if (countryList == null) {
            countryList = new DoublyLinkedList<>();
            studentsByCountry.put(country, countryList);
        }

        countryList.addLast(student);
    }

    /**
     * Removes a student from the collection, identified by their name.
     * <p>
     * This method first finds the student object using {@link #findByName(String)}
     * and then removes that object from both internal lists
     * (`studentsByInsertion` and `studentsByName`).
     *
     * @param name The name of the student to remove.
     */
    @Override
    public void removeStudent(String name) {
        Student student = studentsByName.remove(name.toLowerCase());

        if (student != null) {
            removeStudentFromCountryMap(student);
        }
    }

    /**
     * Helper method to remove a student from the country map.
     * Removes the student from their country's list and cleans up
     * the country entry if the list becomes empty.
     *
     * @param student The student to remove.
     */
    private void removeStudentFromCountryMap(Student student) {
        String country = student.getCountry().toLowerCase();
        List<Student> countryList = studentsByCountry.get(country);

        if (countryList != null) {
            int index = countryList.indexOf(student);
            countryList.remove(index);

            if (countryList.isEmpty()) {
                studentsByCountry.remove(country);
            }
        }
    }


    /**
     * Finds a student by their name using a case-insensitive linear search.
     * <p>
     * This search is performed on the name-sorted list (`studentsByName`).
     *
     * @param name The name of the student to find.
     * @return The {@link Student} object, or {@code null} if not found.
     */
    @Override
    public Student findByName(String name) {
        return studentsByName.get(name.toLowerCase());
    }


    /**
     * Gets an iterator over all students in the collection,
     * sorted alphabetically by name.
     *
     * @return A sorted {@link Iterator} of all {@link Student}s.
     */
    @Override
    public Iterator<Student> listAllStudents() {
        return studentsByName.values();
    }

    /**
     * Gets an iterator over all students from a specific country,
     * in their original order of registration (insertion order).
     *
     * @param country The country name to filter by.
     * @return An {@link Iterator} of {@link Student}s from that country.
     */
    @Override
    public Iterator<Student> listStudentsByCountry(String country) {
        List<Student> list = studentsByCountry.get(country.toLowerCase());

        if (list != null) {
            return list.iterator();
        }

        return new DoublyLinkedList<Student>().iterator();

    }

}