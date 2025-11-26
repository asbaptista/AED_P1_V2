/**
 * Enumeration of all valid user commands for the application.
 * Each constant corresponds to a specific action a user can perform.
 * Provides a static method to safely parse a string into a command.
 */
public enum Commands {
    /**
     * Defines the new geographic bounding rectangle.
     *
     */
    BOUNDS,

    /**
     * Saves the current geographic bounding rectangle to a text file.
     *
     */
    SAVE,

    /**
     * Loads a geographic bounding rectangle from a text file.
     *
     */
    LOAD,

    /**
     * Adds a new service (Eating, Lodging, or Leisure) to the current area.
     *
     */
    SERVICE,

    /**
     * Displays the list of all services in the current area, in order of registration.
     *
     */
    SERVICES,

    /**
     * Adds a student to the current geographic area.
     *
     */
    STUDENT,

    /**
     * Lists all students or those of a given country.
     *
     */
    STUDENTS,

    /**
     * Removes a student from the current geographic area.
     *
     */
    LEAVE,

    /**
     * Changes the location of a student to a leisure or eating service.
     *
     */
    GO,

    /**
     * Changes the home (lodging) of a student.
     *
     */
    MOVE,

    /**
     * Lists all students who are in a given service (eating or lodging).
     *
     */
    USERS,

    /**
     * Evaluates a service with a star rating and a description.
     *
     */
    STAR,

    /**
     * Locates a student, showing their current service location.
     *
     */
    WHERE,

    /**
     * Lists locations visited by one student (for Bookish and Outgoing types).
     *
     */
    VISITED,

    /**
     * Lists all services ordered by their average star rating.
     *
     */
    RANKING,

    /**
     * Lists the service(s) of an indicated type with a given score
     * that are closest to the student's location.
     *
     */
    RANKED,

    /**
     * Lists all services that have at least one review containing the specified tag (word).
     *
     */
    TAG,

    /**
     * Finds the most relevant service of a certain type for a specific student
     * (based on price for Thrifty, or stars for others).
     *
     */
    FIND,

    /**
     * Shows the available commands.
     *
     */
    HELP,

    /**
     * Terminates the execution of the program.
     *
     */
    EXIT;

    /**
     * A static factory method to convert a raw string from user input
     * into a valid {@code Commands} constant.
     * <p>
     * The comparison is case-insensitive and trims whitespace.
     *
     * @param command The raw string input from the user (e.g., "exit", "STUDENT").
     * @return The corresponding {@code Commands} constant.
     * @throws IllegalArgumentException if the command string is null or
     * does not match any valid command.
     */
    public static Commands fromString(String command) {
        if (command == null) {
            throw new IllegalArgumentException("null word");
        }

        // Iterate through all enum values
        for (Commands e : Commands.values()) {
            // Compare the enum constant's name (e.g., "BOUNDS")
            // with the trimmed, case-insensitive input string.
            if (e.name().equalsIgnoreCase(command.trim())) {
                return e;
            }
        }

        // No match found
        throw new IllegalArgumentException("invalid");
    }
}