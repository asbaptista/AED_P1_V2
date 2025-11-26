package Services;

/**
 * Interface defining the contract for a single service evaluation (review).
 * It ensures that every evaluation object provides a star rating,
 * a text description, and a method to check for tags within that description.
 */
public interface Evaluation {

    /**
     * Gets the star rating of this evaluation.
     *
     * @return The integer star rating (e.g., 1-5).
     */
    int getStars();

    /**
     * Gets the text description (comment) of this evaluation.
     *
     * @return The text comment string.
     */
    String getDescription();

    /**
     * Checks if the evaluation's description contains a specific tag (word).
     * The implementing method is responsible for the comparison logic
     * (e.g., case-sensitivity, splitting by whitespace).
     *
     _
     _
     * @param tag The tag (word) to search for.
     * @return true if the tag is found, false otherwise.
     */
    boolean containsTag(String tag);

}