package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;

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

    public MovableCar(Car car, int rows, int cols, int squareSize, Grid grid, Controller controller) {
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
    }

    public void drag(int dx, int dy) {
        char[][] newBoard = null;
        try {
            if (orientation == 'V') {
                newBoard = parent.moveCar(carSymbol, dy, dy > 0 ? 'D' : 'U');
                if (newBoard != null) {
                    row = Math.max(0, Math.min(rows - length, initialRow + dy));
                }
            } else {
                newBoard = parent.moveCar(carSymbol, dx, dx > 0 ? 'R' : 'L');
                if (newBoard != null) {
                    col = Math.max(0, Math.min(cols - length, initialCol + dx));
                }
            }
            if (newBoard != null) {
				for (int i = 0; i < newBoard.length; i++) {
					for (int j = 0; j < newBoard[i].length; j++) {
						System.out.print(newBoard[i][j]);
					}
					System.out.println();
				}
                parent.setCars(controller.getCars());
                parent.setBoard(newBoard);
                System.out.println("EN DRAG");
                parent.repaint(); // Repintar el panel después de mover el coche
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
}