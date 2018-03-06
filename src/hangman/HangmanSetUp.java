package hangman;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// https://docs.oracle.com/javase/7/docs/api/javax/swing/JDialog.html
// https://stackoverflow.com/questions/4585867/transparent-jbutton
// https://www.w3schools.com/charsets/ref_utf_arrows.asp
// http://www.fileformat.info/info/unicode/char/1f310/index.htm
// https://www.w3schools.com/charsets/ref_utf_dingbats.asp
// http://www.fileformat.info/info/unicode/char/1f6c8/index.htm

/**
 * Class that runs set up window and gets user selections for configuration and provides rules for user.
 *
 * @author Chami Lamelas
 */
public class HangmanSetUp extends JDialog {
    /**
     * For booting up game.
     */
    private HangmanConfiguration gameConfiguration;
    /**
     * For booting up set-up window.
     */
    private HangmanConfiguration setUpConfiguration;
    /**
     * For displaying the rules.
     */
    private JTextArea rules;
    /**
     * For user to control what rules are being viewed.
     */
    private JScrollPane rulesCtrl;
    /**
     * Menu label.
     */
    private JLabel text;
    /**
     * Text entry label.
     */
    private JLabel minWordLabel;
    /**
     * Text entry to choose minimum word length
     */
    private JTextField minWordLengthTxtEntry;
    /**
     * Difficulty selection menu.
     */
    private JComboBox difficultyMenu;
    /**
     * Checkbox to select keep duplicates setting.
     */
    private JCheckBox optionKeepDuplicates;
    /**
     * Checkbox to select keep proper nouns setting.
     */
    private JCheckBox optionKeepProperNouns;
    /**
     * File path label.
     */
    private JLabel filePathLabel;
    /**
     * Text area to display file path
     */
    private JTextArea filepathDisplay;
    /**
     * For user to control file path display area
     */
    private JScrollPane filePathDisplayCtrl;
    /**
     * Check box to display file path or not
     */
    private JCheckBox filePathDisplaySelection;
    /**
     * Button to reset setUpConfiguration.
     */
    private JButton resetConfig;
    /**
     * Button to reset scores.
     */
    private JButton resetScores;
    /**
     * Button to browse the web.
     */
    private JButton browse;
    /**
     * Button to start game.
     */
    private JButton start;
    /**
     * File path error text
     */
    public static final String FILE_PATH_ERROR_TXT = "No file path specified.";
    /**
     * Menu bar for set up window
     */
    private JMenuBar setUpMenuBar;
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
     * Constructor to instantiate object
     *
     * @param config - configuration used to set up window
     * @param width  - width of display
     * @param height - height of display
     * @param img    - image icon
     */

    public HangmanSetUp(HangmanConfiguration config, int width, int height, Image img) {
        setUpConfiguration = config;
        setTitle("Hangman - Set-up");
        setBounds((int) (0.2 * width), (int) (0.14 * height), (int) (0.6 * width), (int) (0.72 * height));
        setResizable(true);
        setIconImage(img);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setModalityType(ModalityType.APPLICATION_MODAL);

        addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent arg0) {
            }

            public void windowClosed(WindowEvent arg0) {
            }

            @Override
            public void windowClosing(WindowEvent arg0) {
                exitSetUp();
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

        buildUIElements();
        buildUILayout();

        setVisible(true);
    }

    /**
     * Builds dialog box's UI elements.
     */
    private void buildUIElements() {
        rules = new JTextArea(15, 95);
        rules.setEditable(false);
        rules.setText("Hangman - Version: BETA \n\nRules\n\n1. The program will stop you if you try to type more than one word or if you don't enter \nanything. This is following the idea that a human opponent would do the same for you.\n2. If you guess an incorrect character twice, the program will not deduct you of lives more \nthan once.\n3. If you type in a word instead of a letter, which is legal, you will NOT be warned. However, \nyou will lose entirely if the word is not the actual word.\n4. In addition, if you guess a word that is not the same length as the actual word, the \nprogram will still not correct you.\n\nScoring System\n\n-15 points for guessing the word in 1 turn\n-2 points for each turn you have not used.\n-5 points for easy difficulty\n-10 points for medium difficulty\n-15 points for hard difficulty\n-20 points for extreme difficulty\n\nSet-up\n\n1. The first set-up portion is to choose the configuration you would like for the game. This \nincludes five parts: the choice of difficulty, which determines your number of lives: easy: \n" + HangmanGame.LIVES_ON_EASY_DIFFICULTY + " lives, medium: " + HangmanGame.LIVES_ON_MEDIUM_DIFFICULTY + " lives, hard:" + HangmanGame.LIVES_ON_HARD_DIFFICULTY + " lives, extreme: " + HangmanGame.LIVES_ON_EXTREME_DIFFICULTY + " lives; whether or not you wish to \nkeep duplicate words; whether or not you wish to keep proper nouns; whether or not to display \nthe file path; and lastly the minimum word length you would like to be allowed. \nHowever, for minimum word length, the minimum is 2, NOT 0. Also, as an idea, the largest word \nin most english dictionaries is " + HangmanGame.MAXIMUM_MINIMUM_WORD_LENGTH + " letters. \n\n2. The next part of the set-up allows you to reset the configuration and scores saved on the \ncomputer. This would erase all configuration data (including the last file you chose if you \nhad one chosen). \n\nNow you can start the game. If there is no file specified, then a dialog window will open \nallowing you to choose a file from your computer. \nIf you wish to change the file between games, click \"Restore Game Settings\". \nIf you have not yet downloaded a file, you can click the \"Browse Web\" button to open your \nbrowser to search for a source file. \n\nThe program will remove punctuation, related characters, and words that are less than the \nminimum word length you specify. ");
        rulesCtrl = new JScrollPane(rules);

        minWordLabel = new JLabel("Enter desired minimum word length: ");

        minWordLengthTxtEntry = new JTextField(2);
        minWordLengthTxtEntry.setText("" + setUpConfiguration.getMinimumWordLength());

        text = new JLabel(
                "Enter desired difficulty:");

        difficultyMenu = new JComboBox();
        difficultyMenu.addItem("Easy");
        difficultyMenu.addItem("Medium");
        difficultyMenu.addItem("Hard");
        difficultyMenu.addItem("Extreme");

        difficultyMenu.setSelectedItem(setUpConfiguration.getDifficulty());
        difficultyMenu.setToolTipText("Choose game difficulty: Easy (" + HangmanGame.LIVES_ON_EASY_DIFFICULTY + " lives), Medium (" + HangmanGame.LIVES_ON_MEDIUM_DIFFICULTY + " lives), Hard (" + HangmanGame.LIVES_ON_HARD_DIFFICULTY + " lives) and Extreme (" + HangmanGame.LIVES_ON_EXTREME_DIFFICULTY + " lives)");

        optionKeepDuplicates = new JCheckBox("Keep Duplicate Words");
        optionKeepDuplicates.setSelected(setUpConfiguration.isKeepDuplicates());
        optionKeepDuplicates.setToolTipText("All words that are ");
        optionKeepProperNouns = new JCheckBox("Keep Proper Nouns");
        optionKeepProperNouns.setSelected(setUpConfiguration.isKeepProperNouns());

        filePathLabel = new JLabel("File path: ");

        filepathDisplay = new JTextArea(1, 20);
        filepathDisplay.setEditable(false);
        if (setUpConfiguration.getFilepath() == null)
            filepathDisplay.setText(FILE_PATH_ERROR_TXT);
        else if(!setUpConfiguration.isDisplayFilePath())
            filepathDisplay.setText("Check box to re-enable.");
        else
            filepathDisplay.setText(setUpConfiguration.getFilepath());

        filePathDisplayCtrl = new JScrollPane(filepathDisplay);

        filePathDisplaySelection = new JCheckBox("Show File Path");
        filePathDisplaySelection.setSelected(setUpConfiguration.isDisplayFilePath());

        // https://stackoverflow.com/questions/9882845/jcheckbox-actionlistener-and-itemlistener
        // "Both ItemListener as well as ActionListener, in case of JCheckBox have the same behaviour.
        // However, major difference is ItemListener can be triggered by calling the setSelected(true) on the checkbox "

        filePathDisplaySelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!filepathDisplay.getText().equalsIgnoreCase(FILE_PATH_ERROR_TXT)) {
                    if (filePathDisplaySelection.isSelected()) {
                        filepathDisplay.setText(setUpConfiguration.getFilepath());
                    }
                    else
                        filepathDisplay.setText("Check box to re-enable.");
                }
                // if no file path has been chosen, check box is effectively inactive
            }
        });

        resetConfig = new JButton("Restore Game Settings \u21BA");
        resetConfig.setToolTipText("Restores user game settings. ");

        resetConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // update data on hard disk
                HangmanConfigurationManager manager;
                File dir = new File("Hangman Data");
                if (dir.isDirectory()) {
                    manager = new HangmanConfigurationManager(dir.getAbsolutePath());
                } else {
                    HangmanDataManager dataMgr = new HangmanDataManager();
                    manager = new HangmanConfigurationManager(dataMgr.getGameDataDirPath());
                }

                manager.resetGameSettings();
                manager.writeGameSettings();

                // update RAM data (setUpConfiguration)
                setUpConfiguration.reset();

                resetUI();
            }
        });

        resetScores = new JButton("Refresh Scores \u21BB");
        resetScores.setToolTipText("Refreshes scores on the computer. ");

        // https://www.mkyong.com/java/how-to-delete-file-in-java/

        resetScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File dir = new File("Hangman Data"); // if no folder exists, one will be created on boot

                // https://stackoverflow.com/questions/5603966/how-to-make-filefilter-in-java

                File[] files = dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getAbsolutePath().equals(dir.getAbsolutePath() + "\\hangman_scores.dat");
                    }
                });

                if(files.length == 1) {
                    files[0].delete();
                    JOptionPane.showMessageDialog(null, "Deleted scores data on computer. ", "Hangman - Scores Reset", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "No scores data found. Make sure you did not move the file \"hangman_scores.dat\"", "Hangman - Scores Reset", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        browse = new JButton("Browse Web");
        browse.setForeground(Color.BLUE);
        browse.setToolTipText("Click to open web browser to locate a source file.");
        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // launches web browser
                Desktop currentDesktop = Desktop.getDesktop();
                try {
                    currentDesktop.browse(new URI("www.google.com"));
                } catch (URISyntaxException exception) {
                    HangmanDisplay.displayError(exception.getMessage());
                } catch (IOException exception) {
                    HangmanDisplay.displayError(exception.getMessage());
                }
            }
        });

        start = new JButton("\u25BA");
        start.setForeground(Color.GREEN);
        start.setToolTipText("Starts game with these settings.");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int minLength = Integer.parseInt(minWordLengthTxtEntry.getText());
                    if (minLength >= HangmanGame.MINIMUM_MINIMUM_WORD_LENGTH) {
                        // builds game configuration
                        String tempFP = setUpConfiguration.getFilepath();
                        // System.out.println(tempFP);
                        if (tempFP != null && !new File(setUpConfiguration.getFilepath()).exists()) {
                            tempFP = null;
                            JLabel fileNotFoundText = new JLabel("<html>File \"" + tempFP + "\"not found. <br/>Will now open file chooser utility.</html>");
                            fileNotFoundText.setFont(new Font("book antiqua", Font.BOLD, 16));
                            fileNotFoundText.setForeground(Color.RED);
                            JOptionPane.showMessageDialog(null, fileNotFoundText, "Hangman - File not Found ",
                                    JOptionPane.PLAIN_MESSAGE);
                        }

                        gameConfiguration = new HangmanConfiguration(difficultyMenu.getSelectedItem().toString(), optionKeepDuplicates.isSelected(), optionKeepProperNouns.isSelected(), minLength, tempFP, filePathDisplaySelection.isSelected());

                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "<html>ERROR!<br /><br />The value you entered for the minimum word length is too small. <br />The smallest possible value is " + HangmanGame.MINIMUM_MINIMUM_WORD_LENGTH + ".</html>", "Hangman - Set-up", JOptionPane.ERROR_MESSAGE);
                        minWordLengthTxtEntry.setText("");
                    }
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "<html>ERROR!<br /><br />The value you entered for the minimum word length is not a number. <br />The value must be a number, and the smallest possible value is " + HangmanGame.MINIMUM_MINIMUM_WORD_LENGTH + ".</html>", "Hangman - Set-up", JOptionPane.ERROR_MESSAGE);
                    minWordLengthTxtEntry.setText("");
                }

            }
        });

        this.getRootPane().setDefaultButton(start); // this way, if the user simply hits "return" or "enter" on boot-up, the program will start

        setUpMenuBar = new JMenuBar();

        gameInfo = new JMenu("Information");
        gameInfo.setToolTipText("Click to get information about Hangman");
        gameInfo.setMnemonic(KeyEvent.VK_I);

        aboutInfo = new JMenuItem("About "); // \uD83D\uDEC8
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
     * Resets user input fields.
     */
    private void resetUI() {
        filepathDisplay.setText(FILE_PATH_ERROR_TXT);
        optionKeepDuplicates.setSelected(HangmanGame.DEFAULT_DUPLICATES_SETTING);
        optionKeepProperNouns.setSelected(HangmanGame.DEFAULT_PROPER_NOUNS_SETTING);
        difficultyMenu.setSelectedItem(HangmanGame.DEFAULT_DIFFICULTY);
        minWordLengthTxtEntry.setText("" + HangmanGame.DEFAULT_MINIMUM_WORD_LENGTH);
        filePathDisplaySelection.setSelected(HangmanGame.DEFAULT_FILE_PATH_DISPLAY_SETTING);
    }

    /**
     * Opens dialog window to confirm user exit
     */
    private void exitSetUp()
    {
        JLabel closingLabel = new JLabel(
                "<html>Are you sure you would like to exit, even though you have not finished game set-up? </html>");
        closingLabel.setFont(new Font("book antiqua", Font.PLAIN, 16));
        int choice = JOptionPane.showOptionDialog(null, closingLabel,
                "Hangman - Set-up Cancelled ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (choice == JOptionPane.OK_OPTION)
            System.exit(0);
    }

    /**
     * Builds UI's element layout.
     */
    private void buildUILayout() {
        gameInfo.add(aboutInfo);
        gameInfo.add(helpInfo);

        setUpMenuBar.add(gameInfo);

        //setUpMenuBar.add(Box.createHorizontalGlue()); // pushes the components that follow to the right
        //setUpMenuBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); // puts the components of the menu bar in the desired order

        setJMenuBar(setUpMenuBar);

        JPanel rulesPanel = new JPanel();
        rulesPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "Welcome to Hangman!",
                TitledBorder.CENTER, TitledBorder.LEFT, new Font("book antiqua", Font.BOLD, 16), Color.BLACK));
        rulesPanel.add(rulesCtrl);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        optionsPanel.add(text);
        optionsPanel.add(difficultyMenu);
        optionsPanel.add(optionKeepDuplicates);
        optionsPanel.add(optionKeepProperNouns);

        JPanel minWordLengthPanel = new JPanel();
        minWordLengthPanel.add(minWordLabel);
        minWordLengthPanel.add(minWordLengthTxtEntry);

        JPanel filePanel = new JPanel();
        filePanel.add(filePathLabel);
        filePanel.add(filePathDisplayCtrl);
        filePanel.add(filePathDisplaySelection);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(resetConfig);
        buttonsPanel.add(resetScores);
        buttonsPanel.add(browse);
        buttonsPanel.add(start);

        JPanel northPanel = new JPanel();
        northPanel.add(rulesPanel);

        JPanel southUpperPanel = new JPanel();
        southUpperPanel.setLayout(new FlowLayout());
        southUpperPanel.add(optionsPanel);
        southUpperPanel.add(minWordLengthPanel);

        JPanel southLowerPanel = new JPanel();
        southLowerPanel.setLayout(new FlowLayout());
        southLowerPanel.add(filePanel);
        southLowerPanel.add(buttonsPanel);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "Set-up Options"));
        southPanel.add(southUpperPanel, BorderLayout.NORTH);
        southPanel.add(southLowerPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.CENTER);

        //https://stackoverflow.com/questions/11425103/how-to-get-the-windows-native-look-in-java-gui-programming

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException classNotFoundException) {
        }
        catch (IllegalAccessException illegalAccessException) {
        }
        catch (InstantiationException instantiationException) {
        }
        catch (UnsupportedLookAndFeelException unsupportedLandFException) {
        }


        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Gets config data.
     *
     * @return config data.
     */
    public HangmanConfiguration getGameConfiguration() {
        return gameConfiguration;
    }
}
