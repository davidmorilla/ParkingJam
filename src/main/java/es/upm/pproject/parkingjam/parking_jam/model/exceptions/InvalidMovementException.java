package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class InvalidMovementException extends Exception{

    public InvalidMovementException() {
        super("ERROR: The movement is not valid.");
    }   
    
}
