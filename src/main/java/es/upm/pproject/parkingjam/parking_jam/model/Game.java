package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

// Game es la clase central del model, gestionando los niveles y la puntuacion
public class Game {
    private int acumulatedScore;        // Puntuación conjunta de anteriores niveles del juego
    private int levelNumber;            // Número del nivel actual del juego
    private Level level;                // Nivel actual del juego
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    
    public Game(int numLevel) throws IllegalExitsNumberException, IllegalCarDimensionException {
    	logger.info("Creating new game...");
        acumulatedScore = 0;
        levelNumber = numLevel - 1;
        loadNewLevel();    
        logger.info("New game has been created.");
    }
    
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

    public int loadSavedLevel(GameSaver gs) throws IllegalExitsNumberException, IllegalCarDimensionException {
        logger.info("Loading saved level...");
        LevelReader lr = new LevelReader();
        char[][] board = lr.readMap(levelNumber, true);
        levelNumber = lr.getLevelNumber(); // Obtener el número de nivel del LevelReader
		
        level = new Level(board, new LevelConverter().convertLevel(board), lr.getGameSaver());
        int cols = level.getDimensions().getRight();
        int rows = level.getDimensions().getLeft();
        List<Pair<Character, Pair<Integer,Character>>> history = gs.loadHistory(cols, rows);
        level.setHistory(history);
        System.out.println(history.size());
        
        logger.info("Saved level {} has been loaded.", levelNumber);
        return levelNumber;
    }

    public int loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException {
        logger.info("Loading new level...");
        levelNumber++;
        acumulatedScore += level != null ? level.getScore() : 0;
        LevelReader lr = new LevelReader();
        char[][] board = lr.readMap(levelNumber, false);
        level = new Level(board, new LevelConverter().convertLevel(board), lr.getGameSaver());
        logger.info("New level {} has been loaded.", levelNumber);
        return levelNumber;
    }

    public char[][] moveCar(char car, int length, char way) throws SameMovementException {
        return level.moveCar(car, length, way, false);
    }

    public char[][] undoMovement() throws CannotUndoMovementException, SameMovementException {
        return level.undoMovement();
    }

    public int getGameScore() {
        int score = level.getScore();
        return  score + acumulatedScore;
    }

    public int getAccumulatedScore() {
        
        return  acumulatedScore;
    }

    public int getLevelScore() {
        return level.getScore();
    }

    public int getLevelNumber() {
        logger.info("Getting level number...");
        logger.info("Level number has been given (levelNumber: {}).", levelNumber);
        return levelNumber;
    }

    public Level getLevel() {
        logger.info("Getting level...");
        logger.info("Level has been given.");
        return level;
    }

    public Map<Character, Car> getCars() {
        return level.getCars();
    }

    public void resetLevel() {
        level.resetLevel();
    }

    public void setGameScore(int totalPoints) {
        this.acumulatedScore = totalPoints;
    }

    public void resetOriginalLevel() {
        level.resetOriginalLevel(levelNumber );
        
    }

    public void setLevelScore(int score) {
        level.setPunctuation(score);
    }

    public void loadLevel(int levelNumber) {
        logger.info("Loading level "+levelNumber+"...");
        
        LevelReader lr = new LevelReader();
        char[][] board = lr.readMap(levelNumber, false);
        try {
            level = new Level(board, new LevelConverter().convertLevel(board), lr.getGameSaver());
        } catch (IllegalExitsNumberException | IllegalCarDimensionException e) {
            e.printStackTrace();
        }
        logger.info("New level {} has been loaded.", levelNumber);
    }
    
    public GameSaver getGameSaver() {
    	return level.getGameSaver();
    }

    public void saveGame(GameSaver gameSaver) {
    	gameSaver.saveGame(level.getHistory(), this.getGameScore(), this.getLevelScore(), getBoard());
    }
}
