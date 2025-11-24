import Exceptions.*;
import Services.*;
import Services.ServiceType;
import Students.Student;
import Students.*;
import dataStructures.*;

import java.io.File;
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

    // --- Fields ---

    /**
     * A counter for the number of areas managed (not currently used).
     */
    int areaCount;

    /**
     * The currently active {@link Area} being managed by the system.
     * All operations are performed on this area.
     */
    Area currentArea;

    /**
     * A list to store the names of all known areas (not currently used).
     */
    List<String> areasNames;

    // --- Constructor ---

    /**
     * Constructs a new SystemManager.
     * Initializes the system with no area loaded.
     */
    public SystemManagerImpl() {
        this.areaCount = 0;
        this.currentArea = null;
        this.areasNames = new ListInArray<>(100);
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
        saveCurrentAreaToFile(currentArea);
    }

    /**
     * {@inheritDoc}
     * Saves the current area (if one exists) before loading the new one.
     */
    @Override
    public void loadArea(String name) throws BoundsNotFoundException {
        if (currentArea != null) {
            // Guardar sempre a área corrente (independentemente de o load falhar ou não)
            saveCurrentAreaToFile(currentArea);
        }

        Area loadedArea = loadAreaFromFile(name);
        if (loadedArea != null) {
            currentArea = loadedArea;
        } else {
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
        } else return currentArea;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equalBounds(long topLat, long leftLong, long bottomLat, long rightLong) {
        if (currentArea == null) {
            return false;
        } else {
            return currentArea.getTopLat() == topLat &&
                    currentArea.getLeftLong() == leftLong &&
                    currentArea.getBottomLat() == bottomLat &&
                    currentArea.getRightLong() == rightLong;
        }
    }

    // --- Service Management ---

    /**
     * {@inheritDoc}
     * This implementation validates all parameters before creating and adding the service
     * to the current area.
     */
    @Override
    public void addService(ServiceType type, String name, long lat, long lon, int price, int value)
            throws InvalidServiceTypeException, InvalidLocationException, InvalidMenuPriceException,
            InvalidRoomPriceException, InvalidTicketPriceException, InvalidDiscountPriceException,
            InvalidCapacityException, ServiceAlreadyExistsException {

        if (!validServiceType(type)) {
            throw new InvalidServiceTypeException();
        }
        if (!validLocation(lat, lon)) {
            throw new InvalidLocationException();
        }
        if (price <= 0) {
            switch (type) {
                case EATING -> throw new InvalidMenuPriceException();
                case LODGING -> throw new InvalidRoomPriceException();
                case LEISURE -> throw new InvalidTicketPriceException();
            }
        }
        if (!(0 <= value && value <= 100) && type == ServiceType.LEISURE) {
            throw new InvalidDiscountPriceException();
        }
        if (value <= 0 && (type == ServiceType.LODGING || type == ServiceType.EATING)) {
            throw new InvalidCapacityException();
        }
        if (currentArea.containsService(name)) {
            throw new ServiceAlreadyExistsException();
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
        Service service = currentArea.getService(serviceName);
        if (rating < 1 || rating > 5) {
            throw new InvalidStarsException();
        }
        if (service == null) {
            throw new ServiceNotFoundException();
        }

        // Check if the star rating changes to trigger a ranking update
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
    public Iterator<Service> listServices() throws NoServicesException {
        if (currentArea.getNumberOfServices() == 0) {
            throw new NoServicesException();
        }
        return currentArea.getServices();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Service> getRankedServices() {
        return currentArea.getRankedServices();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfServices() {
        return currentArea.getNumberOfServices();
    }

    // --- Student Management ---

    /**
     * {@inheritDoc}
     * This implementation validates all parameters before creating and adding the student
     * to the current area.
     */
    @Override
    public void addStudent(StudentType type, String name, String country, String lodgingName)
            throws SystemBoundsNotDefinedException, InvalidStudentTypeException,
            LodgingNotFoundException, StudentAlreadyExistsException, LodgingIsFullException {
        if (currentArea == null) {
            throw new SystemBoundsNotDefinedException();
        }
        Service lodging = currentArea.getService(lodgingName);
        if (!(lodging instanceof Lodging)) {//lodging == null
            throw new LodgingNotFoundException();
        }
        if (((Lodging) lodging).isFull()) {
            throw new LodgingIsFullException();
        }
        if (!isStudentTypeValid(type)) {
            throw new InvalidStudentTypeException();
        }
        if (studentAlreadyExists(name)) {
            throw new StudentAlreadyExistsException();
        }
        Student student = createStudentByType(type, name, country, (Lodging) lodging);
        currentArea.addStudent(student);
        //((Lodging)lodging).addOccupant(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Student> listStudents(String filter) {
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
        // Before removing from the area, remove from their current location's occupant list
        if (student.getCurrent() instanceof Eating eating) {
            eating.removeOccupant(student);
        } else if (student.getCurrent() instanceof Lodging lodging) {
            lodging.removeOccupant(student);
        }
        currentArea.removeStudent(name);
    }

    // --- Student & Service Interaction (Actions) ---

    /**
     * {@inheritDoc}
     */
    @Override
    public void goToLocation(String studentName, String serviceName)
            throws StudentNotFoundException, ServiceNotFoundException,
            AlreadyThereException, EatingIsFullException, NotValidServiceException {

        if (!currentArea.containsService(serviceName)) {
            throw new ServiceNotFoundException();
        }
        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        Service service = currentArea.getService(serviceName);
        if (service instanceof Lodging) {
            throw new NotValidServiceException();
        }
        if (student.getCurrent().getName().equals(serviceName)) {
            throw new AlreadyThereException();
        }
        if (service instanceof Eating eating) {
            if (!eating.hasCapacity()) {
                throw new EatingIsFullException();
            }
        }
        student.goToLocation(service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveStudentHome(String studentName, String lodgingName)
            throws StudentNotFoundException, LodgingNotFoundException,
            LodgingIsFullException, StudentIsThriftyException, AlreadyStudentHomeException {

        Service service = currentArea.getService(lodgingName);
        if (!(service instanceof Lodging lodging)) {
            throw new LodgingNotFoundException();
        }
        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        if (student.getHome() == lodging) {
            throw new AlreadyStudentHomeException();
        }
        if (lodging.isFull()) {
            throw new LodgingIsFullException();
        }
        if (student instanceof Thrifty thrifty) {
            if (!thrifty.canMoveTo(lodging)) {
                throw new StudentIsThriftyException();
            }
        }
        student.moveHome(lodging);
    }

    // --- System Queries (Reports) ---

    /**
     * {@inheritDoc}
     */
    @Override
    public Service whereIsStudent(String studentName) throws StudentNotFoundException {
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
        if (student instanceof Thrifty && service instanceof Eating eatingService) {
            return ((Thrifty) student).isDistracted(eatingService);
        } else return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Service> listVisitedLocations(String studentName)
            throws StudentNotFoundException, StudentIsThriftyException,
            NoVisitedLocationsException {

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
    public TwoWayIterator<Student> listUsersInService(String order, String serviceName)
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

        // Get the appropriate iterator from the service
        TwoWayIterator<Student> it = (service instanceof Eating)
                ? ((Eating) service).getOccupantsIterator()
                : ((Lodging) service).getOccupantsIterator();

        // If reverse order ("<"), fast-forward the iterator to the end
        // so that .previous() can be called.
        if ("<".equals(order)) {
            while (it.hasNext()) it.next();
        }
        return it;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Service> listServicesWithTag(String tag) {
        return new FilterIterator<>(currentArea.getServices(), s -> s.hasEvaluationWithTag(tag));
    }

    /**
     * {@inheritDoc}
     * Note: This implementation iterates all services from the insertion-order list
     * (`currentArea.getServices()`). Its tie-breaking order for distance
     * is therefore by insertion, not by ranking-update time.
     */
    @Override
    public Iterator<Service> getRankedServicesByTypeAndStars(ServiceType type, int stars, String studentName)
            throws InvalidStarsException, StudentNotFoundException, NoTypeServicesWithStarsException,
            InvalidServiceTypeException, NoServicesOfThisTypeException {

        if (stars < 1 || stars > 5) {
            throw new InvalidStarsException();
        }
        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }
        if (!validServiceType(type)) {
            throw new InvalidServiceTypeException();
        }

        // First pass: check if any of this type exist
        FilterIterator<Service> filteredByType = new FilterIterator<>(currentArea.getServices(), s -> s.getType() == type);
        if (!filteredByType.hasNext()) {
            throw new NoServicesOfThisTypeException();
        }

        // Second pass: check if any match the star rating
        FilterIterator<Service> filteredByTypeStars = new FilterIterator<>(currentArea.getServices(), s -> s.getType() == type && s.getAvgStar() == stars);
        if (!filteredByTypeStars.hasNext()) {
            throw new NoTypeServicesWithStarsException();
        }

        // Third pass: find the closest
        List<Service> closestServices = new ListInArray<>(420);
        long minDistance = Long.MAX_VALUE;

        // Reset iterator for the third pass
        filteredByTypeStars.rewind();

        while (filteredByTypeStars.hasNext()) {
            Service service = filteredByTypeStars.next();
            long currentDistance = manhattanDistance(
                    student.getCurrent().getLatitude(),
                    student.getCurrent().getLongitude(),
                    service.getLatitude(),
                    service.getLongitude());

            if (currentDistance < minDistance) {
                // Encontrou um mais próximo — limpa a lista e guarda apenas este
                closestServices = new ListInArray<>(420);
                closestServices.addLast(service);
                minDistance = currentDistance;
            } else if (currentDistance == minDistance) {
                // Outro serviço à mesma distância — adiciona também
                closestServices.addLast(service);
            }
        }

        // Retorna um iterador apenas com os serviços mais próximos
        return closestServices.iterator();
    }

    /**
     * {@inheritDoc}
     * This implementation delegates the selection logic to the student object
     * ({@link Student#findMostRelevant(Iterator)}).
     */
    @Override
    public Service findRelevantServiceForStudent(String studentName, ServiceType serviceType)
            throws StudentNotFoundException, InvalidServiceTypeException, NoServicesOfThisTypeException {

        if (!validServiceType(serviceType)) {
            throw new InvalidServiceTypeException();
        }

        Student student = currentArea.getStudent(studentName);
        if (student == null) {
            throw new StudentNotFoundException();
        }

        // Filter services by the desired type
        Iterator<Service> allServices = currentArea.getServices();
        Iterator<Service> filteredServices = new FilterIterator<>(allServices, s -> s.getType() == serviceType);

        if (!filteredServices.hasNext()) {
            throw new NoServicesOfThisTypeException();
        }

        // Delegate the "relevance" logic to the student
        Service relevantService = student.findMostRelevant(filteredServices);

        return relevantService;
    }

    // --- Property Getters (Convenience) ---

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTopLat(Area area) {
        return area.getTopLat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLeftLong(Area area) {
        return area.getLeftLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getBottomLat(Area area) {
        return area.getBottomLat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getRightLong(Area area) {
        return area.getRightLong();
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
    public String getServiceName(Service service) {
        return service.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceType getServiceType(Service service) {
        return service.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getServiceLatitude(Service service) {
        return service.getLatitude();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getServiceLongitude(Service service) {
        return service.getLongitude();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStudentName(Student student) {
        return student.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentType getStudentType(Student student) {
        return student.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Service getStudentCurrentLocation(Student student) {
        return student.getCurrent();
    }

    /**
     * Finds a service by its name.
     * (Not declared in the interface but required by Main).
     *
     * @param name The name of the service to find (case-insensitive).
     * @return The {@link Service} object, or {@code null} if not found.
     */
    public Service getServiceByName(String name) {
        return currentArea.getService(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudentByName(String name) {
        return currentArea.getStudent(name);
    }

    // --- Utility Methods ---

    /**
     * {@inheritDoc}
     */
    @Override
    public long manhattanDistance(long lat1, long lon1, long lat2, long lon2) {
        return Math.abs(lat1 - lat2) + Math.abs(lon1 - lon2);
    }

    // --- Private Helper Methods ---

    // --- Private File I/O Helpers ---

    /**
     * Saves the specified Area object to a serialized file in the "data/" directory.
     *
     * @param area The Area object to save.
     */
    private void saveCurrentAreaToFile(Area area) {
        String filename = "data/" + getAreaFileName(area.getName());
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs(); // Criar diretório se não existir
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(area);
            oos.flush();
        } catch (IOException ignored) {
            // Errors on save are ignored
        }
    }

    /**
     * Loads an Area object from a serialized file in the "data/" directory.
     *
     * @param name The name of the area to load.
     * @return The deserialized Area object, or null if loading fails.
     */
    private Area loadAreaFromFile(String name) {
        String filename = "data/" + getAreaFileName(name);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Area) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null; // File not found or corrupt
        }
    }

    /**
     * Generates a file-safe name for an area.
     * Converts to lowercase and replaces spaces with underscores.
     *
     * @param name The original area name.
     * @return The formatted file name (e.g., "lisbon_area.ser").
     */
    private String getAreaFileName(String name) {
        return name.toLowerCase().replace(" ", "_") + ".ser";
    }

    /**
     * Checks if a serialized file for an area exists.
     *
     * @param name The name of the area.
     * @return true if the .ser file exists, false otherwise.
     */
    private boolean areaFileExists(String name) {
        String filename = getAreaFileName(name);
        return new File(filename).exists();
    }


    // --- Private Validation Helpers ---

    /**
     * Checks if the given coordinates form a valid bounding box.
     *
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
        return (currentArea.isWithinBounds(lat, lon));
    }

    /**
     * Checks if the {@link ServiceType} is valid.
     *
     * @param type The ServiceType enum.
     * @return true if the type is EATING, LODGING, or LEISURE.
     */
    private boolean validServiceType(ServiceType type) {
        return (type == ServiceType.EATING || type == ServiceType.LODGING || type == ServiceType.LEISURE);
    }

    /**
     * Checks if a student with the given name already exists in the `currentArea`.
     *
     * @param name The name to check.
     * @return true if the student exists, false otherwise.
     */
    private boolean studentAlreadyExists(String name) {
        return currentArea.getStudent(name) != null;
    }

    /**
     * Checks if the {@link StudentType} is valid.
     *
     * @param type The StudentType enum.
     * @return true if the type is THRIFTY, OUTGOING, or BOOKISH.
     */
    private boolean isStudentTypeValid(StudentType type) {
        return (type == THRIFTY || type == StudentType.OUTGOING || type == StudentType.BOOKISH);
    }

    /**
     * Checks if a service has a specific tag.
     *
     * @param service The service to check.
     * @param tag     The tag to search for.
     * @return true if the service has the tag, false otherwise.
     */
    private boolean serviceHasTag(Service service, String tag) {
        return service.hasEvaluationWithTag(tag);
    }


    // --- Private Factory Helpers ---

    /**
     * Factory method to create a new {@link Service} instance based on its type.
     *
     * @param name  The name of the service.
     * @param lat   The latitude.
     * @param lon   The longitude.
     * @param price The price.
     * @param type  The {@link ServiceType} enum.
     * @param value The value (capacity or discount).
     * @return A new {@link Service} (e.g., EatingImpl, LodgingImpl).
     */
    private Service createService(String name, long lat, long lon, int price, ServiceType type, int value) {
        return switch (type) {
            case EATING -> new EatingImpl(name, lat, lon, price, value);
            case LODGING -> new LodgingImpl(name, lat, lon, price, value);
            case LEISURE -> new LeisureImpl(name, lat, lon, price, value);
            default -> null; // Should be unreachable if validServiceType is used
        };
    }

    /**
     * Factory method to create a new {@link Student} instance based on its type.
     *
     * @param type        The {@link StudentType} enum.
     * @param name        The name of the student.
     * @param country     The student's country.
     * @param lodgingName The student's home lodging.
     * @return A new {@link Student} (e.g., BookishImpl, ThriftyImpl).
     */
    private Student createStudentByType(StudentType type, String name, String country, Lodging lodgingName) {
        return switch (type) {
            case BOOKISH -> new BookishImpl(name, country, lodgingName);
            case THRIFTY -> new ThriftyImpl(name, country, lodgingName);
            case OUTGOING -> new OutgoingImpl(name, country, lodgingName);
            default -> null; // Should be unreachable if isStudentTypeValid is used
        };
    }
}