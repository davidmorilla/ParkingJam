package es.upm.pproject.parkingjam.parking_jam.utilities;
/**
 * A utility class to represent coordinates on a grid.
 */
public class Coordinates{
    private int x;
    private int y;
    
    /**
     * Constructs a new Coordinates object with the specified x and y values.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns the x-coordinate.
     * 
     * @return the x-coordinate
     */
    public int getX(){
        return this.x;
    }
    
    /**
     * Returns the y-coordinate.
     * 
     * @return the y-coordinate
     */
    public int getY(){
        return this.y;
    }
    
    /**
     * Sets the x-coordinate.
     * 
     * @param x the new x-coordinate
     */
    public void setX(int x){
        this.x = x;
    }
    
    /**
     * Sets the y-coordinate.
     * 
     * @param y the new y-coordinate
     */
    public void setY(int y){
        this.y = y;
    }
    
    /**
     * Sets both the x and y coordinates.
     * 
     * @param x the new x-coordinate
     * @param y the new y-coordinate
     */
    public void setCoordinates(int x, int y){
        this.x = x;
        this.y = y;
    }
}
