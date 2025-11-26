import Exceptions.*;
import java.util.Scanner;
import Services.*;
import Services.ServiceType;
import Students.Student;
import Students.StudentType;
import dataStructures.*;


// rankingByStars, listInarray com doubly linked la dentro
// teste


/**
 * Main entry point for the "Home Away From Home" application.
 * This class is responsible for:
 * 1. The main application loop (REPL - Read-Eval-Print Loop).
 * 2. Parsing user input from the console (using {@link Scanner}).
 * 3. Delegating all business logic to a {@link SystemManager}.
 * 4. Handling all exceptions thrown by the manager and translating
 * them into user-friendly messages (via the {@link Message} enum).
 * 5. Printing all output (success messages and errors) to the console.
 */
public class Main {

    /**
     * Main method. Initializes the system and starts the command loop.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SystemManager manager = new SystemManagerImpl();
        Scanner scanner = new Scanner(System.in);
        String command;

        do {
            command = scanner.next();
            Commands cmd = Commands.fromString(command, scanner);

            boolean hasArea;
            try {
                manager.getCurrentArea();
                hasArea = true;
            } catch (NoAreaLoadedException e) {
                hasArea = false;
            }

            if (cmd == null && hasArea) {
                System.out.println(Message.UNKNOWN_COMMAND);
                continue;
            }

            if (!hasArea) {
                // Only these commands are allowed when no bounds are defined
                switch (cmd) {
                    case EXIT -> handleExit(manager);
                    case BOUNDS -> handleBounds(scanner, manager);
                    case LOAD -> handleLoad(scanner, manager);
                    case HELP -> handleHelp();
                    case null -> {}
                    default -> System.out.println(Message.SYSTEM_BOUNDS_NOT_DEFINED);
                }
            } else {
                // All commands are allowed when bounds are defined
                switch (cmd) {
                    case EXIT -> handleExit(manager);
                    case BOUNDS -> handleBounds(scanner, manager);
                    case SAVE -> handleSave(manager);
                    case LOAD -> handleLoad(scanner, manager);
                    case SERVICE -> handleService(scanner, manager);
                    case SERVICES -> handleServices(manager);
                    case STUDENT -> handleStudent(scanner, manager);
                    case STUDENTS -> handleStudents(scanner, manager);
                    case LEAVE -> handleLeave(manager, scanner);
                    case GO -> handleGo(manager, scanner);
                    case MOVE -> handleMove(scanner, manager);
                    case USERS -> handleUsers(scanner, manager);
                    case WHERE -> handleWhere(scanner, manager);
                    case VISITED -> handleVisited(scanner, manager);
                    case STAR -> handleStar(scanner, manager);
                    case RANKING -> handleRanking(manager);
                    case RANKED -> handleRanked(scanner, manager);
                    case TAG -> handleTag(scanner, manager);
                    case FIND -> handleFind(scanner, manager);
                    case HELP -> handleHelp();
                    default -> System.out.println(Message.UNKNOWN_COMMAND);
                }
            }

        } while (!command.equalsIgnoreCase("exit"));

    }

    // --- Private Enums for Commands and Messages ---

    /**
     * Enumerates all valid application commands.
     * Provides a parser to convert raw string input into a command.
     */
    public enum Commands {
        BOUNDS,
        SAVE,
        LOAD,
        SERVICE,
        SERVICES,
        STUDENT,
        STUDENTS,
        LEAVE,
        GO,
        MOVE,
        USERS,
        STAR,
        WHERE,
        VISITED,
        RANKING,
        RANKED,
        TAG,
        FIND,
        HELP,
        EXIT;

        /**
         * Parses a raw string into a {@code Commands} enum constant.
         *
         * @param command The raw command string from the user.
         * @param scanner The scanner (for potential future use, e.g., consuming extra tokens).
         * @return The matching {@code Commands} constant, or {@code null} if no match.
         */
        public static Commands fromString(String command, Scanner scanner) {
            for (Commands e : Commands.values()) {
                if (e.name().equalsIgnoreCase(command.trim())) {
                    return e;
                }
            }
            return null;
        }
    }

    /**
     * Enumerates all user-facing strings (messages, errors, prompts).
     * This centralizes all application text for easy maintenance and localization.
     */
    private enum Message {
        // --- General Messages ---
        UNKNOWN_COMMAND("Unknown command. Type help to see available commands."),
        SYSTEM_BOUNDS_NOT_DEFINED("System bounds not defined."),
        EXIT("Bye!"),

        // --- Help Text ---
        HELP_TEXT("""
                bounds - Defines the new geographic bounding rectangle
                save - Saves the current geographic bounding rectangle to a text file
                load - Load a geographic bounding rectangle from a text file
                service - Adds a new service to the current geographic bounding rectangle. The service may be eating, lodging or leisure
                services - Displays the list of services in current geographic bounding rectangle, in order of registration
                student - Adds a student to the current geographic bounding rectangle
                students - Lists all the students or those of a given country in the current geographic bounding rectangle, in alphabetical order of the student's name
                leave - Removes a student from the the current geographic bounding rectangle
                go - Changes the location of a student to a leisure service, or eating service
                move - Changes the home of a student
                users - List all students who are in a given service (eating or lodging)
                star - Evaluates a service
                where - Locates a student
                visited - Lists locations visited by one student
                ranking - Lists services ordered by star
                ranked - Lists the service(s) of the indicated type with the given score that are closer to the student location
                tag - Lists all services that have at least one review whose description contains the specified word
                find - Finds the most relevant service of a certain type, for a specific student
                help - Shows the available commands
                exit - Terminates the execution of the program"""),

        // --- Area Command Messages ---
        AREA_CREATED("%s created."),
        INVALID_BOUNDS("Invalid bounds."),
        BOUNDS_ALREADY_EXISTS("Bounds already exists. Please load it!"),
        AREA_SAVED("%s saved."),
        AREA_LOADED("%s loaded."),
        BOUNDS_NOT_FOUND("Bounds %s does not exists."),

        // --- Service Command Messages ---
        SERVICE_ADDED("%s %s added."),
        INVALID_SERVICE_TYPE("Invalid service type!"),
        INVALID_LOCATION("Invalid location!"),
        INVALID_MENU_PRICE("Invalid menu price!"),
        INVALID_ROOM_PRICE("Invalid room price!"),
        INVALID_TICKET_PRICE("Invalid ticket price."),
        INVALID_DISCOUNT_PRICE("Invalid discount price!"),
        INVALID_CAPACITY("Invalid capacity!"),
        SERVICE_ALREADY_EXISTS("%s already exists!"),
        NO_SERVICES("No services yet!"),

        // --- Student Command Messages ---
        STUDENT_ADDED("%s added."),
        INVALID_STUDENT_TYPE("Invalid student type!"),
        LODGING_NOT_FOUND("lodging %s does not exist!%n"),
        LODGING_IS_FULL("lodging %s is full!%n"),
        STUDENT_ALREADY_EXISTS("%s already exists!%n"),
        NO_STUDENTS("No students yet!"),
        NO_STUDENTS_FROM("No students from %s!%n"),
        STUDENT_LEFT("%s has left."),
        STUDENT_NOT_FOUND("%s does not exist!%n"),

        // --- Action Command Messages ---
        STUDENT_GO_OK("%s is now at %s.%n"),
        STUDENT_GO_DISTRACTED("%s is now at %s. %s is distracted!%n"),
        UNKNOWN_SERVICE("Unknown %s!%n"),
        ALREADY_THERE("Already there!"),
        EATING_FULL("eating %s is full!%n"),
        NOT_VALID_SERVICE("%s is not a valid service!%n"),
        STUDENT_MOVE_OK("lodging %s is now %s's home. %s is at home.%n"),
        ALREADY_STUDENT_HOME("That is %s's home!%n"),
        MOVE_NOT_ACCEPTABLE("Move is not acceptable for %s!%n"),
        INVALID_ORDER("This order does not exists!"),
        NO_STUDENTS_ON_SERVICE("No students on %s!%n"),
        SERVICE_NO_ENTRY_EXIT("%s does not control student entry and exit!%n"),
        STUDENT_LOCATION("%s is at %s %s (%d, %d).%n"),
        STUDENT_IS_THRIFTY("%s is thrifty!%n"),
        NO_VISITED_LOCATIONS("%s has not visited any locations!%n"),

        // --- Review/Ranking Command Messages ---
        EVALUATION_REGISTERED("Your evaluation has been registered!"),
        SERVICE_NOT_FOUND("%s does not exist!%n"),
        INVALID_EVALUATION("Invalid evaluation!"),
        NO_SERVICES_IN_SYSTEM("No services in the system."),
        RANKING_HEADER("Services sorted in descending order"),
        RANKED_HEADER("%s services closer with %d average%n"),
        INVALID_STARS("Invalid stars!"),
        NO_SERVICES_OF_TYPE("No %s services!%n"),
        NO_SERVICES_WITH_STARS("No %s services with average!%n"),
        NO_SERVICES_WITH_TAG("There are no services with this tag!");

        private final String text;

        /**
         * Constructor for a message constant.
         * @param text The user-facing string.
         */
        Message(String text) {
            this.text = text;
        }

        /**
         * Returns the raw message text.
         * @return The string.
         */
        @Override
        public String toString() {
            return text;
        }

        /**
         * Returns a formatted string using the message text as a format pattern.
         * @param args Arguments referenced by the format specifiers in the format string.
         * @return A formatted string.
         */
        public String format(Object... args) {
            return String.format(this.text, args);
        }

        /**
         * Prints the message text to the console.
         */
        public void print() {
            System.out.println(this.text);
        }

        /**
         * Prints a formatted string to the console.
         * @param args Arguments referenced by the format specifiers in the format string.
         */
        public void printf(Object... args) {
            System.out.printf(this.text, args);
        }
    }


    // --- Command Handlers ---

    /**
     * Handles the 'exit' command.
     * Saves the current area (if loaded) and prints a goodbye message.
     *
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleExit(SystemManager manager) {
        try {
            manager.saveArea();
        } catch (Exception e) {}
        Message.EXIT.print();
    }

    /**
     * Handles the 'help' command.
     * Prints the list of all available commands and their descriptions.
     */
    private static void handleHelp() {
        Message.HELP_TEXT.print();
    }

    /**
     * Handles the 'bounds' command.
     * Reads coordinates and a name to create a new area.
     *
     * @param sc      The {@link Scanner} to read input from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleBounds(Scanner sc, SystemManager manager) {
        long topLat = sc.nextLong();
        long leftLong = sc.nextLong();
        long bottomLat = sc.nextLong();
        long rightLong = sc.nextLong();
        String name = sc.nextLine().trim();

        try {
            manager.createArea(name, topLat, leftLong, bottomLat, rightLong);
            System.out.println(Message.AREA_CREATED.format(name));
        } catch (InvalidBoundsException e) {
            Message.INVALID_BOUNDS.print();
        } catch (BoundsAlreadyExistsException e) {
            Message.BOUNDS_ALREADY_EXISTS.print();
        }
    }

    /**
     * Handles the 'save' command.
     * Saves the currently loaded area to a file.
     *
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleSave(SystemManager manager) {
        try {
            manager.saveArea();
            System.out.println(Message.AREA_SAVED.format(manager.getCurrentArea().getName()));
        } catch (NoAreaLoadedException e) {
            Message.SYSTEM_BOUNDS_NOT_DEFINED.print();
        }
    }

    /**
     * Handles the 'load' command.
     * Loads a previously saved area from a file.
     *
     * @param sc      The {@link Scanner} to read the area name from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleLoad(Scanner sc, SystemManager manager) {
        String name = sc.nextLine().trim();

        try {
            manager.loadArea(name);
            System.out.println(Message.AREA_LOADED.format(manager.getCurrentArea().getName()));
        } catch (NoAreaLoadedException e) {
            Message.SYSTEM_BOUNDS_NOT_DEFINED.print();
        } catch (BoundsNotFoundException e) {
            System.out.println(Message.BOUNDS_NOT_FOUND.format(name));
        }
    }

    /**
     * Handles the 'service' command.
     * Reads service details and adds a new service to the area.
     *
     * @param sc      The {@link Scanner} to read input from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleService(Scanner sc, SystemManager manager) {
        String typeStr = sc.next();
        ServiceType type = ServiceType.fromString(typeStr);
        long lat = sc.nextLong();
        long lon = sc.nextLong();
        int price = sc.nextInt();
        int value = sc.nextInt();
        String name = sc.nextLine().trim();

        try {
            // A null 'type' will be caught by the manager's validServiceType check
            // or throw a NullPointerException, which is acceptable if we assume
            // InvalidServiceTypeException handles it.
            // Let's add an explicit check for a cleaner error.


            manager.addService(type, name, lat, lon, price, value);
            System.out.println(Message.SERVICE_ADDED.format(type.toString(), name));
        } catch (InvalidServiceTypeException e) {
            Message.INVALID_SERVICE_TYPE.print();
        } catch (InvalidLocationException e) {
            Message.INVALID_LOCATION.print();
        } catch (InvalidBoundsException e) {
            Message.INVALID_BOUNDS.print();
        } catch (SystemBoundsNotDefinedException e) {
            Message.SYSTEM_BOUNDS_NOT_DEFINED.print();
        } catch (InvalidMenuPriceException e) {
            Message.INVALID_MENU_PRICE.print();
        } catch (InvalidRoomPriceException e) {
            Message.INVALID_ROOM_PRICE.print();
        } catch (InvalidTicketPriceException e) {
            Message.INVALID_TICKET_PRICE.print();
        } catch (InvalidDiscountPriceException e) {
            Message.INVALID_DISCOUNT_PRICE.print();
        } catch (InvalidCapacityException e) {
            Message.INVALID_CAPACITY.print();
        } catch (ServiceAlreadyExistsException e) {
            System.out.println(Message.SERVICE_ALREADY_EXISTS.format(manager.getServiceByName(name).getName()));
        }
    }

    /**
     * Handles the 'services' command.
     * Lists all registered services in order of insertion.
     *
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleServices(SystemManager manager) {
        Iterator<Service> iterator;
        try {
            iterator = manager.listServices();
        } catch (NoServicesException e) {
            Message.NO_SERVICES.print();
            return;
        }
        while (iterator.hasNext()) {
            Service service = iterator.next();
            System.out.println(manager.getServiceName(service) + ": " + manager.getServiceType(service) + " " + "(" + manager.getServiceLatitude(service) + ", " + manager.getServiceLongitude(service) + ").");
        }
    }

    /**
     * Handles the 'student' command.
     * Reads student details and adds a new student to the area.
     *
     * @param sc      The {@link Scanner} to read input from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleStudent(Scanner sc, SystemManager manager) {
        String typeStr = sc.nextLine().trim();
        StudentType type = StudentType.fromString(typeStr);
        String name = sc.nextLine().trim();
        String country = sc.nextLine().trim();
        String lodgingName = sc.nextLine().trim();
        try {

            manager.addStudent(type, name, country, lodgingName);
            System.out.println(Message.STUDENT_ADDED.format(name));
        } catch (SystemBoundsNotDefinedException e) {
            Message.SYSTEM_BOUNDS_NOT_DEFINED.print();
        } catch (InvalidStudentTypeException e) {
            Message.INVALID_STUDENT_TYPE.print();
        } catch (LodgingNotFoundException e) {
            Message.LODGING_NOT_FOUND.printf(lodgingName);
        } catch (LodgingIsFullException e) {
            Message.LODGING_IS_FULL.printf(lodgingName);
        } catch (StudentAlreadyExistsException e) {
            Student student = manager.getStudentByName(name);
            Message.STUDENT_ALREADY_EXISTS.printf(manager.getStudentName(student));
        }
    }

    /**
     * Handles the 'students' command.
     * Lists either all students (alphabetically) or students from a specific country (by insertion).
     *
     * @param sc      The {@link Scanner} to read the filter from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleStudents(Scanner sc, SystemManager manager) {
        String filter = sc.nextLine().trim();
        Iterator<Student> iterator = manager.listStudents(filter);
        if (!iterator.hasNext() && filter.equals("all")) {
            Message.NO_STUDENTS.print();
        } else if (!iterator.hasNext()) {
            Message.NO_STUDENTS_FROM.printf(filter);
        } else {
            while (iterator.hasNext()) {
                Student student = iterator.next();
                System.out.println(manager.getStudentName(student) + ": " + manager.getStudentType(student).toString() + " at " + manager.getStudentCurrentLocation(student).getName() + ".");
            }
        }
    }

    /**
     * Handles the 'leave' command.
     * Removes a student from the system.
     *
     * @param manager The {@link SystemManager} instance.
     * @param sc      The {@link Scanner} to read the student name from.
     */
    private static void handleLeave(SystemManager manager, Scanner sc) {
        String studentName = sc.nextLine().trim();
        Student student = manager.getStudentByName(studentName);
        try {
            manager.removeStudent(studentName);
            System.out.println(Message.STUDENT_LEFT.format(student.getName()));
        } catch (StudentNotFoundException e) {
            Message.STUDENT_NOT_FOUND.printf(studentName);
        }
    }

    /**
     * Handles the 'go' command.
     * Moves a student to an Eating or Leisure service.
     *
     * @param manager The {@link SystemManager} instance.
     * @param sc      The {@link Scanner} to read student and service names from.
     */
    private static void handleGo(SystemManager manager, Scanner sc) {
        String studentName = sc.nextLine().trim();
        String serviceName = sc.nextLine().trim();
        Student student = manager.getStudentByName(studentName);
        Service service = manager.getServiceByName(serviceName);
        try {
            manager.goToLocation(studentName, serviceName);
            if (manager.isStudentDistracted(studentName, serviceName)) {
                Message.STUDENT_GO_DISTRACTED.printf(manager.getStudentName(student), manager.getServiceName(service), manager.getStudentName(student));
            } else {
                Message.STUDENT_GO_OK.printf(manager.getStudentName(student), manager.getServiceName(service));
            }
        } catch (ServiceNotFoundException e) {
            Message.UNKNOWN_SERVICE.printf(serviceName);
        } catch (StudentNotFoundException e) {
            Message.STUDENT_NOT_FOUND.printf(studentName);
        } catch (AlreadyThereException e) {
            Message.ALREADY_THERE.print();
        } catch (EatingIsFullException e) {
            Message.EATING_FULL.printf(manager.getServiceName(service));
        } catch (NotValidServiceException e) {
            Message.NOT_VALID_SERVICE.printf(manager.getServiceName(service));
        }
    }

    /**
     * Handles the 'move' command.
     * Changes a student's home to a new Lodging service.
     *
     * @param sc      The {@link Scanner} to read student and lodging names from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleMove(Scanner sc, SystemManager manager) {
        String studentName = sc.nextLine().trim();
        String lodgingName = sc.nextLine().trim();
        Service service = manager.getServiceByName(lodgingName);
        try {
            Student student = manager.getStudentByName(studentName);
            manager.moveStudentHome(studentName, lodgingName);
            Message.STUDENT_MOVE_OK.printf(manager.getServiceName(service), manager.getStudentName(student), student.getName());
        } catch (StudentNotFoundException e) {
            Message.STUDENT_NOT_FOUND.printf(studentName);
        } catch (LodgingNotFoundException e) {
            Message.LODGING_NOT_FOUND.printf(lodgingName);
        } catch (LodgingIsFullException e) {
            Message.LODGING_IS_FULL.printf(lodgingName);
        } catch (AlreadyStudentHomeException e) {
            Student student = manager.getStudentByName(studentName);
            Message.ALREADY_STUDENT_HOME.printf(manager.getStudentName(student));
        } catch (StudentIsThriftyException e) {
            Message.MOVE_NOT_ACCEPTABLE.printf(studentName);
        }
    }

    /**
     * Handles the 'users' command.
     * Lists students currently at an Eating or Lodging service, in a specified order.
     *
     * @param sc      The {@link Scanner} to read order and service name from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleUsers(Scanner sc, SystemManager manager) {
        String order = sc.next();
        String serviceName = sc.nextLine().trim();
        Service service = manager.getServiceByName(serviceName);

        TwoWayIterator<Student> it;
        try {
            it = manager.listUsersInService(order, serviceName);
            boolean printed = false;

            if (">".equals(order)) {
                while (it.hasNext()) {
                    Student student = it.next();
                    System.out.println(manager.getStudentName(student) + ": " + manager.getStudentType(student).toString());
                    printed = true;
                }
            } else { // "<"
                while (it.hasPrevious()) {
                    Student student = it.previous();
                    System.out.println(manager.getStudentName(student) + ": " + manager.getStudentType(student).toString());
                    printed = true;
                }
            }
            if (!printed) {
                Message.NO_STUDENTS_ON_SERVICE.printf(manager.getServiceName(service));
            }
        } catch (InvalidOrderException e) {
            Message.INVALID_ORDER.print();
        } catch (ServiceNotFoundException e) {
            Message.SERVICE_NOT_FOUND.printf(serviceName);
        } catch (ServiceDoesNotControlEntryExitException e) {
            Message.SERVICE_NO_ENTRY_EXIT.printf(manager.getServiceName(service));
        }
    }

    /**
     * Handles the 'where' command.
     * Locates a student and prints their current service location.
     *
     * @param sc      The {@link Scanner} to read the student name from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleWhere(Scanner sc, SystemManager manager) {
        String studentName = sc.nextLine().trim();
        try {
            Service service = manager.whereIsStudent(studentName);
            Student student = manager.getStudentByName(studentName);
            Message.STUDENT_LOCATION.printf(manager.getStudentName(student), service.getName(), service.getType(), service.getLatitude(), service.getLongitude());
        } catch (StudentNotFoundException e) {
            Message.STUDENT_NOT_FOUND.printf(studentName);
        }

    }

    /**
     * Handles the 'visited' command.
     * Lists all locations a specific student (Bookish or Outgoing) has stored.
     *
     * @param sc      The {@link Scanner} to read the student name from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleVisited(Scanner sc, SystemManager manager) {
        String studentName = sc.nextLine().trim();
        Student student = manager.getStudentByName(studentName);
        Iterator<Service> it;
        try {
            it = manager.listVisitedLocations(studentName);
            while (it.hasNext()) {
                Service service = it.next();
                System.out.println(service.getName());
            }
        } catch (StudentNotFoundException e) {
            Message.STUDENT_NOT_FOUND.printf(studentName);
        } catch (StudentIsThriftyException e) {
            Message.STUDENT_IS_THRIFTY.printf(manager.getStudentName(student));
        } catch (NoVisitedLocationsException e) {
            Message.NO_VISITED_LOCATIONS.printf(manager.getStudentName(student));
        }
    }

    /**
     * Handles the 'star' command.
     * Adds a new review (rating and description) to a service.
     *
     * @param sc      The {@link Scanner} to read the rating, service name, and description from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleStar(Scanner sc, SystemManager manager) {
        int stars = sc.nextInt();
        String serviceName = sc.nextLine().trim();
        String description = sc.nextLine().trim();
        try {
            manager.addReviewToService(serviceName, stars, description);
            Message.EVALUATION_REGISTERED.print();
        } catch (ServiceNotFoundException e) {
            Message.SERVICE_NOT_FOUND.printf(serviceName);
        } catch (InvalidStarsException e) {
            Message.INVALID_EVALUATION.print();
        }
    }

    /**
     * Handles the 'ranking' command.
     * Lists all services in descending order of their star rating.
     *
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleRanking(SystemManager manager) {
        Iterator<Service> it = manager.getRankedServices();
        if (!it.hasNext()) {
            Message.NO_SERVICES_IN_SYSTEM.print();
        } else {
            Message.RANKING_HEADER.print();
            while (it.hasNext()) {
                Service service = it.next();
                System.out.printf("%s: %d%n", service.getName(), service.getAvgStar());
            }
        }
    }

    /**
     * Handles the 'ranked' command.
     * Lists services of a specific type and rating, ordered by proximity to a student.
     *
     * @param sc      The {@link Scanner} to read type, stars, and student name from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleRanked(Scanner sc, SystemManager manager) {
        String typeStr = sc.next();
        ServiceType type = ServiceType.fromString(typeStr);
        int stars = sc.nextInt();
        String studentName = sc.nextLine().trim();
        Iterator<Service> it;
        try {

            it = manager.getRankedServicesByTypeAndStars(type, stars, studentName);
            Message.RANKED_HEADER.printf(type.toString(), stars);
            while (it.hasNext()) {
                Service service = it.next();
                System.out.printf("%s%n", service.getName());
            }
        } catch (InvalidServiceTypeException e) {
            Message.INVALID_SERVICE_TYPE.print();
        } catch (NoServicesOfThisTypeException e) {
            Message.NO_SERVICES_OF_TYPE.printf(type.toString());
        } catch (InvalidStarsException e) {
            Message.INVALID_STARS.print();
        } catch (StudentNotFoundException e) {
            Message.STUDENT_NOT_FOUND.printf(studentName);
        } catch (NoTypeServicesWithStarsException e) {
            Message.NO_SERVICES_WITH_STARS.printf(type.toString());
        }
    }

    /**
     * Handles the 'tag' command.
     * Lists all services that have a review containing a specific tag.
     *
     * @param sc      The {@link Scanner} to read the tag from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleTag(Scanner sc, SystemManager manager) {
        String tag = sc.nextLine();
        Iterator<Service> it = manager.listServicesWithTag(tag);
        if (!it.hasNext()) {
            Message.NO_SERVICES_WITH_TAG.print();
        } else {
            while (it.hasNext()) {
                Service service = it.next();
                System.out.println(manager.getServiceType(service) + " " + manager.getServiceName(service));
            }
        }
    }

    /**
     * Handles the 'find' command.
     * Finds the most relevant service of a specific type for a specific student.
     *
     * @param sc      The {@link Scanner} to read student name and service type from.
     * @param manager The {@link SystemManager} instance.
     */
    private static void handleFind(Scanner sc, SystemManager manager) {
        String studentName = sc.nextLine().trim();
        String typeStr = sc.nextLine().trim();
        ServiceType type = ServiceType.fromString(typeStr);

        try {

            Service service = manager.findRelevantServiceForStudent(studentName, type);
            System.out.println(manager.getServiceName(service));
        } catch (StudentNotFoundException e) {
            Message.STUDENT_NOT_FOUND.printf(studentName);
        } catch (InvalidServiceTypeException e) {
            Message.INVALID_SERVICE_TYPE.print();
        } catch (NoServicesOfThisTypeException e) {
            Message.NO_SERVICES_OF_TYPE.printf(type.toString());
        }
    }
}