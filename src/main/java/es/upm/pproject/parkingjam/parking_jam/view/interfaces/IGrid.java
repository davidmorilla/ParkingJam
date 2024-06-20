package es.upm.pproject.parkingjam.parking_jam.view.interfaces;

import java.awt.Point;
import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalDirectionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.InvalidMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.LevelAlreadyFinishedException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.MovementOutOfBoundariesException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.NullBoardException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.DataPanel;
import es.upm.pproject.parkingjam.parking_jam.view.MovableCar;

/**
 * Represents the grid where the game is displayed, including cars, walls and exits
 */
public interface IGrid {

	/**
	 * Retrieves the movable car at a specific point in the grid
	 * @param point Point object representing the coordinates of the point
	 * @return MovableCar object at the specified point, or null if no car is found
	 */
	public MovableCar getMovableCarAt(Point point);

	/**
	 * Gets the board
	 * @return the board
	 */
	public char[][] getBoard();

	/**
	 * Sets the board to a new one
	 * @param board the new board
	 */
	public void setBoard(char[][] board);

	/**
	 * Update the position of all the cars in the map
	 * 
	 * @param cars a map of cars
	 */
	public void setCars(Map<Character, Car> cars);

	/**
	 * Create drawable cars equivalent instances (MovableCar) and store them
	 * 
	 * @param cars map of cars to create their drawable instances
	 * @param dataPanel the datapanel used
	 */
	public void setCarsMap(Map<Character, Car> cars, DataPanel dataPanel);

	/**
	 * Triest to move the car. If successful, returns the new board result of the movement
	 * 
	 * @param car the car symbol, which represents the car that will try to move
	 * @param length the length of the car
	 * @param way the orientation of the car, vertical or horizontal
	 * @return the new board if the movement was successful
     * @throws IllegalDirectionException if the direction specified is not valid. 
     * @throws LevelAlreadyFinishedException if the user attempts to make a movement after the level has finished.
     * @throws NullBoardException if the board is null.
     * @throws MovementOutOfBoundariesException if the movement attempted is out of boundaries
     * @throws InvalidMovementException if the movement is invalid
     * @throws IllegalCarException if the car does not exist
	 */
	public char[][] moveCar(char car, int length, char way) throws IllegalDirectionException, LevelAlreadyFinishedException, InvalidMovementException, MovementOutOfBoundariesException, IllegalCarException, NullBoardException;

	/**
	 * Returns whether or not the level is completed
	 * 
	 * @return true when @ symbol is not found, false otherwhise
	 */
	public boolean isLevelCompleted();

	/**
	 * Return the square size for each tile on the grid
	 * 
	 * @return square size
	 */
	public int getSquareSize();

	/**
	 * Sets the dimensions of the board of the current level
	 * 
	 * @param dimensions the dimensions of the level
	 */
	public void setDimensions(Pair<Integer, Integer> dimensions);
}