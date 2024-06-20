package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.controller.ControllerInterface;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalDirectionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.InvalidMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.LevelAlreadyFinishedException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.MovementOutOfBoundariesException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.NullBoardException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.interfaces.IGrid;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MyMouseAdapter;

import java.awt.event.MouseAdapter;

/**
 * Represents the grid where the game is displayed, including cars, walls and exits.
 * Extends JPanel to provide graphical rendering capabilities.
 */
public class Grid extends JPanel implements IGrid {
	private int rows;
	private int cols;
	private int squareSize = 50;
	private transient Map<Character, Manager> movableCars; // Stores the drawable cars
	private transient ControllerInterface controller;
	private char[][] board;
	private boolean levelCompleted;
	private Map<Integer, String[]> carImages;

	private static final Logger logger = LoggerFactory.getLogger(Grid.class);

	private Random rnd = new Random();

	/**
	 * Constructor for Grid class.
	 * Initializes the grid with given dimensions, cars, board layout, controller, and main frame reference.
	 * @param dimensions Pair object representing grid dimensions (rows, columns)
	 * @param cars Map of characters to Car objects representing movable cars in the game
	 * @param board 2D char array representing the initial layout of the game board
	 * @param controller Controller object to handle game logic
	 */
	public Grid(Pair<Integer, Integer> dimensions, Map<Character, Car> cars, char[][] board, ControllerInterface controller,
			DataPanel dataPanel) {
		this.rows = dimensions.getLeft();
		this.cols = dimensions.getRight();
		this.board = board;
		this.controller = controller;
		this.levelCompleted = false;
		this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
		this.carImages = new HashMap<>();
		carImages.put(2, new String[] { "car2", "car4", "car7", "car9", "car10", "car11", "car12" });
		carImages.put(3, new String[] { "car3", "car5", "car6", "car8" });

		// Create drawable cars equivalent instances (MovableCar) and store them
		setCarsMap(cars,dataPanel);

		// Add a mouse adapter for all the grid
		MouseAdapter mouseAdapter = new MyMouseAdapter(this);
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
	}

	/**
	 * Retrieves the movable car at a specific point in the grid
	 * @param point Point object representing the coordinates of the point
	 * @return MovableCar object at the specified point, or null if no car is found
	 */
	public MovableCar getMovableCarAt(Point point) {
		for (Manager car : movableCars.values()) {

			if (car.getMovableCar().contains(point)) {
				return car.getMovableCar();
			}
		}
		return null;
	}

	/**
	 * Paint the board and the cars, plus the level completed message on top if level is finished
	 * 
	 * @param g the graphics component used to draw
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Paint the board
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				switch(board[i][j]) {
				case '+': // Paint wall
					paintWall(g,i,j);
					break;
				case '@': { // Paint exit
					paintExit(g,i,j);
					break;
				}
				default: { // Paint gray the cell
					g.setColor(Color.GRAY);
					g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
				}
				}
			}
		}

		// Paint all cars
		for (Manager movableCar : movableCars.values()) {
			movableCar.getMovableCar().draw(g);
		}

		if (isLevelCompleted()) {
			// Show the level completed message
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 30));
			String message = "LEVEL COMPLETED";
			int textWidth = g.getFontMetrics().stringWidth(message);
			int textHeight = g.getFontMetrics().getHeight();
			int x = (cols * squareSize - textWidth) / 2;
			int y = (rows * squareSize + textHeight) / 2;
			g.setColor(Color.BLUE);
			g.fillRect(x-15,y-40, 345, 55);
			g.setColor(Color.cyan);
			g.fillRect(x-10,y-35, 335, 45);
			g.setColor(Color.BLACK);
			g.drawString(message, x, y);
		}
	}

	/**
	 * Sets the dimensions of the board of the current level
	 * 
	 * @param dimensions the dimensions of the level
	 */
	public void setDimensions(Pair<Integer, Integer> dimensions){
		this.rows = dimensions.getLeft();
		this.cols = dimensions.getRight();
	}

	/**
	 * Paints an exit in the coordinates x,y given
	 * 
	 * @param g the graphics component used to draw
	 * @param x horizontal coordinate of the cell
	 * @param y vertical coordinate of the cell
	 */
	private void paintExit(Graphics g, int x, int y) {
		String image = "exits/exit%s.png";
		levelCompleted = false;

		if (x == 0)
			image = String.format(image, "Top");
		else if (x == rows - 1)
			image = String.format(image, "Bottom");
		else if (y == 0)
			image = String.format(image, "Left");
		else if (y == cols - 1)
			image = String.format(image, "Right");
		else
			return ;

		try {
			BufferedImage imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
			g.drawImage(imageToDraw, y * squareSize, x * squareSize, squareSize, squareSize, null);
		} catch (IOException e) {
			logger.error("ERROR: Cannot load exit image");
		}
	}

	/**
	 * Paints a wall in the coordinates x,y given
	 * 
	 * @param g the graphics component used to draw
	 * @param x horizontal coordinate of the cell
	 * @param y vertical coordinate of the cell
	 */
	private void paintWall(Graphics g, int x, int y) {
		String image = "wall_%s.png";

		if(y == 0 && (x == 0 || x == rows - 1))
			image = String.format(image, "left_corners");
		else if (y == cols - 1 && (x == 0 || x == rows - 1))
			image = String.format(image, "right_corners");
		else if (y == 0 || y == cols - 1)
			image = String.format(image, "vertical");
		else
			image = String.format(image, "horizontal");

		try {
			g.drawImage(ImageIO.read(getClass().getClassLoader().getResourceAsStream(image)), y * squareSize, x * squareSize,
					squareSize, squareSize, this);
		} catch (IOException e) {
			logger.error("ERROR: Cannot load wall images");
		}
	}

	/**
	 * Gets the board
	 * @return the board
	 */
	public char[][] getBoard() {
		return this.board;
	}

	/**
	 * Sets the board to a new one
	 * @param board the new board
	 */
	public void setBoard(char[][] board) {
		this.board = board;
	}

	/**
	 * Update the position of all the cars in the map
	 * 
	 * @param cars a map of cars
	 */
	public void setCars(Map<Character, Car> cars) {
		for (Map.Entry<Character, Car> entry : cars.entrySet()) {
			Manager movableCar = movableCars.get(entry.getKey());
			if (movableCar != null) {
				movableCar.getMovableCar().updatePosition(entry.getValue().getCoordinates());
			}
		}
	}

	/**
	 * Create drawable cars equivalent instances (MovableCar) and store them
	 * 
	 * @param cars map of cars to create their drawable instances
	 * @param dataPanel the data panel necessary to create the movable cars
	 */
	public final void setCarsMap(Map<Character, Car> cars, DataPanel dataPanel) {
		this.movableCars = new HashMap<>();

		for (Map.Entry<Character, Car> entry : cars.entrySet()) {
			Car car = entry.getValue();
			String[] imagePaths = carImages.get(car.getLength());
			String imagePath;
			if (imagePaths == null) { // if null means that the length is more than 3
				imagePath = "longCar";
			} else {
				imagePath = imagePaths[rnd.nextInt(imagePaths.length)];
			}
			MovableCar movableCar = new MovableCar(car, new Pair<>(rows, cols), squareSize, controller, dataPanel, imagePath);
			movableCars.put(entry.getKey(), new Manager(movableCar));
		}
	}

	/**
	 * Triest to move the car. If successful, returns the new board result of the movement
	 * 
	 * @param car the car symbol, which represents the car that will try to move
	 * @param length the length of the car
	 * @param way the orientation of the car, vertical or horizontal
	 * @return the new board if the movement was successful
     * @throws IllegalDirectionException if the direction specified is not valid. 
     * @throws LevelAlreadyFinishedException if the user attempts to make a movement after the level has finished.
     * @throws NullBoardException if the board is null.
     * @throws MovementOutOfBoundariesException if the movement attempted is out of boundaries
     * @throws InvalidMovementException if the movement is invalid
     * @throws IllegalCarException if the car does not exist
	 */
	public char[][] moveCar(char car, int length, char way) throws IllegalDirectionException, LevelAlreadyFinishedException, InvalidMovementException, MovementOutOfBoundariesException, IllegalCarException, NullBoardException {
		return controller.moveCar(car, length, way);
	}

	/**
	 * Returns whether or not the level is completed
	 * 
	 * @return true when @ symbol is not found, false otherwhise
	 */
	public boolean isLevelCompleted() {
		levelCompleted = true;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				char elem = board[i][j];
				if (elem == '@')
					levelCompleted = false;
			}
		}
		return this.levelCompleted;
	}

	/**
	 * Return the square size for each tile on the grid
	 * 
	 * @return square size
	 */
	public int getSquareSize() {
		return squareSize;
	}
}