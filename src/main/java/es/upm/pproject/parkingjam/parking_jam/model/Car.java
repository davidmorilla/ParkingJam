package es.upm.pproject.parkingjam.parking_jam.model;

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

}
