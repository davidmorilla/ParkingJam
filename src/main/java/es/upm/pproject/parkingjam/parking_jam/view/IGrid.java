package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Point;
import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalDirectionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;

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
	 */
	public void setCarsMap(Map<Character, Car> cars, DataPanel dataPanel);

	/**
	 * Triest to move the car. If successful, returns the new board result of the movement
	 * 
	 * @param car the car symbol, which represents the car that will try to move
	 * @param length the length of the car
	 * @param way the orientation of the car, vertical or horizontal
	 * @return the new board if the movement was successful
	 * @throws SameMovementException if the movement is to the same place.
	 * @throws IllegalDirectionException 
	 */
	public char[][] moveCar(char car, int length, char way) throws SameMovementException, IllegalDirectionException;

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
}