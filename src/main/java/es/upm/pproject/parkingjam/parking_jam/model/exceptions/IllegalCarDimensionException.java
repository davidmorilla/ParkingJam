package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class IllegalCarDimensionException extends Exception{
    public IllegalCarDimensionException(){
        super("ERROR: Illegal red car dimension.");
    }
}
