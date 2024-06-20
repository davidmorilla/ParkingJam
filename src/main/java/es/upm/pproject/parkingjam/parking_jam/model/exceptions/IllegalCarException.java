package es.upm.pproject.parkingjam.parking_jam.model.exceptions;
/**
 * IllegalCarException is thrown when the car represented by a char does not exist
 */
public class IllegalCarException extends Exception{
	/**
	 * IllegalCarDimension constructor. Creates exception
	 * with a predefined message
	 *
	 */
    public IllegalCarException(){
        super("ERROR: The car does not exist.");
    }
}
