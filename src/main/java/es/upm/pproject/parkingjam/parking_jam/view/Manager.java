package es.upm.pproject.parkingjam.parking_jam.view;

public class Manager {
    private MovableCar mc;

    public Manager(MovableCar mc){
        this.mc =mc;
    }

    public MovableCar getMovableCar(){
        return mc;
    }
}
