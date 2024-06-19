package es.upm.pproject.parkingjam.parking_jam.controller;

import java.util.Map;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.interfaces.IGrid;

/**
 * Interface that defines the contract for a game controller.
 * It provides methods to control the game flow, such as loading levels, moving cars, and saving the game state.
 */
public interface ControllerInterface {

	/**
     * Initializes a new game.
     * 
     * @throws IllegalExitsNumberException if the number of exits is illegal
     * @throws IllegalCarDimensionException if the car dimensions are illegal
	 * @throws NullBoardException 
     */
	public void startNewGame() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException;

	/**
	 * Loads a new level in the game.
	 * 
	 * @throws IllegalExitsNumberException if the number of exits is illegal
	 * @throws IllegalCarDimensionException if the car dimensions are illegal
	 * @throws NullBoardException 
	 */
	void loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException;

	/**
	 * Loads a saved level in the game.
	 * 
	 * @return the level number of the loaded saved level
	 * @throws IllegalExitsNumberException if the number of exits is illegal
	 * @throws IllegalCarDimensionException if the car dimensions are illegal
	 * @throws NullBoardException 
	 */
	int loadSavedLevel() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException;

	/**
	 * Loads a specific level by its number.
	 * 
	 * @param levelNumber the number of the level to load
	 * @throws NullBoardException 
	 */
	void loadLevel(int levelNumber) throws NullBoardException;

	/**
	 * Moves a car on the board.
	 * 
	 * @param car the identifier of the car to move
	 * @param length the length to move the car
	 * @param way the direction to move the car
	 * @return the updated board state after the move
	 * @throws NullBoardException 
	 * @throws IllegalDirectionException 
	 * @throws LevelAlreadyFinishedException 
	 * @throws IllegalCarException 
	 * @throws InvalidMovementException 
	 * @throws MovementOutOfBoundariesException
	 */
	char[][] moveCar(char car, int length, char way) throws IllegalDirectionException, InvalidMovementException, LevelAlreadyFinishedException, MovementOutOfBoundariesException, IllegalCarException, NullBoardException;

	/**
	 * Undoes the last car movement.
	 * 
	 * @return the updated board state after undoing the move
	 * @throws CannotUndoMovementException if no movements can be undone
	 * @throws IllegalDirectionException 
	 * @throws LevelAlreadyFinishedException 
	 * @throws NullBoardException 
	 * @throws MovementOutOfBoundariesException 
	 * @throws InvalidMovementException 
	 * @throws IllegalCarException 
	 */
	char[][] undoMovement() throws CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException;

	/**
	 * Resets the current level.
	 * @throws NullBoardException
	 */
	void resetLevel() throws NullBoardException;

	/**
	 * Checks if a car movement is valid.
	 * 
	 * @param car the identifier of the car
	 * @param newCoord the new coordinates for the car
	 * @param way the direction of the movement
	 * @return true if the movement is valid, false otherwise
	 */
	boolean isMoveValid(char car, Coordinates newCoord, char way);

	/**
	 * Checks if the current level is finished.
	 * 
	 * @return true if the level is finished, false otherwise
	 */
	boolean isLevelFinished();

	/**
	 * Saves the current game state.
	 */
	void saveGame();

	/**
     * Resets the level to its original state.
     * @throws NullBoardException
     */
	void resetOriginalLevel() throws NullBoardException;

	/**
     * Gets the current state of the board.
     * 
     * @return the current board state
     */
	char[][] getBoard();

	/**
     * Gets the dimensions of the board.
     * 
     * @return a pair containing the width and height of the board
     */
	Pair<Integer, Integer> getBoardDimensions();

	/**
     * Gets the map of cars on the board.
     * 
     * @return a map of car identifiers to Car objects
     */
	Map<Character, Car> getCars();

	/**
     * Gets the total game score.
     * 
     * @return the total game score
     */
	int getGameScore();

	/**
     * Gets the score for the current level.
     * 
     * @return the current level score
     */
	int getLevelScore();

	/**
     * Gets the current level number.
     * 
     * @return the current level number
     */
	int getLevelNumber();

	/**
     * Sets the score for the current level.
     * 
     * @param score the score to set for the current level
     */
	void setPunctuation(int score);

	/**
     * Sets the total game score.
     * 
     * @param totalPoints the total points to set as the game score
     */
	void setGameScore(int totalPoints);

	/**
	 * Gets the current grid
	 * 
	 * @return the current grid
	 */
    IGrid getGrid();

    /**
	 * Set the car list on the grid
	 */
	void setCars();

	/**
	 * Set the board on the grid
	 */
	void setBoard(char[][] newBoard);
}