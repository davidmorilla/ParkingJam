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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.controller.ControllerInterface;

/**
 * The DataPanel class is a custom JPanel that displays game information
 * such as level number, game score, and level score
 */
public class DataPanel extends JPanel {
    private JLabel levelNumber;
    private JLabel gameScore;
    private JLabel levelScore;
    private static final String LEVEL_NUMBER_TEXT = "LEVEL %d";
    private static final String GAME_SCORE_TEXT = "Game Score: %d";
    private static final String LEVEL_SCORE_TEXT = "Level Score: %d";
    private int level;
    private int totalPoints;
    private int levelPoints;
    private transient ControllerInterface controller;
    private static final Logger logger = LoggerFactory.getLogger(DataPanel.class);
    /**
     * Constructs a DataPanel with the specified game controller
     * @param controller the controller managing the game logic
     */
    public DataPanel(ControllerInterface controller) {
        logger.info("Creating DataPanel...");
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
        logger.info("DataPanel created.");
    }

    /**
     * Creates a JLabel with custom rendering for shadow effect
     * @param text the text to be displayed in the label
     * @return a customized JLabel
     */
    private JLabel createLabel(String text) {
    	logger.info("Creating label...");
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
        label.setFont(new Font("Arial Black", Font.PLAIN, 20));
        logger.info("Created label with text: '{}'", text);
        return label;
    }

    /**
     * Updates the data displayed in the panel
     * @param level the current level number
     * @param gamePoints the total game points
     * @param levelPoints the points for the current level
     */
    public void updateData(int level, int gamePoints, int levelPoints) {
    	logger.info("Updating panel data...");
    	this.level = level;
        this.totalPoints = gamePoints;
        this.levelPoints = levelPoints;
        levelNumber.setText(String.format(LEVEL_NUMBER_TEXT, level));
        gameScore.setText(String.format(GAME_SCORE_TEXT, gamePoints));
        levelScore.setText(String.format(LEVEL_SCORE_TEXT, levelPoints));
        logger.info("Updated data: level={}, gamePoints={}, levelPoints={}", level, gamePoints, levelPoints);
    }

    /**
     * Adds a point to the total game score and updates the displayed data
     */
    public void addPoint() {
    	logger.info("Adding point...");
        levelPoints = controller.getLevelScore();
        this.updateData(level, ++totalPoints, levelPoints);
        logger.info("Added a point. New total points: {}", totalPoints);
    }

    /**
     * Creates a deep copy of the DataPanel
     * 
     * @return a deep copy of the DataPanel
     */
    public DataPanel deepCopy() {
        logger.info("Creating a deep copy...");
    	DataPanel copy = new DataPanel(this.controller);

        // Copy primitive properties
        copy.level = this.level;
        copy.totalPoints = this.totalPoints;
        copy.levelPoints = this.levelPoints;

        copy.levelNumber.setText(this.levelNumber.getText());
        copy.gameScore.setText(this.gameScore.getText());
        copy.levelScore.setText(this.levelScore.getText());
        logger.info("Created a deep copy of DataPanel: level={}, totalPoints={}, levelPoints={}", copy.level, copy.totalPoints, copy.levelPoints);
        return copy;
    }

    /**
     * Loads the score from a saved file and updates the panel.
     */
    public void loadPunctuation() {
    	logger.info("Loading score from file...");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/savedGame/punctuation.txt"))) {
            totalPoints = Integer.parseInt(reader.readLine());
            levelPoints = Integer.parseInt(reader.readLine());

            controller.setPunctuation(levelPoints);
            controller.setGameScore(totalPoints - levelPoints);

            updateData(controller.getLevelNumber(), totalPoints, levelPoints);

            this.revalidate();
            this.repaint();
            
            logger.info("Score loaded from file: totalPoints={}, levelPoints={}", totalPoints, levelPoints);
        } catch (IOException e) {
        	logger.error("There was an error loading the score from  the file: {}.", e.getLocalizedMessage());
        }
    }

    /**
     * Gets the total game score.
     * @return the total game score
     */
    public int getGameScore() {
    	logger.info("Getting game score...");
    	logger.info("Game scored obtained: {}", this.totalPoints);
        return this.totalPoints;
    }

    /**
     * Changes the level name to a new one
     * 
     * @param levelNumber the number, which is the name of the level
     */
    public void changeLevelName(int levelNumber){
    	logger.info("Changing level number of level: [Level {}]...", level);
        this.level = levelNumber;
        this.revalidate();
        this.repaint();
        logger.info("Changed level number to: {}", levelNumber);
    }
}
