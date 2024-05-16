package es.upm.pproject.parkingjam.parking_jam.controller;

import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.view.MainFrame;

public class Controller {
	private MainFrame mframe; 
	private Game game; 
	
	public Controller(MainFrame view, Game model) throws IllegalExitsNumberException, IllegalCarDimensionException {
		mframe = view;
		game = model;
	}
	
	
	public char[][] loadNewLevel() throws IllegalExitsNumberException, IllegalCarDimensionException {
		return game.loadNewLevel();
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
	
}
