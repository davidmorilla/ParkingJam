package es.upm.pproject.parkingjam.parking_jam.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public interface IMainFrame {
    
    /**
	 * Initializes and displays the main menu of the game
	 */
    public void mainMenu();

    /**
	 * Starts the game by initializing the game components and adding them to the main panel
	 */
	public void startGame();

    /**
	 * Starts an only level game by initializing the game components and adding them to the main panel
	 */
	public void startOnlyOneLevelGame();


    /**
	 * Initialize the components depending on whether it's a full game or a single level game
	 */
	public void initializeGameComponents(boolean onlyOneLevel);

    /**
	 * Updates the data panel with the new level and scores
	 */
	public void updateDataPanel();

    /**
	 * Updates the data panel adding 1 to level and total scores
	 */
	public void increaseScore();

    /**
	 * Loads the specified level and starts the game for that level
	 * @param levelNumber the number of the level to load
	 */
	public void loadLevelAndStartGame(int levelNumber);

    /**
	 * Displays the level selection buttons for choosing a specific game level.
	 * Creates a panel with buttons for levels 1 to 5 and a button to return to the main menu
	 */
	public void showLevelButtons();

    /**
	 * Add the title and start, load game and select level buttons of the main menu to the mainPanel
	 */
	public void addMainMenuTitleAndButtons() ;

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
			JLabel titleLabel);


    /**
	 * Creates a title label with the specified text with a shadow
	 * 
	 * @param text the text for the title label
	 * @return the created title label
	 */
	public JLabel createTitleLabel(String text);
	
	/**
	 * Gets the board grid
	 * @return the board grid
	 */
	public IGrid getGrid();

    /**
	 * Sets the board grid to a new one
	 * @param grid the new grid
	 */
	public void setGrid(Grid grid);
}
