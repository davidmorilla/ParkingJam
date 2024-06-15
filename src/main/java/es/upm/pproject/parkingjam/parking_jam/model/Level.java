package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.*;

/**
 * Represents a level in the Parking Jam game.
 * A level contains a board, cars, a score, and supports various operations such
 * as moving cars, undoing movements, and resetting the level.
 */
public class Level {
	private int score; // Score of the level
	private int oldScore;
	private Map<Character, Car> cars; // Map of chat to 'Car' instances on the board
	private Map<Character, Car> carsDefault;
	private char[][] board; // 2D matrix representation of the board
	private char[][] boardDefault;
	private Pair<Integer, Integer> dimensions;
	private List<Pair<Character, Pair<Integer, Character>>> history;
	private GameSaver gameSaver;
	private String msgLog;
	private char[][] previousBoard;
	private Map<Character, Car> previousCars;
	private int previousScore;
	private List<Pair<Character, Pair<Integer, Character>>> previousHistory;

	private static final Logger logger = LoggerFactory.getLogger(Level.class);

	/**
	 * Creates a new level with the given board and cars.
	 * 
	 * @param board the initial board configuration.
	 * @param cars  the initial set of cars on the board.
	 * @param gs    the game saver utility for saving game states.
	 */
	public Level(char[][] board, Map<Character, Car> cars, GameSaver gs) {
		logger.info("Creating level...");
        gameSaver = gs;

        this.board = deepCopy(board); // Copia profunda del tablero inicial
        this.cars = deepCopyCars(cars); // Copia profunda de los coches iniciales
        this.history = new ArrayList<>();
        this.boardDefault = deepCopy(board); // Guardar una copia profunda del estado inicial
        this.carsDefault = deepCopyCars(cars); // Guardar una copia profunda del estado inicial de los coches
        int numRows = board.length;
        int numCols = board[0].length;
        dimensions = new Pair<>(numRows, numCols);

        msgLog = "Level has been created: \n" + charMatrixToString(this.boardDefault);
        logger.info(msgLog);
	}

	/**
	 * Converts a char matrix to a string representation.
	 * 
	 * @param board the char matrix to convert.
	 * @return the string representation of the board.
	 */
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

	/**
	 * Retrieves the history of board states.
	 * 
	 * @return the history stack list.
	 */
	public List<Pair<Character, Pair<Integer, Character>>> getHistory() {
		logger.info("Getting history...");
		logger.info("The history has been given.");
		return this.history;
	}

	/**
	 * Increases the score by 1.
	 */
	private void increaseScore() {
		logger.info("Increasing score...");
		logger.info("Score has been increased by 1 unit (new score: {}).", score + 1);
		score++;
	}

	/**
	 * Decreases the score by 1.
	 */
	private void decreaseScore() {
		logger.info("Decreasing score...");
		logger.info("Score has been decreased by 1 unit(new score: {}).", score - 1);
		score--;
	}

	/**
	 * Creates a deep copy of the given car map.
	 * 
	 * @param original the original car map.
	 * @return a deep copy of the car map.
	 */
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

	/**
	 * Retrieves the dimensions of the board.
	 * 
	 * @return the dimensions of the board.
	 */
	public Pair<Integer, Integer> getDimensions() {
		logger.info("Getting dimensions...");
		logger.info("Dimensions have been given (dimensions: {}x{}).", dimensions.getLeft(), dimensions.getRight());
		return dimensions;
	}

	/**
	 * Retrieves the current board state.
	 * 
	 * @return the current board.
	 */
	public char[][] getBoard() {
		logger.info("Getting board...");
		msgLog = "Board has been given: \n" + charMatrixToString(this.board);
		logger.info(msgLog);
		return this.board;
	}

	/**
	 * Retrieves the current score.
	 * 
	 * @return the current score.
	 */
	public int getScore() {
		logger.info("Getting score...");
		logger.info("Score has been given (score: {}).", score);
		return score;
	}

	/**
	 * Sets the previous score to the value score
	 * 
	 * @param score
	 */
	public void setPreviousScore(int score){
		logger.info("Setting previous score = {}...", score);
		logger.info("The previous score = {} has been given...", score);
		previousScore = score;
	}

	/**
	 * Undoes the last movement and restores the previous board state.
	 * 
	 * @return the restored board state.
	 * @throws CannotUndoMovementException if there are no movements to undo.
	 * @throws SameMovementException       if the movement is to the same position.
	 */
	public char[][] undoMovement() throws CannotUndoMovementException, SameMovementException {
		logger.info("Undoing movement...");
        if (!history.isEmpty() && !this.isLevelFinished(board)) {
            Pair<Character, Pair<Integer, Character>> mov = history.get(history.size() - 1);
            history.remove(history.size() - 1);

            char[][] boardAfterUndo = moveCar(mov.getLeft(), mov.getRight().getLeft(), mov.getRight().getRight(), true);
            decreaseScore();
            if (score == -1) {
                score = oldScore;
            }
            logger.info("Movement has been undone.");
            return boardAfterUndo;
        } else if (previousBoard != null) {
            // Restaurar el estado anterior si el último cambio fue un reset
            logger.info("Restoring previous state before reset...");
            board = deepCopy(previousBoard);
            cars = deepCopyCars(previousCars);
            score = previousScore;
            history = new ArrayList<>(previousHistory);

            // Limpiar los estados guardados para evitar restauraciones múltiples no deseadas
            previousBoard = null;
            previousCars = null;
            previousScore = 0;
            previousHistory = null;

            logger.info("Previous state has been restored.");
            return board;
        } else {
            logger.error("There are no movements to undo because no movement has been done before.");
            throw new CannotUndoMovementException();
        }
	}
	

	/**
	 * Moves the specified car in the specified direction and updates the board
	 * state.
	 * 
	 * @param car    the symbol of the car to move.
	 * @param length the number of tiles to move.
	 * @param way    the direction to move ('L', 'R', 'U', 'D').
	 * @param undo   boolean indicating whether the movement is a new movement or an
	 *               undo movement.
	 * @return the new board state or null if the move is not possible.
	 * @throws SameMovementException if the same movement is repeated.
	 */
	public char[][] moveCar(char car, int length, char way, boolean undo) throws SameMovementException {
		logger.info("Moving car '{}'...", car);

		// Check if the level is already finished
		if (isLevelFinished(board)) {
			logger.warn("Cannot move car '{}', level is finished", car);
			return null;
		}
		if (!undo) { // If it's not an undo movement we must save the movement to the history list
			char undoWay = opposite(way);
			Pair<Character, Pair<Integer, Character>> mov = new Pair<>(car,
					new Pair<>(length, undoWay));
			// Save the current movement to history for undo functionality
			history.add(mov);
		}

		char[][] newBoard = deepCopy(board);// Create a deep copy of the original board
		Coordinates coord = null; // Variable to store the new coordinates after moving the car
		int xCar = 0; // X-coordinate of the car
		int yCar = 0; // Y-coordinate of the car

		// Check if the car exists in the game
		if (cars.get(car) == null) {
			msgLog = "Car " + car + " does not exist.";
			logger.error(msgLog);
			return null;
		}

		// Validate the direction of movement
		if (way == 'L' || way == 'R' || way == 'U' || way == 'D') {
			xCar = cars.get(car).getCoordinates().getX();
			yCar = cars.get(car).getCoordinates().getY();

			// Determine the new coordinates based on the direction and length of the move
			switch (way) {
				case 'L': // Move left
					logger.info("Moving car '{}' {} units left...", car, length);
					coord = new Coordinates(xCar - Math.abs(length), yCar);
					break;
				case 'R': // Move right
					logger.info("Moving car '{}' {} units right...", car, length);
					coord = new Coordinates(xCar + Math.abs(length), yCar);
					break;
				case 'U': // Move up
					logger.info("Moving car '{}' {} units up...", car, length);
					coord = new Coordinates(xCar, yCar - Math.abs(length));
					break;
				case 'D': // Move down
					logger.info("Moving car '{}' {} units down...", car, length);
					coord = new Coordinates(xCar, yCar + Math.abs(length));
					break;
			}
			// Check if the new coordinates are within the board boundaries
			if (coord.getX() >= 1 && coord.getX() < board[0].length - 1 && coord.getY() >= 1
					&& coord.getY() < board.length - 1) {

				// Check if the movement is valid (no obstacles, etc.)
				if (checkMovementValidity(car, coord, way)) {
					try {
						deleteCar(car, newBoard, cars); // Remove the car from the current position
						addCar(car, newBoard, cars, coord); // Add the car to the new position
						this.cars.get(car).setCoordinates(coord.getX(), coord.getY()); // Update the car's coordinates

						// Update the board with the new state
						board = newBoard;

						// Increase the score for the valid move if it's not an undo movement
						if (!undo)
							increaseScore();

					} catch (IllegalCarException e) {
						msgLog = "Car " + car + " does not exist.";
						logger.error(msgLog);
						e.printStackTrace();
					}
				} else {
					// Handle invalid movement due to obstacles or if the level is finished
					if (this.isLevelFinished(newBoard)) {
						logger.warn("Cannot move car '{}', level is finished", car);
					} else {
						logger.warn("Cannot move car '{}', there's an obstacle", car);
					}
					this.history.remove(history.size() - 1);
					return null;
				}

			} else {
				// Handle movement that goes out of board boundaries
				logger.warn("Cannot move car '{}', new movement is out of reach.", car);
				return null;
			}
		} else {
			// Handle invalid movement direction
			logger.warn("Invalid movement orientation.");
			return null;
		}
		logger.info("Car '{}' has been moved.", car);
		return newBoard; // Return the new board state after the move
	}

	/**
	 * Returns the opposite direction of the specified direction.
	 * 
	 * @param way the direction for which the opposite is needed ('L' for left, 'R'
	 *            for right, 'U' for up, 'D' for down).
	 * @return the opposite direction ('R' for 'L', 'L' for 'R', 'D' for 'U', 'U'
	 *         for 'D').
	 */
	private char opposite(char way) {
		if (way == 'L')
			return 'R'; // Left -> Right
		if (way == 'R')
			return 'L'; // Right -> Left
		if (way == 'U')
			return 'D'; // Up -> Down
		return 'U'; // Down -> Up (default if way is 'D')
	}

	/**
	 * Creates a deep copy of the given char matrix.
	 * 
	 * @param original the original char matrix.
	 * @return a deep copy of the char matrix.
	 */
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

	/**
	 * Resets the level to its initial state.
	 * The score is reset to 0, the board and cars are restored to their default
	 * states.
	 */
	public void resetLevel() {
		logger.info("Resetting level...");
	
		// Guardar el estado actual antes de resetear
		previousBoard = deepCopy(board);
		previousCars = deepCopyCars(cars);
		previousScore = score;
		previousHistory = new ArrayList<>(history);
	
		oldScore = score;
		history = new ArrayList<>();
		this.score = 0;
		this.board = deepCopy(boardDefault); // Restores the deep copy of the initial board
		this.cars = deepCopyCars(carsDefault); //Restores the deep copy of the initial cars
	
		logger.info("Level has been reset.");
	}
	

	/**
	 * Deletes a car from the board.
	 * 
	 * @param car   the symbol of the car to delete.
	 * @param board the current board state.
	 * @param cars  the current map of cars.
	 * @throws IllegalCarException if the car does not exist on the board.
	 */
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

	/**
	 * Adds a car to the board at the specified coordinates.
	 * 
	 * @param car   the symbol of the car to add.
	 * @param board the current board state.
	 * @param cars  the current map of cars.
	 * @param coord the coordinates where the car should be added.
	 * @throws IllegalCarException if the car does not exist or the placement is
	 *                             invalid.
	 */
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

	/**
	 * Checks if the movement of a car to new coordinates in a specified direction
	 * is valid.
	 * 
	 * @param carChar        the symbol of the car to move.
	 * @param newCoordinates the new coordinates for the car.
	 * @param way            the direction of the movement ('L', 'R', 'U', 'D').
	 * @return true if the movement is valid, false otherwise.
	 * @throws SameMovementException if the movement is invalid due to repeating the
	 *                               same movement.
	 */
	public boolean checkMovementValidity(char carChar, Coordinates newCoordinates, char way)
			throws SameMovementException {
		logger.info("Checking movement validity (car: '{}', x: {}, y: {}, way: '{}') ...", carChar,
				newCoordinates.getX(), newCoordinates.getY(), way);

		// Retrieve car details
		Car car = cars.get(carChar);
		Coordinates carCoordinates = car.getCoordinates();
		int carLength = car.getLength();
		char carOrientation = car.getOrientation();
		int boardWidth = board[0].length;
		int boardHeight = board.length;

		// Check if the new coordinates are within the board limits
		if (newCoordinates.getX() < 0 || newCoordinates.getX() >= boardWidth || newCoordinates.getY() < 0
				|| newCoordinates.getY() >= boardHeight) {
			logger.warn("Invalid movement: Trying to move out of board limits.");

			return false;
		}

		// Check if the car's movement is valid within the board dimensions
		if (carOrientation == 'V') { // Car is oriented vertically
			// Check if the car stays within the vertical bounds and doesn't move
			// horizontally
			if (newCoordinates.getY() < 0 || newCoordinates.getY() + carLength > boardHeight
					|| newCoordinates.getX() != carCoordinates.getX()) {
				logger.warn(
						"Invalid movement: Trying to move out of board limits or make a horizontal move in an vertical car.");
				return false;
			}
		} else { // Car is oriented horizontally
			// Check if the car stays within the horizontal bounds and doesn't move
			// vertically
			if (newCoordinates.getX() < 0 || newCoordinates.getX() + carLength > boardWidth
					|| newCoordinates.getY() != carCoordinates.getY()) {
				logger.warn(
						"Invalid movement: Trying to move out of board limits or make a vertical move in an horizontal car.");
				return false;
			}
		}

		// Verify all cells along the path of the movement
		if (carOrientation == 'V') { // Car is oriented vertically
			int startY = Math.min(carCoordinates.getY(), newCoordinates.getY());
			int endY = Math.max(carCoordinates.getY() + carLength, newCoordinates.getY() + carLength);
			for (int y = startY; y < endY; y++) {
				// Check if the cell is empty or occupied by the car itself or an exit '@'
				if (board[y][carCoordinates.getX()] != ' ' && board[y][carCoordinates.getX()] != carChar
						&& board[y][carCoordinates.getX()] != '@') {
					logger.warn("Invalid movement: Trying to move to an invalid cell.");
					return false;
				}
			}
		} else { // Car is oriented horizontally
			int startX = Math.min(carCoordinates.getX(), newCoordinates.getX());
			int endX = Math.max(carCoordinates.getX() + carLength, newCoordinates.getX() + carLength);
			for (int x = startX; x < endX; x++) {
				// Check if the cell is empty or occupied by the car itself or an exit '@'
				if (board[carCoordinates.getY()][x] != ' ' && board[carCoordinates.getY()][x] != carChar
						&& board[carCoordinates.getY()][x] != '@') {
					logger.warn("Invalid movement: Trying to move to an invalid cell.");
					return false;
				}
			}
		}
		// If all checks pass, the movement is valid
		logger.info("Valid movement.");
		return true;
	}

	/**
	 * Retrieves the map of cars.
	 * 
	 * @return the current map of cars.
	 */
	public Map<Character, Car> getCars() {
		logger.info("Getting all cars...");
		msgLog = "All cars have been given (cars: " + cars.keySet().toString() + ").";
		logger.info(msgLog);
		return this.cars;
	}

	/**
	 * Checks if the level is finished.
	 * 
	 * @param board the current board state.
	 * @return true if the level is finished, false otherwise.
	 */
	public boolean isLevelFinished(char[][] board) {
		logger.info("Getting finishing state of level...");
		boolean res = true;
		for (int i = 0; i < board.length && res; i++) {
			for (int j = 0; j < board[i].length && res; j++) {
				char elem = board[i][j];
				if (elem == '@') {
					res = false;
				}
			}
		}
		logger.info("Finishing state has been obtained. Level is finished: {}", res);
		return res;
	}

	/**
	 * Sets the current score.
	 * 
	 * @param score the new score to set.
	 */
	public void setPunctuation(int score) {
		logger.info("Setting level score = {}...", score);
		this.score = score;
		logger.info("Level score = {} has been set.", score);
	}

	/**
	 * Sets the old score.
	 * 
	 * @param score the new old score to set.
	 */
	public void setOldScore(int score) {
		logger.info("Setting old level score = {}...", score);
		this.oldScore = score;
		logger.info("Level old score = {} has been set.", score);
	}

	/**
	 * Retrieves the old score.
	 * 
	 * @return the old score.
	 */
	public int getOldScore() {
		logger.info("Getting old level score...");
		logger.info("Level old score = {} has been obtained.", oldScore);
		return this.oldScore;
	}

	/**
	 * Resets the original level to its initial state based on the specified level
	 * number.
	 * 
	 * @param levelNumber the level number to reset.
	 */
	public void resetOriginalLevel(int levelNumber) {
		logger.info("Resetting original level...");

		previousBoard = deepCopy(board);
		previousCars = deepCopyCars(cars);
		previousScore = score;
		previousHistory = new ArrayList<>(history);

		oldScore = score;
		history = new ArrayList<>();
		LevelReader lr = new LevelReader();
		char[][] board = lr.readMap(levelNumber, false);
		try {

			this.board = deepCopy(board); // deep copy of the board
			this.cars = deepCopyCars(new LevelConverter().convertLevel(board)); // deep copy of the cars

			this.boardDefault = deepCopy(board); // deep copy of the initial state
			this.carsDefault = deepCopyCars(new LevelConverter().convertLevel(board)); // deep copy of the initial cars

		} catch (IllegalExitsNumberException | IllegalCarDimensionException e) {
			logger.error("There was an error resetting the original level: {}", e.getLocalizedMessage());
			e.printStackTrace();
		}
		logger.info("Original level {} has been resetted.", levelNumber);

	}

	/**
	 * Sets the movement history.
	 * 
	 * @param history the new list representing the movement history.
	 */
	public void setHistory(List<Pair<Character, Pair<Integer, Character>>> history) {
		logger.info("Setting history ...");
		this.history = history;
		logger.info("History has been set.");
	}

	/**
	 * Retrieves the 'GameSaver' instance used to save the game state.
	 * 
	 * @return the 'GameSaver' instance.
	 */
	public GameSaver getGameSaver() {
		logger.info("Getting GameSaver...");
		logger.info("GameSaver has been obtained.");
		return this.gameSaver;
	}
}
