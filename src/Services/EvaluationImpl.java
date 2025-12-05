package Services;

import java.io.*;

public class EvaluationImpl implements Evaluation, Serializable {

    int stars;

    String description;

    public EvaluationImpl(int stars, String description) {
        this.stars = stars;
        this.description = description;
    }

    @Override
    public int getStars() {
        return stars;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean containsTag(String tag) {
        char[] text = description.toCharArray();
        char[] pattern = tag.toCharArray();

        int[] lps = LPS(pattern);
        return kmpSearchWord(text, pattern, lps);
    }

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

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

}