package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MyMouseAdapter;

public class Grid extends JPanel {
    private int rows;
    private int cols;
    private int squareSize = 50;
    private Map<Character, MovableCar> movableCars; // Almacenar las instancias de MovableCar
    private Controller controller;
    private char[][] board;
    private boolean levelCompleted;
    private MainFrame mf;

    private static final Logger logger = LoggerFactory.getLogger(Grid.class);
    
    public Grid(Pair<Integer, Integer> dimensions, Map<Character, Car> cars, char[][] board, Controller controller,
            MainFrame mf) {
        this.rows = dimensions.getLeft();
        this.cols = dimensions.getRight();
        this.board = board;
        this.controller = controller;
        this.levelCompleted = false;
        this.mf = mf;
        this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
        this.movableCars = new HashMap<>();

        // Crear instancias de MovableCar y almacenarlas en el mapa
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
            Car car = entry.getValue();
            MovableCar movableCar = new MovableCar(car, rows, cols, squareSize, this, controller, this.mf);
            movableCars.put(entry.getKey(), movableCar);
            logger.info("Created the car {}",car.getSymbol());
        }

        // Añadir un solo MouseAdapter para toda la cuadrícula
        MyMouseAdapter mouseAdapter = new MyMouseAdapter(squareSize, this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    // Método para obtener el coche en un punto específico
    public MovableCar getMovableCarAt(Point point) {
        for (MovableCar car : movableCars.values()) {
            if (car.contains(point)) {
                return car;
            }
        }
        return null;
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
                } else if (elem == '@') {
                    levelCompleted = false;
                    g.setColor(Color.WHITE);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                }
            }
            logger.info("Board drawn.");
        }

        // Dibujar todos los coches movibles
        for (MovableCar movableCar : movableCars.values()) {
            movableCar.draw(g);
            logger.info("Car {} drawn.", movableCar.getSymbol());
        }

        if (isLevelCompleted()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30)); // Cambiar la fuente y el tamaño del texto
            g.drawString("LEVEL COMPLETED", (cols * squareSize) / 2 - 100, (rows * squareSize) / 2);
        }

    }

    private char getElemAt(char[][] board, int row, int col) {
        return board[col][row];
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public char[][] getBoard() {
        return this.board;
    }

    public void setCars(Map<Character, Car> cars) {
        // Actualizar las instancias de MovableCar
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
            MovableCar movableCar = movableCars.get(entry.getKey());
            if (movableCar != null) {
                movableCar.updateCar(entry.getValue());
            }
        }
    }

    public void setCarsMap(Map<Character, Car> cars) {
        // Actualizar las instancias de MovableCar
        this.movableCars = new HashMap<>();
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
            Car car = entry.getValue();
            MovableCar movableCar = new MovableCar(car, rows, cols, squareSize, this, controller, this.mf);
            movableCars.put(entry.getKey(), movableCar);
        }
    }

    public char[][] moveCar(char car, int length, char way) throws SameMovementException {
        return controller.moveCar(car, length, way);
    }

    public boolean isLevelCompleted() {
        levelCompleted = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char elem = getElemAt(board, j, i);
                if (elem == '@')
                    levelCompleted = false;
            }
        }
        return this.levelCompleted;
    }

}