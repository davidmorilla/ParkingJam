package es.upm.pproject.parkingjam.parking_jam.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.utils.BackgroundPanel;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The MainFrame class is the main window of the Parking Jam game application. 
 * It handles the initialization of the game, display of the main menu, and transitions between different game states
 */
public class MainFrame extends JFrame {
	private Controller controller;
	private ImageIcon icon;
	private MusicPlayer musicPlayer;
	private JPanel mainPanel;
	private DataPanel dataPanel;
	private Grid gridPanel;
	
	private boolean levelSavedLoaded = false;

	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

	/**
	 * Constructs a new MainFrame with the specified game controller.
	 * @param controller the controller managing the game logic
	 */
	public MainFrame(final Controller controller) {
		super("Parking Jam - Programming Project");
		this.controller = controller;
		mainMenu();
	}

	/**
	 * Initializes and displays the main menu of the game
	 */
	private void mainMenu() {
		// Set frame configuration
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setBounds(0, 0, 600, 600);

		// Play music
		String musicUrl = "ambience.wav";
		musicPlayer = new MusicPlayer(musicUrl);
		musicPlayer.play();

		// Set icon and background images
		icon = new ImageIcon(getClass().getClassLoader().getResource("logo.png"));

		this.setIconImage(icon.getImage());

		BufferedImage backgroundImage = null;
		try {
			URL imageUrl = getClass().getClassLoader().getResource("background.jpg");
			backgroundImage = ImageIO.read(imageUrl);
		} catch (IOException e) {
			logger.error("Could not load background image");
			e.printStackTrace();
		}

		mainPanel = new BackgroundPanel(backgroundImage);
		mainPanel.setLayout(new GridBagLayout());

		// Add the title and start, load game and select level buttons of the main menu to the mainPanel
		addMainMenuTitleAndButtons();

		// Display as visible all the components on main menu screen
		add(mainPanel);
		this.setVisible(true);
	}

	/**
	 * Starts the game by initializing the game components and adding them to the main panel
	 */
	private void startGame() {
		GridBagConstraints gbc = new GridBagConstraints();

		// Add level and scores panel
		Pair<Integer, Integer> dimensions = controller.getBoardDimensions();
		if (dataPanel == null)
			dataPanel = new DataPanel(controller);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(dataPanel, gbc);

		// Add the board
		gridPanel = new Grid(dimensions, controller.getCars(), controller.getBoard(), controller, this);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(gridPanel, gbc);

		// Create a button panel and add the undo, reset level and next level buttons
		JPanel buttonPanel = new JPanel();
		
		JButton actionButton = new JButton("UNDO");
		buttonPanel.add(actionButton);

		JButton resetButton = new JButton("RESET LEVEL");
		buttonPanel.add(resetButton);

		JButton nextButton = new JButton("NEXT LEVEL");
		buttonPanel.add(nextButton);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.SOUTH;
		mainPanel.add(buttonPanel, gbc);

		// Undo button functionality
		actionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					char[][] oldBoard = controller.undoMovement();
					gridPanel.setCars(controller.getCars());
					gridPanel.setBoard(oldBoard);
					gridPanel.repaint();
					musicPlayer.playErase();
					logger.info("Movement undone.");
				} catch (CannotUndoMovementException e) {
					e.printStackTrace();
					logger.error("Cannot undo movement, there is none done previously.");
				} catch (SameMovementException e) {
					e.printStackTrace();
				}
				updateDataPanel();
			}
		});

		// Reset level button functionality
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!levelSavedLoaded) {
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

		// Next level button functionality
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
					// Show all levels passed screen
					logger.info("All levels finished.");
					mainPanel.removeAll();
					mainPanel.revalidate();
					mainPanel.repaint();

					// Add all congratulations messages and the total score
					JLabel congratsLabel = new JLabel("Congratulations!", SwingConstants.CENTER);
					congratsLabel.setFont(new Font("Impact", Font.PLAIN, 50));
					congratsLabel.setForeground(Color.BLACK);

					JLabel congratsLabel2 = new JLabel("All levels passed!", SwingConstants.CENTER);
					congratsLabel2.setFont(new Font("Impact", Font.PLAIN, 40));
					congratsLabel2.setForeground(Color.BLACK);

					JLabel totalScore = new JLabel("TOTAL SCORE: " + dataPanel.getGameScore(), SwingConstants.CENTER);
					totalScore.setFont(new Font("Impact", Font.PLAIN, 40));
					totalScore.setForeground(Color.BLACK);

					mainPanel.setLayout(new GridBagLayout());
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.gridx = 0;
					gbc.gridy = 0;
					gbc.anchor = GridBagConstraints.CENTER;

					mainPanel.add(congratsLabel, gbc);
					gbc.gridy = 1;

					mainPanel.add(congratsLabel2, gbc);

					gbc.gridy = 2;

					mainPanel.add(totalScore, gbc);
					mainPanel.revalidate();
					mainPanel.repaint();

					logger.info("Congratulations message displayed.");
					musicPlayer.playLevelSuccess();
				}
			}
		});

		// Add bar with a button to save the game
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
		JMenuItem saveMenuItem = new JMenuItem("Save game");

		// Save game button functionality
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
	}

	/**
	 * Gets the board grid
	 * @return the board grid
	 */
	public Grid getGrid() {
		return this.gridPanel;
	}

	/**
	 * Sets the board grid to a new one
	 * @param grid the new grid
	 */
	public void setGrid(Grid grid) {
		mainPanel.add(gridPanel, BorderLayout.CENTER);
	}

	/**
	 * Updates de data panel with the new level and scores
	 */
	public void updateDataPanel() {
		dataPanel.updateData(controller.getLevelNumber(), controller.getGameScore(), controller.getLevelScore());
		logger.info("Panel data updated.");
	}

	/**
	 * Updates the data panel adding 1 to level and total scores
	 */
	public void increaseScore() {
		dataPanel.addPoint();
		logger.info("Score increased.");
	}

	/**
	 * Starts an only level game by initializing the game components and adding them to the main panel
	 */
	private void startOnlyOneLevelGame() {
		GridBagConstraints gbc = new GridBagConstraints();

		// Add level and scores panel
		Pair<Integer, Integer> dimensions = controller.getBoardDimensions();
		if (dataPanel == null)
			dataPanel = new DataPanel(controller);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(dataPanel, gbc);

		// Add the board
		gridPanel = new Grid(dimensions, controller.getCars(), controller.getBoard(), controller, this);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(gridPanel, gbc);

		// Create a button panel and add the undo, reset level and back to menu buttons
		JPanel buttonPanel = new JPanel();

		JButton actionButton = new JButton("UNDO");
		buttonPanel.add(actionButton);

		JButton resetButton = new JButton("RESET LEVEL");
		buttonPanel.add(resetButton);

		JButton backToMenu = new JButton("BACK TO MENU");
		buttonPanel.add(backToMenu);

		// Undo button functionality
		actionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					char[][] oldBoard = controller.undoMovement();
					gridPanel.setCars(controller.getCars());
					gridPanel.setBoard(oldBoard);
					gridPanel.repaint();
					musicPlayer.playErase();
					logger.info("Movement undone.");
				} catch (CannotUndoMovementException e) {
					e.printStackTrace();
					logger.error("Cannot undo movement, there is none done previously.");
				} catch (SameMovementException e) {
					e.printStackTrace();
				}
				updateDataPanel();
			}
		});

		// Reset level button functionality
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!levelSavedLoaded) {
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

		// Back to menu button functionality
		backToMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainPanel.removeAll();
				mainPanel.revalidate();
				mainPanel.repaint();
				showLevelButtons();
			}
		});
		
		// Add buttons to the main panel
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.SOUTH;
		mainPanel.add(buttonPanel, gbc);
	}

	/**
	 * Displays the level selection buttons for choosing a specific game level.
	 * Creates a panel with buttons for levels 1 to 5 and a button to return to the main menu
	 */
	private void showLevelButtons() {
		// Panel for the level buttons
		JPanel levelPanel = new JPanel(new GridLayout(2, 5, 10, 10));
		levelPanel.setOpaque(false);

		// Add go to menu button
		JButton backButton = new JButton("GO TO MAIN MENU");

		// Go to menu button functionality
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.revalidate();
				mainPanel.repaint();
				addMainMenuTitleAndButtons();
			}
		});

		// Button for each level in the game
		JButton level1Button = new JButton("Level 1");
		JButton level2Button = new JButton("Level 2");
		JButton level3Button = new JButton("Level 3");
		JButton level4Button = new JButton("Level 4");
		JButton level5Button = new JButton("Level 5");

		// Functionality for each level button
		level1Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.revalidate();
				mainPanel.repaint();
				controller.loadLevel(1);
				startOnlyOneLevelGame();
				musicPlayer.playLevelStart();
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
				musicPlayer.playLevelStart();
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
				musicPlayer.playLevelStart();
			}
		});

		level4Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.revalidate();
				mainPanel.repaint();
				controller.loadLevel(4);
				startOnlyOneLevelGame();
				musicPlayer.playLevelStart();
			}
		});

		level5Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.revalidate();
				mainPanel.repaint();
				controller.loadLevel(5);
				startOnlyOneLevelGame();
				musicPlayer.playLevelStart();
			}
		});

		// Add level buttons to level panel
		levelPanel.add(level1Button);
		levelPanel.add(level2Button);
		levelPanel.add(level3Button);
		levelPanel.add(level4Button);
		levelPanel.add(level5Button);

		// Add all components of this screen
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(levelPanel, gbc);
		gbc.gridy = 2;
		mainPanel.add(backButton, gbc);

		// Show level selection screen
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	/**
	 * Add the title and start, load game and select level buttons of the main menu to the mainPanel
	 */
	private void addMainMenuTitleAndButtons() {
		// Add title
		JLabel titleLabel = createTitleLabel("PARKING JAM");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(titleLabel, gbc);

		// Add start button
		final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		JButton startButton = new JButton("NEW GAME");
		buttonPanel.add(startButton);

		// Add load gamebutton
		JButton loadGameButton = new JButton("LOAD LAST GAME");
		buttonPanel.add(loadGameButton);

		// Add select level button
		JButton selectLevel = new JButton("SELECT LEVEL");
		buttonPanel.add(selectLevel);

		// Start button functionality
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

		// Load game button functionality
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
					musicPlayer.playLevelStart();
				} catch (IllegalExitsNumberException | IllegalCarDimensionException e) {
					e.printStackTrace();
				}
			}
		});

		// Select level button functionality
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

				// Show select level screen
				showLevelButtons();
			}
		});

		gbc.gridy = 1;
		mainPanel.add(buttonPanel, gbc);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	/**
	 * Creates a title label with the specified text with a shadow
	 * @param text the text for the title label
	 * @return the created title label
	 */
	private JLabel createTitleLabel(String text) {
		JLabel label = new JLabel(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g.create();
				// Draw the shadow
				g2d.setColor(Color.GRAY);
				g2d.drawString(getText(), getInsets().left + 3,
						getInsets().top + getFontMetrics(getFont()).getAscent() + 3);
				// Draw the text
				g2d.setColor(getForeground());
				g2d.drawString(getText(), getInsets().left, getInsets().top + getFontMetrics(getFont()).getAscent());
				g2d.dispose();
			}
		};

		// Set label configuration
		label.setFont(new Font("Impact", Font.PLAIN, 50));
		label.setForeground(Color.BLACK);
		label.setOpaque(false);
		return label;
	}
}
