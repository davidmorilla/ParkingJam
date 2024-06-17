package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class LevelAlreadyFinishedException extends Exception {
	public LevelAlreadyFinishedException() {
		super("ERROR: The level is already finished. Cannot move the car");
	}
}
