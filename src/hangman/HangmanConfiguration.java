package hangman;

import java.io.Serializable;

/**
 * Class that represents how the configuration for a hangman game would be stored
 *
 * @author Chami Lamelas
 */
public class HangmanConfiguration implements Serializable {

    /**
     * Difficulty setting configuration property.
     */
    private String difficulty;
    /**
     * Keep duplicates setting configuration property.
     */
    private boolean keepDuplicates;
    /**
     * Keep proper nouns setting configuration property.
     */
    private boolean keepProperNouns;
    /**
     * Minimum word length.
     */
    private int minimumWordLength;
    /**
     * File path configuration property.
     */
    private String filepath;
    /**
     * File path display state
     */
    private boolean displayFilePath;

    /**
     * Constructor to instantiate HangmanConfiguration objects
     * @param diff The difficulty
     * @param keepDupes The boolean state representing whether or not to keep duplicates
     * @param keepPropNouns The boolean state representing whether or not to keep proper nouns
     * @param minWordLength The integer value representing the minimum number of letters in the smallest word permitted by the program
     * @param path The file path to the file used in playing the game
     * @param displayPath The boolean state representing whether or not to display file path
     */
    public HangmanConfiguration(String diff, boolean keepDupes, boolean keepPropNouns, int minWordLength, String path, boolean displayPath) {
        difficulty = diff;
        keepDuplicates = keepDupes;
        keepProperNouns = keepPropNouns;
        minimumWordLength = minWordLength;
        filepath = path;
        displayFilePath = displayPath;
    }

    /**
     * Gets the difficulty setting configuration property.
     *
     * @return the difficulty setting configuration property.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the keep duplicates setting configuration property.
     *
     * @return the keep duplicates setting configuration property.
     */
    public boolean isKeepDuplicates() {
        return keepDuplicates;
    }

    /**
     * Gets the keep proper nouns setting configuration property.
     *
     * @return the keep proper nouns setting configuration property.
     */
    public boolean isKeepProperNouns() {
        return keepProperNouns;
    }

    /**
     * Gets the file path configuration property.
     *
     * @return the file path configuration property.
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Sets the file path to a new file path.
     *
     * @param newFilePath a new file path
     */
    public void setFilePath(String newFilePath) {
        filepath = newFilePath;
    }

    /**
     * Gets the minimum word length
     *
     * @return the minimum word length
     */
    public int getMinimumWordLength() {
        return minimumWordLength;
    }

    /**
     * Gets the file path display configuration property
     *
     * @return The file path display configuration property
     */
    public boolean isDisplayFilePath() {
        return displayFilePath;
    }

    /**
     * Sets the configuration to default values
     */
    public void reset() {
        difficulty = HangmanGame.DEFAULT_DIFFICULTY;
        keepDuplicates = HangmanGame.DEFAULT_DUPLICATES_SETTING;
        keepProperNouns = HangmanGame.DEFAULT_PROPER_NOUNS_SETTING;
        minimumWordLength = HangmanGame.DEFAULT_MINIMUM_WORD_LENGTH;
        filepath = HangmanGame.DEFAULT_CONFIG_FILE_PATH;
        displayFilePath = HangmanGame.DEFAULT_FILE_PATH_DISPLAY_SETTING;
    }
}
