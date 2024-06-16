package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

/**
 * SameMovementException is thrown then the player tries
 * to make the same movement twice
 */
public class SameMovementException extends Exception{
	
	/**
	 * SameMovementException constructor. Creates exception
	 * with a predefined message
	 */
    public SameMovementException(){
        super("ERROR: Cannot repeat movement.");
    }
}
