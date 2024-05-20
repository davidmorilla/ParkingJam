package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;

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
	private char orientation, carSymbol;
	private int length;
	private Controller controller;
	private MainFrame mf;
	private boolean dragging;
	
	private static final Logger logger = LoggerFactory.getLogger(Grid.class);

	public MovableCar(Car car, int rows, int cols, int squareSize, Grid grid, Controller controller, MainFrame mf) {
		this.parent = grid;
		this.carSymbol = car.getSymbol();
		this.row = car.getCoordinates().getY();
		this.col = car.getCoordinates().getX();
		this.color = chooseColor(carSymbol);
		this.rows = rows;
		this.cols = cols;
		this.squareSize = squareSize;
		this.orientation = car.getOrientation();
		this.length = car.getLength();
		this.controller = controller;
		this.mf = mf;
	}

	public void updateCar(Car car) {
		this.row = car.getCoordinates().getY();
		this.col = car.getCoordinates().getX();
		this.orientation = car.getOrientation();
		this.length = car.getLength();
	}

	public void draw(Graphics g) {
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

	public boolean contains(Point p) {
		if (orientation == 'V') {
			return p.x >= col * squareSize && p.x < (col + 1) * squareSize &&
					p.y >= row * squareSize && p.y < (row + length) * squareSize;
		} else {
			return p.x >= col * squareSize && p.x < (col + length) * squareSize &&
					p.y >= row * squareSize && p.y < (row + 1) * squareSize;
		}
	}

	public void startDrag() {
		initialCol = col;
		initialRow = row;
		dragging = true;
	}

	public void endDrag() {
		dragging = false;
		int dx = col - initialCol;
		int dy = row - initialRow;
		if (dx != 0 || dy != 0) {
			moveCar(dx, dy);
		}
	}

	public void drag(int dx, int dy) {
		if (!dragging) return;

		int newRow = row;
		int newCol = col;

		try {
			if (orientation == 'V') {
				newRow = Math.max(0, Math.min(rows - length, initialRow + dy));
			} else {
				newCol = Math.max(0, Math.min(cols - length, initialCol + dx));
			}
			
			if (!controller.isLevelFinished() && controller.isMoveValid(carSymbol, new Coordinates(newCol, newRow), orientation == 'V' ? (dy > 0 ? 'D' : 'U') : (dx > 0 ? 'R' : 'L'))) {
				row = newRow;
				col = newCol;
				parent.repaint(); // Repintar solo si el movimiento es válido
			}
			else{
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void moveCar(int dx, int dy) {
		try {
			char[][] newBoard = null;
			if (orientation == 'V') {
				newBoard = parent.moveCar(carSymbol, Math.abs(dy), dy > 0 ? 'D' : 'U');
			} else {
				newBoard = parent.moveCar(carSymbol, Math.abs(dx), dx > 0 ? 'R' : 'L');
			}

			if (newBoard != null) {
				logger.info("Car {} moved.", carSymbol);
				mf.increaseScore();
				parent.setCars(controller.getCars());
				parent.setBoard(newBoard);
				parent.repaint(); // Repintar el panel después de mover el coche en el modelo
				for(int i = 0; i<newBoard.length; i++){
					for(int j = 0; j<newBoard[i].length; j++){
						System.out.print(newBoard[i][j]);
					}
					System.out.println();
				}
			}

			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static Color chooseColor(char c) {
		if (c == '*')
			return Color.RED;
		else if (c >= 'a' && c <= 'z') {
			switch ((c - 97) % 9) {
			case 0:
				return Color.PINK;
			case 1:
				return Color.BLUE;
			case 2:
				return Color.GREEN;
			case 3:
				return Color.ORANGE;
			case 4:
				return Color.GRAY;
			case 5:
				return Color.CYAN;
			case 6:
				return Color.YELLOW;
			case 7:
				return Color.MAGENTA;
			case 8:
				return Color.DARK_GRAY;
			}
		}
		return null;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public int getSymbol() {
		return carSymbol;
	}
}