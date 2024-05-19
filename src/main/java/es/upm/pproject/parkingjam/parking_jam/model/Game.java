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
		logger.info("Creating new game...");
		acumulatedScore = 0;
		levelNumber = 0;
		loadNewLevel();
		logger.info("New game has been created.");
	}

	public Pair<Integer, Integer> getDimensions() {
        // We need to return the dimensions of the current level
		return level.getDimensions();
    }

	public char[][] getBoard() {
		return level.getBoard();
	}
		
	public char[][] loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException {
		logger.info("Loading level {}...", levelNumber);
		levelNumber++;
		acumulatedScore+= level !=null ? level.getScore() : 0;
		char[][] board = new LevelReader().readMap(levelNumber);
		level = new Level(board, new LevelConverter().convertLevel(board));
		logger.info("Level {} has been loaded.", levelNumber);
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
		logger.info("Getting level number...");
		logger.info("Level number has been given (levelNumber: {}).", levelNumber);
		return levelNumber;
	}

	public Level getLevel(){
		logger.info("Getting level...");
		logger.info("Level has been given.");
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
