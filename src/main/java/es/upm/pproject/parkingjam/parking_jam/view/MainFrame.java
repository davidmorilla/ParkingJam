package es.upm.pproject.parkingjam.parking_jam.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.utilities.OldBoardData;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.utils.BackgroundPanel;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame extends JFrame {
    private Controller controller;
    private ImageIcon icon;
    private JPanel mainPanel;
    private DataPanel dataPanel;
    private Grid gridPanel;
    private JButton startButton;
    private JButton actionButton;
    private JButton resetButton;
    private JButton nextButton;
    private JButton loadGameButton;
    private JButton selectLevel;
    private JButton back2Menu;
    private MusicPlayer musicPlayer;
    private JLabel titleLabel;
    private BufferedImage backgroundImage;
    private boolean levelSavedLoaded = false;

    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

    public MainFrame(Controller controller) {
        super("Parking Jam - Programming Project");
        this.controller = controller;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setBounds(0, 0, 600, 600);

        String musicUrl = "ambience.wav";
        musicPlayer = new MusicPlayer(musicUrl);
        musicPlayer.play();

        try {
            icon = new ImageIcon(new URL(
                    "https://store-images.s-microsoft.com/image/apps.32576.13852498558802281.02407fd2-7c4b-4af9-a1f0-3b18d41974a0.70dbf666-990b-481d-b089-01bbce54de27?mode=scale&q=90&h=200&w=200&background=%23FFFFFF"));
        } catch (MalformedURLException e) {
            logger.error("Could not load game icon");
            e.printStackTrace();
        }
        this.setIconImage(icon.getImage());

        try {
            URL imageUrl = new URL(
                    "https://img.freepik.com/premium-vector/top-view-city-from-streets-roads-houses-cars_70347-4869.jpg");
            backgroundImage = ImageIO.read(imageUrl);
        } catch (IOException e) {
            logger.error("Could not load background image");
            e.printStackTrace();
        }

        mainPanel = new BackgroundPanel(backgroundImage);
        mainPanel.setLayout(new GridBagLayout());

        titleLabel = createTitleLabel("PARKING JAM");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        startButton = new JButton("NEW GAME");
        buttonPanel.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainPanel.remove(startButton);
                mainPanel.remove(buttonPanel);
                mainPanel.remove(selectLevel);
                buttonPanel.remove(selectLevel);
                mainPanel.remove(loadGameButton);
                mainPanel.remove(titleLabel);
                mainPanel.revalidate();
                mainPanel.repaint();
                startGame();
                musicPlayer.playLevelStart();
            }
        });

        loadGameButton = new JButton("LOAD LAST GAME");
        buttonPanel.add(loadGameButton);

        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainPanel.remove(startButton);
                mainPanel.remove(selectLevel);
                buttonPanel.remove(selectLevel);
                buttonPanel.remove(startButton);
                buttonPanel.remove(loadGameButton);
                mainPanel.remove(titleLabel);
                mainPanel.revalidate();
                mainPanel.repaint();
                try {
                    levelSavedLoaded = true;
                    controller.loadSavedLevel();

                    dataPanel = new DataPanel(controller);
                    dataPanel.loadPunctuation();
                    startGame();
                } catch (IllegalExitsNumberException | IllegalCarDimensionException e) {
                    e.printStackTrace();
                }
            }
        });

        selectLevel = new JButton("SELECT LEVEL");
        buttonPanel.add(selectLevel);

        selectLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainPanel.remove(startButton);
                mainPanel.remove(selectLevel);
                buttonPanel.remove(selectLevel);
                buttonPanel.remove(startButton);
                buttonPanel.remove(loadGameButton);
                mainPanel.remove(titleLabel);
                mainPanel.revalidate();
                mainPanel.repaint();

                // Mostrar tres nuevos botones
                showLevelButtons();
            }
        });

        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem saveMenuItem = new JMenuItem("Save game");

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.saveGame();
                    JOptionPane.showMessageDialog(MainFrame.this, "Partida guardada con éxito.");
                } catch (Exception ex) {
                    logger.error("Error al guardar la partida", ex);
                    JOptionPane.showMessageDialog(MainFrame.this, "Error al guardar la partida.");
                }
            }
        });

        menu.add(saveMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        add(mainPanel);
        this.setVisible(true);
    }

    private void startGame() {
        GridBagConstraints gbc = new GridBagConstraints();

        Pair<Integer, Integer> dimensions = controller.getBoardDimensions();
        if (dataPanel == null)
            dataPanel = new DataPanel(controller);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(dataPanel, gbc);

        gridPanel = new Grid(dimensions, controller.getCars(), controller.getBoard(), controller, this);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(gridPanel, gbc);

        JPanel buttonPanel = new JPanel();
        actionButton = new JButton("UNDO");
        buttonPanel.add(actionButton);

        resetButton = new JButton("RESET LEVEL");
        buttonPanel.add(resetButton);

        nextButton = new JButton("NEXT LEVEL");
        buttonPanel.add(nextButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonPanel, gbc);

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    OldBoardData old = controller.undoMovement();
                    char[][] oldBoard = old.getBoard();
                    gridPanel.setCars(old.getCars());
                    gridPanel.setBoard(oldBoard);
                    gridPanel.repaint();
                    musicPlayer.playErase();
                    logger.info("Movement undone.");
                } catch (CannotUndoMovementException e) {
                    e.printStackTrace();
                    logger.error("Cannot undo movement, there is none done previously.");
                }
                updateDataPanel();
            }
        });

        // Añadir ActionListener al botón RESET LEVEL
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!levelSavedLoaded) {
                    System.out.println("Level not saved restarted");
                    controller.resetLevel();
                    gridPanel.setCars(controller.getCars());
                    gridPanel.setBoard(controller.getBoard());
                    gridPanel.repaint();
                    updateDataPanel();
                    logger.info("Level reset.");
                    musicPlayer.playLevelStart();
                } else {
                    controller.resetOriginalLevel();
                    gridPanel.setCars(controller.getCars());
                    gridPanel.setBoard(controller.getBoard());
                    gridPanel.repaint();
                    updateDataPanel();
                    logger.info("Level reset.");
                    musicPlayer.playLevelStart();
                }

            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    levelSavedLoaded = false;
                    if (gridPanel.isLevelCompleted()) {

                        controller.loadNewLevel();
                        updateDataPanel();
                        gridPanel.setCarsMap(controller.getCars());
                        gridPanel.setCars(controller.getCars());
                        gridPanel.setBoard(controller.getBoard());
                        gridPanel.repaint();

                        logger.info("Game advanced to next level.");
                        musicPlayer.playLevelSuccess();
                    }
                } catch (Exception e) {
                    logger.error("Could not load next level.");
                    e.printStackTrace();
                }
            }
        });
    }

    public Grid getGrid() {
        return this.gridPanel;
    }

    public void setGrid(Grid grid) {
        mainPanel.add(gridPanel, BorderLayout.CENTER);
    }

    public void updateDataPanel() {
        System.out.println(controller.getGameScore());
        dataPanel.updateData(controller.getLevelNumber(), controller.getGameScore(), controller.getLevelScore());
        logger.info("Panel data updated.");
    }

    public void increaseScore() {
        dataPanel.addPoint();
        logger.info("Score increased.");
    }

    // Método para crear el JLabel del título con sombra
    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // Dibujar la sombra
                g2d.setColor(Color.GRAY);
                g2d.drawString(getText(), getInsets().left + 3,
                        getInsets().top + getFontMetrics(getFont()).getAscent() + 3);
                // Dibujar el texto principal
                g2d.setColor(getForeground());
                g2d.drawString(getText(), getInsets().left, getInsets().top + getFontMetrics(getFont()).getAscent());
                g2d.dispose();
            }
        };
        label.setFont(new Font("Impact", Font.PLAIN, 50));
        label.setForeground(Color.BLACK);
        label.setOpaque(false);
        return label;
    }

    private void startOnlyOneLevelGame() {
        GridBagConstraints gbc = new GridBagConstraints();

        Pair<Integer, Integer> dimensions = controller.getBoardDimensions();
        if (dataPanel == null)
            dataPanel = new DataPanel(controller);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(dataPanel, gbc);

        gridPanel = new Grid(dimensions, controller.getCars(), controller.getBoard(), controller, this);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(gridPanel, gbc);

        JPanel buttonPanel = new JPanel();
        actionButton = new JButton("UNDO");
        buttonPanel.add(actionButton);

        resetButton = new JButton("RESET LEVEL");
        buttonPanel.add(resetButton);

        back2Menu = new JButton("BACK TO MENU");
        buttonPanel.add(back2Menu);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(buttonPanel, gbc);

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    OldBoardData old = controller.undoMovement();
                    char[][] oldBoard = old.getBoard();
                    gridPanel.setCars(old.getCars());
                    gridPanel.setBoard(oldBoard);
                    gridPanel.repaint();
                    musicPlayer.playErase();
                    logger.info("Movement undone.");
                } catch (CannotUndoMovementException e) {
                    e.printStackTrace();
                    logger.error("Cannot undo movement, there is none done previously.");
                }
                updateDataPanel();
            }
        });

        // Añadir ActionListener al botón RESET LEVEL
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!levelSavedLoaded) {
                    System.out.println("Level not saved restarted");
                    controller.resetLevel();
                    gridPanel.setCars(controller.getCars());
                    gridPanel.setBoard(controller.getBoard());
                    gridPanel.repaint();
                    updateDataPanel();
                    logger.info("Level reset.");
                    musicPlayer.playLevelStart();
                } else {
                    controller.resetOriginalLevel();
                    gridPanel.setCars(controller.getCars());
                    gridPanel.setBoard(controller.getBoard());
                    gridPanel.repaint();
                    updateDataPanel();
                    logger.info("Level reset.");
                    musicPlayer.playLevelStart();
                }

            }
        });

        back2Menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                showLevelButtons();
            }
        });
    }

    // Método para mostrar los tres botones de selección de nivel
    private void showLevelButtons() {
        JPanel levelPanel = new JPanel(new GridLayout(2, 5, 10, 10)); // Panel con tres botones en una fila
        levelPanel.setOpaque(false);

        JButton level1Button = new JButton("Level 1");
        JButton level2Button = new JButton("Level 2");
        JButton level3Button = new JButton("Level 3");
        JButton backButton = new JButton("GO TO MAIN MENU");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();

                showMainButtons();

            }
        });

        level1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                controller.loadLevel(1);
                startOnlyOneLevelGame();
            }
        });

        level2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                controller.loadLevel(2);
                startOnlyOneLevelGame();
            }
        });

        level3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                controller.loadLevel(3);

                startOnlyOneLevelGame();
            }
        });

        levelPanel.add(backButton);
        levelPanel.add(level1Button);
        levelPanel.add(level2Button);
        levelPanel.add(level3Button);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(levelPanel, gbc);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showMainButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
        buttonPanel.setOpaque(false);
        
        titleLabel = createTitleLabel("PARKING JAM");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        JButton newGameButton = new JButton("NEW GAME");
        buttonPanel.add(newGameButton);
    
        JButton loadLastGameButton = new JButton("LOAD LAST GAME");
        buttonPanel.add(loadLastGameButton);
    
        JButton selectLevelButton = new JButton("SELECT LEVEL");
        buttonPanel.add(selectLevelButton);
    
        // Agregar ActionListeners a los botones principales
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                startGame();
                musicPlayer.playLevelStart();
            }
        });
    
        loadLastGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                try {
                    levelSavedLoaded = true;
                    controller.loadSavedLevel();
    
                    dataPanel = new DataPanel(controller);
                    dataPanel.loadPunctuation();
                    startGame();
                } catch (IllegalExitsNumberException | IllegalCarDimensionException e) {
                    e.printStackTrace();
                }
            }
        });
    
        selectLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
                showLevelButtons();
            }
        });
    
        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
