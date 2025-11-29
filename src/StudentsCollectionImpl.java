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
 * **alphabetically by name** using a {@link StudentNameComparator}
 *.</li>
 * </ol>
 * This class is serializable and uses custom `writeObject` and `readObject`
 * methods to ensure the sorted list is correctly rebuilt upon deserialization.
 */
public class StudentsCollectionImpl implements StudentCollection, Serializable {


    @Serial
    private static final long serialVersionUID = 1L;
    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */

    /**
     * List of students, automatically sorted alphabetically by name.
     */
    private  SortedMap<String, Student> studentsByName;

    /**
     * List of students, maintained in their original insertion order.
     */
    private Map<String, List<Student>> studentsByCountry;


    // --- Constructor ---

    /**
     * Constructs a new, empty student collection.
     * Initializes both the insertion-order list and the name-sorted list,
     * providing the {@link StudentNameComparator} to the latter.
     */
    public StudentsCollectionImpl() {
        this.studentsByName = new AVLSortedMap<>();
        this.studentsByCountry = new SepChainHashTable<>();
    }

    // --- State Modifiers ---

    /**
     * Adds a new student to the collection.
     * The student is added to the end of the insertion-order list (`studentsByInsertion`)
     * and also added in its correct sorted position in the name-sorted
     * list (`studentsByName`).
     *
     * @param student The {@link Student} to add.
     */
    @Override
    public void addStudent(Student student) {
        studentsByName.put(student.getName().toLowerCase(), student); // dps confirmar o toLoweCase;

        String country = student.getCountry().toLowerCase(); // dps confirmar o toLoweCase;
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
        String lowerName = name.toLowerCase(); // por enquanto fica assim , dps confirmar

        Student student = studentsByName.remove(lowerName);

        if (student != null) {
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

    }

    // --- Querying & Searching ---

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

    // --- Iterators & Retrieval ---

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
     * in their original order of registration (insertion order)
     *.
     *
     * @param country The country name to filter by.
     * @return A {@link FilterIterator} of {@link Student}s from that country,
     * in registration order.
     */
    @Override
    public Iterator<Student> listStudentsByCountry(String country) {
        List<Student> list = studentsByCountry.get(country.toLowerCase());
        if (list != null) {
            return list.iterator();
        }
        return new DoublyLinkedList<Student>().iterator(); // empty iterator

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