package Services;

import Exceptions.EatingIsFullException;
import Exceptions.InvalidCapacityException;
import Exceptions.InvalidMenuPriceException;
import Students.Student;
import dataStructures.*;

public class EatingImpl extends ServiceAbs implements Eating {
    private final int seats;
    private final TwoWayList<Student> occupants;

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

    @Override
    public int getCapacity() {
        return seats;
    }

    @Override
    public boolean hasCapacity() {
        return occupants.size() < seats;
    }

    @Override
    public void addOccupant(Student student) throws EatingIsFullException {
        if (!hasCapacity()) {
            throw new EatingIsFullException();
        }
        occupants.addLast(student);
    }

    @Override
    public void removeOccupant(Student student) {
        int index = occupants.indexOf(student);
        if (index != -1) {
            occupants.remove(index);
        }
    }

    @Override
    public TwoWayIterator<Student> getOccupantsIterator() {
        return occupants.twoWayiterator();
    }

}