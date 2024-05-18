package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.Objects;

import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;

public class Car {
	// X e Y representan la 1a posición leída del coche en el fichero del mapa
	//(la de arriba de un coche en vertical y la de la izquierda de uno en horizontal)
	private char symbol;
    private Coordinates coordinates;
    private int length;
    private char orientation;

    public Car(char symbol, int x, int y, int length, char orientation) {
        this.symbol = symbol;
        this.coordinates = new Coordinates(x, y);
        this.length = length;
        this.orientation = orientation;
    }
	
	public char getSymbol() {
		return symbol;
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

	public Coordinates getCoordinates(){
        return this.coordinates; 
    }

    public void setCoordinates(int x, int y) {
        this.coordinates.setX(x);
        this.coordinates.setY(y);
    }

    public int getLength(){
        return this.length;
    }

    public void setLength(int l){
        this.length = l;
    }

    public char getOrientation(){
        return this.orientation;
    }

    public void setOrientation(char o){
        this.orientation = o;
    }
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

    @Override
    public int hashCode() {
        return Objects.hash(symbol, coordinates.getX(), coordinates.getY(), length, orientation);
    }
}


