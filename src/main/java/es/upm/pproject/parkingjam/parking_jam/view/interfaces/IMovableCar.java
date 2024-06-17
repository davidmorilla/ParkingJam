package es.upm.pproject.parkingjam.parking_jam.view.interfaces;

import java.awt.Graphics;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.InvalidMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.LevelAlreadyFinishedException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.MovementOutOfBoundariesException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.NullBoardException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;

public interface IMovableCar {
    /**
	 * Returns the current column (horizontal) position of the car
	 * 
	 * @return car's horizontal position
	 */
	public int getCol();

    /**
	 * Returns the current row (vertical) position of the car
	 * 
	 * @return car's vertical position
	 */
	public int getRow();

    /**
	 * Update the position of the car
	 *
	 * @param coordinates the new position x,y of the car
	 */
	public void updatePosition(Coordinates coordinates);

    /**
	 * Draw the car on the given graphical component given
	 *
	 * @param g The graphical componente in which to draw this car
	 */
	public void draw(Graphics g);


   /**
	 * Initiates the dragging process
	 */
	public void startDrag();

    /**
	 * Ends the dragging process and moves the car if it has been dragged
	 * @throws LevelAlreadyFinishedException 
	 * @throws NullBoardException 
	 * @throws IllegalCarException 
	 * @throws MovementOutOfBoundariesException 
	 * @throws InvalidMovementException 
	 */
	public void endDrag() throws LevelAlreadyFinishedException, InvalidMovementException, MovementOutOfBoundariesException, IllegalCarException, NullBoardException;


    /**
	 * Drag the car to a new position
	 *
	 * @param dx X axis offset
	 * @param dy Y axis offset
	 */
	public void drag(int dx, int dy);


    /**
	 * Move the car con the board and update its graphical representation
	 *
	 * @param dx X axis offset
	 * @param dy Y axis offset
	 * @throws LevelAlreadyFinishedException 
	 * @throws NullBoardException 
	 * @throws IllegalCarException 
	 * @throws MovementOutOfBoundariesException 
	 * @throws InvalidMovementException 
	 */
	public void moveCar(int dx, int dy) throws LevelAlreadyFinishedException, InvalidMovementException, MovementOutOfBoundariesException, IllegalCarException, NullBoardException;

    /**
	 * Load the car's image from resources folders
	 *
	 * @param image Name of car's image
	 */
	public void loadImage(String image);
}
