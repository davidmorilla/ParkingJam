package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;

public class DataPanel extends JPanel {
	private JLabel levelNumber;
    private JLabel gameScore;
    private JLabel levelScore;
    private static String LEVEL_NUMBER_TEXT = "NIVEL %d";
    private static String GAME_SCORE_TEXT = "Puntuación del juego: %d";
    private static String LEVEL_SCORE_TEXT = "Puntuación del nivel: %d";
    private int level;
    private int totalPoints;
    private int levelPoints;
    private Controller controller;

    public DataPanel(Controller controller) {
        BoxLayout boxY = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxY);
        level = 1;
        totalPoints =0;
        levelPoints = controller.getGameScore();
        this.controller = controller;
        levelNumber = new JLabel(String.format(LEVEL_NUMBER_TEXT, level));
        gameScore = new JLabel(String.format(GAME_SCORE_TEXT, totalPoints));
        levelScore = new JLabel(String.format(LEVEL_SCORE_TEXT, levelPoints));
        
        add(levelNumber, Component.CENTER_ALIGNMENT);
        add(gameScore, Component.CENTER_ALIGNMENT);
        add(levelScore, Component.CENTER_ALIGNMENT);
    }

    public void updateData(int level, int gamePoints, int levelPoints) {
        System.out.println("LEVEL: " + level + " GAME SCORE: " + gamePoints + "    LEVEL SCORE: " +levelPoints);
        this.level = level;
        this.totalPoints = gamePoints;
        this.levelPoints = levelPoints;
    	levelNumber.setText(String.format(LEVEL_NUMBER_TEXT, level));
    	gameScore.setText(String.format(GAME_SCORE_TEXT, gamePoints));
    	levelScore.setText(String.format(LEVEL_SCORE_TEXT, levelPoints));
    }

    public void addPoint(){
        levelPoints = controller.getLevelScore();
        //totalPoints = levelPoints;
        this.updateData(level, ++totalPoints, levelPoints);
    }

    public DataPanel deepCopy() {
        DataPanel copy = new DataPanel(this.controller);

        // Copiar las propiedades primitivas
        copy.level = this.level;
        copy.totalPoints = this.totalPoints;
        copy.levelPoints = this.levelPoints;

        // Copiar los textos de los JLabels
        copy.levelNumber.setText(this.levelNumber.getText());
        copy.gameScore.setText(this.gameScore.getText());
        copy.levelScore.setText(this.levelScore.getText());

        return copy;
    }

    
}