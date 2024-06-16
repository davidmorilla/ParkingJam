package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

/**
 * InvalidMovementException is thrown then a invalid
 * movement of a car is made
 */
public class InvalidMovementException extends Exception{

	/**
	 * InvalidMovementException constructor. Creates exception
	 * with a predefined message
	 */
    public InvalidMovementException() {
        super("ERROR: The movement is not valid.");
    }   
    
}
