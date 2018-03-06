package hangman;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Class used to draw the hangman and the nuce
 *
 * @author Chami Lamelas
 */
public class HangmanDrawing extends JComponent {

    /**
     * The Hangman game for which this class will be used on.
     */
    private HangmanGame game;

    /**
     * Constructor to instantiate HangmanDrawing objects
     * @param g The hangman game for which this drawing class will be used on
     */
    public HangmanDrawing(HangmanGame g) {
        game = g;
    }

    @Override
    /**
     * Draws the man.
     *
     * @param g - the graphics component to protect.
     */
    public void paintComponent(Graphics g) {
        // building gallows
        g.drawLine(250, 10, 250, 520); // main part of gallows
        g.drawLine(170, 520, 330, 520); // horizontal part of base
        g.drawLine(170, 520, 170, 530); // left (to viewer) part of base
        g.drawLine(330, 520, 330, 530); // right (to viewer) part of base
        g.drawLine(250, 10, 340, 10); // horizontal part of connection to body
        g.drawLine(340, 10, 340, 60); // vertical part of connection to body

        int n = game.getNumIncorrect(); // used to build body parts

        // building body parts
        if (n >= 1) // head
            g.drawOval(275, 60, 130, 130);
        if (n >= 2) // body
            g.drawLine(275 + 65, 60 + 130, 275 + 65, 60 + 300);
        if (n >= 3) // (his) left arm
            g.drawLine(275 + 65, 240, 275 + 65 + 70, 340);
        if (n >= 4) // (his) right arm
            g.drawLine(275 + 65, 240, 275 + 65 - 70, 340);
        if (n >= 5) // (his) left leg
            g.drawLine(275 + 65, 60 + 300, 275 + 65 + 80, 500);
        if (n >= 6) // (his) right leg
            g.drawLine(275 + 65, 60 + 300, 275 + 65 - 80, 500);
        if (n >= 7) { // (his) left eye
            g.drawOval(265 + 30, 100, 30, 10);
            g.drawOval(265 + 40, 100, 10, 10);
        }
        if (n >= 8) { // (his) right eye
            g.drawOval(355, 100, 30, 10);
            g.drawOval(365, 100, 10, 10);
        }
        if (n >= 9) { // nose
            g.drawLine(275 + 65, 110, 275 + 55, 140);
            g.drawLine(275 + 65, 110, 275 + 75, 140);
            g.drawLine(275 + 55, 140, 275 + 74, 140);
        }
        if (n >= 10) // mouth
            g.drawArc(275 + 35, 150, 60, 15, 180, 180);
        if (n >= 11) // (his) left ear
            g.drawArc(272, 100, 35, 25, 120, 90);
        if (n >= 12) // (his) right ear
            g.drawArc(275 + 100, 103, 35, 25, 320, 95);
        if (n >= 13) // (his) left eyebrow
            g.drawArc(265 + 25, 95, 35, 5, 0, 150);
        if (n >= 14) // (his) right eyebrow
            g.drawArc(350, 95, 35, 5, 0, 150);
        if (n >= 15) // (his) left hand finger #1 - top
            g.drawLine(275 + 65 + 70, 340, 275 + 65 + 80, 343);
        if (n >= 16) // (his) left hand finger #2 - center
            g.drawLine(275 + 65 + 70, 340, 275 + 65 + 80, 353);
        if (n >= 17) // (his) left hand finger #3 - bottom
            g.drawLine(275 + 65 + 70, 340, 275 + 65 + 80, 363);
        if (n >= 18) // (his) right hand finger #1 - top
            g.drawLine(275 + 65 - 70, 340, 275 + 65 - 80, 343);
        if (n >= 19) // (his) right hand finger #2 - center
            g.drawLine(275 + 65 - 70, 340, 275 + 65 - 80, 353);
        if (n >= 20) // (his) right hand finger #3 - bottom
            g.drawLine(275 + 65 - 70, 340, 275 + 65 - 80, 363);
    }

}
