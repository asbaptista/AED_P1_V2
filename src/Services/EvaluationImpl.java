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
    public boolean containsTag(String tag) { //kmp algorithm
        if (description == null || tag == null) {
            return false;
        }
        int descriptionLength = description.length();
        int tagLength = tag.length();

        if (descriptionLength == 0 || tagLength == 0 || tagLength > descriptionLength) {
            return false;
        }
        int[] lps = LPS(tag);

        int i = 0;
        int j = 0;

        while(i<descriptionLength){
            if(charsEqualIgnoreCase(description.charAt(i), tag.charAt(j))){
                i++;
                j++;
            }
            if( j == tagLength){
                int wordStart = i-tagLength;
                int wordEnd = i;
                
                if (isSeparateWord(wordStart, wordEnd)) {
                    return true;
                }
                j = lps[j - 1];
            } else if (i < descriptionLength && ! charsEqualIgnoreCase(description.charAt(i), tag. charAt(j))) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }

        }
        return false;
    }

    private boolean isSeparateWord(int wordStart, int wordEnd) {
        boolean left = (wordStart == 0) || Character.isWhitespace(description.charAt(wordStart - 1));
        boolean right = (wordEnd == description.length()) || Character.isWhitespace(description.charAt(wordEnd));
        return left && right;
    }

    private int[] LPS(String tag) {
        int m = tag.length();
        int[] lps = new int[m];

        int len = 0;
        int i = 1;
        lps[0] = 0;
        while (i < m) {
            if (charsEqualIgnoreCase(tag.charAt(i), tag. charAt(len))) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    private boolean charsEqualIgnoreCase(char c1, char c2) {
        return Character.toLowerCase(c1) == Character.toLowerCase(c2);
    }


}