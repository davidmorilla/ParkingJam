package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;

/**
 * The DataPanel class is a custom JPanel that displays game information
 * such as level number, game score, and level score
 */
public class DataPanel extends JPanel {
    private JLabel levelNumber;
    private JLabel gameScore;
    private JLabel levelScore;
    private final static String LEVEL_NUMBER_TEXT = "LEVEL %d";
    private final static String GAME_SCORE_TEXT = "Game Score: %d";
    private final static String LEVEL_SCORE_TEXT = "Level Score: %d";
    private int level;
    private int totalPoints;
    private int levelPoints;
    private Controller controller;

    /**
     * Constructs a DataPanel with the specified game controller
     * @param controller the controller managing the game logic
     */
    public DataPanel(Controller controller) {
        this.setOpaque(false);
        BoxLayout boxY = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxY);
        level = controller.getLevelNumber();
        totalPoints = controller.getGameScore();
        levelPoints = controller.getLevelScore();
        this.controller = controller;

        levelNumber = createLabel(String.format(LEVEL_NUMBER_TEXT, level));
        gameScore = createLabel(String.format(GAME_SCORE_TEXT, totalPoints));
        levelScore = createLabel(String.format(LEVEL_SCORE_TEXT, levelPoints));

        add(levelNumber);
        add(gameScore);
        add(levelScore);
    }

    /**
     * Creates a JLabel with custom rendering for shadow effect
     * @param text the text to be displayed in the label
     * @return a customized JLabel
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // Draw shadow
                g2d.setColor(Color.GRAY);
                g2d.drawString(getText(), getInsets().left + 2, getInsets().top + getFontMetrics(getFont()).getAscent() + 2);
                // Draw main text
                g2d.setColor(getForeground());
                g2d.drawString(getText(), getInsets().left, getInsets().top + getFontMetrics(getFont()).getAscent());
                g2d.dispose();
            }
        };
        label.setForeground(Color.BLACK); // Set text color to black
        label.setFont(new Font("Impact", Font.PLAIN, 20));
        return label;
    }

    /**
     * Updates the data displayed in the panel
     * @param level the current level number
     * @param gamePoints the total game points
     * @param levelPoints the points for the current level
     */
    public void updateData(int level, int gamePoints, int levelPoints) {
        this.level = level;
        this.totalPoints = gamePoints;
        this.levelPoints = levelPoints;
        levelNumber.setText(String.format(LEVEL_NUMBER_TEXT, level));
        gameScore.setText(String.format(GAME_SCORE_TEXT, gamePoints));
        levelScore.setText(String.format(LEVEL_SCORE_TEXT, levelPoints));
    }

    /**
     * Adds a point to the total game score and updates the displayed data
     */
    public void addPoint() {
        levelPoints = controller.getLevelScore();
        this.updateData(level, ++totalPoints, levelPoints);
    }

    /**
     * Creates a deep copy of the DataPanel
     * @return a deep copy of the DataPanel
     */
    public DataPanel deepCopy() {
        DataPanel copy = new DataPanel(this.controller);

        // Copy primitive properties
        copy.level = this.level;
        copy.totalPoints = this.totalPoints;
        copy.levelPoints = this.levelPoints;

        copy.levelNumber.setText(this.levelNumber.getText());
        copy.gameScore.setText(this.gameScore.getText());
        copy.levelScore.setText(this.levelScore.getText());

        return copy;
    }

    /**
     * Loads the score from a saved file and updates the panel.
     */
    public void loadPunctuation() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/punctuation.txt"))) {
            totalPoints = Integer.parseInt(reader.readLine());
            levelPoints = Integer.parseInt(reader.readLine());

            controller.setPunctuation(levelPoints);
            controller.setGameScore(totalPoints - levelPoints);

            updateData(controller.getLevelNumber(), totalPoints, levelPoints);

            this.revalidate();
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the total game score.
     * @return the total game score
     */
    public int getGameScore() {
        return this.totalPoints;
    }
}
