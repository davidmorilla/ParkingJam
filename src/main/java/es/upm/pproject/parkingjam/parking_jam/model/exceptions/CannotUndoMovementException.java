package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

/**
 * CannotUndoMovementException is thrown then the player can't
 * undo a movement
 */
public class CannotUndoMovementException extends Exception {
	
	/**
	 * CannotUndoMovementException constructor. Creates exception
	 * with a predefined message
	 */
    public CannotUndoMovementException(){
        super("ERROR: There are no movements to undo.");
    }
}
