package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class IllegalDirectionException extends Exception{
	public IllegalDirectionException(){
        super("ERROR: Direction must be one of the following: 'R'(Right), 'L'(Left), 'U'(Up), 'D'(Down)");
    }
}
