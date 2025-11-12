package Services;

import java.io.Serializable;

/**
 * Implements the {@link Evaluation} interface.
 * This class is a simple data object that holds the information
 * for a single service review, including a star rating and a text description.
 * It is serializable.
 */
public class EvaluationImpl implements Evaluation, Serializable {

    // --- Fields ---

    /**
     * Standard serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The star rating assigned in this evaluation (e.g., 1-5).
     */
    int stars;

    /**
     * The text comment provided in this evaluation.
     */
    String description;

    // --- Constructor ---

    /**
     * Constructs a new Evaluation object.
     *
     * @param stars       The star rating given in this evaluation.
     * @param description The text comment for this evaluation.
     */
    public EvaluationImpl(int stars, String description) {
        this.stars = stars;
        this.description = description;
    }

    // --- Getters ---

    /**
     * Gets the star rating of this evaluation.
     *
     * @return The integer star rating (1-5).
     */
    @Override
    public int getStars() {
        return stars;
    }

    /**
     * Gets the text description of this evaluation.
     *
     * @return The text comment string.
     */
    @Override
    public String getDescription() {
        return description;
    }

    // --- Public Methods ---

    /**
     * Checks if the evaluation's description contains a specific tag (word).
     * This method splits the description by whitespace and performs a
     * case-insensitive comparison for each word against the provided tag.
     *
     * @param tag The tag (word) to search for. This method expects the
     * tag to be provided in lowercase for a correct match.
     * @return true if the tag is found as a standalone word, false otherwise.
     */
    @Override
    public boolean containsTag(String tag) {
        tag = tag.trim();

        String[] words = description.split("\\s");
        for (String word : words) {
            if (word.toLowerCase().equals(tag)) return true;
        }
        return false;
    }
}