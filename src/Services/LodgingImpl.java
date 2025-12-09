package Services;

import Exceptions.InvalidCapacityException;
import Exceptions.InvalidRoomPriceException;
import Exceptions.LodgingIsFullException;
import Students.Student;
import dataStructures.DoublyLinkedList;
import dataStructures.TwoWayIterator;
import dataStructures.TwoWayList;

public class LodgingImpl extends ServiceAbs implements Lodging {
    private final int rooms;

    private final TwoWayList<Student> occupants;

    public LodgingImpl(String name, long lat, long lon, int price, int rooms) throws InvalidRoomPriceException, InvalidCapacityException {
        super(name, lat, lon, price, Services.ServiceType.LODGING, rooms);
        if (price <= 0) {
            throw new InvalidRoomPriceException();
        }
        if (rooms <= 0) {
            throw new InvalidCapacityException();
        }
        this.rooms = rooms;
        this.occupants = new DoublyLinkedList<>();
    }

    @Override
    public boolean isFull() {
        return occupants.size() == rooms;
    }

    @Override
    public void addOccupant(Student student) throws LodgingIsFullException {
        if (isFull()) {
            throw new LodgingIsFullException();
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