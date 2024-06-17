package es.upm.pproject.parkingjam.parking_jam.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import es.upm.pproject.parkingjam.parking_jam.controller.ControllerInterface;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.utils.BackgroundPanel;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MainFrame class is the main window of the Parking Jam game application.
 * It handles the initialization of the game, display of the main menu, and
 * transitions between different game states
 */
public class MainFrame extends JFrame implements IMainFrame {
	private transient ControllerInterface controller;
	private transient MusicPlayer musicPlayer;
	private JPanel mainPanel;
	private DataPanel dataPanel;
	private transient IGrid gridPanel;

	private boolean levelSavedLoaded = false;
	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

	private static final String FONT = "impact";

	/**
	 * Constructs a new MainFrame with the specified game controller.
	 * 
	 * @param controller the controller managing the game logic
	 */
	public MainFrame(final ControllerInterface controller) {
		super("Parking Jam - Programming Project");
		this.controller = controller;
		logger.info("Initializing MainFrame...");
		mainMenu();
	}

	@Override
	/**
	 * Initializes and displays the main menu of the game
	 */
	public final void mainMenu() {
		logger.info("Initializing main menu...");
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
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("logo.png"));
		this.setIconImage(icon.getImage());

		BufferedImage backgroundImage = null;
		try {
			URL imageUrl = getClass().getClassLoader().getResource("background.jpg");
			backgroundImage = ImageIO.read(imageUrl);
		} catch (IOException e) {
			logger.error("Could not load background image: {}.", e.getLocalizedMessage());
		}

		mainPanel = new BackgroundPanel(backgroundImage);
		mainPanel.setLayout(new GridBagLayout());

		// Add the title and start, load game and select level buttons of the main menu
		// to the mainPanel
		addMainMenuTitleAndButtons();

		// Display as visible all the components on main menu screen
		add(mainPanel);
		this.setVisible(true);
		logger.info("Main menu initialized and displayed.");
	}

	@Override
	/**
	 * Starts the game by initializing the game components and adding them to the
	 * main panel
	 */
	public void startGame() {
		logger.info("Starting game...");
		initializeGameComponents(false); // false because it's not just one level
	}

	@Override
	/**
	 * Starts an only level game by initializing the game components and adding them
	 * to the main panel
	 */
	public void startOnlyOneLevelGame() {
		logger.info("Starting only one level game...");
		initializeGameComponents(true); // true because it's only one level
	}

	@Override
	/**
	 * Initialize the components depending on whether it's a full game or a single
	 * level game
	 */
	public void initializeGameComponents(boolean onlyOneLevel) {
		logger.info("Initializing game components (onlyOneLevel={})...", onlyOneLevel);
		GridBagConstraints gbc = new GridBagConstraints();
		Pair<Integer, Integer> dimensions = controller.getBoardDimensions();

		if (dataPanel == null) {
			dataPanel = new DataPanel(controller);
		}

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(dataPanel, gbc);

		gridPanel = new Grid(dimensions, controller.getCars(), controller.getBoard(), controller, dataPanel);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add((Grid) gridPanel, gbc);

		JPanel buttonPanel = new JPanel();
		JButton undoButton = new JButton("UNDO");
		buttonPanel.add(undoButton);

		JButton resetButton = new JButton("RESET LEVEL");
		buttonPanel.add(resetButton);

		// Different buttons based on whether it's only one level or not
		if (onlyOneLevel) {
			// Back to menu button
			JButton backToMenu = new JButton("BACK TO LEVEL MENU");
			buttonPanel.add(backToMenu);

			backToMenu.addActionListener(arg0 -> backToMenuAction());
		} else {
			// Next level button
			JButton nextButton = new JButton("NEXT LEVEL");
			buttonPanel.add(nextButton);

			nextButton.addActionListener(arg0 -> nextLevelAction());

			// Save game button functionality
			JButton saveGameButton = new JButton("SAVE GAME");
			buttonPanel.add(saveGameButton);

			saveGameButton.addActionListener(arg0 -> saveGameAction());
		}

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.SOUTH;
		mainPanel.add(buttonPanel, gbc);

		// Undo button functionality (common for both)
		undoButton.addActionListener(arg0 -> undoMovementAction());

		// Reset level button functionality (common for both)
		resetButton.addActionListener(arg0 -> resetLevelAction());

		// Update data panel after setting up components
		updateDataPanel();
		logger.info("Game components initialized.");
	}
	
	@Override
	/**
	 * Add the title and start, load game and select level buttons of the main menu
	 * to the mainPanel
	 */
	public void addMainMenuTitleAndButtons() {
		logger.info("Adding title and start, load game and select level buttons of the main menu...");
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
		startButton.addActionListener(arg0 -> newGameAction(startButton, selectLevel, buttonPanel, loadGameButton, titleLabel));

		// Load game button functionality
		loadGameButton.addActionListener(arg0 -> loadLastGameAction(startButton, selectLevel, buttonPanel, loadGameButton, titleLabel));

		// Select level button functionality
		selectLevel.addActionListener(arg0 -> selectLevelAction(startButton, selectLevel, buttonPanel, loadGameButton, titleLabel));

		gbc.gridy = 1;
		mainPanel.add(buttonPanel, gbc);
		mainPanel.revalidate();
		mainPanel.repaint();
		logger.info("Ttitle and start, load game and select level buttons have been added to the main menu");
	}
	
	@Override
	/**
	 * Displays the level selection buttons for choosing a specific game level.
	 * Creates a panel with buttons for levels 1 to 5 and a button to return to the
	 * main menu
	 */
	public void showLevelButtons() {
		logger.info("Displaying level buttons...");
		JPanel levelPanel = new JPanel(new GridLayout(2, 5, 10, 10));
		levelPanel.setOpaque(false);

		JButton backButton = new JButton("GO TO MAIN MENU");
		backButton.addActionListener(arg0 -> backToMainMenuAction());

		JButton level1Button = new JButton("Level 1");
		JButton level2Button = new JButton("Level 2");
		JButton level3Button = new JButton("Level 3");
		JButton level4Button = new JButton("Level 4");
		JButton level5Button = new JButton("Level 5");

		level1Button.addActionListener(arg0 -> level1Action());
		level2Button.addActionListener(arg0 -> level2Action());
		level3Button.addActionListener(arg0 -> level3Action());
		level4Button.addActionListener(arg0 -> level4Action());
		level5Button.addActionListener(arg0 -> level5Action());


		levelPanel.add(level1Button);
		levelPanel.add(level2Button);
		levelPanel.add(level3Button);
		levelPanel.add(level4Button);
		levelPanel.add(level5Button);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(levelPanel, gbc);
		gbc.gridy = 2;
		mainPanel.add(backButton, gbc);

		mainPanel.revalidate();
		mainPanel.repaint();
		logger.info("Level buttons displayed.");
	}

	//--------BUTTON FUNCTIONALITIES------------
	/**
	 * Handles the action of the "Back to Menu" button.
	 * <p>
	 * This method is triggered when the "Back to Menu" button is clicked. It logs an informational message,
	 * clears the main panel, and displays the level selection buttons.
	 */
	private void backToMenuAction() {
		logger.info("Back to menu button clicked.");
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
		showLevelButtons();
	}

	/**
	 * Handles the action of the "Next Level" button.
	 * <p>
	 * Advances the game to the next level if the current level is completed.
	 * If all levels are completed, displays a congratulations message and total score.
	 */
	private void nextLevelAction() {
		try {
			levelSavedLoaded = false;
			if (gridPanel.isLevelCompleted()) {
				controller.loadNewLevel();
				updateDataPanel();
				gridPanel.setCarsMap(controller.getCars(),dataPanel);
				gridPanel.setCars(controller.getCars());
				gridPanel.setBoard(controller.getBoard());
				((Grid) gridPanel).repaint();
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
			congratsLabel.setFont(new Font(FONT, Font.PLAIN, 50));
			congratsLabel.setForeground(Color.BLACK);

			JLabel congratsLabel2 = new JLabel("All levels passed!", SwingConstants.CENTER);
			congratsLabel2.setFont(new Font(FONT, Font.PLAIN, 40));
			congratsLabel2.setForeground(Color.BLACK);

			JLabel totalScore = new JLabel("TOTAL SCORE: " + dataPanel.getGameScore(),
					SwingConstants.CENTER);
			totalScore.setFont(new Font(FONT, Font.PLAIN, 40));
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

	/**
	 * Handles the action of the "Save Game" button.
	 * <p>
	 * This method is triggered when the "Save Game" button is clicked. It saves the current
	 * state of the level.
	 */
	private void saveGameAction() {
		try {
			logger.info("Save game button clicked.");
			controller.saveGame();
			JOptionPane.showMessageDialog(MainFrame.this, "Game successfully saved.");
		} catch (Exception ex) {
			logger.error("There was an error saving the game: {}", ex.getLocalizedMessage());
			JOptionPane.showMessageDialog(MainFrame.this, "There was an error saving the game.");
		}
	}
	
	/**
	 * Handles the action of the "Undo" button.
	 * <p>
	 * This method is triggered when the "Undo" button is clicked.
	 * Attempts to undo the last movement in the game.
	 * Updates the game grid and cars positions accordingly.
	 */
	private void undoMovementAction() {
		try {
			logger.info("Undo movement button clicked.");
			char[][] oldBoard = controller.undoMovement();
			gridPanel.setCars(controller.getCars());
			gridPanel.setBoard(oldBoard);
			((Grid) gridPanel).repaint();
			musicPlayer.playErase();
		} catch (Exception e) {
			logger.error("Couldn´t undo movement: {}", e.getLocalizedMessage());
		}
		updateDataPanel();
	}
	
	/**
	 * Handles the action of the "Reset" button.
	 * <p>
	 * This method is triggered when the "Reset" button is clicked.
	 * Resets the current level in the game.
	 */
	private void resetLevelAction() {
		logger.info("Reset button clicked.");
		if (!levelSavedLoaded) {
			controller.resetLevel();
			gridPanel.setCars(controller.getCars());
			gridPanel.setBoard(controller.getBoard());
			((Grid) gridPanel).repaint();
			updateDataPanel();
			musicPlayer.playLevelStart();
		} else {
			controller.resetOriginalLevel();
			gridPanel.setCars(controller.getCars());
			gridPanel.setBoard(controller.getBoard());
			((Grid) gridPanel).repaint();
			updateDataPanel();
			musicPlayer.playLevelStart();
		}
	}
	
	/**
	 * Handles the action when the 'New Game' button is clicked.
	 * It starts a new game from the first level
	 */
	private void newGameAction(JButton startButton, JButton selectLevel, JPanel buttonPanel, JButton loadGameButton, JLabel titleLabel) {
		logger.info("New Game button clicked.");
		clearMainPanel(startButton, selectLevel, buttonPanel, loadGameButton, titleLabel);

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
	
	/**
	 * Handles the action when the 'Load Last Game' button is clicked.
	 * Loads the level and its state as it was last saved.
	 */
	private void loadLastGameAction(JButton startButton, JButton selectLevel, JPanel buttonPanel, JButton loadGameButton, JLabel titleLabel) {
		logger.info("Load Last Game button clicked.");
		clearMainPanel(startButton, selectLevel, buttonPanel, loadGameButton, titleLabel);
		try {
			levelSavedLoaded = true;
			controller.loadSavedLevel();

			dataPanel = new DataPanel(controller);
			dataPanel.loadPunctuation();
			startGame();
			musicPlayer.playLevelStart();
		} catch (IllegalExitsNumberException | IllegalCarDimensionException e) {
			logger.error("Cannot load saved game: {}", e.getLocalizedMessage());
		}
	}
	
	/**
	 * Handles the action when the 'Select Level' button is clicked.
	 * Loads the menu to choose a specific level.
	 * @param titleLabel 
	 * @param loadGameButton 
	 * @param buttonPanel 
	 * @param selectLevel 
	 * @param startButton 
	 */
	private void selectLevelAction(JButton startButton, JButton selectLevel, JPanel buttonPanel, JButton loadGameButton, JLabel titleLabel) {
		logger.info("Select Level button clicked.");
		clearMainPanel(startButton, selectLevel, buttonPanel, loadGameButton, titleLabel);
		// Show select level screen
		showLevelButtons();
	}
	
	/**
	 * Handles the action when the 'Go to main menu' button is clicked.
	 * Navigates the user back to the main menu.
	 */
	private void backToMainMenuAction() {
		logger.info("'Go to main menu' button clicked.");
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
		addMainMenuTitleAndButtons();
	}
	
	/**
	 * Handles the action when the Level 1 button is clicked.
	 * Loads level 1 and starts the game.
	 */
	private void level1Action() {
		logger.info("Level 1 button clicked.");
		loadLevelAndStartGame(1);
	}
	
	/**
	 * Handles the action when the Level 2 button is clicked.
	 * Loads level 2 and starts the game.
	 */
	private void level2Action() {
		logger.info("Level 2 button clicked.");
		loadLevelAndStartGame(2);
	}
	
	/**
	 * Handles the action when the Level 3 button is clicked.
	 * Loads level 3 and starts the game.
	 */
	private void level3Action() {
		logger.info("Level 3 button clicked.");
		loadLevelAndStartGame(3);
	}
	
	/**
	 * Handles the action when the Level 4 button is clicked.
	 * Loads level 4 and starts the game.
	 */
	private void level4Action() {
		logger.info("Level 4 button clicked.");
		loadLevelAndStartGame(4);
	}
	
	/**
	 * Handles the action when the Level 5 button is clicked.
	 * Loads level 5 and starts the game.
	 */
	private void level5Action() {
		logger.info("Level 5 button clicked.");
		loadLevelAndStartGame(5);
	}

	//--------END OF BUTTON FUNCTIONALITIES------------

	@Override
	/**
	 * Updates the data panel with the new level and scores
	 */
	public void updateDataPanel() {
		logger.info("Updating data panel...");
		dataPanel.updateData(controller.getLevelNumber(), controller.getGameScore(), controller.getLevelScore());
		logger.info("Panel data updated.");
	}

	@Override
	/**
	 * Updates the data panel adding 1 to level and total scores
	 */
	public void increaseScore() {
		logger.info("Increasing score...");
		dataPanel.addPoint();
		logger.info("Score increased.");
	}

	@Override
	/**
	 * Loads the specified level and starts the game for that level
	 * 
	 * @param levelNumber the number of the level to load
	 */
	public void loadLevelAndStartGame(int levelNumber) {
		logger.info("Loading level {} and starting game...", levelNumber);
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();
		controller.loadLevel(levelNumber);
		startOnlyOneLevelGame();
		musicPlayer.playLevelStart();
		levelSavedLoaded = false;
	}


	

	@Override
	/**
	 * Clears the entire mainPanel
	 * 
	 * @param startButton
	 * @param selectLevel
	 * @param buttonPanel
	 * @param loadGameButton
	 * @param titleLabel
	 */
	public void clearMainPanel(JButton startButton, JButton selectLevel, JPanel buttonPanel, JButton loadGameButton,
			JLabel titleLabel) {
		logger.info("Clearing main panel...");
		mainPanel.remove(startButton);
		mainPanel.remove(selectLevel);
		buttonPanel.remove(selectLevel);
		buttonPanel.remove(startButton);
		buttonPanel.remove(loadGameButton);
		mainPanel.remove(titleLabel);
		mainPanel.revalidate();
		mainPanel.repaint();
		logger.info("Main panel cleared.");
	}

	@Override
	/**
	 * Creates a title label with the specified text with a shadow
	 * 
	 * @param text the text for the title label
	 * @return the created title label
	 */
	public JLabel createTitleLabel(String text) {
		logger.info("Creating title label...");
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
		label.setFont(new Font(FONT, Font.PLAIN, 50));
		label.setForeground(Color.BLACK);
		label.setOpaque(false);
		logger.info("Title lable created.");
		return label;
	}

	@Override
	/**
	 * Gets the board grid
	 * 
	 * @return the board grid
	 */
	public IGrid getGrid() {
		logger.info("Getting the grid...");
		logger.info("The grid was obtained.");
		return this.gridPanel;
	}

	@Override
	/**
	 * Sets the board grid to a new one
	 * 
	 * @param grid the new grid
	 */
	public void setGrid(Grid grid) {
		logger.info("Setting a new grid...");
		mainPanel.add((Grid) gridPanel, BorderLayout.CENTER);
		logger.info("New grid set.");
	}
}
