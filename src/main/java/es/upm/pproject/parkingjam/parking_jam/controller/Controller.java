package es.upm.pproject.parkingjam.parking_jam.controller;

import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.GameSaver;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.utilities.OldBoardData;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.MainFrame;

public class Controller {
	private MainFrame mframe; 
	private Game game; 
	private GameSaver gameSaver;
	
	public Controller() throws IllegalExitsNumberException, IllegalCarDimensionException {
		game = new Game();
		mframe = new MainFrame(this);
		gameSaver = new GameSaver();
	}
	
	public void loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException {
		game.loadNewLevel();
		
	}

	public int loadSavedLevel() throws IllegalExitsNumberException, IllegalCarDimensionException {
		return game.loadSavedLevel();
		
	}
	
	public char[][] moveCar(char car, int length, char way) throws SameMovementException {
		return game.moveCar(car, length, way);
	}
	
	public OldBoardData undoMovement() throws CannotUndoMovementException {
		
		return game.undoMovement();
	}
	
	public char[][] getBoard(){
		return game.getBoard();
	}

	public Pair<Integer, Integer> getBoardDimensions() {
        // This will return the board dimensions of the current level
		return game.getDimensions();
    }
	
	public Map<Character,Car> getCars(){
		return game.getCars();
	}
	
	public int getGameScore() {
		return game.getGameScore();
	}
	
	public int getLevelScore() {
		return game.getLevelScore();
	}

	public int getLevelNumber() {
		return game.getLevelNumber();
	}	

	public void resetLevel(){
		game.resetLevel();
	}
	
	public boolean isMoveValid(char car, Coordinates newCoord, char way) throws SameMovementException {
		return game.getLevel().checkMovementValidity(car, newCoord, way);
	}

	public boolean isLevelFinished(){
		return game.getLevel().isLevelFinished(this.getBoard()); 
	}

	public void setPunctuation(int score) {
		game.getLevel().setPunctuation(score);
	}

	public void saveGame() {
		gameSaver.savePunctuation(game.getGameScore(), this.getLevelScore());
		game.getLevel().updateGameSaved();
	}

	public void setGameScore(int totalPoints) {
		game.setGameScore(totalPoints);
	}

	public void resetOriginalLevel() {
		int newGamePunctuation = game.getAccumulatedScore();
		this.setGameScore(newGamePunctuation);
		game.getLevel().setOldScore(game.getLevelScore());
		
		game.setLevelScore(0);
		System.out.println(game.getLevel().getOldScore());
		game.resetOriginalLevel();
	}

	public void loadLevel(int levelNumber) {
		game.loadLevel(levelNumber);
	}
	
}
