package hangman;

//https://stackoverflow.com/questions/1090098/newline-in-jlabel

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Display class that also sets up and runs the game.
 *
 * @author Chami Lamelas
 */
public class HangmanDisplay extends JFrame implements ActionListener {
    /**
     * Game used by the display.
     */
    private HangmanGame game;
    /**
     * Drawing class used by the display.
     */
    private HangmanDrawing gameDrawing;
    /**
     * Class used by display to save this score to set of scores.
     */
    private HangmanScoreManager scoreSavingManager;
    /**
     * Class used by display to save score of this game.
     */
    private HangmanScorer gameScore;
    /**
     * Class used by display to save configuration.
     */
    private HangmanConfigurationManager configSavingManager;
    /**
     * Image file path.
     */
    private String imagePath;
    /**
     * The image.
     */
    private BufferedImage image;
    /**
     * Score report display element.
     */
    private JLabel scoreReportLbl;
    /**
     * User progress display element.
     */
    private JLabel userProgressLbl;
    /**
     * Incorrect answers display element.
     */
    private JLabel incorrectAnswersLbl;
    /**
     * User lives left display element.
     */
    private JLabel userLivesLeftLbl;
    /**
     * User's guess input element.
     */
    private JTextField userInputFld;
    /**
     * User input control element.
     */
    private JButton userInputBtn;
    /**
     * Default field text for letter guessing portion
     */
    public static final String NEXT_LETTER_GUESS = "Enter guess";
    /**
     * /**
     * Default field error message for next letter guess portion.
     */
    public static final String NEXT_LETTER_ERROR_MSG = "Must be 1 character or word";
    /**
     * Display window's size.
     */
    private final int WINDOW_WIDTH;
    /**
     * Display window's size.
     */
    private final int WINDOW_HEIGHT;
    /**
     * Default text field width of objects of this class
     */
    public static final int DEFAULT_TEXT_FIELD_WIDTH = 15;
    /**
     * Menu bar of UI
     */
    private JMenuBar mainMenuBar;
    /**
     * Menu with game information
     */
    private JMenu gameInfo;
    /**
     * About info button on menu bar
     */
    private JMenuItem aboutInfo;
    /**
     * Help Info on menu bar
     */
    private JMenuItem helpInfo;

    /**
     * Constructor instantiate HangmanDisplay objects.
     */
    public HangmanDisplay() {

        // Sets screen size based on computer dimensions
        GraphicsConfiguration gc = this.getGraphicsConfiguration();
        int screenWidth = (int) gc.getBounds().getWidth();
        int screenHeight = (int) gc.getBounds().getHeight();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        WINDOW_WIDTH = screenWidth - (screenInsets.left + screenInsets.right);
        WINDOW_HEIGHT = screenHeight - (screenInsets.top + screenInsets.bottom);

        // Gets the image
        try {
            imagePath = "/icon.png";
            InputStream imgStream = this.getClass().getResourceAsStream(imagePath);
            if (imgStream != null)
                image = ImageIO.read(imgStream);
            else
                JOptionPane.showMessageDialog(null, new JLabel("<html>There was an error locating the image file used for the game icon. <br/> The game will proceed normally without an icon. </html>"), "Hangman - Image Location Error", JOptionPane.INFORMATION_MESSAGE);
            this.setIconImage(image);
        } catch (IOException exception) {
            displayError(exception.getMessage());
        }

        File dir = new File("Hangman Data");
        if (dir.isDirectory()) {
            configSavingManager = new HangmanConfigurationManager(dir.getAbsolutePath());
        } else {
            HangmanDataManager dataMgr = new HangmanDataManager();
            configSavingManager = new HangmanConfigurationManager(dataMgr.getGameDataDirPath());
        }
        configSavingManager.getGameSettings();

        gameScore = new HangmanScorer();

        HangmanSetUp setUp = new HangmanSetUp(configSavingManager.getConfig(), WINDOW_WIDTH, WINDOW_HEIGHT, image);
        HangmanConfiguration gameConfiguration = setUp.getGameConfiguration();

        if (gameConfiguration.getFilepath() == null) // if there is no file path in configuration, run file selection utility
            gameConfiguration.setFilePath(getFilePathToStartGame());

        game = new HangmanGame(gameConfiguration);
        //System.out.println(game.toString());
        configSavingManager.saveUserSettings(game); // game has been loaded with set-up data, can now be used to save user settings
        configSavingManager.writeGameSettings(); // writes to file

        gameDrawing = new HangmanDrawing(game);

        scoreSavingManager = new HangmanScoreManager(game.getGameDataManager().getGameDataDirPath());
        scoreSavingManager.readData();

        addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent arg0) {
            }

            public void windowClosed(WindowEvent arg0) {
            }

            @Override
            public void windowClosing(WindowEvent arg0) {
                endGameEarly();
            }

            public void windowDeactivated(WindowEvent arg0) {
            }

            public void windowDeiconified(WindowEvent arg0) {
            }

            public void windowIconified(WindowEvent arg0) {
            }

            public void windowOpened(WindowEvent arg0) {
            }

        });

        buildMenuBar();
        buildScoreReportSection();
        buildUserProgressSection();
        buildIncorrectAnswersSection();
        buildUserInputSection();
        buildLayout();

        setTitle("Hangman - " + game.getDifficulty() + " Difficulty");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Gets file path to start the game.
     *
     * @return file path to start the game.
     */
    private String getFilePathToStartGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Hangman - Select Words File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            // cannot cancel closing..because you cannot edit close operation
            displayError("You have not selected an input file. The program will now be terminated. ");
            System.exit(0);
            return null;
        }
    }

    /**
     * Builds UI's menu bar
     */
    private void buildMenuBar() {
        mainMenuBar = new JMenuBar();

        gameInfo = new JMenu("Information");
        gameInfo.setToolTipText("Click to get information about Hangman");
        gameInfo.setMnemonic(KeyEvent.VK_I);

        aboutInfo = new JMenuItem("About"); // \uD83D\uDEC8
        aboutInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "ABOUT MESSAGE!", "Hangman - About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        aboutInfo.setToolTipText("Click to learn about the development and purpose of Hangman!");

        helpInfo = new JMenuItem("Help "); // \u2753
        helpInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, new JLabel("<html>Rules<br/><br/>1. The program will stop you if you try to type more than one word or if you don't enter <br/>anything. This is following the idea that a human opponent would do the same for you.<br/>2. If you guess an incorrect character twice, the program will not deduct you of lives more <br/>than once.<br/>3. If you type in a word instead of a letter, which is legal, you will NOT be warned. However, <br/>you will lose entirely if the word is not the actual word.<br/>4. In addition, if you guess a word that is not the same length as the actual word, the <br/>program will still not correct you.<br/><br/>Scoring System<br/><br/>-15 points for guessing the word in 1 turn<br/>-2 points for each turn you have not used.<br/>-5 points for easy difficulty<br/>-10 points for medium difficulty<br/>-15 points for hard difficulty<br/>-20 points for extreme difficulty<br/><br/>Set-up<br/><br/>1. The first set-up portion is to choose the configuration you would like for the game. This <br/>includes five parts: the choice of difficulty, which determines your number of lives: easy: <br/>" + HangmanGame.LIVES_ON_EASY_DIFFICULTY + " lives, medium: " + HangmanGame.LIVES_ON_MEDIUM_DIFFICULTY + " lives, hard:" + HangmanGame.LIVES_ON_HARD_DIFFICULTY + " lives, extreme: " + HangmanGame.LIVES_ON_EXTREME_DIFFICULTY + " lives; whether or not you wish to <br/>keep duplicate words; whether or not you wish to keep proper nouns; whether or not to <br/>display the file path; and lastly the minimum word length you would like to be allowed. <br/>However, for minimum word length, the minimum is 2, NOT 0. Also, as an idea, the largest word <br/>in most english dictionaries <br/>is " + HangmanGame.MAXIMUM_MINIMUM_WORD_LENGTH + " letters. <br/><br/>2. The next part of the set-up allows you to reset the configuration and scores saved on the <br/>computer. This would erase all configuration data (including the last file you chose if you <br/>had one chosen). <br/><br/>Now you can start the game. If there is no file specified, then a dialog window will open <br/>allowing you to choose a file from your computer. <br/>If you wish to change the file between games, click \"Restore Game Settings\".<br/>If you have not yet downloaded a file, you <br/>can click the \"Browse Web\" button to open your browser to search for a source file. <br/><br/>The program will remove punctuation, related characters, and words that are less than the <br/>minimum word length you specify. </html>"), "Hangman - Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpInfo.setToolTipText("Click to get help about how Hangman works!");

    }

    /**
     * Builds score report section of UI
     */
    private void buildScoreReportSection() {
        scoreReportLbl = new JLabel("<html> Scores on Computer: " + scoreSavingManager.getData().numScores() + "<br>Current Max Score: " + scoreSavingManager.getData().findMaxScore() + "</html>");
        scoreReportLbl.setFont(new Font("book antiqua", Font.PLAIN, 16));
        scoreReportLbl.setForeground(Color.BLACK);
        scoreReportLbl.setToolTipText("Score information on the computer");
    }

    /**
     * Builds user progress section of the UI
     */
    private void buildUserProgressSection() {
        userProgressLbl = new JLabel(game.getUserProgress());
        userProgressLbl.setFont(new Font("book antiqua", Font.PLAIN, 44));
        userProgressLbl.setForeground(Color.GREEN);
        userProgressLbl.setToolTipText("Your progress on guessing the word");
    }

    /**
     * Builds incorrect answers section of the UI
     */
    private void buildIncorrectAnswersSection() {
        incorrectAnswersLbl = new JLabel("");
        incorrectAnswersLbl.setFont(new Font("book antiqua", Font.PLAIN, 16));
        incorrectAnswersLbl.setForeground(Color.RED);
        incorrectAnswersLbl.setToolTipText("Your incorrect answers");
    }

    /**
     * Builds user input section of the UI
     */
    private void buildUserInputSection() {
        userLivesLeftLbl = new JLabel("Lives: " + (game.getStartLives() + "/" + game.getStartLives()));
        userLivesLeftLbl.setFont(new Font("book antiqua", Font.PLAIN, 16));
        userLivesLeftLbl.setForeground(Color.BLACK);
        userLivesLeftLbl.setToolTipText("Your current number of lives (" + (game.getStartLives() - game.getNumIncorrect()) + ") out of the total number of lives for the difficulty you chose (" + game.getDifficulty() + ": " + game.getStartLives() + ")");

        userInputFld = new JTextField(DEFAULT_TEXT_FIELD_WIDTH);
        userInputFld.setText("Enter guess");
        userInputFld.setFont(new Font("book antiqua", Font.PLAIN, 16));
        userInputFld.setForeground(Color.BLUE);
        userInputFld.addActionListener(this);
        userInputFld.setActionCommand("userInput");
        userInputFld.setToolTipText("Enter a letter that you think may be in the word or what you think the word is!");
        userInputFld.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent arg0) {
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                if (userInputFld.getText().equals(NEXT_LETTER_GUESS) || userInputFld.getText().equals(NEXT_LETTER_ERROR_MSG)) {
                    userInputFld.setText("");
                }
            }

            public void mouseReleased(MouseEvent arg0) {
            }

        });

        userInputFld.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                // if substring(1) is not used, char is displayed 2x
                // https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key
                // if it is any other key besides "Enter" then clear
                if ((userInputFld.getText().equals(NEXT_LETTER_GUESS) || userInputFld.getText().equals(NEXT_LETTER_ERROR_MSG)) && arg0.getKeyCode() != KeyEvent.VK_ENTER)
                    userInputFld.setText(("" + arg0.getKeyChar()).substring(1));
            }

            public void keyReleased(KeyEvent arg0) {
            }

            public void keyTyped(KeyEvent arg0) {
            }

        });

        //http://www.iemoji.com/view/emoji/1036/proposed/two-button-mouse
        userInputBtn = new JButton("\u25BA");
        userInputBtn.addActionListener(this);
        userInputBtn.setActionCommand("userInput");
        userInputBtn.setForeground(Color.BLUE);
        userInputBtn.setToolTipText("See if what you typed is either a letter in the word or is the word!");
    }


    /**
     * Class's action listener for the text field and button, because upon either clicking the button or clicking
     * "Enter" in the text field, the same action will be performed.
     *
     * @param e an ActionEvent sent by a UI element
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Test:" + userInputFld.getText());
        if (userInputFld.getText().isEmpty() || userInputFld.getText().indexOf(" ") != -1) {
            userInputFld.setText(NEXT_LETTER_ERROR_MSG);
        } else {
            updateGame(userInputFld.getText());
        }
    }


    /**
     * Updates game
     *
     * @param s a String to be analyzed by the game
     */
    private void updateGame(String s) {
        String out = game.isInWord(s.toLowerCase());
        updateUI(out);
        if (out.equals("got word")) {
            game.setGameEndTime(System.currentTimeMillis());
            gameScore.calculateScore(game.getTurns(), game.getDifficulty());
            scoreSavingManager.writeData(gameScore);
            winGame();
            System.exit(0);
        } else if (out.equals("out of lives") || out.equals("wrong word")) {
            scoreSavingManager.writeData(gameScore);
            loseGame(out);
            System.exit(0);
        }
    }

    /**
     * Displayed dialog if the user wins the game
     */
    private void winGame() {
        JLabel winText = new JLabel("Congratulations, you have won the game on " + game.getDifficulty()
                + " difficulty in " + game.getTurns() + " turn(s) in " + game.getGameTime());
        winText.setFont(new Font("book antiqua", Font.BOLD, 16));
        winText.setForeground(Color.GREEN);
        JOptionPane.showMessageDialog(null, winText, "Hangman - Program Finishing",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displayed dialog if the user loses the game
     *
     * @param reason The reason why the user lost the game.
     */
    private void loseGame(String reason) {
        JLabel loseText;
        if (reason.equalsIgnoreCase("out of lives"))
            loseText = new JLabel("You have run out of lives. ");
        else
            loseText = new JLabel("The word you guessed (" + game.getIncorrectGuesses().get(game.getIncorrectGuesses().size() - 1) + ") is incorrect. ");
        loseText.setFont(new Font("book antiqua", Font.BOLD, 16));
        loseText.setForeground(Color.RED);
        JOptionPane.showMessageDialog(null, loseText, "Hangman - Program Finishing ",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displayed dialog if the user ends the game early (clicks the "X" in the main window)
     */
    private void endGameEarly() {
        JLabel closingLabel = new JLabel(
                "<html>Are you sure you would like to exit, even though you have not finished playing? <br><br>Your number of turns or difficulty will not be recorded for this game. <br>There will be no record of the score data from this game. </html>");
        closingLabel.setFont(new Font("book antiqua", Font.PLAIN, 16));
        int choice = JOptionPane.showOptionDialog(null, closingLabel,
                "Hangman - Closing ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (choice == JOptionPane.OK_OPTION)
            System.exit(0);
    }


    /**
     * Updates the game's UI upon change
     *
     * @param s a String that will determine how the UI will be changed
     */
    private void updateUI(String s) {
        if (s.equals("added to word") || s.equals("got word"))
            userProgressLbl.setText(game.getUserProgress());
        else if (s.equals("not in word") || s.equals("out of lives") || s.equals("wrong word")) {
            if (incorrectAnswersLbl.getText().equals("")) {
                incorrectAnswersLbl.setText(game.getIncorrectGuesses().get(game.getIncorrectGuesses().size() - 1));
            } else
                incorrectAnswersLbl.setText(incorrectAnswersLbl.getText() + ", "
                        + game.getIncorrectGuesses().get(game.getIncorrectGuesses().size() - 1));
            if (s.equals("out of lives") || s.equals("wrong word"))
                userProgressLbl.setText(game.getActualWord());
            add(gameDrawing);
        }
        userLivesLeftLbl.setText("Lives: " + (game.getStartLives() - game.getNumIncorrect()) + "/" + game.getStartLives()); // updates lives label
        // updates tool tip of lives label
        userLivesLeftLbl.setToolTipText("Your current number of lives (" + (game.getStartLives() - game.getNumIncorrect()) + ") out of the total number of lives for the difficulty you chose (" + game.getDifficulty() + ": " + game.getStartLives() + ")");
        userInputFld.setText(NEXT_LETTER_GUESS);
        repaint();
    }

    /**
     * Builds UI elements' layout
     */
    private void buildLayout() {
        gameInfo.add(aboutInfo);
        gameInfo.add(helpInfo);

        mainMenuBar.add(gameInfo);

        //setUpMenuBar.add(Box.createHorizontalGlue()); // pushes the components that follow to the right
        //setUpMenuBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // puts the components of the menu bar in the desired order

        setJMenuBar(mainMenuBar);

        JPanel scoreReportPnl = new JPanel();
        scoreReportPnl.add(scoreReportLbl);
        scoreReportPnl.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "User's Progress",
                TitledBorder.CENTER, TitledBorder.LEFT, new Font("book antiqua", Font.BOLD, 16), Color.BLACK));


        JPanel userProgressPnl = new JPanel();
        userProgressPnl.add(userProgressLbl);
        userProgressPnl.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "User's Progress",
                TitledBorder.CENTER, TitledBorder.LEFT, new Font("book antiqua", Font.BOLD, 16), Color.BLACK));

        JPanel incorrectAnswersPnl = new JPanel();
        incorrectAnswersPnl.add(incorrectAnswersLbl);
        incorrectAnswersPnl.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "Incorrect Guesses",
                TitledBorder.CENTER, TitledBorder.LEFT, new Font("book antiqua", Font.BOLD, 16), Color.BLACK));

        JPanel userInputPnl = new JPanel();
        userInputPnl.add(userLivesLeftLbl);
        userInputPnl.add(userInputFld);
        userInputPnl.add(userInputBtn);
        userInputPnl.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "User Input Area",
                TitledBorder.CENTER, TitledBorder.LEFT, new Font("book antiqua", Font.BOLD, 16), Color.BLACK));

        JPanel westernPnl = new JPanel();
        westernPnl.add(scoreReportPnl);
        westernPnl.add(userProgressPnl);
        westernPnl.add(incorrectAnswersPnl);
        westernPnl.add(userInputPnl);
        westernPnl.setLayout(new GridLayout(4, 1));

        setLayout(new GridLayout(1, 2));
        add(westernPnl);
        add(gameDrawing);

    }

    /**
     * Displays game error message. Is a class function because it is a utility used elsewhere in the package
     *
     * @param errorMsg an error message to be displayed
     */
    public static void displayError(String errorMsg) {
        JLabel text = new JLabel(errorMsg);
        text.setFont(new Font("book antiqua", Font.PLAIN, 16));
        text.setForeground(Color.BLUE);
        JOptionPane.showMessageDialog(null, text, "Hangman - Program Closing. ",
                JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }


}
