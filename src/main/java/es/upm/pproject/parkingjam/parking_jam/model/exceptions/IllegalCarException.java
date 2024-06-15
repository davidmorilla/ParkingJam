package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class IllegalCarException extends Exception{
    public IllegalCarException(){
        super("ERROR: The car does not exist.");
    }
}
