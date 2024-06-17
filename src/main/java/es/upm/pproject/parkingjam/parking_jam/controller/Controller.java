package es.upm.pproject.parkingjam.parking_jam.controller;

import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.GameSaver;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.MainFrame;
import es.upm.pproject.parkingjam.parking_jam.view.interfaces.IGrid;

/**
 * Controller class that manages the interaction between the view (MainFrame) and the model (Game).
 * It provides methods to control the game flow, such as loading levels, moving cars, and saving the game state.
 */
public class Controller implements ControllerInterface {
	private MainFrame mf;
	private Game game; 
	private GameSaver gameSaver;
	
	/**
     * Initializes the game, the main frame, and the game saver.
     * 
     * @throws IllegalExitsNumberException if the number of exits is illegal
     * @throws IllegalCarDimensionException if the car dimensions are illegal
	 * @throws NullBoardException 
     */
	public Controller() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
		game = new Game();
		mf = new MainFrame(this);
		gameSaver = game.getGameSaver();
	}
	
	 /**
     * Loads a new level in the game.
     * 
     * @throws IllegalExitsNumberException if the number of exits is illegal
     * @throws IllegalCarDimensionException if the car dimensions are illegal
	 * @throws NullBoardException 
     */
	public void loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
		game.loadNewLevel();

	}
	
	/**
     * Loads a saved level in the game.
     * 
     * @return the level number of the loaded saved level
     * @throws IllegalExitsNumberException if the number of exits is illegal
     * @throws IllegalCarDimensionException if the car dimensions are illegal
	 * @throws NullBoardException 
     */
	public int loadSavedLevel() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
		return game.loadSavedLevel(gameSaver);

	}
	
	/**
     * Loads a specific level by its number.
     * 
     * @param levelNumber the number of the level to load
	 * @throws NullBoardException 
     */
	public void loadLevel(int levelNumber) throws NullBoardException {
		game.loadLevel(levelNumber);
	}
	
	/**
     * Moves a car on the board.
     * 
     * @param car the identifier of the car to move
     * @param length the length to move the car
     * @param way the direction to move the car
     * @return the updated board state after the move
     * @throws SameMovementException if the same movement is attempted consecutively
	 * @throws IllegalDirectionException 
	 * @throws LevelAlreadyFinishedException 
	 * @throws NullBoardException 
	 * @throws MovementOutOfBoundariesException 
	 * @throws InvalidMovementException 
	 * @throws IllegalCarException 
     */
	public char[][] moveCar(char car, int length, char way) throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
		return game.moveCar(car, length, way);
	}
	
	/**
     * Undoes the last car movement.
     * 
     * @return the updated board state after undoing the move
     * @throws CannotUndoMovementException if no movements can be undone
     * @throws SameMovementException if the same movement is attempted consecutively
	 * @throws IllegalDirectionException 
	 * @throws LevelAlreadyFinishedException 
	 * @throws NullBoardException 
	 * @throws MovementOutOfBoundariesException 
	 * @throws InvalidMovementException 
	 * @throws IllegalCarException 
     */
	public char[][] undoMovement() throws CannotUndoMovementException, SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
		return game.undoMovement();
	}
	
	/**
     * Resets the current level.
	 * @throws NullBoardException 
     */
	public void resetLevel() throws NullBoardException{
		game.resetLevel();
	}
	
	/**
    * Checks if a car movement is valid.
    * 
    * @param car the identifier of the car
    * @param newCoord the new coordinates for the car
    * @param way the direction of the movement
    * @return true if the movement is valid, false otherwise
    * @throws SameMovementException if the same movement is attempted consecutively
    */
	public boolean isMoveValid(char car, Coordinates newCoord, char way) throws SameMovementException {
		return game.getLevel().checkMovementValidity(car, newCoord, way);
	}
	
	/**
    * Checks if the current level is finished.
    * 
    * @return true if the level is finished, false otherwise
    */
	public boolean isLevelFinished(){
		return game.getLevel().isLevelFinished(this.getBoard()); 
	}
	
	/**
     * Saves the current game state.
     */
	public void saveGame() {
		game.saveGame(gameSaver);

	}
	
	/**
     * Resets the level to its original state.
	 * @throws NullBoardException 
     */
	public void resetOriginalLevel() throws NullBoardException {
		int newGamePunctuation = game.getAccumulatedScore();
		this.setGameScore(newGamePunctuation);
		game.getLevel().setOldScore(game.getLevelScore());
		
		game.resetOriginalLevel();
		game.setLevelScore(0);
	}
	
	/**
     * Gets the current state of the board.
     * 
     * @return the current board state
     */
	public char[][] getBoard(){
		return game.getBoard();
	}
	
	/**
     * Gets the dimensions of the board.
     * 
     * @return a pair containing the width and height of the board
     */
	public Pair<Integer, Integer> getBoardDimensions() {
		// This will return the board dimensions of the current level
		return game.getDimensions();
	}
	
	/**
     * Gets the map of cars on the board.
     * 
     * @return a map of car identifiers to Car objects
     */
	public Map<Character,Car> getCars(){
		return game.getCars();
	}
	
	/**
     * Gets the total game score.
     * 
     * @return the total game score
     */
	public int getGameScore() {
		return game.getGameScore();
	}
	
	/**
     * Gets the score for the current level.
     * 
     * @return the current level score
     */
	public int getLevelScore() {
		return game.getLevelScore();
	}
	
	/**
     * Gets the current level number.
     * 
     * @return the current level number
     */
	public int getLevelNumber() {
		return game.getLevelNumber();
	}	
	
	/**
     * Sets the score for the current level.
     * 
     * @param score the score to set for the current level
     */
	public void setPunctuation(int score) {
		game.getLevel().setPunctuation(score);
	}
	
	/**
     * Sets the total game score.
     * 
     * @param totalPoints the total points to set as the game score
     */
	public void setGameScore(int totalPoints) {
		game.setGameScore(totalPoints);
	}


	/**
	 * Gets the current grid
	 * 
	 * @return the current grid
	 */
	public IGrid getGrid() {
		return mf.getGrid();
	}

	/**
	 * Set the car list on the grid
	 */
	public void setCars() {
		mf.getGrid().setCars(getCars());
	}

	/**
	 * Set the board on the grid
	 */
	public void setBoard(char[][] newBoard) {
		mf.getGrid().setBoard(newBoard);
	}
	
}
