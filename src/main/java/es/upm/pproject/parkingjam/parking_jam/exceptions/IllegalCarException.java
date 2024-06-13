package es.upm.pproject.parkingjam.parking_jam.exceptions;

public class IllegalCarException extends Exception{
    public IllegalCarException(){
        super("ERROR: The car does not exist.");
    }
}
