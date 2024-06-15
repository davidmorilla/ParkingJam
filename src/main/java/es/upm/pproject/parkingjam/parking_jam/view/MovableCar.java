package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.LevelConverter;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalDirectionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

/**
 * MovableCar represents a car that can be moved on the board.
 * This class manages its graphical representation and the logic of movements
 */
public class MovableCar {
	private Grid parent;
	private int row;
	private int col;
	private int rows;
	private int cols;
	private int squareSize;
	private Color color;
	private int initialCol; // Columna inicial al empezar a arrastrar
	private int initialRow; // Fila inicial al empezar a arrastrar
	private char orientation;
	private char carSymbol;
	private int length;
	private Controller controller;
	private MainFrame mf;
	private boolean dragging;
	private BufferedImage horizontalCarImage;
	private BufferedImage verticalCarImage;
    private static final Logger logger = LoggerFactory.getLogger(MovableCar.class);


	/**
	 * MovableCar's Constructor
	 *
	 * @param car        	The Car object that represents this car
	 * @param rows       	Number of rows in the board
	 * @param cols       	Number of columns in the board
	 * @param squareSize 	Size of each cell in the board
	 * @param grid       	The panel where the car is drawn
	 * @param controller 	The game controller
	 * @param mf         	The mainframe of the application
	 * @param image      	Name of the car's image
	 */
	public MovableCar(Car car, Pair<Integer,Integer> dimensions, int squareSize, Grid grid, Controller controller, MainFrame mf, String image) {
		this.parent = grid;
		this.carSymbol = car.getSymbol();
		this.row = car.getCoordinates().getY();
		this.col = car.getCoordinates().getX();
		this.rows = dimensions.getLeft();
		this.cols = dimensions.getRight();
		this.squareSize = squareSize;
		this.orientation = car.getOrientation();
		this.length = car.getLength();
		this.controller = controller;
		this.mf = mf;

		// Load car's image
		if(car.getSymbol() == '*') {
			loadImage("car1");
		}
		else {
			loadImage(image);

		}
	}
	
	/**
	 * Returns the current column (horizontal) position of the car
	 * 
	 * @return car's horizontal position
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Returns the current row (vertical) position of the car
	 * 
	 * @return car's vertical position
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Update the position of the car
	 *
	 * @param coordinates the new position x,y of the car
	 */
	public void updatePosition(Coordinates coordinates) {
		this.row = coordinates.getY();
		this.col = coordinates.getX();
	}

	/**
	 * Draw the car on the given graphical component given
	 *
	 * @param g The graphical componente in which to draw this car
	 */
	public void draw(Graphics g) {
		BufferedImage imageToDraw = (orientation == 'V') ? verticalCarImage : horizontalCarImage;
		if (imageToDraw != null) {
			// If image found, paint it
			if (orientation == 'V') {
				g.drawImage(imageToDraw, col * squareSize, row * squareSize, squareSize, length * squareSize, null);
			} else {
				g.drawImage(imageToDraw, col * squareSize, row * squareSize, length * squareSize, squareSize, null);
			}
		}
		else {
			// If image was not found, paint the car with its color
			g.setColor(color);
			if (orientation == 'V') {
				g.fillRect(col * squareSize, row * squareSize, squareSize, length * squareSize);
				g.setColor(Color.BLACK);
				g.drawRect(col * squareSize, row * squareSize, squareSize, length * squareSize);
			} else {
				g.fillRect(col * squareSize, row * squareSize, length * squareSize, squareSize);
				g.setColor(Color.BLACK);
				g.drawRect(col * squareSize, row * squareSize, length * squareSize, squareSize);
			}
		}
	}

	/**
	 * Verify if a given point is inside the car's area
	 *      
	 * @param p The point to verify
	 * @return true if the point is insde the car's area, false otherwise
	 */
	public boolean contains(Point p) {
		if (orientation == 'V') {
			return p.x >= col * squareSize && p.x < (col + 1) * squareSize &&
					p.y >= row * squareSize && p.y < (row + length) * squareSize;
		} else {
			return p.x >= col * squareSize && p.x < (col + length) * squareSize &&
					p.y >= row * squareSize && p.y < (row + 1) * squareSize;
		}
	}

	/**
	 * Initiates the dragging process
	 */
	public void startDrag() {
		initialCol = col;
		initialRow = row;
		dragging = true;
	}

	/**
	 * Ends the dragging process and moves the car if it has been dragged
	 */
	public void endDrag() {
		dragging = false;
		int dx = col - initialCol;
		int dy = row - initialRow;
		if (dx != 0 || dy != 0) {
			moveCar(dx, dy);
		}
	}

	/**
	 * Drag the car to a new position
	 *
	 * @param dx X axis offset
	 * @param dy Y axis offset
	 */
	public void drag(int dx, int dy) {
		if (!dragging) return;

		// Calculate the new row and column for the car
		int newRow = row;
		int newCol = col;
		
		if (orientation == 'V') {
			newRow = Math.max(0, Math.min(rows - length, initialRow + dy));
		} else {
			newCol = Math.max(0, Math.min(cols - length, initialCol + dx));
		}
		
		try {
			// Determine the direction based on orientation and movement
		    char direction;
		    if (orientation == 'V') {
		        direction = (dy > 0) ? 'D' : 'U';
		    } else {
		        direction = (dx > 0) ? 'R' : 'L';
		    }
			// Update to the new position and repaint only if level is not finished and the move is valid
			if (!controller.isLevelFinished() && controller.isMoveValid(carSymbol, new Coordinates(newCol, newRow), direction)) {
				row = newRow;
				col = newCol;
				parent.repaint();
			}
		} catch (Exception e) {
			logger.error("ERROR: The position of the car could not be updated and repainted");
		}
	}

	/**
	 * Move the car con the board and update its graphical representation
	 *
	 * @param dx X axis offset
	 * @param dy Y axis offset
	 */
	private void moveCar(int dx, int dy) {
		try {
			// Try to move the car and the new board
			char[][] newBoard = null;
			if (orientation == 'V') {
				newBoard = parent.moveCar(carSymbol, Math.abs(dy), dy > 0 ? 'D' : 'U');
			} else {
				newBoard = parent.moveCar(carSymbol, Math.abs(dx), dx > 0 ? 'R' : 'L');
			}

			// If succesful moving the car, update the graphical representation and data
			if (newBoard != null) {
				mf.increaseScore();
				parent.setCars(controller.getCars());
				parent.setBoard(newBoard);
				// Repaint the board after moving the car on the model
				parent.repaint();
			}
		} catch (SameMovementException | IllegalDirectionException e) {
			logger.error("ERROR: The movement of the car is the same or the direction is not valid");
		}
	}
	
	/**
	 * Load the car's image from resources folders
	 *
	 * @param image Name of car's image
	 */
	private void loadImage(String image) {
		try {
			// Path for such resources
			String horizontalPath = "cars/" + image + "_horizontal.png";
			String verticalPath = "cars/" + image + "_vertical.png";
			horizontalCarImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(horizontalPath));
			verticalCarImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(verticalPath));
		} catch (IOException e) {
			logger.error("ERROR: Could not load car image");
		}
	}
}
