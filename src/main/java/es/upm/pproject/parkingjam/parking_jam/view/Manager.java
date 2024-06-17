package es.upm.pproject.parkingjam.parking_jam.view;

/**
 * Manager manages an instance of MovableCar
 */
public class Manager {
    private MovableCar mc;

    /**
     * Manager constructor
     * 
     * @param mc the MovableCar instance to be managed
     */
    public Manager(MovableCar mc){
        this.mc = mc;
    }

    /**
     * Get the MovableCar instance managed
     * 
     * @return the MovableCar instance managed
     */
    public MovableCar getMovableCar(){
        return mc;
    }
}

