package es.upm.pproject.parkingjam.parking_jam.exceptions;

public class SameMovementException extends Exception{
    public SameMovementException(){
        super("ERROR: Cannot repeat movement.");
    }
}
