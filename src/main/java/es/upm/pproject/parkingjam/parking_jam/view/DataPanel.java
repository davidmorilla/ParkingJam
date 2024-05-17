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

    public DataPanel() {
        BoxLayout boxY = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxY);
        
        levelNumber = new JLabel(String.format(LEVEL_NUMBER_TEXT, 1));
        gameScore = new JLabel(String.format(GAME_SCORE_TEXT, 0));
        levelScore = new JLabel(String.format(LEVEL_SCORE_TEXT, 0));
        
        add(levelNumber, Component.CENTER_ALIGNMENT);
        add(gameScore, Component.CENTER_ALIGNMENT);
        add(levelScore, Component.CENTER_ALIGNMENT);
    }

    public void updateData(int level, int gamePoints, int levelPoints) {
    	levelNumber.setText(String.format(LEVEL_NUMBER_TEXT, level));
    	gameScore.setText(String.format(GAME_SCORE_TEXT, gamePoints));
    	levelScore.setText(String.format(LEVEL_SCORE_TEXT, levelPoints));
    }
}