package Students;

/**
 * Enumeration of the different student types available in the system.
 * Each type defines a specific behavior and set of preferences
 *.
 */
public enum StudentType {
    /**
     * A student primarily interested in leisure and cultural services
     *.
     */
    BOOKISH,

    /**
     * A student interested in eating out and visiting all types of locations
     *.
     */
    OUTGOING,

    /**
     * A student primarily concerned with saving money and finding the
     * least expensive services.
     */
    THRIFTY;

    // --- Static Methods ---

    /**
     * Converts a case-insensitive input string to its corresponding {@link StudentType} constant.
     * This is a static factory method used to safely parse user input.
     *
     * @param text The text to convert (e.g., "thrifty", "BOOKISH").
     * @return The matching {@code StudentType}, or {@code null} if no match is found.
     */
    public static StudentType fromString(String text) {
        if (text != null) {
            for (StudentType st : StudentType.values()) {
                if (text.equalsIgnoreCase(st.name())) {
                    return st;
                }
            }
        }
        return null; // Will be handled as InvalidStudentTypeException by the caller
    }

    // --- Instance Methods ---

    /**
     * Returns the name of the enum constant in lowercase.
     * This overrides the default {@code toString()} (which returns uppercase)
     * to provide consistency with the program's console output requirements.
     *
     * @return The lowercase string representation (e.g., "bookish", "outgoing", or "thrifty").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}