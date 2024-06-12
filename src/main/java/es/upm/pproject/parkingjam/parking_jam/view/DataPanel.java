package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Component;
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

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // Dibujar la sombra
                g2d.setColor(Color.GRAY);
                g2d.drawString(getText(), getInsets().left + 2, getInsets().top + getFontMetrics(getFont()).getAscent() + 2);
                // Dibujar el texto principal
                g2d.setColor(getForeground());
                g2d.drawString(getText(), getInsets().left, getInsets().top + getFontMetrics(getFont()).getAscent());
                g2d.dispose();
            }
        };
        label.setForeground(Color.BLACK); // Set text color to white
        label.setFont(new Font("Impact", Font.PLAIN, 20));
        return label;
    }

    public void updateData(int level, int gamePoints, int levelPoints) {
        this.level = level;
        this.totalPoints = gamePoints;
        this.levelPoints = levelPoints;
        levelNumber.setText(String.format(LEVEL_NUMBER_TEXT, level));
        gameScore.setText(String.format(GAME_SCORE_TEXT, gamePoints));
        levelScore.setText(String.format(LEVEL_SCORE_TEXT, levelPoints));
    }

    public void addPoint() {
        levelPoints = controller.getLevelScore();
        this.updateData(level, ++totalPoints, levelPoints);
    }

    public DataPanel deepCopy() {
        DataPanel copy = new DataPanel(this.controller);

        // Copiar las propiedades primitivas
        copy.level = this.level;
        copy.totalPoints = this.totalPoints;
        copy.levelPoints = this.levelPoints;

        copy.levelNumber.setText(this.levelNumber.getText());
        copy.gameScore.setText(this.gameScore.getText());
        copy.levelScore.setText(this.levelScore.getText());

        return copy;
    }

    public void loadPunctuation() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/punctuation.txt"))) {
            totalPoints = Integer.parseInt(reader.readLine());
            levelPoints = Integer.parseInt(reader.readLine());

            controller.setPunctuation(levelPoints);
            controller.setGameScore(totalPoints-levelPoints);

            updateData(controller.getLevelNumber(), totalPoints, levelPoints);

            this.revalidate();
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
