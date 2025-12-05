package SystemManager;

import Exceptions.*;
import Services.*;
import Students.*;
import dataStructures.*;


import java.io.*;

public class AreaImpl implements Area, Serializable {
    String name;
    private final long topLat;
    private final long bottomLat;
    private final long leftLong;
    private final long rightLong;
    private final StudentsCollectionImpl students;
    private final ServicesCollectionImpl services;

    public AreaImpl(String name, long topLat, long leftLong, long bottomLat, long rightLong) {
        this.name = name;
        this.topLat = topLat;
        this.leftLong = leftLong;
        this.bottomLat = bottomLat;
        this.rightLong = rightLong;
        this.students = new StudentsCollectionImpl();
        this.services = new ServicesCollectionImpl();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getTopLat() {
        return topLat;
    }

    @Override
    public long getLeftLong() {
        return leftLong;
    }

    @Override
    public long getBottomLat() {
        return bottomLat;
    }

    @Override
    public long getRightLong() {
        return rightLong;
    }

    @Override
    public boolean isWithinBounds(long lat, long lon) {
        return lat <= topLat && lat >= bottomLat &&
                lon >= leftLong && lon <= rightLong;
    }

    @Override
    public void addService(Service service)throws ServiceAlreadyExistsException {
        services.add(service);
    }

    @Override
    public Iterator<Service> getServices() {
        return services.listServices();
    }

    @Override
    public boolean containsService(String name) {
        return services.contains(name);
    }

    @Override
    public Service getService(String name) {
        return services.findByName(name);
    }

    @Override
    public int getNumberOfServices() {
        return services.size();
    }

    @Override
    public ServiceCollection getServicesCollection() {
        return services;
    }

    @Override
    public Iterator<Service> getRankedServices() {
        return services.getServicesByStars();
    }

    @Override
    public void updateRankingByStars(Service service, int oldStars) {
        services.updateRankingByStars(service, oldStars);
    }

    @Override
    public Iterator<Service> getServicesByTypeAndStars(ServiceType type, int stars) {
        return services.getServicesByTypeAndStars(type, stars);
    }

    @Override
    public void addStudent(Student student)throws StudentAlreadyExistsException {
        students.addStudent(student);
    }

    @Override
    public void createAndAddService(ServiceType type, String name, long lat, long lon, int price, int value)
            throws ServiceAlreadyExistsException, InvalidMenuPriceException,
            InvalidRoomPriceException, InvalidTicketPriceException,
            InvalidDiscountPriceException, InvalidCapacityException {
        Service service = createService(name, lat, lon, price, type, value);
        addService(service);
    }

    private Service createService(String name, long lat, long lon, int price, ServiceType type, int value)
            throws InvalidMenuPriceException, InvalidRoomPriceException,
            InvalidTicketPriceException, InvalidCapacityException,
            InvalidDiscountPriceException {
        return switch (type) {
            case EATING -> new EatingImpl(name, lat, lon, price, value);
            case LODGING -> new LodgingImpl(name, lat, lon, price, value);
            case LEISURE -> new LeisureImpl(name, lat, lon, price, value);
        };
    }

    @Override
    public Student getStudent(String name) {
        return students.findByName(name);
    }

    @Override
    public boolean containsStudent(String name) {
        return students.findByName(name) != null;
    }

    @Override
    public void removeStudent(String name) {
        students.removeStudent(name);
    }

    @Override
    public Iterator<Student> listAllStudents() {
        return students.listAllStudents();
    }

    @Override
    public Iterator<Student> listStudentsByCountry(String filter) {
        return students.listStudentsByCountry(filter);
    }

    @Override
    public Iterator<Service> getServicesByTypeOrderedByStars(ServiceType type) {
        return services.getServicesByTypeOrderedByStars(type);
    }

    @Override
    public boolean hasServicesOfType(ServiceType type) {
        return services.hasServicesOfType(type);
    }

    @Override
    public void addReviewToService(String serviceName, int rating, String comment) {
        Service service = getService(serviceName);
        int oldAvgStar = service.getAvgStar();
        service.addReview(rating, comment);
        int newAvgStar = service.getAvgStar();
        if (newAvgStar != oldAvgStar) {
            updateRankingByStars(service, oldAvgStar);
        }
    }

    @Override
    public void removeStudentWithCleanup(String studentName) {
        Student student = getStudent(studentName);
        Service current = student.getCurrent();
        if (current instanceof Eating eating) {
            eating.removeOccupant(student);
        } else if (current instanceof Lodging lodging) {
            lodging.removeOccupant(student);
        }
        removeStudent(studentName);
    }

    @Override
    public void moveStudentToService(String studentName, String serviceName)
            throws AlreadyThereException, NotValidServiceException, EatingIsFullException {
        Student student = getStudent(studentName);
        Service service = getService(serviceName);
        student.goToLocation(service);
    }

    @Override
    public void moveStudentHome(String studentName, String lodgingName)
            throws AlreadyStudentHomeException, LodgingIsFullException, StudentIsThriftyException {
        Student student = getStudent(studentName);
        Service service = getService(lodgingName);
        student.moveHome(((Lodging) service));
    }

    @Override
    public Service getStudentCurrentLocation(String studentName) {
        Student student = getStudent(studentName);
        return student.getCurrent();
    }

    @Override
    public Service getStudentCurrentLocation(StudentReadOnly student) {
        return getStudentCurrentLocation(student.getName());
    }

    @Override
    public boolean isStudentDistracted(String studentName, String serviceName) {
        Student student = getStudent(studentName);
        Service service = getService(serviceName);
        return student instanceof Thrifty thrifty
                && service instanceof Eating eating
                && thrifty.isDistracted(eating);
    }

    @Override
    public Iterator<Service> getStudentVisitedLocations(String studentName) {
          return getStudent(studentName).getVisitedIterator();
    }

    @Override
    public TwoWayIterator<Student> getServiceOccupants(String serviceName) {
        Service service = getService(serviceName);
        if (service instanceof Eating eating) {
            return eating.getOccupantsIterator();
        } else if (service instanceof Lodging lodging) {
            return lodging.getOccupantsIterator();
        }
        return null;
    }

    @Override
    public Service findRelevantServiceForStudent(String studentName, ServiceType serviceType) {
        Student student = getStudent(studentName);
        Iterator<Service> typeServicesIterator = getServicesByTypeOrderedByStars(serviceType);
        return student.findMostRelevant(typeServicesIterator);
    }

    @Override
    public Iterator<Service> getClosestServicesByTypeAndStars(String studentName, ServiceType type, int stars) {
        Student student = getStudent(studentName);

        Iterator<Service> filteredByTypeStars = getServicesByTypeAndStars(type, stars);
        List<Service> closestServices = new DoublyLinkedList<>();
        long minDistance = Long.MAX_VALUE;

        while (filteredByTypeStars.hasNext()) {
            Service service = filteredByTypeStars.next();
            long currentDistance = calculateManhattanDistance(
                    student.getCurrent().getLatitude(),
                    student.getCurrent().getLongitude(),
                    service.getLatitude(),
                    service.getLongitude());

            if (currentDistance < minDistance) {
                closestServices = new DoublyLinkedList<>();
                closestServices.addLast(service);
                minDistance = currentDistance;
            } else if (currentDistance == minDistance) {
                closestServices.addLast(service);
            }
        }

        return closestServices.iterator();
    }

    private long calculateManhattanDistance(long lat1, long lon1, long lat2, long lon2) {
        return Math.abs(lat1 - lat2) + Math.abs(lon1 - lon2);
    }

    @Override
    public boolean isLodgingService(String serviceName) {
        Service service = getService(serviceName);
        return service instanceof Lodging;
    }

    @Override
    public boolean isServiceWithOccupancy(String serviceName) {
        Service service = getService(serviceName);
        return service instanceof Eating || service instanceof Lodging;
    }

    @Override
    public boolean isStudentThrifty(String studentName) {
        Student student = getStudent(studentName);
        return student instanceof Thrifty;
    }

    @Override
    public void addStudentWithLodging(StudentType type, String name, String country, String lodgingName)
            throws StudentAlreadyExistsException, LodgingIsFullException {
        Service service = getService(lodgingName);
        if (service instanceof Lodging lodging) {
            Student student = createStudentByType(type, name, country, lodging);
            addStudent(student);
        }
    }

    @Override
    public String getServiceNameProperty(ServiceReadOnly service) {
        return service.getName();
    }

    @Override
    public ServiceType getServiceTypeProperty(ServiceReadOnly service) {
        return service.getType();
    }

    @Override
    public long getServiceLatitudeProperty(ServiceReadOnly service) {
        return service.getLatitude();
    }

    @Override
    public long getServiceLongitudeProperty(ServiceReadOnly service) {
        return service.getLongitude();
    }

    @Override
    public String getStudentNameProperty(StudentReadOnly student) {
        return student.getName();
    }

    @Override
    public StudentType getStudentTypeProperty(StudentReadOnly student) {
        return student.getType();
    }

    private Student createStudentByType(StudentType type, String name, String country, Lodging lodging)
            throws LodgingIsFullException {
        return switch (type) {
            case BOOKISH -> new BookishImpl(name, country, lodging);
            case THRIFTY -> new ThriftyImpl(name, country, lodging);
            case OUTGOING -> new OutgoingImpl(name, country, lodging);
        };
    }

}

