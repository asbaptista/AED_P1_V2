package Services;

/**
 * Represents the distinct types of services available in the application.
 * This enumeration provides type-safe constants for service categories.
 */
public enum ServiceType {

    EATING,
    LODGING,
    LEISURE;

    /**
     * Converts a case-insensitive input String to the corresponding {@code ServiceType} enum.
     * This method is used to safely parse user input.
     *
     * @param text The input text to convert (e.g., "eating", "LODGING").
     * @return The matching {@code ServiceType enum constant, or  null if no match is found.
     */
    public static ServiceType fromString(String text) {
        if (text != null) {
            for (ServiceType st : ServiceType.values()) {
                if (text.equalsIgnoreCase(st.name())) {
                    return st;
                }
            }
        }
        return null; // Return null if no match is found
    }

    /**
     * Returns the name of the enum constant in lowercase.
     * This provides a consistent, user-friendly string representation for program outputs.
     *
     * @return The lowercase string representation of the service type (e.g., "eating", "lodging", or "leisure").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}