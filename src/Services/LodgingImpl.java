package Services;

import Students.Student;
import dataStructures.DoublyLinkedList;
import dataStructures.TwoWayIterator;
import dataStructures.TwoWayList;

import java.io.Serializable;

/**
 * Implementation of the {@link Lodging} service.
 * Represents a lodging service (e.g., student residence, hostel) that provides rooms
 * and manages a list of student occupants. This class is serializable.
 */
public class LodgingImpl extends ServiceAbs implements Lodging, Serializable {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The total number of rooms (capacity) in this lodging.
     */
    int rooms;

    /**
     * The current number of occupied rooms.
     */
    int occupiedRooms;

    /**
     * A list of {@link Student}s currently residing (occupying) this lodging.
     */
    TwoWayList<Student> occupants;

    // --- Constructor ---

    /**
     * Constructs a new Lodging service.
     *
     * @param name  The name of the lodging.
     * @param lat   The latitude coordinate.
     * @param lon   The longitude coordinate.
     * @param price The monthly price per room.
     * @param rooms The total number of rooms (capacity) available.
     */
    public LodgingImpl(String name, long lat, long lon, int price, int rooms) {
        super(name, lat, lon, price, Services.ServiceType.LODGING, rooms);
        this.rooms = rooms;
        this.occupants = new DoublyLinkedList<>();
        this.occupiedRooms = 0;
    }

    // --- Public Methods (from Lodging interface) ---

    /**
     * Checks if the lodging is at full capacity.
     *
     * @return true if the number of occupied rooms is greater than or equal to
     * the total number of rooms, false otherwise.
     */
    @Override
    public boolean isFull() {
        return occupiedRooms >= rooms;
    }

    /**
     * Adds a student to the list of occupants and increments the occupied room count.
     * This method assumes a check for {@link #isFull()} has been made externally.
     *
     * @param student The {@link Student} to add as an occupant.
     */
    @Override
    public void addOccupant(Student student) {
        occupants.addLast(student);
        occupiedRooms++;
    }

    /**
     * Removes a student from the list of occupants and decrements the occupied room count.
     *
     * @param student The {@link Student} to remove from the occupants list.
     */
    @Override
    public void removeOccupant(Student student) {
        // Note: indexOf(student) is O(N). This operation is O(N).
        occupants.remove(occupants.indexOf(student));
        occupiedRooms--;
    }

    /**
     * Gets a two-way iterator over the list of current occupants.
     *
     * @return A {@link TwoWayIterator} of {@link Student}s.
     */
    @Override
    public TwoWayIterator<Student> getOccupantsIterator() {
        return occupants.twoWayiterator();
    }
}