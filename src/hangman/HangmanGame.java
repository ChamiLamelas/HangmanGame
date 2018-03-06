package hangman;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class that represents a Hangman Game
 *
 * @author Chami Lamelas
 */
public class HangmanGame {
    /**
     * Lives on easy difficulty.
     */
    public static final int LIVES_ON_EASY_DIFFICULTY = 20;
    /**
     * Lives on medium difficulty.
     */
    public static final int LIVES_ON_MEDIUM_DIFFICULTY = 14;
    /**
     * Lives on hard difficulty.
     */
    public static final int LIVES_ON_HARD_DIFFICULTY = 10;
    /**
     * Lives on extreme difficulty.
     */
    public static final int LIVES_ON_EXTREME_DIFFICULTY = 6;
    /**
     * Default difficulty setting
     */
    public static final String DEFAULT_DIFFICULTY = "Medium";
    /**
     * Default keep duplicates setting
     */
    public static final boolean DEFAULT_DUPLICATES_SETTING = true;
    /**
     * Default keep proper nouns setting
     */
    public static final boolean DEFAULT_PROPER_NOUNS_SETTING = true;
    /**
     * Default file location.
     */
    public static final String DEFAULT_CONFIG_FILE_PATH = null;
    /**
     * Default path display state
     */
    public static final boolean DEFAULT_FILE_PATH_DISPLAY_SETTING = true;
    /**
     * Default minimum word length.
     */
    public static final int DEFAULT_MINIMUM_WORD_LENGTH = 5; // 5 was initially the fixed minimum, until the variable minimum word length feature was added
    /**
     * Maximum minimum word length for user interface component (can be overridden by user)
     */
    public static final int MAXIMUM_MINIMUM_WORD_LENGTH = 45; // 45 is longest word in any major english dictionary
    /**
     * Minimum minimum word length for user interface component (can not be overridden by user)
     */
    public static final int MINIMUM_MINIMUM_WORD_LENGTH = 2; // 2 seemed reasonable
    /**
     * Game's difficulty.
     */
    private String difficulty;
    /**
     * Game's start time.
     */
    private long gameStartTime;
    /**
     * Game's end time.
     */
    private long gameEndTime;
    /**
     * Player's start lives.
     */
    private int startLives;
    /**
     * Player's number of turns, which is based on difficulty level.
     */
    private int turns;
    /**
     * Words that can be used by program.
     */
    private Map<Integer, String> words;
    /**
     * Source of words.
     */
    private File file;
    /**
     * Number of incorrect guesses.
     */
    private int numIncorrect;
    /**
     * The incorrect guesses.
     */
    private ArrayList<String> incorrectGuesses;
    /**
     * The actual word; the one the user is trying to guess.
     */
    private String actualWord;
    /**
     * Representation of how close the user is to getting the actual word.
     */
    private String userProgress;
    /**
     * User specification on whether or not to use words that are duplicated.
     */
    private boolean keepDuplicates;
    /**
     * User specification on whether or not to use proper nouns.
     */
    private boolean keepProperNouns;
    /**
     * Game's minimum word length.
     */
    private int minimumWordLength;
    /**
     * Game's file path display setting
     */
    private boolean displayFilePath;
    /**
     * Game's data manager
     */
    private HangmanDataManager gameDataManager;

    /**
     * Constructor to instantiate HangmanGame object (i.e. Start the game)
     *
     * @param configuration The specific game configuration
     */
    public HangmanGame(HangmanConfiguration configuration) {
        gameDataManager = new HangmanDataManager();

        file = new File(configuration.getFilepath());
        difficulty = configuration.getDifficulty();
        keepProperNouns = configuration.isKeepProperNouns();
        keepDuplicates = configuration.isKeepDuplicates();
        minimumWordLength = configuration.getMinimumWordLength();
        displayFilePath = configuration.isDisplayFilePath();

        getWords();
        getRandomWord();
        readyUserProgress();

        startLives = determineStartLives();
        turns = 0;

        numIncorrect = 0;
        incorrectGuesses = new ArrayList<String>();

        gameStartTime = System.currentTimeMillis();
        gameEndTime = 0;
    }

    /**
     * Gets words from a file
     */
    protected void getWords() {

        //https://stackoverflow.com/questions/19992018/reading-single-quotes-from-text-file-in-java-using-bufferedreader
        words = new HashMap<Integer, String>();
        try {
            BufferedReader wordRdr = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line = null;
            int wordCounter = 0;
            try {
                while ((line = wordRdr.readLine()) != null) {
                    Scanner lineRdr = new Scanner(line);
                    while (lineRdr.hasNext()) {
                        String word = lineRdr.next();
                        word = filter(word);

                        boolean isClear = false;// is it clear to add next word
                        if (word.length() >= minimumWordLength) {
                            isClear = true;
                            /*
                                minimum word length determines the minimum word length the user would like.
                                however, the user can still change these settings, so they have to be
                                checked.
                             */
                            if (!keepDuplicates) {
                                /*
                                    checks if the word is a duplicate by checking if the Map holding already added
                                    words contains this word.
                                 */
                                if (containsWord(word))
                                    isClear = false;
                            }
                            if (!keepProperNouns) {
                                /*
                                    checks if the word is a proper noun by seeing if the first letter is capitalized
                                    which is checked by checking if the first letter is the same in the word and in the
                                    lower case version of the word.
                                 */
                                if (!word.substring(0, 1).equals(word.substring(0, 1).toLowerCase()))
                                    isClear = false;
                            }
                            //System.out.println(nextWord + ": " + isClear);

                        }
                        if (isClear) {
                            words.put(wordCounter, word);
                            wordCounter++; // to increment key for each new added word
                        }
                    }
                    lineRdr.close();
                }

                wordRdr.close();
            } catch (IOException e) {
                HangmanDisplay.displayError(e.getMessage());
            }
            if (words.isEmpty())
                HangmanDisplay.displayError("The selected file \"" + file.getName()
                        + "\" was found to be empty or could not be read. \n\nPlease restart and choose a new file. ");


        } catch (FileNotFoundException e) {
            HangmanDisplay.displayError("There was an error reading the file \"" + file.getName() + "\". Make sure \"" + file.getName() + "\" is not a directory!");
        }

        // FOR DEBUGGING ONLY


        /*
        for (int j = 0; j < words.size(); j++)
            System.out.println(words.get(j));
            */

    }

    /**
     * Removes all/filters out non letter or digit characters from a String
     * @param sequence A String that has not been filtered.
     * @return The filtered String
     */
    private String filter(String sequence) {
        String filteredSequence = "";
        for (int i = 0; i < sequence.length(); i++) {
            if (Character.isLetterOrDigit(sequence.charAt(i)))
                filteredSequence += sequence.charAt(i);
        }
        return filteredSequence;
    }


    /**
     * Updated version of containsValue() from HashMap.
     * <p>
     * If the word occurs at the beginning of a sentence in the words source file then it will be capitalized.
     * containsValue() is case-sensitive: this would lead to duplicates still existing as "The" and "the" for example.
     *
     * @param word A word.
     * @return The occurrence of a word represented as a boolean value
     */
    // https://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html#containsValue(java.lang.Object)
    // "Returns true if this map maps one or more keys to the specified value".
    private boolean containsWord(String word) {
        // if words contains the word all lowercase (as it would appear in the main part of a sentence/phrase)
        // OR
        // if words contains the word with its first letter capitalized (as it would appear at the beginning of a sentence/phrase)
        return (words.containsValue(word.toLowerCase()) || words.containsValue(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()));
    }

    /**
     * Gets a random word from the Map of words.
     */
    protected void getRandomWord() {
        int randomIdx = (int) (Math.random() * words.size());
        actualWord = words.get(randomIdx);
        //words.remove(randomIdx); -> read if a "play again" feature is added, so the same word isn't used.
    }

    /**
     * On the screen, the user progress section will be represented as a sequence of hyphens as long as the actual
     * word's length.
     */
    protected void readyUserProgress() {
        userProgress = "";
        for (int i = 0; i < actualWord.length(); i++)
            userProgress += "-";
    }

    /**
     * Returns the status of the guess' occurrence in the actual word.
     * Note - Initially used boolean but due to various return possibilities, switched to String return
     */
    public String isInWord(String guess) {
        if (guess.length() == 1) {
            if (actualWord.toLowerCase().indexOf(guess) == -1) {
                if (incorrectGuesses.indexOf(guess) == -1) {
                    incorrectGuesses.add(guess);
                    loseLife();
                    if (numIncorrect == startLives)
                        return "out of lives";
                    turns++;
                    return "not in word";
                }
                return "repeat";
            } else {

                for (int i = 0; i < actualWord.length(); i++)
                    if (actualWord.toLowerCase().substring(i, i + 1).equals(guess)) {
                        userProgress = userProgress.substring(0, i) + guess + userProgress.substring(i + 1);
                        turns++;
                    }
                if (actualWord.toLowerCase().equals(userProgress))
                    return "got word";
                return "added to word";
            }
        } else {
            if (!guess.equalsIgnoreCase(actualWord)) {
                numIncorrect = startLives; // to set lives to 0 in UI updater
                // method if the first condition is
                // the one that is met
                incorrectGuesses.add(guess);
                turns++;
                return "wrong word";
            } else {
                userProgress = guess;
                turns++;
                return "got word";
            }
        }
    }

    /**
     * Determines startLives based on difficulty
     */
    private int determineStartLives() {
        if (difficulty.equals("Extreme"))
            return LIVES_ON_EXTREME_DIFFICULTY;
        if (difficulty.equals("Hard"))
            return LIVES_ON_HARD_DIFFICULTY;
        if (difficulty.equals("Medium"))
            return LIVES_ON_MEDIUM_DIFFICULTY;
        if (difficulty.equals("Easy"))
            return LIVES_ON_EASY_DIFFICULTY;
        return -1;
    }

    /**
     * Gets start time.
     *
     * @return game start time
     */
    public long getGameStartTime() {
        return gameStartTime;
    }

    /**
     * Gets game end time.
     *
     * @return game end time
     */
    public long getGameEndTime() {
        return gameEndTime;
    }

    /**
     * Sets game end time to an end time.
     *
     * @param gameEndTime - an end time
     */
    public void setGameEndTime(long gameEndTime) {
        this.gameEndTime = gameEndTime;
    }

    /**
     * Gets game difficulty.
     *
     * @return game difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Increases the number of incorrect guesses; represents the user's loss of life.
     */
    public void loseLife() {
        numIncorrect++;
    }

    /**
     * Gets the user's start lives.
     *
     * @return the user's start lives
     */
    public int getStartLives() {
        return startLives;
    }

    /**
     * Gets the user's number of incorrect guesses.
     *
     * @return the user's number of incorrect guesses.
     */
    public int getNumIncorrect() {
        return numIncorrect;
    }

    /**
     * Gets the number of turns the user is allowed.
     *
     * @return number of turns.
     */
    public int getTurns() {
        return turns;
    }

    /**
     * Gets the user's incorrect guesses.
     *
     * @return the user's incorrect guesses.
     */
    public ArrayList<String> getIncorrectGuesses() {
        return incorrectGuesses;
    }

    /**
     * Gets the actual word.
     *
     * @return the actual word.
     */
    public String getActualWord() {
        return actualWord;
    }

    /**
     * Gets the user's progress.
     *
     * @return the user's progress.
     */
    public String getUserProgress() {
        return userProgress;
    }

    /**
     * Gets the user's choice on whether or not to keep duplicates.
     *
     * @return the user's choice on whether or not to keep duplicates.
     */
    public boolean isKeepDuplicates() {
        return keepDuplicates;
    }

    /**
     * Gets the user's choice on whether or not to keep proper nouns.
     *
     * @return the user's choice on whether or not to keep proper nouns.
     */
    public boolean isKeepProperNouns() {
        return keepProperNouns;
    }

    /**
     * Gets the user's choice of minimum word length
     *
     * @return the user's choice of minimum word length
     */

    public int getMinimumWordLength() {
        return minimumWordLength;
    }

    /**
     * Gets games file path.
     *
     * @return game's file path.
     */
    public String getFilePath() {
        return file.getAbsolutePath();
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
     * Gets game's data manager.
     *
     * @return The game's data manager.
     */
    public HangmanDataManager getGameDataManager() {
        return gameDataManager;
    }


    /**
     * Calculates the game time
     *
     * @return the game time as a String
     */
    public String getGameTime() {
        String gameTime = "";

        long start = gameStartTime;
        long end = gameEndTime;

        long tMilliSeconds = end - start;

        long tSeconds = tMilliSeconds / 1000;
        long tMinutes = 0;
        long tHours = 0;

        if (tSeconds > 60) {
            tMinutes = tSeconds / 60; // have to cast to truncate
            tSeconds %= 60;
            gameTime = "";

            if (tMinutes > 60) {
                tHours = tMinutes / 60;
                tMinutes %= 60;
                gameTime += tHours + " hour(s) " + tMinutes + " minute(s) " + tSeconds;
            } else
                gameTime = tMinutes + " minute(s) " + gameTime;
        } else
            gameTime = tSeconds + " second(s)";

        return gameTime;
    }
}
