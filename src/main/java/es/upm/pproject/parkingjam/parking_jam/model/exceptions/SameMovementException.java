package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class SameMovementException extends Exception{
    public SameMovementException(){
        super("ERROR: Cannot repeat movement.");
    }
}
