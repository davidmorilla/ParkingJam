package es.upm.pproject.parkingjam.parking_jam.view;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.swing.*;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Controller controller;
    private ImageIcon icon;
    private JPanel mainPanel;
    private JPanel scorePanel;
    private Grid gridPanel;

    public MainFrame(Controller controller) {
        super("Parking Jam - Programming Project");
        // setLayout(null);

        this.controller = controller;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        try {
            icon = new ImageIcon(new URL(
                    "https://store-images.s-microsoft.com/image/apps.32576.13852498558802281.02407fd2-7c4b-4af9-a1f0-3b18d41974a0.70dbf666-990b-481d-b089-01bbce54de27?mode=scale&q=90&h=200&w=200&background=%23FFFFFF"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.setIconImage(icon.getImage());
        mainPanel = new JPanel(new BorderLayout());

        scorePanel = new JPanel();
        BoxLayout boxY = new BoxLayout(scorePanel, BoxLayout.Y_AXIS);
        scorePanel.setLayout(boxY);

        JLabel gameScoreLabel = new JLabel("Puntuación del juego: 0");
        JLabel levelScoreLabel = new JLabel("Puntuación del nivel: 0");
        scorePanel.add(gameScoreLabel, Component.CENTER_ALIGNMENT);
        scorePanel.add(levelScoreLabel, Component.CENTER_ALIGNMENT);

        Pair<Integer, Integer> dimensions = controller.getDimensions();
        System.out.println(dimensions.getLeft());
        System.out.println(dimensions.getRight());
        int rows = dimensions.getLeft();
        int cols = dimensions.getRight();
        Map<Character,Car> cars = this.controller.getCars();
        gridPanel = new Grid(rows, cols,cars, controller.getBoard(), controller);
        
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        

        mainPanel.add(scorePanel, BorderLayout.LINE_END);
        // mainPanel.add(gridPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        // -------------

        add(mainPanel);
        setBounds(0, 0, 1200, 800);
        // this.setExtendedState(JFrame.MAXIMIZED_BOTH); Pantalla maximizada
        this.setVisible(true);

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Grid getGrid() {
        return this.gridPanel;
    }

    public void setGrid(Grid grid) {
        mainPanel.add(gridPanel, BorderLayout.CENTER);
    }
    
}
