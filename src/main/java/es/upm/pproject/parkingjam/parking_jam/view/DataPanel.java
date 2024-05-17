package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

    public DataPanel() {
        BoxLayout boxY = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxY);
        level = 1;
        totalPoints =0;
        levelPoints = 0;
        
        levelNumber = new JLabel(String.format(LEVEL_NUMBER_TEXT, level));
        gameScore = new JLabel(String.format(GAME_SCORE_TEXT, totalPoints));
        levelScore = new JLabel(String.format(LEVEL_SCORE_TEXT, levelPoints));
        
        add(levelNumber, Component.CENTER_ALIGNMENT);
        add(gameScore, Component.CENTER_ALIGNMENT);
        add(levelScore, Component.CENTER_ALIGNMENT);
    }

    public void updateData(int level, int gamePoints, int levelPoints) {
    	levelNumber.setText(String.format(LEVEL_NUMBER_TEXT, level));
    	gameScore.setText(String.format(GAME_SCORE_TEXT, gamePoints));
    	levelScore.setText(String.format(LEVEL_SCORE_TEXT, levelPoints));
    }

    public void addPoint(){
        this.updateData(level, ++totalPoints, ++levelPoints);
    }

    
}