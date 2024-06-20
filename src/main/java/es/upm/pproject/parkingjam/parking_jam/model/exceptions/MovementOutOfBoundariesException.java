package es.upm.pproject.parkingjam.parking_jam.model.exceptions;
/**
 * MovementOutOfBoundariesException is thrown when a movement to an out of boundaries tile is a attempted.
 */
public class MovementOutOfBoundariesException extends Exception{
	/**
	 * MovementOutOfBoundariesException constructor. Creates exception
	 * with a predefined message
	 */
	public MovementOutOfBoundariesException() {
		super("ERROR: The new coordinates of the car in the movement are out of boundaries");
	}
}
