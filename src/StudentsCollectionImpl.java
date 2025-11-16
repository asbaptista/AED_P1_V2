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

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * List of students, automatically sorted alphabetically by name.
     */
    SortedList<Student> studentsByName;

    /**
     * List of students, maintained in their original insertion order.
     */
    TwoWayList<Student> studentsByInsertion;

    // --- Constructor ---

    /**
     * Constructs a new, empty student collection.
     * Initializes both the insertion-order list and the name-sorted list,
     * providing the {@link StudentNameComparator} to the latter.
     */
    public StudentsCollectionImpl() {
        this.studentsByName = new SortedDoublyLinkedList<>(new StudentNameComparator());
        this.studentsByInsertion = new DoublyLinkedList<>();
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
        studentsByInsertion.addLast(student);
        studentsByName.add(student);
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
        Student student = findByName(name);
        if (student != null) {
            // This is an O(N) removal for a DoublyLinkedList
            int index = studentsByInsertion.indexOf(student);
            studentsByInsertion.remove(index);
            // This is also an O(N) removal for a SortedDoublyLinkedList
            studentsByName.remove(student);
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
        // This is an O(N) search on the (sorted) doubly linked list
        Iterator<Student> it = studentsByName.iterator();
        while (it.hasNext()) {
            Student student = it.next();
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null;
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
        return studentsByName.iterator();
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
        return new FilterIterator<>(studentsByInsertion.iterator(),
                student -> student.getCountry().equalsIgnoreCase(country));
    }

    /**
     * Gets an iterator over students in their original insertion order.
     *
     * @return An {@link Iterator} of {@link Student}s.
     */
    public Iterator<Student> getStudentsByInsertion() {
        return studentsByInsertion.iterator();
    }

    /**
     * Gets an iterator over students sorted alphabetically by name.
     *
     * @return A sorted {@link Iterator} of {@link Student}s.
     */
    public Iterator<Student> getStudentsByName() {
        return studentsByName.iterator();
    }

    // --- Serialization Methods ---

    /**
     * Custom serialization method.
     * <p>
     * Saves the total size and then each student from the `studentsByInsertion`
     * (insertion-order) list. The `studentsByName` list is **not** saved,
     * as it will be rebuilt during deserialization.
     *
     * @param oos The ObjectOutputStream to write to.
     * @throws IOException If an I/O error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(studentsByInsertion.size());

        Iterator<Student> it = studentsByInsertion.iterator();
        while (it.hasNext()) {
            oos.writeObject(it.next());
        }
    }

    /**
     * Custom deserialization method.
     * <p>
     * Reads the services in their original insertion order and re-adds
     * them using the {@link #addStudent(Student)} method.
     * This single call correctly rebuilds *both* the `studentsByInsertion` list
     * and the `studentsByName` sorted list, ensuring the system state is consistent.
     *
     _
     _
     * @param ois The ObjectInputStream to read from.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        // Initialize transient fields
        this.studentsByName = new SortedDoublyLinkedList<>(new StudentNameComparator());
        this.studentsByInsertion = new DoublyLinkedList<>();

        // Read students and rebuild both lists by calling addStudent()
        int size = ois.readInt();
        for (int i = 0; i < size; i++) {
            Student student = (Student) ois.readObject();
            this.addStudent(student);
        }
    }
}