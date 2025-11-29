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
     * Standard serial version UID for serialization.
     */
    @Serial
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
     * This method performs a case-insensitive search for the tag as a standalone word
     * using the KMP algorithm.
     *
     * @param tag The tag (word) to search for.
     * @return true if the tag is found as a standalone word, false otherwise.
     */
    @Override
    public boolean containsTag(String tag) {
        char[] text = description.toCharArray();
        char[] pattern = tag.trim().toCharArray();

        // Convert pattern to lowercase for comparison
        char[] lowerPattern = new char[pattern.length];
        for (int k = 0; k < pattern.length; k++) {
            lowerPattern[k] = toLowerCase(pattern[k]);
        }

        // Extract and search each word from the description
        int n = text.length;
        int wordStart = 0;

        for (int i = 0; i <= n; i++) {
            // End of word: either whitespace or end of text
            if (i == n || isWhitespace(text[i])) {
                int wordLength = i - wordStart;

                if (wordLength > 0) {
                    // Extract current word and convert to lowercase
                    char[] word = new char[wordLength];
                    for (int k = 0; k < wordLength; k++) {
                        word[k] = toLowerCase(text[wordStart + k]);
                    }

                    // Check if word matches pattern exactly
                    if (word.length == lowerPattern.length && kmpSearch(word, lowerPattern)) {
                        return true;
                    }
                }

                // Move to start of next word (skip whitespace)
                wordStart = i + 1;
            }
        }

        return false;
    }

    /**
     * Performs KMP (Knuth-Morris-Pratt) search algorithm to find a pattern in text.
     *
     * @param text    The text to search in.
     * @param pattern The pattern to search for.
     * @return true if the pattern is found in the text, false otherwise.
     */
    public static boolean kmpSearch(char[] text, char[] pattern) {
        int n = text.length;
        int m = pattern.length;
        int[] lps = LPS(pattern);
        int i = 0, j = 0;
        while (i < n) {
            if (pattern[j] == text[i]) {
                i++;
                j++;
            }
            if (j == m) //found
                return true;
            if (i < n && pattern[j] != text[i]) {
                if (j != 0)
                    j = lps[j - 1]; //reuse suffix of P[0..j-1]
                else
                    i++;
            }

        }
        return false;
    }


    private static int[] LPS(char[] pattern) {
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
     * Converts a character to lowercase.
     *
     * @param c The character to convert.
     * @return The lowercase version of the character.
     */
    private static char toLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c + 32);
        }
        return c;
    }

    /**
     * Checks if a character is whitespace.
     *
     * @param c The character to check.
     * @return true if the character is whitespace, false otherwise.
     */
    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

}