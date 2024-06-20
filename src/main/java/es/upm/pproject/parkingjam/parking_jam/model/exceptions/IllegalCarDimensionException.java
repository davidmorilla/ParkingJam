package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

/**
 * IllegalCarDimensionException is thrown when the red car size
 * does not match the 2x1 or the 1x2 dimensions
 */
public class IllegalCarDimensionException extends Exception{
	
	/**
	 * IllegalCarDimensionException constructor. Creates exception
	 * with a predefined message
	 */
    public IllegalCarDimensionException(){
        super("ERROR: Illegal red car dimension.");
    }
}
