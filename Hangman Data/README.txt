Hangman - README
----------------

README Created on 03/17/2018 at 09:38 PM by Chami

USER INSTRUCTIONS:

DO NOT delete "hangman.config" and "hangman_scores" unless you wish to delete game configuration and saved scores.
BEFORE running this program, please have the Java Development Kit installed for the Java version you are running. 

GAME RULES: 

1. The program will stop you if you try to type more than one word or if you don't enter anything. 
This is following the idea that a human opponent would do the same for you.
2. If you guess an incorrect character twice, the program will not deduct you of lives more than once.
3. If you type in a word instead of a letter, which is legal, you will NOT be warned. 
However, you will lose entirely if the word is not the actual word.
4. In addition, if you guess a word that is not the same length as the actual word, the program will still not correct you.

SCORING SYSTEM: 

-15 points for guessing the word in 1 turn
-2 points for each turn you have not used.
-5 points for easy difficulty
-10 points for medium difficulty
-15 points for hard difficulty
-20 points for extreme difficulty

SET-UP: 

1. The first set-up portion is to choose the configuration you would like for the game. 
This includes five parts: 
(i) the choice of difficulty, which determines your number of lives: 
-easy: "20" lives
-medium: "14" lives
-hard:"10" lives
-extreme: "6" lives
(ii) whether or not you wish to keep duplicate words
(iii) whether or not you wish to keep proper nouns
(iv) the minimum word length you would like to be allowed.
(v) whether or not to display the file path
However, for minimum word length, the minimum is 2, NOT 0.
Also, as an idea, the largest word in most english dictionaries is "45" letters. 
2. The next part of the set-up allows you to reset the configuration and scores saved on the computer. 
This would erase all configuration data (including the last file you chose if you \nhad one chosen). 
Now you can start the game. 
If there is no file specified, then a dialog window will open allowing you to choose a file from your computer. 
If you have not yet downloaded a file, you can click the \"Browse\" button to open your browser to search for a source file. 
The program will remove punctuation, related characters, and words that are 4 characters \nor less.

AUTHOR: Shadow76 // Chami Lamelas
