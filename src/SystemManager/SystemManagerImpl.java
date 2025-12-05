package SystemManager;

import Exceptions.*;
import Services.*;
import Students.*;
import dataStructures.*;
import java.io.*;
import static Students.StudentType.*;

public class SystemManagerImpl implements SystemManager {

    Area currentArea;

    public SystemManagerImpl() {
        this.currentArea = null;
    }

    @Override
    public void createArea(String name, long topLat, long leftLong, long bottomLat, long rightLong)
            throws InvalidBoundsException, BoundsAlreadyExistsException {
        if (!areBoundsValid(topLat, leftLong, bottomLat, rightLong)) {
            throw new InvalidBoundsException();
        }
        if ((currentArea != null && (currentArea.getName().equalsIgnoreCase(name)) || equalBounds(topLat, leftLong, bottomLat, rightLong))) {
            throw new BoundsAlreadyExistsException();
        }
        if (currentArea != null) {
            saveCurrentAreaToFile(currentArea);
        }
        currentArea = new AreaImpl(name, topLat, leftLong, bottomLat, rightLong);
    }

    @Override
    public void loadArea(String name) throws BoundsNotFoundException {
        if (currentArea != null) {
            saveCurrentAreaToFile(currentArea);
        }

        currentArea = loadAreaFromFile(name);
        if (currentArea == null) {
            throw new BoundsNotFoundException();
        }
    }

    @Override
    public void saveArea() throws NoAreaLoadedException {
        if (currentArea == null) {
            throw new NoAreaLoadedException();
        }
        saveCurrentAreaToFile(currentArea);
    }

    @Override
    public AreaReadOnly getCurrentArea() throws NoAreaLoadedException {
        if (currentArea == null) {
            throw new NoAreaLoadedException();
        }
        return currentArea;
    }

    @Override
    public boolean equalBounds(long topLat, long leftLong, long bottomLat, long rightLong) {
        return currentArea != null &&
                currentArea.getTopLat() == topLat &&
                currentArea.getLeftLong() == leftLong &&
                currentArea.getBottomLat() == bottomLat &&
                currentArea.getRightLong() == rightLong;
    }

    @Override
    public void addService(ServiceType type, String name, long lat, long lon, int price, int value) throws InvalidServiceTypeException, InvalidLocationException, InvalidMenuPriceException, InvalidRoomPriceException, InvalidTicketPriceException, InvalidDiscountPriceException, InvalidCapacityException, ServiceAlreadyExistsException {

        if (type==null) {
            throw new InvalidServiceTypeException();
        }
        if (!validLocation(lat, lon)) {
            throw new InvalidLocationException();
        }

        currentArea.createAndAddService(type, name, lat, lon, price, value);
    }

    @Override
    public void addReviewToService(String serviceName, int rating, String comment)
            throws ServiceNotFoundException, InvalidStarsException {
        if (rating < 1 || rating > 5) {
            throw new InvalidStarsException();
        }
        if (!currentArea.containsService(serviceName)) {
            throw new ServiceNotFoundException();
        }
        currentArea.addReviewToService(serviceName, rating, comment);
    }

    @Override
    public Iterator<? extends ServiceReadOnly> listServices() throws NoServicesException {
        if (currentArea.getNumberOfServices() == 0) {
            throw new NoServicesException();
        }
        return currentArea.getServices();
    }

    @Override
    public Iterator<? extends ServiceReadOnly> getRankedServices() {
        return currentArea.getRankedServices();
    }

    @Override
    public void addStudent(StudentType type, String name, String country, String lodgingName) throws SystemBoundsNotDefinedException, InvalidStudentTypeException, LodgingNotFoundException, StudentAlreadyExistsException, LodgingIsFullException {

        if (currentArea == null) {
            throw new SystemBoundsNotDefinedException();
        }
        if (!currentArea.isLodgingService(lodgingName)) {
            throw new LodgingNotFoundException();
        }

        if (!isStudentTypeValid(type)) {
            throw new InvalidStudentTypeException();
        }

        currentArea.addStudentWithLodging(type, name, country, lodgingName);
    }

    @Override
    public Iterator<? extends StudentReadOnly> listStudents(String filter) {
        if (filter.equalsIgnoreCase("all")) {
            return currentArea.listAllStudents();
        } else {
            return currentArea.listStudentsByCountry(filter);
        }
    }

    @Override
    public void removeStudent(String name) throws StudentNotFoundException {
        if (!currentArea.containsStudent(name)) {
            throw new StudentNotFoundException();
        }
        currentArea.removeStudentWithCleanup(name);
    }

    @Override
    public void goToLocation(String studentName, String serviceName) throws StudentNotFoundException, ServiceNotFoundException, AlreadyThereException, EatingIsFullException, NotValidServiceException {

        if (!currentArea.containsService(serviceName)) {
            throw new ServiceNotFoundException();
        }
        if (!currentArea.containsStudent(studentName)) {
            throw new StudentNotFoundException();
        }

        currentArea.moveStudentToService(studentName, serviceName);
    }

    @Override
    public void moveStudentHome(String studentName, String lodgingName) throws StudentNotFoundException, LodgingNotFoundException, LodgingIsFullException, StudentIsThriftyException, AlreadyStudentHomeException {

        if (!currentArea.isLodgingService(lodgingName)) {
            throw new LodgingNotFoundException();
        }
        if (!currentArea.containsStudent(studentName)) {
            throw new StudentNotFoundException();
        }

        currentArea.moveStudentHome(studentName, lodgingName);
    }

    @Override
    public ServiceReadOnly whereIsStudent(String studentName) throws StudentNotFoundException {
        if (!currentArea.containsStudent(studentName)) {
            throw new StudentNotFoundException();
        }
        return currentArea.getStudentCurrentLocation(studentName);
    }

    @Override
    public boolean isStudentDistracted(String studentName, String serviceName) {
        return currentArea.isStudentDistracted(studentName, serviceName);
    }

    @Override
    public Iterator<? extends ServiceReadOnly> listVisitedLocations(String studentName)
            throws StudentNotFoundException, StudentIsThriftyException, NoVisitedLocationsException {

        if (!currentArea.containsStudent(studentName)) {
            throw new StudentNotFoundException();
        }
        if (currentArea.isStudentThrifty(studentName)) {
            throw new StudentIsThriftyException();
        }
        Iterator<Service> visitedIterator = currentArea.getStudentVisitedLocations(studentName);
        if (visitedIterator == null || !visitedIterator.hasNext()) {
            throw new NoVisitedLocationsException();
        }
        return visitedIterator;
    }

    @Override
    public TwoWayIterator< ? extends StudentReadOnly> listUsersInService(String order, String serviceName)
            throws InvalidOrderException, ServiceNotFoundException, ServiceDoesNotControlEntryExitException {

        if (!">".equals(order) && !"<".equals(order)) {
            throw new InvalidOrderException();
        }
        if (!currentArea.containsService(serviceName)) {
            throw new ServiceNotFoundException();
        }
        if (!currentArea.isServiceWithOccupancy(serviceName)) {
            throw new ServiceDoesNotControlEntryExitException();
        }

        TwoWayIterator<Student> it = currentArea.getServiceOccupants(serviceName);

        if ("<".equals(order)) {
            while (it.hasNext()) it.next();
        }
        return it;
    }

    @Override
    public Iterator<? extends ServiceReadOnly> listServicesWithTag(String tag) {
        return currentArea.getServicesCollection().getServicesByTag(tag);
    }

    @Override
    public Iterator<? extends ServiceReadOnly> getRankedServicesByTypeAndStars(ServiceType type, int stars, String studentName)
            throws InvalidStarsException, StudentNotFoundException, NoTypeServicesWithStarsException,
            InvalidServiceTypeException, NoServicesOfThisTypeException {

        if (stars < 1 || stars > 5) {
            throw new InvalidStarsException();
        }
        if (!currentArea.containsStudent(studentName)) {
            throw new StudentNotFoundException();
        }
        if (type==null) {
            throw new InvalidServiceTypeException();
        }

        if (!currentArea.hasServicesOfType(type)) {
            throw new NoServicesOfThisTypeException();
        }

        Iterator<Service> filteredByTypeStars = currentArea.getServicesByTypeAndStars(type, stars);
        if (!filteredByTypeStars.hasNext()) {
            throw new NoTypeServicesWithStarsException();
        }

        return currentArea.getClosestServicesByTypeAndStars(studentName, type, stars);
    }

    @Override
    public ServiceReadOnly findRelevantServiceForStudent(String studentName, ServiceType serviceType)
            throws StudentNotFoundException, InvalidServiceTypeException, NoServicesOfThisTypeException {

        if (serviceType==null) {
            throw new InvalidServiceTypeException();
        }

        if (!currentArea.containsStudent(studentName)) {
            throw new StudentNotFoundException();
        }

        Iterator<Service> typeServicesIterator = currentArea.getServicesByTypeOrderedByStars(serviceType);

        if (!typeServicesIterator.hasNext()) {
            throw new NoServicesOfThisTypeException();
        }

        return currentArea.findRelevantServiceForStudent(studentName, serviceType);
    }

    @Override
    public String getName(AreaReadOnly area) {
        return area.getName();
    }

    @Override
    public String getServiceName(ServiceReadOnly service) {
        return currentArea.getServiceNameProperty(service);
    }

    @Override
    public ServiceType getServiceType(ServiceReadOnly service) {
        return currentArea.getServiceTypeProperty(service);
    }

    @Override
    public long getServiceLatitude(ServiceReadOnly service) {
        return currentArea.getServiceLatitudeProperty(service);
    }

    @Override
    public long getServiceLongitude(ServiceReadOnly service) {
        return currentArea.getServiceLongitudeProperty(service);
    }

    @Override
    public String getStudentName(StudentReadOnly student) {
        return currentArea.getStudentNameProperty(student);
    }

    @Override
    public StudentType getStudentType(StudentReadOnly student) {
        return currentArea.getStudentTypeProperty(student);
    }

    @Override
    public ServiceReadOnly getStudentCurrentLocation(StudentReadOnly student) {
        return currentArea.getStudentCurrentLocation(student);
    }

    @Override
    public ServiceReadOnly getServiceByName(String name) {
        return currentArea.getService(name);
    }

    @Override
    public StudentReadOnly getStudentByName(String name) {
        return currentArea.getStudent(name);
    }

    @Override
    public long manhattanDistance(long lat1, long lon1, long lat2, long lon2) {
        return Math.abs(lat1 - lat2) + Math.abs(lon1 - lon2);
    }

    @Override
    public boolean hasAreaLoaded() {
        return currentArea != null;
    }

    private void saveCurrentAreaToFile(Area area) {
        String filename = getAreaFileName(area.getName());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(area);
        } catch (Exception ignored) {
        }
    }

    private Area loadAreaFromFile(String name) {
        String filename = getAreaFileName(name);
        try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(filename)))) {
            return (Area) ois.readObject();
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String getAreaFileName(String name) {
        return name.toLowerCase().replace(" ", "_") + ".ser";
    }

    private boolean areBoundsValid(long topLat, long leftLong, long bottomLat, long rightLong) {
        return topLat > bottomLat && leftLong < rightLong;
    }

    private boolean validLocation(long lat, long lon) {
        return currentArea.isWithinBounds(lat, lon);
    }

    private boolean isStudentTypeValid(StudentType type) {
        return type == THRIFTY || type == StudentType.OUTGOING || type == StudentType.BOOKISH;
    }
}
