import Exceptions.*;
import Services.*;
import Services.ServiceType;
import Students.Student;
import Students.*;
import dataStructures.*;
import java.io.*;

import static Students.StudentType.THRIFTY;

/**
 * Implements the {@link SystemManager} interface.
 * <p>
 * This class is the core engine of the application, acting as a Facade to
 * manage the currently active {@link Area}, and orchestrate all operations
 * related to students, services, and their interactions. It handles all
 * business logic, validation, and persistence (saving/loading) of area data.
 */
public class SystemManagerImpl implements SystemManager {


    /**
     * The currently active {@link Area} being managed by the system.
     * All operations are performed on this area.
     */
    Area currentArea;

    // --- Constructor ---

    /**
     * Constructs a new SystemManager.
     * Initializes the system with no area loaded.
     */
    public SystemManagerImpl() {
        this.currentArea = null;
    }

    // --- Area Lifecycle Management ---

    /**
     * {@inheritDoc}
     * Saves the current area (if one exists) before creating and saving the new one.
     */
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

    /**
     * {@inheritDoc}
     * Saves the current area (if one exists) before loading the new one.
     */
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




    /**
     * {@inheritDoc}
     */
    @Override
    public void saveArea() throws NoAreaLoadedException {
        if (currentArea == null) {
            throw new NoAreaLoadedException();
        }
        saveCurrentAreaToFile(currentArea);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Area getCurrentArea() throws NoAreaLoadedException {
        if (currentArea == null) {
            throw new NoAreaLoadedException();
        }
        return currentArea;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equalBounds(long topLat, long leftLong, long bottomLat, long rightLong) {
        return currentArea != null &&
                currentArea.getTopLat() == topLat &&
                currentArea.getLeftLong() == leftLong &&
                currentArea.getBottomLat() == bottomLat &&
                currentArea.getRightLong() == rightLong;
    }


    /**
     * {@inheritDoc}
     * This implementation validates all parameters before creating and adding the service
     * to the current area.
     */
    @Override
    public void addService(ServiceType type, String name, long lat, long lon, int price, int value) throws InvalidServiceTypeException, InvalidLocationException, InvalidMenuPriceException, InvalidRoomPriceException, InvalidTicketPriceException, InvalidDiscountPriceException, InvalidCapacityException, ServiceAlreadyExistsException {

        if (type==null) {
            throw new InvalidServiceTypeException();
        }
        if (!validLocation(lat, lon)) {
            throw new InvalidLocationException();
        }

        Service service = createService(name, lat, lon, price, type, value);
        currentArea.addService(service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addReviewToService(String serviceName, int rating, String comment)
            throws ServiceNotFoundException, InvalidStarsException {
        if (rating < 1 || rating > 5) {
            throw new InvalidStarsException();
        }
        Service service = currentArea.getService(serviceName);
        if (service == null) {
            throw new ServiceNotFoundException();
        }

        int oldAvgStar = service.getAvgStar();
        service.addReview(rating, comment);
        int newAvgStar = service.getAvgStar();
        if (newAvgStar != oldAvgStar) {
            currentArea.updateRankingByStars(service, oldAvgStar);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<? extends ServiceReadOnly> listServices() throws NoServicesException {
        if (currentArea.getNumberOfServices() == 0) {
            throw new NoServicesException();
        }
        return currentArea.getServices();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<? extends ServiceReadOnly> getRankedServices() {
        return currentArea.getRankedServices();
    }


    /**
     * {@inheritDoc}
     * This implementation validates all parameters before creating and adding the student
     * to the current area.
     */
    @Override
    public void addStudent(StudentType type, String name, String country, String lodgingName) throws SystemBoundsNotDefinedException, InvalidStudentTypeException, LodgingNotFoundException, StudentAlreadyExistsException, LodgingIsFullException {

        if (currentArea == null) {
            throw new SystemBoundsNotDefinedException();
        }
        Service lodging = currentArea.getService(lodgingName);
        if (!(lodging instanceof Lodging)) {
            throw new LodgingNotFoundException();
        }

        if (!isStudentTypeValid(type)) {
            throw new InvalidStudentTypeException();
        }

        Student student = createStudentByType(type, name, country, (Lodging) lodging);
        currentArea.addStudent(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<? extends StudentReadOnly> listStudents(String filter) {
        if (filter.equalsIgnoreCase("all")) {
            return currentArea.listAllStudents();
        } else {
            return currentArea.listStudentsByCountry(filter);
        }
    }

    /**
     * {@inheritDoc}
     * Also removes the student from their current service's occupant list (if applicable).
     */
    @Override
    public void removeStudent(String name) throws StudentNotFoundException {
        Student student = currentArea.getStudent(name);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        if (student.getCurrent() instanceof Eating eating) {
            eating.removeOccupant(student);
        } else if (student.getCurrent() instanceof Lodging lodging) {
            lodging.removeOccupant(student);
        }
        currentArea.removeStudent(name);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void goToLocation(String studentName, String serviceName) throws StudentNotFoundException, ServiceNotFoundException, AlreadyThereException, EatingIsFullException, NotValidServiceException {

        Service service = currentArea.getService(serviceName);
        if (service == null) {
            throw new ServiceNotFoundException();
        }
        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }

        student.goToLocation(service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveStudentHome(String studentName, String lodgingName) throws StudentNotFoundException, LodgingNotFoundException, LodgingIsFullException, StudentIsThriftyException, AlreadyStudentHomeException {

        Service service = currentArea.getService(lodgingName);
        if (!(service instanceof Lodging lodging)) {
            throw new LodgingNotFoundException();
        }
        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }

        student.moveHome(lodging);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceReadOnly whereIsStudent(String studentName) throws StudentNotFoundException {
        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        return student.getCurrent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStudentDistracted(String studentName, String serviceName) {
        Student student = currentArea.getStudent(studentName);
        Service service = currentArea.getService(serviceName);
        return student instanceof Thrifty thrifty
                && service instanceof Eating eating
                && thrifty.isDistracted(eating);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<? extends ServiceReadOnly> listVisitedLocations(String studentName)
            throws StudentNotFoundException, StudentIsThriftyException, NoVisitedLocationsException {

        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        if (student instanceof Thrifty) {
            throw new StudentIsThriftyException();
        }
        Iterator<Service> visitedIterator = student.getVisitedIterator();
        if (!visitedIterator.hasNext()) {
            throw new NoVisitedLocationsException();
        }
        return visitedIterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TwoWayIterator< ? extends StudentReadOnly> listUsersInService(String order, String serviceName)
            throws InvalidOrderException, ServiceNotFoundException, ServiceDoesNotControlEntryExitException {

        if (!">".equals(order) && !"<".equals(order)) {
            throw new InvalidOrderException();
        }
        Service service = currentArea.getService(serviceName);
        if (service == null) {
            throw new ServiceNotFoundException();
        }
        if (!(service instanceof Eating) && !(service instanceof Lodging)) {
            throw new ServiceDoesNotControlEntryExitException();
        }

        TwoWayIterator<Student> it = (service instanceof Eating)
                ? ((Eating) service).getOccupantsIterator()
                : ((Lodging) service).getOccupantsIterator();


        if ("<".equals(order)) {
            while (it.hasNext()) it.next();
        }
        return it;
    }

    /**
     * {@inheritDoc}
     * Optimized implementation using the tagMap for O(1) lookup instead of O(n*m) iteration.
     */
    @Override
    public Iterator<? extends ServiceReadOnly> listServicesWithTag(String tag) {
        return currentArea.getServicesCollection().getServicesByTag(tag);
    }

    /**
     * {@inheritDoc}
     * Optimized implementation using the servicesByTypeAndStars index for O(1) lookup.
     */
    @Override
    public Iterator<? extends ServiceReadOnly> getRankedServicesByTypeAndStars(ServiceType type, int stars, String studentName)
            throws InvalidStarsException, StudentNotFoundException, NoTypeServicesWithStarsException,
            InvalidServiceTypeException, NoServicesOfThisTypeException {

        if (stars < 1 || stars > 5) {
            throw new InvalidStarsException();
        }
        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        if (type==null) {
            throw new InvalidServiceTypeException();
        }

        if (!currentArea. hasServicesOfType(type)) {
            throw new NoServicesOfThisTypeException();
        }

        Iterator<Service> filteredByTypeStars = currentArea.getServicesByTypeAndStars(type, stars);
        if (!filteredByTypeStars.hasNext()) {
            throw new NoTypeServicesWithStarsException();
        }

        List<Service> closestServices = new DoublyLinkedList<>();
        long minDistance = Long.MAX_VALUE;

        while (filteredByTypeStars.hasNext()) {
            Service service = filteredByTypeStars.next();
            long currentDistance = manhattanDistance(
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

    /**
     * {@inheritDoc}
     * This implementation delegates the selection logic to the student object
     * ({@link Student#findMostRelevant(Iterator)}).
     */
    @Override
    public ServiceReadOnly findRelevantServiceForStudent(String studentName, ServiceType serviceType)
            throws StudentNotFoundException, InvalidServiceTypeException, NoServicesOfThisTypeException {

        if (serviceType==null) {
            throw new InvalidServiceTypeException();
        }

        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }

        Iterator<Service> typeServicesIterator = currentArea.getServicesByTypeOrderedByStars(serviceType);

        if (!typeServicesIterator.hasNext()) {
            throw new NoServicesOfThisTypeException();
        }


        return student.findMostRelevant(typeServicesIterator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(Area area) {
        return area.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServiceName(ServiceReadOnly service) {
        return service.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceType getServiceType(ServiceReadOnly service) {
        return service.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getServiceLatitude(ServiceReadOnly service) {
        return service.getLatitude();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getServiceLongitude(ServiceReadOnly service) {
        return service.getLongitude();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStudentName(StudentReadOnly student) {
        return student.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentType getStudentType(StudentReadOnly student) {
        return student.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceReadOnly getStudentCurrentLocation(StudentReadOnly student) {
        return student.getCurrent();
    }

    /**
     * Finds a service by its name.
     * (Not declared in the interface but required by Main).
     *
     * @param name The name of the service to find (case-insensitive).
     * @return The {@link Service} object, or {@code null} if not found.
     */
    public ServiceReadOnly getServiceByName(String name) {
        return currentArea.getService(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentReadOnly getStudentByName(String name) {
        return currentArea.getStudent(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long manhattanDistance(long lat1, long lon1, long lat2, long lon2) {
        return Math.abs(lat1 - lat2) + Math.abs(lon1 - lon2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAreaLoaded() {
        return currentArea != null;
    }

    // --- Private Helper Methods ---

    /**
     * Saves the given area to a serialized file.
     * The filename is generated based on the area's name.
     *
     * @param area The area to save.
     */
    private void saveCurrentAreaToFile(Area area) {
        String filename = getAreaFileName(area.getName());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(area);
        } catch (Exception ignored) {
        }
    }

    /**
     * Loads an area from a serialized file.
     *
     * @param name The name of the area to load.
     * @return The loaded {@link Area} object, or {@code null} if the file doesn't exist or an error occurs.
     */
    private Area loadAreaFromFile(String name) {
        String filename = getAreaFileName(name);
        try (ObjectInputStream ois = new ObjectInputStream((new FileInputStream(filename)))) {
            return (Area) ois.readObject();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Generates a file-safe name for an area.
     * Converts to lowercase and replaces spaces with underscores.
     *
     * @param name The original area name.
     * @return The formatted file name (e.g., "lisbon_area.ser").
     */
    private static String getAreaFileName(String name) {
        return name.toLowerCase().replace(" ", "_") + ".ser";
    }


    /**
     * Checks if the given coordinates form a valid bounding box.
     *
     * @param topLat    The top latitude.
     * @param leftLong  The left longitude.
     * @param bottomLat The bottom latitude.
     * @param rightLong The right longitude.
     * @return true if top > bottom and left < right, false otherwise.
     */
    private boolean areBoundsValid(long topLat, long leftLong, long bottomLat, long rightLong) {
        return topLat > bottomLat && leftLong < rightLong;
    }

    /**
     * Checks if a given coordinate is within the `currentArea` bounds.
     * Assumes `currentArea` is not null.
     *
     * @param lat The latitude to check.
     * @param lon The longitude to check.
     * @return true if the location is valid, false otherwise.
     */
    private boolean validLocation(long lat, long lon) {
        return currentArea.isWithinBounds(lat, lon);
    }




    /**
     * Checks if the {@link StudentType} is valid.
     *
     * @param type The StudentType enum.
     * @return true if the type is THRIFTY, OUTGOING, or BOOKISH.
     */
    private boolean isStudentTypeValid(StudentType type) {
        return type == THRIFTY || type == StudentType.OUTGOING || type == StudentType.BOOKISH;
    }


    /**
     * Factory method to create a new {@link Service} instance based on its type.
     *
     * @param name  The name of the service.
     * @param lat   The latitude.
     * @param lon   The longitude.
     * @param price The price.
     * @param type  The {@link ServiceType} enum.
     * @param value The value (capacity or discount).
     * @return A new {@link Service} (e.g., EatingImpl, LodgingImpl, LeisureImpl).
     * @throws InvalidMenuPriceException     if the menu price is invalid.
     * @throws InvalidRoomPriceException     if the room price is invalid.
     * @throws InvalidTicketPriceException   if the ticket price is invalid.
     * @throws InvalidCapacityException      if the capacity is invalid.
     * @throws InvalidDiscountPriceException if the discount price is invalid.
     */
    private Service createService(String name, long lat, long lon, int price, ServiceType type, int value) throws InvalidMenuPriceException, InvalidRoomPriceException, InvalidTicketPriceException,
            InvalidCapacityException, InvalidDiscountPriceException{
        return switch (type) {
            case EATING -> new EatingImpl(name, lat, lon, price, value);
            case LODGING -> new LodgingImpl(name, lat, lon, price, value);
            case LEISURE -> new LeisureImpl(name, lat, lon, price, value);
        };
    }

    /**
     * Factory method to create a new {@link Student} instance based on its type.
     *
     * @param type The {@link StudentType} enum.
     * @param name The name of the student.
     * @param country The student's country.
     * @param lodging The student's home lodging.
     * @return A new {@link Student} (e.g., BookishImpl, ThriftyImpl, OutgoingImpl).
     * @throws LodgingIsFullException if the lodging is at capacity.
     */
    private Student createStudentByType(StudentType type, String name, String country, Lodging lodging) throws LodgingIsFullException {
        return switch (type) {
            case BOOKISH -> new BookishImpl(name, country, lodging);
            case THRIFTY -> new ThriftyImpl(name, country, lodging);
            case OUTGOING -> new OutgoingImpl(name, country, lodging);
        };

    }
}
