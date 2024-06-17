package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class MovementOutOfBoundariesException extends Exception{
	public MovementOutOfBoundariesException() {
		super("ERROR: The new coordinates of the car in the movement are out of boundaries");
	}
}
