package Services;

import java.io.*;

/**
 * Implements the {@link Evaluation} interface.
 * This class is a simple data object that holds the information
 * for a single service review, including a star rating and a text description.
 * It is serializable.
 */
public class EvaluationImpl implements Evaluation, Serializable {

    // --- Fields ---
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
     * This method performs a search for the tag as a standalone word using KMP algorithm.
     *
     * @param tag The tag (word) to search for.
     * @return true if the tag is found as a standalone word, false otherwise.
     */
    @Override
    public boolean containsTag(String tag) {
        char[] text = description.toCharArray();
        char[] pattern = tag.toCharArray();

        int[] lps = LPS(pattern);
        return kmpSearchWord(text, pattern, lps);
    }

    /**
     * KMP search that matches complete words only (bounded by whitespace).
     *
     * @param text    The text to search in.
     * @param pattern The pattern to search for.
     * @param lps     The LPS array for the pattern.
     * @return true if pattern is found as a complete word, false otherwise.
     */
    private boolean kmpSearchWord(char[] text, char[] pattern, int[] lps) {
        int n = text.length;
        int m = pattern.length;
        int i = 0, j = 0;

        while (i < n) {
            if (pattern[j] == text[i]) {
                i++;
                j++;
            }

            if (j == m) {
                // Check if this is a complete word (bounded by whitespace or text boundaries)
                boolean startOk = (i - m == 0) || isWhitespace(text[i - m - 1]);
                boolean endOk = (i == n) || isWhitespace(text[i]);

                if (startOk && endOk) {
                    return true;
                }
                // Continue searching
                j = lps[j - 1];
            } else if (i < n && pattern[j] != text[i]) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return false;
    }

    /**
     * Computes the Longest Proper Prefix which is also Suffix (LPS) array
     * for the KMP algorithm.
     *
     * @param pattern The pattern for which to compute the LPS array.
     * @return The LPS array.
     */
    private int[] LPS(char[] pattern) {
        int m = pattern.length;
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        lps[0] = 0;

        while (i < m) {
            if (pattern[i] == pattern[len]) {
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



    /**
     * Checks if a character is whitespace.
     */
    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

}