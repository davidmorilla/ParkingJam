package es.upm.pproject.parkingjam.parking_jam.model.exceptions;
/**
 * NullBoardException is thrown when the board is null.
 */
public class NullBoardException extends Exception{
	/**
	 * NullBoardException constructor. Creates exception
	 * with a predefined message
	 */
	public NullBoardException() {
		super("ERROR: The board is null");
	}
}
