package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.*;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MyMouseAdapter;

public class Grid extends JPanel {
    private int rows;
    private int cols;
    private int squareSize = 50;
    private Map<Character, Car> cars;
    private Controller controller;
    private char[][] board;

    public Grid(Pair<Integer, Integer> dimensions, Map<Character, Car> cars, char[][] board, Controller controller) {
        this.rows = dimensions.getLeft();
        this.cols = dimensions.getRight();
        this.cars = cars;
        this.board = board;
        this.controller = controller;
        this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));

        // Añadir el MouseAdapter para cada coche movible
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
            Car car = entry.getValue();
            MovableCar movableCar =  new MovableCar(car, rows, cols, squareSize, this);
            MyMouseAdapter mouseAdapter = new MyMouseAdapter(squareSize, movableCar, this);
            this.addMouseListener(mouseAdapter);
            this.addMouseMotionListener(mouseAdapter);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar el tablero
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char elem = getElemAt(board, j, i);
                if (elem == '+') {
                    g.setColor(Color.BLACK);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * squareSize, i * squareSize, squareSize, squareSize);
                } else if (elem == '@') {
                    g.setColor(Color.GREEN);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * squareSize, i * squareSize, squareSize, squareSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * squareSize, i * squareSize, squareSize, squareSize);
                }
            }
        }

        // Dibujar todos los coches
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
            char carKey = entry.getKey();
            Car car = entry.getValue();
            MovableCar movableCar = new MovableCar(car, rows, cols, squareSize, this);
            movableCar.draw(g);
        }
    }

    private char getElemAt(char[][] board, int row, int col) {
        return board[col][row];
    }

    public void setBoard(char [][] board){
        this.board = board;
    }

    public char [][] getBoard(){
        return this.board;
    }
    
    public char[][] moveCar(char car, int length, char way) throws SameMovementException {
  		return controller.moveCar(car, length, way);
  	}
}
