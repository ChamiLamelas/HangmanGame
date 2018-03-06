package hangman;

import java.io.*;

/**
 * Class that manages hangman game scores
 *
 * @author Chami Lamelas
 */
public class HangmanScoreManager implements Serializable {
    /**
     * The file to which the scores will be read and written to.
     */
    private File file;
    /**
     * The user's scores.
     */
    private HangmanScoreList data;

    /**
     * Constructor used to instantiate HangmanScoreManager objects.
     */
    public HangmanScoreManager(String dirPath) {
        file = new File(dirPath + "\\hangman_scores.dat");
    }

    /**
     * Get's user score data.
     *
     * @return the data
     */
    public HangmanScoreList getData() {
        return data;
    }

    /**
     * Read's user score data from the file.
     */
    public void readData() {
        try {
            if (file.exists()) {
                ObjectInputStream readData = new ObjectInputStream(new FileInputStream(file));
                data = (HangmanScoreList) readData.readObject();
                readData.close();
            } else {
                data = new HangmanScoreList();
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
     * Writes the new user score data to the file.
     *
     * @param newScore - new score to be added to the file.
     */
    public void writeData(HangmanScorer newScore) {
        try {
            data.addScore(newScore);
            ObjectOutputStream dataWriter = new ObjectOutputStream(new FileOutputStream(file));
            dataWriter.writeObject(data);
            dataWriter.close();
        } catch (IOException e) {
            HangmanDisplay.displayError("Auto-generated Error: " + e.getMessage());
        }
    }
}
