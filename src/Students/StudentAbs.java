package Students;

import Exceptions.*;
import Services.*;
import dataStructures.*;

import java.io.*;

public abstract class StudentAbs implements Student, Serializable {

    private final String name;
    private final String country;
    protected Lodging home;
    protected Service current;
    protected final TwoWayList<Service> visitedServices;
    protected final Map<Service, Boolean> visitedServicesSet;
    private final StudentType type;

    public StudentAbs(String name, String country, Lodging home, StudentType type) throws LodgingIsFullException {
        this.name = name;
        this.country = country;
        this.home = home;
        this.current = home;
        this.type = type;
        this.visitedServices = new DoublyLinkedList<>();
        this.visitedServicesSet = new ClosedHashTable<>();
        home.addOccupant(this);
    }

    @Override
    public StudentType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public Lodging getHome() {
        return home;
    }

    @Override
    public Service getCurrent() {
        return current;
    }

    @Override
    public Iterator<Service> getVisitedIterator() {
        return visitedServices.iterator();
    }

    @Override
    public void goToLocation(Service service) throws AlreadyThereException, NotValidServiceException, EatingIsFullException {

        if (service instanceof Lodging) {
            throw new NotValidServiceException();
        }
        if (current.getName().equalsIgnoreCase(service.getName())) {
            throw new AlreadyThereException();
        }

        updateOccupancy(current, false);
        current = service;
        updateOccupancy(service, true);

        if (current instanceof Eating && this instanceof Thrifty) {
            ((Thrifty) this).visitEating((Eating) current);
        }

        registerVisit(service);
    }

    @Override
    public void moveHome(Lodging newHome) throws AlreadyStudentHomeException, LodgingIsFullException, StudentIsThriftyException {

        if (home == newHome) {
            throw new AlreadyStudentHomeException();
        }
        if (newHome.isFull()) {
            throw new LodgingIsFullException();
        }

        if (this instanceof Thrifty) {
            if (!((Thrifty) this).canMoveTo(newHome)) {
                throw new StudentIsThriftyException();
            }
        }

        home.removeOccupant(this);
        home = newHome;

        if (current != home) {
            if (current instanceof Eating) {
                ((Eating) current).removeOccupant(this);
            }
            current = home;
        }

        newHome.addOccupant(this);

        if (this instanceof Thrifty) {
            ((Thrifty) this).updateCheapestLodging(newHome);
        } else {
            registerVisit(newHome);
        }
    }

    @Override
    public Service findMostRelevant(Iterator<Service> services) {
        Service bestService = null;

        while (services.hasNext()) {
            Service current = services.next();

            if (bestService == null) {
                bestService = current;
            } else if (current.getAvgStar() > bestService.getAvgStar()) {
                bestService = current;
            }
        }
        return bestService;
    }

    private void updateOccupancy(Service service, boolean add) throws EatingIsFullException {
        if (service instanceof Eating) {
            if (add) {
                ((Eating) service).addOccupant(this);
            } else {
                ((Eating) service).removeOccupant(this);
            }
        }
    }

    protected abstract void registerVisit(Service service);
}