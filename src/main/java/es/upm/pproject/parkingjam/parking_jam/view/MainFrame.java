package es.upm.pproject.parkingjam.parking_jam.view;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.OldBoardData;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class MainFrame extends JFrame {
    private Controller controller;
    private ImageIcon icon;
    private JPanel mainPanel;
    private DataPanel dataPanel;
    private Grid gridPanel;
    private JButton actionButton;
    private JButton resetButton;
    private MusicPlayer musicPlayer;

    public MainFrame(Controller controller) {
        super("Parking Jam - Programming Project");
        this.controller = controller;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setBounds(0, 0, 600, 600);

        try {
            icon = new ImageIcon(new URL("https://store-images.s-microsoft.com/image/apps.32576.13852498558802281.02407fd2-7c4b-4af9-a1f0-3b18d41974a0.70dbf666-990b-481d-b089-01bbce54de27?mode=scale&q=90&h=200&w=200&background=%23FFFFFF"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.setIconImage(icon.getImage());
        mainPanel = new JPanel(new BorderLayout());

        Pair<Integer, Integer> dimensions = controller.getBoardDimensions();
        dataPanel = new DataPanel(controller);
        mainPanel.add(dataPanel, BorderLayout.LINE_END);

        gridPanel = new Grid(dimensions, controller.getCars(), controller.getBoard(), controller, dataPanel);
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // Crear un JPanel para el botón
        JPanel buttonPanel = new JPanel();
        actionButton = new JButton("UNDO");
        buttonPanel.add(actionButton);

        resetButton = new JButton("RESET LEVEL");
        buttonPanel.add(resetButton);

        // Añadir el JPanel del botón al mainPanel en la parte inferior
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Añadir ActionListener al botón UNDO
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    OldBoardData old = controller.undoMovement();
                    char[][] oldBoard = old.getBoard();
                    gridPanel.setCars(old.getCars());
                    gridPanel.setBoard(oldBoard);
                    gridPanel.repaint();
                } catch (CannotUndoMovementException e) {
                    e.printStackTrace();
                }
                updateDataPanel();
            }
        });

        // Añadir ActionListener al botón RESET LEVEL
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                controller.resetLevel();
                gridPanel.setCars(controller.getCars());
                gridPanel.setBoard(controller.getBoard());
                gridPanel.repaint();
                updateDataPanel();
            }
        });

        add(mainPanel);

        // Iniciar la música
        String musicUrl = "ambience.wav"; // Reemplaza con tu URL de sonido
        musicPlayer = new MusicPlayer(musicUrl);
        musicPlayer.play();

        this.setVisible(true);
    }

    public Grid getGrid() {
        return this.gridPanel;
    }

    public void setGrid(Grid grid) {
        mainPanel.add(gridPanel, BorderLayout.CENTER);
    }

    public void updateDataPanel() {
        dataPanel.updateData(controller.getLevelNumber(), controller.getGameScore(), controller.getLevelScore());
    }
}
