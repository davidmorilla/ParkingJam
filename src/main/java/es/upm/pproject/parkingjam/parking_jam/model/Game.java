package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

/**
 * The 'Game' class manages the current state of the game, including levels and scores.
 * It provides methods to load new levels, move cars, undo movements, and save the game state.
 */
public class Game {
	private int acumulatedScore;        // Accumulated score from previous levels of the game
	private int levelNumber;            // Current level number of the game
	private Level level;                // Current level instance
	private static final Logger logger = LoggerFactory.getLogger(Game.class);
	/**
     * Constructs a new 'Game' object starting from the specified level number.
     *
     * @param numLevel the starting level number.
     * @throws IllegalExitsNumberException if there is an illegal number of exits in the level.
     * @throws IllegalCarDimensionException if there are illegal dimensions for cars in the level.
	 * @throws NullBoardException 
     */
	public Game(int numLevel) throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
		logger.info("Creating new game...");
		acumulatedScore = 0;
		levelNumber = numLevel - 1;
		loadNewLevel();    
		logger.info("New game has been created.");
	}
	/**
     * Constructs a new 'Game' object starting from level 1.
     *
     * @throws IllegalExitsNumberException if there is an illegal number of exits in the level.
     * @throws IllegalCarDimensionException if there are illegal dimensions for cars in the level.
	 * @throws NullBoardException 
     */
	public Game() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
		logger.info("Creating new game...");
		acumulatedScore = 0;
		levelNumber = 0;
		loadNewLevel();    
		logger.info("New game has been created.");
	}
	/**
     * Retrieves the dimensions (rows and columns) of the current level's board.
     *
     * @return a 'Pair' object containing the dimensions (rows, columns).
     */
	public Pair<Integer, Integer> getDimensions() {
		// We need to return the dimensions of the current level
		return level.getDimensions();
	}
	/**
     * Retrieves the current board configuration of the level.
     *
     * @return a 2D char array representing the current board configuration.
     */
	public char[][] getBoard() {
		return level.getBoard();
	}

	/**
     * Loads a saved level using the provided 'GameSaver' instance.
     *
     * @param gs the 'GameSaver' instance containing the saved game data.
     * @return the level number of the loaded saved level.
     * @throws IllegalExitsNumberException if there is an illegal number of exits in the level.
     * @throws IllegalCarDimensionException if there are illegal dimensions for cars in the level.
	 * @throws NullBoardException 
     */
	public int loadSavedLevel(GameSaver gs) throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
		logger.info("Loading saved level...");
		LevelReader lr = new LevelReader();
		char[][] board = lr.readMap(levelNumber, true);
		levelNumber = lr.getLevelNumber(); // Get the level number from LevelReader
		level = new Level(board, new LevelConverter().convertLevel(board), lr.getGameSaver());
		int cols = level.getDimensions().getRight();
		int rows = level.getDimensions().getLeft();
		List<Pair<Character, Pair<Integer,Character>>> history = gs.loadHistory(cols, rows);
		level.setHistory(history);

		logger.info("Saved level {} has been loaded.", levelNumber);
		return levelNumber;
	}
	 /**
     * Loads the next new level.
     *
     * @return the level number of the loaded new level.
     * @throws IllegalExitsNumberException if there is an illegal number of exits in the level.
     * @throws IllegalCarDimensionException if there are illegal dimensions for cars in the level.
	 * @throws NullBoardException 
     */
	public final int loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
		logger.info("Loading new level...");
		levelNumber++;
		acumulatedScore += level != null ? level.getScore() : 0;
		LevelReader lr = new LevelReader();
		char[][] board = lr.readMap(levelNumber, false);
		level = new Level(board, new LevelConverter().convertLevel(board), lr.getGameSaver());
		logger.info("New level {} has been loaded.", levelNumber);
		return levelNumber;
	}
    /**
     * Moves a specified car on the current level board.
     *
     * @param car the identifier of the car to move.
     * @param length the distance to move the car.
     * @param way the direction (either 'H' for horizontal or 'V' for vertical) to move the car.
     * @return a 2D char array representing the updated board configuration after the movement.
     * @throws SameMovementException if the same movement has been attempted twice consecutively.
     * @throws IllegalDirectionException 
     * @throws LevelAlreadyFinishedException 
     * @throws NullBoardException 
     * @throws MovementOutOfBoundariesException 
     * @throws InvalidMovementException 
     * @throws IllegalCarException 
     */
	public char[][] moveCar(char car, int length, char way) throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
		return level.moveCar(car, length, way, false);
	}
    /**
     * Undoes the last movement made on the current level board.
     *
     * @return an 'OldBoardData' object representing the board state before the last movement.
     * @throws CannotUndoMovementException if there is no movement to undo.
     * @throws SameMovementException if the movement is to the same place.
     * @throws IllegalDirectionException 
     * @throws LevelAlreadyFinishedException 
     * @throws NullBoardException 
     * @throws MovementOutOfBoundariesException 
     * @throws InvalidMovementException 
     * @throws IllegalCarException 
     */
	public char[][] undoMovement() throws CannotUndoMovementException, SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
		return level.undoMovement();
	}
    /**
     * Retrieves the current game score, including accumulated and current level scores.
     *
     * @return the total game score.
     */
	public int getGameScore() {
		logger.info("Getting game score...");
		int score = level.getScore();
		logger.info("Game score: {} has been given.", score + acumulatedScore);
		return  score + acumulatedScore;
	}
    /**
     * Retrieves the accumulated score from previous levels.
     *
     * @return the accumulated score.
     */ 
	public int getAccumulatedScore() {
		logger.info("Getting acumulated score...");
		logger.info("Acumulated score: {} has been given.",acumulatedScore);
		return  acumulatedScore;
	}
	/**
     * Retrieves the score of the current level.
     *
     * @return the score of the current level.
     */
	public int getLevelScore() {
		return level.getScore();
	}
    /**
     * Retrieves the current level number.
     *
     * @return the current level number.
     */
	public int getLevelNumber() {
		logger.info("Getting level number...");
		logger.info("Level number: {} has been given.", levelNumber);
		return levelNumber;
	}
    /**
     * Retrieves the current 'Level' object instance.
     *
     * @return the current 'Level' instance.
     */
	public Level getLevel() {
		logger.info("Getting level...");
		logger.info("Level has been given.");
		return level;
	}
    /**
     * Retrieves the map of cars on the current level.
     *
     * @return a 'Map' containing car identifiers mapped to 'Car' objects.
     */
	public Map<Character, Car> getCars() {
		return level.getCars();
	}
    /**
     * Resets the current level to its initial state.
     * @throws NullBoardException 
     */
	public void resetLevel() throws NullBoardException {
		level.resetLevel();
	}
    /**
     * Sets the accumulated score to a specified value.
     *
     * @param totalPoints the total points to set as accumulated score.
     */
	public void setGameScore(int totalPoints) {
		logger.info("Setting game score = {}...", totalPoints );
		logger.info("Game score = {} successfully set.", totalPoints );
		this.acumulatedScore = totalPoints;
	}
    /**
     * Resets the original configuration of the current level.
     * @throws NullBoardException 
     * 
     */
	public void resetOriginalLevel() throws NullBoardException {
		level.resetOriginalLevel(levelNumber );

	}
    /**
     * Sets the score of the current level.
     * 
     * @param score the score to set for the current level.
     */ 
	public void setLevelScore(int score) {
		level.setPunctuation(score);
	}
    /**
     * Loads a specific level.
     * 
     * @param levelNumber the number of the level to load.
     * @throws NullBoardException 
     */
	public void loadLevel(int levelNumber) throws NullBoardException {
		
		String msgLog = "Loading level "+levelNumber+"...";
		logger.info(msgLog);

		LevelReader lr = new LevelReader();
		char[][] board = lr.readMap(levelNumber, false);
		try {
			this.levelNumber = levelNumber;
			
			level = new Level(board, new LevelConverter().convertLevel(board), lr.getGameSaver());
		} catch (IllegalExitsNumberException | IllegalCarDimensionException e) {
			logger.error("Cannot load level. The number of exists and/or car dimensions are not valid");
		}
		logger.info("New level {} has been loaded.", levelNumber);
	}
    /**
    * Retrieves the 'GameSaver' instance used to save the game state.
    * @return the 'GameSaver' instance
    */
	public GameSaver getGameSaver() {
		return level.getGameSaver();
	}
    /**
    * Saves the current game state using the provided 'GameSaver' instance.
    * 
    * @param gameSaver the 'GameSaver' instance used to save the game state.
    */
	public void saveGame(GameSaver gameSaver) {
		gameSaver.saveGame(level.getHistory(), this.getGameScore(), this.getLevelScore(), getBoard(), "Level " + levelNumber);
	}
}
