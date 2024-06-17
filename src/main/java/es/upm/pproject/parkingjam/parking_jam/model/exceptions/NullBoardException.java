package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class NullBoardException extends Exception{
	public NullBoardException() {
		super("ERROR: The board is null");
	}
}
