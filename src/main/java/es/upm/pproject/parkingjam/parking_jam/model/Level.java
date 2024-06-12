package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.*;

public class Level {
	private int score, oldScore; // Puntuación del nivel
	private Map<Character, Car> cars, carsDefault; // Coches del tablero
	private char[][] board, boardDefault; // Mapa con la representación de los vehículos con letras
	private Pair<Integer, Integer> dimensions;
	private Stack<OldBoardData> history;
	private GameSaver gameSaver;

	private static final Logger logger = LoggerFactory.getLogger(Game.class);

	public Level(char[][] board, Map<Character, Car> cars, GameSaver gs) {
		logger.info("Creating level...");
		gameSaver = gs;

		this.board = deepCopy(board); // Copia profunda del tablero inicial
		this.cars = deepCopyCars(cars); // Copia profunda de los coches iniciales
		this.history = new Stack<>();
		this.boardDefault = deepCopy(board); // Guardar una copia profunda del estado inicial
		this.carsDefault = deepCopyCars(cars); // Guardar una copia profunda del estado inicial de los coches
		int numRows = board.length;
		int numCols = board[0].length;
		dimensions = new Pair<>(numRows, numCols);

		logger.info("Level has been created: \n{}.", charMatrixToString(this.boardDefault));
	}

	private String charMatrixToString(char[][] board) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				sb.append(board[i][j]).append(' ');
			}
			sb.append('\n'); // New line after each row
		}
		return sb.toString();
	}

	private void increaseScore() {
		logger.info("Increasing score...");
		logger.info("Score has been increased by 1 unit (new score: {}).", score + 1);
		score++;
		// gameSaver.saveBoard(board);
	}

	private void decreaseScore() {
		logger.info("Decreasing score...");
		logger.info("Score has been decreased by 1 unit(new score: {}).", score - 1);
		score--;
		// gameSaver.saveBoard(board);
	}

	private void addToHistory() {
		Map<Character, Car> carsCopy = deepCopyCars(cars); // Copia profunda de los coches
		char[][] boardCopy = deepCopy(board); // Copia profunda del tablero
		OldBoardData copy = new OldBoardData(boardCopy, carsCopy);

		history.push(copy);
	}

	private Map<Character, Car> deepCopyCars(Map<Character, Car> original) {
		Map<Character, Car> copy = new HashMap<>();
		for (Map.Entry<Character, Car> entry : original.entrySet()) {
			Car car = entry.getValue();
			Car carCopy = new Car(car.getSymbol(), car.getCoordinates().getX(), car.getCoordinates().getY(),
					car.getLength(), car.getOrientation());
			copy.put(entry.getKey(), carCopy);
		}
		return copy;
	}

	public Pair<Integer, Integer> getDimensions() {
		logger.info("Getting dimensions...");
		logger.info("Dimensions have been given (dimensions: {}x{}).", dimensions.getLeft(), dimensions.getRight());
		return dimensions;
	}

	public char[][] getBoard() {
		logger.info("Getting board...");
		logger.info("Board has been given: \n{}", charMatrixToString(this.board));
		return this.board;
	}

	public int getScore() {
		logger.info("Getting score...");
		logger.info("Score has been given (score: {}).", score);
		return score;
	}

	public OldBoardData undoMovement() throws CannotUndoMovementException {
		logger.info("Undoing movement...");
		if (!history.isEmpty() && !this.isLevelFinished(board)) {

			OldBoardData restoredBoard = history.pop();
			this.board = deepCopy(restoredBoard.getBoard());
			this.cars = deepCopyCars(restoredBoard.getCars());
			decreaseScore();
			if (score == -1) {
				System.out.println("---------------------------- " + oldScore);
				score = oldScore;
			}
			logger.info("Movement has been undone.");
			return restoredBoard;
		} else {
			logger.error("There are no movements to undo because no movement has been done before.");
			throw new CannotUndoMovementException();
		}
	}

	// Moves the car in the way and the length specified when its possible and
	// uploads the current board,
	// returns the new board or null if the car does not exist or its not possible
	// to move the car to the specified possition.
	public char[][] moveCar(char car, int length, char way) throws SameMovementException {
		logger.info("Moving car '{}'...", car);
		if (isLevelFinished(board)) {
			logger.warn("Cannot move car '{}', level is finished", car);
			return null;
		}
		addToHistory();
		char[][] newBoard = deepCopy(board); // Hacer una copia profunda de la matriz original
		Coordinates coord = null;
		int xCar = 0;
		int yCar = 0;

		if (cars.get(car) == null) {
			logger.error("Car '{}' does not exist.", car);
			return null;
		}
		if (way == 'L' || way == 'R' || way == 'U' || way == 'D') {
			xCar = cars.get(car).getCoordinates().getX();
			yCar = cars.get(car).getCoordinates().getY();

			switch (way) {
				// Left
				case 'L':
					logger.info("Moving car '{}' {} units left...", car, length);
					coord = new Coordinates(xCar - Math.abs(length), yCar);
					break;
				// Right
				case 'R':
					logger.info("Moving car '{}' {} units right...", car, length);
					coord = new Coordinates(xCar + Math.abs(length), yCar);
					break;
				// Up
				case 'U':
					logger.info("Moving car '{}' {} units up...", car, length);
					coord = new Coordinates(xCar, yCar - Math.abs(length));
					break;
				// Down
				case 'D':
					logger.info("Moving car '{}' {} units down...", car, length);
					coord = new Coordinates(xCar, yCar + Math.abs(length));
					break;
			}
			// Verificar si las nuevas coordenadas están dentro de los límites del tablero
			if (coord.getX() >= 1 && coord.getX() < board[0].length - 1 && coord.getY() >= 1
					&& coord.getY() < board.length - 1) {
				if (checkMovementValidity(car, coord, way)) {
					try {
						deleteCar(car, newBoard, cars);
						addCar(car, newBoard, cars, coord);
						this.cars.get(car).setCoordinates(coord.getX(), coord.getY());

						// Add the old map at the top of the stack
						board = newBoard; // No es necesario actualizar la matriz original
						increaseScore();

					} catch (IllegalCarException e) {

						e.printStackTrace();
					}
				} else {
					if (this.isLevelFinished(newBoard)) {
						logger.warn("Cannot move car '{}', level is finished", car);
					} else {
						logger.warn("Cannot move car '{}', there's an obstacle", car);
					}
					this.history.pop();
					return null;
				}

			} else {
				// this.history.pop();
				logger.warn("Cannot move car '{}', new movement is out of reach.", car);
				return null;
			}
		} else {
			logger.warn("Invalid movement orientation.");
			return null;
		}
		logger.info("Car '{}' has been moved.", car);
		return newBoard;
	}

	private char[][] deepCopy(char[][] original) {
		if (original == null) {
			return null;
		}

		final char[][] result = new char[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = original[i].clone();
		}
		return result;
	}

	public void resetLevel() {
		logger.info("Resetting level...");
		oldScore = score;
		addToHistory();
		this.score = 0;
		this.board = deepCopy(boardDefault); // Restaurar la copia profunda del tablero inicial
		this.cars = deepCopyCars(carsDefault); // Restaurar la copia profunda de los coches iniciales
		logger.info("Level has been reset.");
		// this.updateGameSaved();
	}

	private void deleteCar(char car, char[][] board, Map<Character, Car> cars) throws IllegalCarException {
		logger.info("Deleting car '{}'...", car);
		int xCar = 0;
		int yCar = 0;
		if (cars.get(car) == null) {
			logger.error("Car '{}' does not exist.", car);
			throw new IllegalCarException();
		}

		xCar = cars.get(car).getCoordinates().getX();
		yCar = cars.get(car).getCoordinates().getY();
		for (int i = 1; i <= cars.get(car).getLength(); i++) {
			if (cars.get(car).getOrientation() == 'H')
				board[yCar][xCar + (i - 1)] = ' ';
			else
				board[yCar + (i - 1)][xCar] = ' ';
		}
		logger.info("Car '{}' has been deleted.", car);
	}

	private void addCar(char car, char[][] board, Map<Character, Car> cars, Coordinates coord)
			throws IllegalCarException {
		logger.info("Adding car '{}'...", car);
		int xCar = 0;
		int yCar = 0;
		if (cars.get(car) == null) {
			logger.error("Car '{}' does not exist.", car);
			throw new IllegalCarException();
		}

		xCar = coord.getX();
		yCar = coord.getY();
		for (int i = 1; i <= cars.get(car).getLength(); i++) {
			if (cars.get(car).getOrientation() == 'H')
				board[yCar][xCar + (i - 1)] = car;
			else
				board[yCar + (i - 1)][xCar] = car;
		}
		logger.info("Car '{}' has been added.", car);
	}

	public boolean checkMovementValidity(char carChar, Coordinates newCoordinates, char way)
			throws SameMovementException {
		logger.info("Checking movement validity (car: '{}', x: {}, y: {}, way: '{}') ...", carChar,
				newCoordinates.getX(), newCoordinates.getY(), way);

		Car car = cars.get(carChar);
		Coordinates carCoordinates = car.getCoordinates();
		int carLength = car.getLength();
		char carOrientation = car.getOrientation();
		int boardWidth = board[0].length;
		int boardHeight = board.length;

		// Verificar si las nuevas coordenadas están dentro de los límites del tablero
		if (newCoordinates.getX() < 0 || newCoordinates.getX() >= boardWidth || newCoordinates.getY() < 0
				|| newCoordinates.getY() >= boardHeight) {
			logger.warn("Invalid movement: Trying to move out of board limits.");

			return false;
		}

		// Verificar si las coordenadas del coche están dentro del tablero
		if (carOrientation == 'V') {
			if (newCoordinates.getY() < 0 || newCoordinates.getY() + carLength > boardHeight
					|| newCoordinates.getX() != carCoordinates.getX()) {
				logger.warn(
						"Invalid movement: Trying to move out of board limits or make a horizontal move in an vertical car.");
				return false;
			}
		} else { // carOrientation == 'H'
			if (newCoordinates.getX() < 0 || newCoordinates.getX() + carLength > boardWidth
					|| newCoordinates.getY() != carCoordinates.getY()) {
				logger.warn(
						"Invalid movement: Trying to move out of board limits or make a vertical move in an horizontal car.");
				return false;
			}
		}

		// Verificar todas las celdas a lo largo del camino del movimiento
		if (carOrientation == 'V') {
			int startY = Math.min(carCoordinates.getY(), newCoordinates.getY());
			int endY = Math.max(carCoordinates.getY() + carLength, newCoordinates.getY() + carLength);
			for (int y = startY; y < endY; y++) {
				if (board[y][carCoordinates.getX()] != ' ' && board[y][carCoordinates.getX()] != carChar
						&& board[y][carCoordinates.getX()] != '@') {
					logger.warn("Invalid movement: Trying to move to a not valid cell.");
					return false;
				}
			}
		} else { // carOrientation == 'H'
			int startX = Math.min(carCoordinates.getX(), newCoordinates.getX());
			int endX = Math.max(carCoordinates.getX() + carLength, newCoordinates.getX() + carLength);
			for (int x = startX; x < endX; x++) {
				if (board[carCoordinates.getY()][x] != ' ' && board[carCoordinates.getY()][x] != carChar
						&& board[carCoordinates.getY()][x] != '@') {
					logger.warn("Invalid movement: Trying to move to a not valid cell.");
					return false;
				}
			}
		}
		logger.info("Valid movement.");
		return true;
	}

	public Map<Character, Car> getCars() {
		logger.info("Getting all cars...");
		logger.info("All cars have been given (cars: {} ).", cars.keySet().toString());
		return this.cars;
	}

	public boolean isLevelFinished(char[][] board) {
		boolean res = true;
		for (int i = 0; i < board.length && res; i++) {
			for (int j = 0; j < board[i].length && res; j++) {
				char elem = board[i][j];
				if (elem == '@') {
					res = false;
				}
			}
		}
		return res;
	}

	public void updateGameSaved() {
		this.gameSaver.saveBoard(this.board);
	}

	public void setPunctuation(int score) {
		this.score = score;
	}

	public void setOldScore(int score){
		this.oldScore =score;
	}

	public int getOldScore(){
		return this.oldScore;
	}

	public void resetOriginalLevel(int levelNumber) {
		logger.info("Resetting original level...");
		
		addToHistory();
		LevelReader lr = new LevelReader();
		char[][] board = lr.readMap(levelNumber, false);
		try {

			this.board = deepCopy(board); // Copia profunda del tablero inicial
			this.cars = deepCopyCars(new LevelConverter().convertLevel(board)); // Copia profunda de los coches
																				// iniciales

			this.boardDefault = deepCopy(board); // Guardar una copia profunda del estado inicial
			this.carsDefault = deepCopyCars(new LevelConverter().convertLevel(board)); // Guardar una copia profunda del
																						// estado inicial de los coches
			
		} catch (IllegalExitsNumberException | IllegalCarDimensionException e) {
			e.printStackTrace();
		}
		logger.info("Level {} has been resetted.", levelNumber);

	}
}
