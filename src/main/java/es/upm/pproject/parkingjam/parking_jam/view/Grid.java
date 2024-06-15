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
import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalDirectionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MyMouseAdapter;

/**
 * Represents the grid where the game is displayed, including cars, walls, and exits.
 * Extends JPanel to provide graphical rendering capabilities.
 */
public class Grid extends JPanel {
	private int rows;
	private int cols;
	private int squareSize = 50;
	private Map<Character, MovableCar> movableCars; // Stores the drawable cars
	private Controller controller;
	private char[][] board;
	private boolean levelCompleted;
	private MainFrame mf;
	private Map<Integer, String[]> carImages;

	/**
	 * Constructor for Grid class.
	 * Initializes the grid with given dimensions, cars, board layout, controller, and main frame reference.
	 * @param dimensions Pair object representing grid dimensions (rows, columns)
	 * @param cars Map of characters to Car objects representing movable cars in the game
	 * @param board 2D char array representing the initial layout of the game board
	 * @param controller Controller object to handle game logic
	 * @param mf MainFrame object representing the main application frame
	 */
	public Grid(Pair<Integer, Integer> dimensions, Map<Character, Car> cars, char[][] board, Controller controller,
			MainFrame mf) {
		this.rows = dimensions.getLeft();
		this.cols = dimensions.getRight();
		this.board = board;
		this.controller = controller;
		this.levelCompleted = false;
		this.mf = mf;
		this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
		this.carImages = new HashMap<>();
		carImages.put(2, new String[] { "car2", "car4", "car7", "car9", "car10", "car11", "car12" });
		carImages.put(3, new String[] { "car3", "car5", "car6", "car8" });

		// Create drawable cars equivalent instances (MovableCar) and store them
		setCarsMap(cars);

		// Add a mouse adapter for all the grid
		MyMouseAdapter mouseAdapter = new MyMouseAdapter(squareSize, this);
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
	}

	/**
	 * Retrieves the movable car at a specific point in the grid
	 * @param point Point object representing the coordinates of the point
	 * @return MovableCar object at the specified point, or null if no car is found
	 */
	public MovableCar getMovableCarAt(Point point) {
		for (MovableCar car : movableCars.values()) {
			if (car.contains(point)) {
				return car;
			}
		}
		return null;
	}

	/**
	 * Paint the board and the cars, plus the level completed message on top if level is finished
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		HashMap<String,BufferedImage> wallTypeMap = new HashMap<>();
		try {
			wallTypeMap.put("topLeft", ImageIO.read(getClass().getClassLoader().getResourceAsStream("wall_left_corners.png")));
			wallTypeMap.put("topRight", ImageIO.read(getClass().getClassLoader().getResourceAsStream("wall_right_corners.png")));
			wallTypeMap.put("bottomLeft", ImageIO.read(getClass().getClassLoader().getResourceAsStream("wall_left_corners.png")));
			wallTypeMap.put("bottomRight", ImageIO.read(getClass().getClassLoader().getResourceAsStream("wall_right_corners.png")));
			wallTypeMap.put("vertical", ImageIO.read(getClass().getClassLoader().getResourceAsStream("wall_vertical.png")));
			wallTypeMap.put("horizontal", ImageIO.read(getClass().getClassLoader().getResourceAsStream("wall_horizontal.png")));
		} catch (IOException e) {
		}

		// Paint the board
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				char elem = board[i][j];
				if (elem == '+') {
					String cornerType = getCornerType(j, i);
					switch (cornerType) {
					case "topLeft": // If wall is top left corner
						g.drawImage(wallTypeMap.get(cornerType), j * squareSize, i * squareSize, squareSize, squareSize, null);
						break;
					case "topRight": // If wall is top right corner
						g.drawImage(wallTypeMap.get(cornerType), j * squareSize, i * squareSize, squareSize, squareSize, null);
						break;
					case "bottomLeft": // If wall is bottom left corner
						g.drawImage(wallTypeMap.get(cornerType), j * squareSize, i * squareSize, squareSize, squareSize, null);
						break;
					case "bottomRight": // If wall is bottom right corner
						g.drawImage(wallTypeMap.get(cornerType), j * squareSize, i * squareSize, squareSize, squareSize, null);
						break;
					default:
						if (j == 0 || j == cols - 1) { // If wall is vertical
							g.drawImage(wallTypeMap.get("vertical"), j * squareSize, i * squareSize, squareSize, squareSize, this);
						} else { // If wall is horizontal
							g.drawImage(wallTypeMap.get("horizontal"), j * squareSize, i * squareSize, squareSize, squareSize, this);
						}
					}
				} else if (elem == '@') {
					levelCompleted = false;
					// Paint wall if on exit side
					String side = getExitSide(j, i);
					if(side != null) {
						try {
						BufferedImage imageToDraw = ImageIO.read(
								getClass().getClassLoader().getResourceAsStream("exits/exit" + side + ".png"));
						
						g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize, null);
						} catch (IOException e) {
						}
					}
				} else {
					// Paint gray the cell
					g.setColor(Color.GRAY);
					g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
				}
			}
		}

		// Paint all cars
		for (MovableCar movableCar : movableCars.values()) {
			movableCar.draw(g);
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
			g.fillRect(x-15,y-40, 325, 55);
			g.setColor(Color.cyan);
			g.fillRect(x-10,y-35, 315, 45);
			g.setColor(Color.BLACK);
			g.drawString(message, x, y);
		}
	}

	/**
	 * Returns which type of corner is the cell given
	 * 
	 * @param x horizontal coordinate of the cell
	 * @param y vertical coordinate of the cell
	 * @return bottomLeft, bottomRight, topLeft or topRight depending on which side is the cell
	 */
	private String getCornerType(int x, int y) {
		if (x == 0 && y == 0) {
			return "topLeft";
		} else if (x == cols - 1 && y == 0) {
			return "topRight";
		} else if (x == 0 && y == rows - 1) {
			return "bottomLeft";
		} else if (x == cols - 1 && y == rows - 1) {
			return "bottomRight";
		} else {
			return "";
		}
	}

	/**
	 * Returns in which side of the board is the exit
	 * 
	 * @param x horizontal coordinate of the exit
	 * @param y vertical coordinate of the exit
	 * @return Top, Bottom, Left or Right depending on which side is the exit, null if not on a side
	 */
	private String getExitSide(int x, int y) {
		if (y == 0) {
			return "Top";
		} else if (y == rows - 1) {
			return "Bottom";
		} else if (x == 0) {
			return "Left";
		} else if (x == cols - 1) {
			return "Right";
		} else {
			return null;
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
			MovableCar movableCar = movableCars.get(entry.getKey());
			if (movableCar != null) {
				movableCar.updatePosition(entry.getValue().getCoordinates());
			}
		}
	}

	/**
	 * Create drawable cars equivalent instances (MovableCar) and store them
	 * 
	 * @param cars map of cars to create their drawable instances
	 */
	public void setCarsMap(Map<Character, Car> cars) {
		this.movableCars = new HashMap<>();

		for (Map.Entry<Character, Car> entry : cars.entrySet()) {
			Car car = entry.getValue();
			String[] imagePaths = carImages.get(car.getLength());
			String imagePath;
			if (imagePaths == null) { // if null means that the length is more than 3
				imagePath = "longCar";
			} else {
				imagePath = imagePaths[new Random().nextInt(imagePaths.length)];
			}
			MovableCar movableCar = new MovableCar(car, rows, cols, squareSize, this, controller, this.mf, imagePath);
			movableCars.put(entry.getKey(), movableCar);
		}
	}

	/**
	 * Triest to move the car. If successful, returns the new board result of the movement
	 * 
	 * @param car the car symbol, which represents the car that will try to move
	 * @param length the length of the car
	 * @param way the orientation of the car, vertical or horizontal
	 * @return the new board if the movement was successful
	 * @throws SameMovementException if the movement is to the same place.
	 * @throws IllegalDirectionException 
	 */
	public char[][] moveCar(char car, int length, char way) throws SameMovementException, IllegalDirectionException {
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

}