package SystemManager;

import Exceptions.StudentAlreadyExistsException;
import Students.Student;
import dataStructures.*;
import java.io.*;

public class StudentsCollectionImpl implements StudentCollection, Serializable {

    private final SortedMap<String, Student> studentsByName;
    private final Map<String, List<Student>> studentsByCountry;

    public StudentsCollectionImpl() {
        this.studentsByName = new AVLSortedMap<>();
        this.studentsByCountry = new SepChainHashTable<>();
    }

    @Override
    public void addStudent(Student student) throws StudentAlreadyExistsException {
        if (findByName(student.getName()) != null) {
            throw new StudentAlreadyExistsException();
        }

        studentsByName.put(student.getName().toLowerCase(), student);
        addStudentToCountryMap(student);
    }

    private void addStudentToCountryMap(Student student) {
        String country = student.getCountry().toLowerCase();
        List<Student> countryList = studentsByCountry.get(country);

        if (countryList == null) {
            countryList = new DoublyLinkedList<>();
            studentsByCountry.put(country, countryList);
        }

        countryList.addLast(student);
    }

    @Override
    public void removeStudent(String name) {
        Student student = studentsByName.remove(name.toLowerCase());

        if (student != null) {
            removeStudentFromCountryMap(student);
        }
    }

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

    @Override
    public Student findByName(String name) {
        return studentsByName.get(name.toLowerCase());
    }

    @Override
    public Iterator<Student> listAllStudents() {
        return studentsByName.values();
    }

    @Override
    public Iterator<Student> listStudentsByCountry(String country) {
        List<Student> list = studentsByCountry.get(country.toLowerCase());

        if (list != null) {
            return list.iterator();
        }

        return new DoublyLinkedList<Student>().iterator();

    }

}