package es.upm.pproject.parkingjam.parking_jam.utilities;

import java.util.Objects;
/**
 * The Car class represents a car in the Parking Jam game. Each car has a symbol, coordinates,
 * length, and orientation. The car can move within the constraints of the game board and its 
 * attributes can be modified and retrieved.
 */
public class Car {
	
	private char symbol;          // The symbol representing the car
    private Coordinates coordinates; // The coordinates of the car's position
    private int length;           // The length of the car
    private char orientation;     // The orientation of the car ('V' for vertical, 'H' for horizontal)

    /**
     * Creates a car object and initializes its attributes.
     *
     * @param symbol the char which will represent the car. It must have this format: [a-zA-Z]{1}.
     * @param x a positive integer that determines the x coordinate where the uppermost left part of the car is placed.
     * @param y a positive integer that determines the y coordinate where the uppermost left part of the car is placed.
     * @param length a positive integer equal to or greater than 2 that determines the number of tiles that the car occupies.
     * @param orientation a char that represents the orientation that the car will have. It must be either 'V' for vertical or 'H' for horizontal.
     */
    public Car(char symbol, int x, int y, int length, char orientation) {
        this.symbol = symbol;
        this.coordinates = new Coordinates(x, y);
        this.length = length;
        this.orientation = orientation;
    }
	
    /**
     * Retrieves the char that represents the car.
     *
     * @return the char that represents this car.
     */
	public char getSymbol() {
		return symbol;
	}

	/**
     * Modifies the char that represents the car.
     *
     * @param symbol the new symbol that will represent the car.
     */
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

    /**
     * Retrieves the coordinates of the car.
     *
     * @return the coordinates where the uppermost left part of the car is placed.
     */
	public Coordinates getCoordinates(){
        return this.coordinates; 
    }

    /**
     * Modifies the coordinates of the car.
     *
     * @param x a positive integer that determines the new x coordinate where the uppermost left part of the car is placed.
     * @param y a positive integer that determines the new y coordinate where the uppermost left part of the car is placed.
     */
    public void setCoordinates(int x, int y) {
        this.coordinates.setX(x);
        this.coordinates.setY(y);
    }

    /**
     * Retrieves the length of the car.
     *
     * @return the number of tiles that the car occupies.
     */
    public int getLength(){
        return this.length;
    }

    /**
     * Modifies the length of the car.
     *
     * @param length a positive integer equal to or greater than 2 that determines the new number of tiles that the car occupies.
     */
    public void setLength(int length){
        this.length = length;
    }
    /**
     * Retrieves the orientation of the car.
     *
     * @return a char that represents the orientation that the car has. 'V' stands for vertical and 'H' for horizontal.
     */
    public char getOrientation(){
        return this.orientation;
    }
    
    /**
     * Modifies the orientation of the car.
     *
     * @param orientation a char that represents the new orientation that the car has. It must be either 'V' for vertical or 'H' for horizontal.
     */
    public void setOrientation(char orientation){
        this.orientation = orientation;
    }
    
    /**
     * Checks if this car is equal to another object.
     *
     * @param o the object to compare with.
     * @return true if the object is equal to this car, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return symbol == car.symbol &&
               length == car.length &&
               orientation == car.orientation &&
               coordinates.getX() == car.getCoordinates().getX()&&
               coordinates.getY() == car.getCoordinates().getY();
    }
    
    /**
     * Returns a hash code value for the car.
     *
     * @return a hash code value for this car.
     */
    @Override
    public int hashCode() {
        return Objects.hash(symbol, coordinates.getX(), coordinates.getY(), length, orientation);
    }
    
    /**
     * Returns a string representation of the car.
     *
     * @return a string representing the car.
     */
    @Override
    public String toString() {
        return this.symbol + ": {x=" + this.coordinates.getX() + ", y=" + this.coordinates.getY() + "}\nOrientation: " + this.orientation + "\nLength: " + this.length;
    }
}


