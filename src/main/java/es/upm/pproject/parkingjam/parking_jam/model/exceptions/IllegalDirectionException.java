package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

/**
 * IllegalDirectionException is thrown then the car
 * tries to move in an unknown direction
 */
public class IllegalDirectionException extends Exception{
	
	/**
	 * IllegalDirectionException constructor. Creates exception
	 * with a predefined message
	 */
	public IllegalDirectionException(){
        super("ERROR: Direction must be one of the following: 'R'(Right), 'L'(Left), 'U'(Up), 'D'(Down)");
    }
}
