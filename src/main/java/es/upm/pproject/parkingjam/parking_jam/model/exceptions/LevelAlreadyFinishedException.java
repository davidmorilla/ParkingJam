package es.upm.pproject.parkingjam.parking_jam.model.exceptions;
/**
 * LevelAlreadyFinishedException is thrown when a movement is 
 * attempted once the level has already finished.
 */
public class LevelAlreadyFinishedException extends Exception {
	
	/**
	 * LevelAlreadyFinishedException constructor. Creates exception
	 * with a predefined message
	 *
	 */
	public LevelAlreadyFinishedException() {
		super("ERROR: The level is already finished. Cannot move the car");
	}
}
