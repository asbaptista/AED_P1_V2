package Services;

import Exceptions.EatingIsFullException;
import Exceptions.InvalidCapacityException;
import Exceptions.InvalidMenuPriceException;
import Students.Student;
import dataStructures.*;

/**
 * Implementation of the {@link Eating} service.
 * Represents an eating service (e.g., canteen, restaurant) that manages
 * capacity (seats) and a list of current student occupants.
 * This class is serializable.
 */
public class EatingImpl extends ServiceAbs implements Eating {
    /**
     * The total number of seats (capacity) in this eating service.
     */
    int seats;

    /**
     * A list of {@link Student}s currently present (occupying) this service.
     */
    TwoWayList<Student> occupants;

    /**
     * Constructs a new Eating service.
     *
     * @param name  The name of the eating service.
     * @param lat   The latitude coordinate.
     * @param lon   The longitude coordinate.
     * @param price The price (e.g., student menu price).
     * @param seats The total number of seats (capacity).
     */
    public EatingImpl(String name, long lat, long lon, int price, int seats)throws InvalidMenuPriceException, InvalidCapacityException {
        super(name, lat, lon, price, Services.ServiceType.EATING, seats);
        if (price <= 0) {
            throw new InvalidMenuPriceException();
        }
        if (seats <= 0) {
            throw new InvalidCapacityException();
        }
        this.seats = seats;
        this.occupants = new DoublyLinkedList<>();
    }

    /**
     * Gets the total capacity (number of seats) of the eating service.
     *
     * @return The total seating capacity.
     */
    @Override
    public int getCapacity() {
        return seats;
    }

    /**
     * Checks if the eating service currently has space for more occupants.
     *
     * @return true if the current occupant count is less than the total capacity, false otherwise.
     */
    @Override
    public boolean hasCapacity() {
        return occupants.size() < seats;
    }

    /**
     * Adds a student to the list of current occupants,
     * only if the service is not already full.
     *
     * @param student The {@link Student} to add as an occupant.
     */
    @Override
    public void addOccupant(Student student) throws EatingIsFullException {
        if (!hasCapacity()) {
            throw new EatingIsFullException();
        }
        occupants.addLast(student);
    }

    /**
     * Removes a student from the list of current occupants.
     *
     * @param student The {@link Student} to remove from the occupants list.
     */
    @Override
    public void removeOccupant(Student student) {
        int index = occupants.indexOf(student);
        if (index != -1) {
            occupants.remove(index);
        }
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