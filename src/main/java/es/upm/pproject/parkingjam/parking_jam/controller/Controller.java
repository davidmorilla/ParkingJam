package es.upm.pproject.parkingjam.parking_jam.controller;

import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.Grid;
import es.upm.pproject.parkingjam.parking_jam.view.MainFrame;

public class Controller {
	private MainFrame mframe; 
	private Game game; 
	
	public Controller() throws IllegalExitsNumberException, IllegalCarDimensionException {
		game = new Game(this);
		mframe = new MainFrame(this);
	}
	
	
	public char[][] loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException {
		return game.loadNewLevel();
	}

	public Pair<Integer, Integer> getDimensions() {
        // This will return the dimensions of the current level
		return game.getDimensions();
    }
	
	public char[][] moveCar(char car, int length, char way) throws SameMovementException {
		return game.moveCar(car, length, way);
	}
	
	public void undoMovement() throws CannotUndoMovementException {
		game.undoMovement();
	}
	
	public int getGameScore() {
		return game.getGameScore();
	}
	
	public int getLevelScore() {
		return game.getLevelScore();
	}

	public Map<Character,Car> getCars(){
		return game.getCars();
	}

	public char[][] getBoard(){
		System.out.println("\nEN CONTROLLER\n");
		for(int i = 0; i<game.getBoard().length; i++){
			for(int j = 0; j<game.getBoard()[i].length; j++){
				System.out.print(game.getBoard()[i][j]);
			}
			System.out.println();
		}
		return game.getBoard();
	}

	public char[][] move(char car,int length , char way) throws SameMovementException{
		return game.moveCar(car, length, way);
	}

	
	
}
