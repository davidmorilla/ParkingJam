package es.upm.pproject.parkingjam.parking_jam.model.exceptions;

public class CannotUndoMovementException extends Exception {
    public CannotUndoMovementException(){
        super("ERROR: There are no movements to undo.");
    }
}
