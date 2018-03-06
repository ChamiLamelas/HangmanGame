package hangman;

import java.io.Serializable;

/**
 * Class that scores hangman games.
 *
 * @author Chami Lamelas
 */
public class HangmanScorer implements Serializable {
    /**
     * Conversion factor for generating the score based on number of turns left.
     */
    public static final int TURNS_LEFT_CONVERSION = 2;
    /**
     * Conversion factor for generating the score based on number of difficulty.
     */
    public static final int DIFFICULTY_CONVERSION = 5;
    /**
     * Easy difficulty multiplier.
     */
    public static final int EASY_DIFFICULTY_MULTIPLIER = 1;
    /**
     * Medium difficulty multiplier.
     */
    public static final int MEDIUM_DIFFICULTY_MULTIPLIER = 2;
    /**
     * Hard difficulty multiplier.
     */
    public static final int HARD_DIFFICULTY_MULTIPLIER = 3;
    /**
     * Extreme difficulty multiplier.
     */
    public static final int EXTREME_DIFFICULTY_MULTIPLIER = 4;
    /**
     * User score.
     */
    private int userScore;

    /**
     * Constructor used to instantiate HangmanScorer objects.
     */
    public HangmanScorer() {
        userScore = 0;
    }

    /**
     * Calculates score based on difficulty and turns used.
     *
     * @param usedTurns            - number of used turns
     * @param userChosenDifficulty - difficulty chosen by the user
     */
    public void calculateScore(int usedTurns, String userChosenDifficulty) {
        if (usedTurns == 1)
            userScore += 15;
        if (userChosenDifficulty.equals("Easy")) {
            userScore += TURNS_LEFT_CONVERSION * (HangmanGame.LIVES_ON_EASY_DIFFICULTY - usedTurns);
            userScore += DIFFICULTY_CONVERSION * EASY_DIFFICULTY_MULTIPLIER;
        } else if (userChosenDifficulty.equals("Medium")) {
            userScore += TURNS_LEFT_CONVERSION * (HangmanGame.LIVES_ON_MEDIUM_DIFFICULTY - usedTurns);
            userScore += DIFFICULTY_CONVERSION * MEDIUM_DIFFICULTY_MULTIPLIER;
        } else if (userChosenDifficulty.equals("Hard")) {
            userScore += TURNS_LEFT_CONVERSION * (HangmanGame.LIVES_ON_HARD_DIFFICULTY - usedTurns);
            userScore += DIFFICULTY_CONVERSION * HARD_DIFFICULTY_MULTIPLIER;
        } else if (userChosenDifficulty.equals("Extreme")) {
            userScore += TURNS_LEFT_CONVERSION * (HangmanGame.LIVES_ON_EXTREME_DIFFICULTY - usedTurns);
            userScore += DIFFICULTY_CONVERSION * EXTREME_DIFFICULTY_MULTIPLIER;
        }
    }

    /**
     * Gets the user's score.
     *
     * @return the user's score.
     */
    public int getUserScore() {
        return userScore;
    }

}
