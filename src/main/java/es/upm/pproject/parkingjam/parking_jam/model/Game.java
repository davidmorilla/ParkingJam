package es.upm.pproject.parkingjam.parking_jam.model;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;

// Game es la clase central del model, gestionando los niveles y la puntuacion
public class Game {
	private int acumulatedScore;		// Puntuación conjunta de anteriores niveles del juego
	private int levelNumber;			// Número del nivel actual del juego
	private Level level;				// Nivel actual del juego
	
	public Game() throws IllegalExitsNumberException, IllegalCarDimensionException {
		acumulatedScore = 0;
		levelNumber = 0;
		loadNewLevel();	
	}
	
	public char[][] loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException {
		levelNumber++;
		char[][] board = new LevelReader().readMap(levelNumber);
		level = new Level(board, new LevelConverter().convertLevel(board));
		return board;
	}
	
	public char[][] moveCar(char car, int length, char way) throws SameMovementException {
		return level.moveCar(car, length, way);
	}

	public void undoMovement() throws CannotUndoMovementException {
		level.undoMovement();
	}
	
	public int getGameScore() {
		return level.getScore() + acumulatedScore;
	}
	
	public int getLevelScore() {
		return level.getScore();
	}
	
}
