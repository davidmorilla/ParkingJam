package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

/**
 * IllegalExitsNumberException is thrown then the level
 * contains an incorrect number of exits
 */
public class IllegalExitsNumberException extends Exception{
	
	/**
	 * IllegalExitsNumberException constructor. Creates exception
	 * with a predefined message
	 */
    public IllegalExitsNumberException(){
        super("ERROR: Illegal level design.");
    }
}
