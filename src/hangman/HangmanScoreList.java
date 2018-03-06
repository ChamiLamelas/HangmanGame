package hangman;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that represents the list of scores of hangman games
 *
 * @author Chami Lamelas
 */
public class HangmanScoreList implements Serializable {
    /**
     * Data set of scores.
     */
    private ArrayList<HangmanScorer> scores;

    /**
     * Constructor used to instantiate HangmanScoreList objects.
     */
    public HangmanScoreList() {
        scores = new ArrayList<HangmanScorer>();
    }

    /**
     * Adds a score to the data set.
     *
     * @param s - a score
     */
    public void addScore(HangmanScorer s) {
        scores.add(s);
    }

    /**
     * Returns the number of scores in the data set.
     *
     * @return the number of scores in the data set.
     */
    public int numScores() {
        return scores.size();
    }

    /**
     * Find's the max score in the data set.
     *
     * @return the max score
     */
    public int findMaxScore() {
        if (scores.size() > 0) {
            int max = scores.get(0).getUserScore();
            for (int i = 1; i < scores.size(); i++)
                if (scores.get(i).getUserScore() > max)
                    max = scores.get(i).getUserScore();
            return max;
        }
        return 0;
    }

    /**
     * Gets the user's scores data
     *
     * @return the scores
     */
    public ArrayList<HangmanScorer> getScores() {
        return scores;
    }

}
