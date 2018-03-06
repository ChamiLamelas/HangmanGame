package hangman;

import java.io.*;

/**
 * Class that manages game configurations.
 *
 * @author Chami Lamelas
 */
public class HangmanConfigurationManager implements Serializable {

    /**
     * File to which configuration will be stored
     */
    private File file;
    /**
     * Configuration saved as a HangmanConfiguration instance
     */
    private HangmanConfiguration config;

    /**
     * Constructor to instantiate HangmanConfigurationManager objects
     */
    public HangmanConfigurationManager(String dirPath) {
        file = new File(dirPath + "\\hangman.config");
        config = new HangmanConfiguration(HangmanGame.DEFAULT_DIFFICULTY, HangmanGame.DEFAULT_PROPER_NOUNS_SETTING, HangmanGame.DEFAULT_DUPLICATES_SETTING, HangmanGame.DEFAULT_MINIMUM_WORD_LENGTH, HangmanGame.DEFAULT_CONFIG_FILE_PATH, HangmanGame.DEFAULT_FILE_PATH_DISPLAY_SETTING);
    }

    /**
     * Gets the configuration
     *
     * @return the configuration as a HangmanConfiguration instance
     */
    public HangmanConfiguration getConfig() {
        return config;
    }

    /**
     * Gets the game settings from a file for use, and if the file doesn't exist, returns the default game configuration
     */
    public void getGameSettings() {
        try {
            if (file.exists()) {
                ObjectInputStream readConfig = new ObjectInputStream(new FileInputStream(file));
                config = (HangmanConfiguration) readConfig.readObject();
                //System.out.println(((HangmanConfiguration) readConfig.readObject()).isDisplayFilePath());
                readConfig.close();
            } else {
                resetGameSettings();
            }
        } catch (ClassNotFoundException e) {
            HangmanDisplay.displayError("<html>ClassNotFoundException: " + e.getMessage()
                    + "<br><br>There was an error reading the data. </html>");
        } catch (FileNotFoundException e) {
            HangmanDisplay.displayError("<html>FileNotFoundException: " + e.getMessage()
                    + "<br><br>There was an error with the file </html>" + file.getName() + ".");
        } catch (IOException e) {
            HangmanDisplay.displayError("IOException: " + e.getMessage());
        }
    }

    /**
     * Saves a provided HangmanGame instance's settings
     *
     * @param g a HangmanGame
     */
    public void saveUserSettings(HangmanGame g) {
        //System.out.println(g.toString());
        config = new HangmanConfiguration(g.getDifficulty(), g.isKeepDuplicates(), g.isKeepProperNouns(), g.getMinimumWordLength(), g.getFilePath(), g.isDisplayFilePath());
    }

    /**
     * Resets the manager's configuration to the game default configuration
     */
    public void resetGameSettings() {
         config.reset();
    }

    /**
     * Writes the manager's configuration to a file
     */
    public void writeGameSettings() {
        try {
            ObjectOutputStream configWriter = new ObjectOutputStream(new FileOutputStream(file));
            configWriter.writeObject(config);
            configWriter.close();
        } catch (FileNotFoundException e) {
            HangmanDisplay.displayError("<html>FileNotFoundException: " + e.getMessage()
                    + "<br><br>There was an error with the file </html>" + file.getName() + ".");
        } catch (IOException e) {
            HangmanDisplay.displayError("IOException: " + e.getMessage());
        }
    }

}