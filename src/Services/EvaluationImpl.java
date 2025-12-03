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
     * This method performs a case-insensitive search for the tag as a standalone word.
     *
     * @param tag The tag (word) to search for.
     * @return true if the tag is found as a standalone word, false otherwise.
     */
    @Override
    public boolean containsTag(String tag) {
        char[] text = description.toCharArray();
        char[] pattern = trimWhitespace(tag.toCharArray());

        if (pattern.length == 0) {
            return false;
        }

        int wordStart = 0;
        for (int i = 0; i <= text.length; i++) {
            if (i == text.length || isWhitespace(text[i])) {
                int wordLength = i - wordStart;
                if (wordLength == pattern.length && matchesWord(text, wordStart, pattern)) {
                    return true;
                }
                wordStart = i + 1;
            }
        }
        return false;
    }

    /**
     * Checks if a word in the text matches the pattern (case-insensitive).
     *
     * @param text      The text array.
     * @param start     The start index of the word.
     * @param pattern   The pattern to match.
     * @return true if the word matches the pattern, false otherwise.
     */
    private static boolean matchesWord(char[] text, int start, char[] pattern) {
        for (int i = 0; i < pattern.length; i++) {
            if (toLowerCase(text[start + i]) != toLowerCase(pattern[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     * Removes leading and trailing whitespace from a character array.
     */
    private static char[] trimWhitespace(char[] chars) {
        int start = 0;
        while (start < chars.length && isWhitespace(chars[start])) {
            start++;
        }

        int end = chars.length - 1;
        while (end >= start && isWhitespace(chars[end])) {
            end--;
        }

        int length = end - start + 1;
        if (length <= 0) {
            return new char[0];
        }

        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = chars[start + i];
        }

        return result;
    }

    /**
     * Converts a character to lowercase.
     */
    private static char toLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c + 32);
        }
        return c;
    }

    /**
     * Checks if a character is whitespace.
     */
    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

}