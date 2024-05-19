package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.OldBoardData;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

// Game es la clase central del model, gestionando los niveles y la puntuacion
public class Game {
	private int acumulatedScore;		// Puntuación conjunta de anteriores niveles del juego
	private int levelNumber;			// Número del nivel actual del juego
	private Level level;				// Nivel actual del juego
	
	private static final Logger logger = LoggerFactory.getLogger(Game.class);
	
	public Game() throws IllegalExitsNumberException, IllegalCarDimensionException {
		acumulatedScore = 0;
		levelNumber = 0;
		loadNewLevel();	
	}

	public Pair<Integer, Integer> getDimensions() {
        // We need to return the dimensions of the current level
		return level.getDimensions();
    }

	public char[][] getBoard() {
		return level.getBoard();
	}
		
	public char[][] loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException {
		levelNumber++;
		acumulatedScore+= level !=null ? level.getScore() : 0;
		char[][] board = new LevelReader().readMap(levelNumber);
		level = new Level(board, new LevelConverter().convertLevel(board));
		logger.info("Load level {}", levelNumber);
		return board;
	}
	
	public char[][] moveCar(char car, int length, char way) throws SameMovementException {
		return level.moveCar(car, length, way);
	}

	public OldBoardData undoMovement() throws CannotUndoMovementException {
		return level.undoMovement();
	}
	
	public int getGameScore() {
		return level.getScore() + acumulatedScore;
	}
	
	public int getLevelScore() {
		return level.getScore();
	}
	
	public int getLevelNumber() {
		return levelNumber;
	}

	public Level getLevel(){
		return level;
	}

	public Map<Character,Car> getCars(){
		return level.getCars();
	}

	public void resetLevel(){
		level.resetLevel();
	}

	public void increaseScore(){
		level.increaseScore();
	}
	
}
