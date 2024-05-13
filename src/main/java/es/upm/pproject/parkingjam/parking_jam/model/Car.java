package es.upm.pproject.parkingjam.parking_jam.model;

import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;

public class Car {
	// X e Y representan la 1a posición leída del coche en el fichero del mapa
	//(la de arriba de un coche en vertical y la de la izquierda de uno en horizontal)
	private Coordinates pos;            // Posición (X,Y) del coche
	private int length;                 // Longitud del coche
	private char orientation;           // Orientación del coche

	public Car(int posX, int posY, int length, char orientation){
		this.pos = new Coordinates(posX, posY);
		this.length = length;
		this.orientation = orientation;

	}

	public Coordinates getCoordinates(){
		return this.pos; 
	}

	public void setCoordinates(int x, int y){
		pos.setCoordinates(x, y);
	}

	public int getLength(){
		return this.length;
	}

	public char getOrientation(){
		return this.orientation;
	}

}
