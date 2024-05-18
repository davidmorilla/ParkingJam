package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.*;

public class Level {
    private int score, oldScore; // Puntuación del nivel
    private Map<Character, Car> cars, carsDefault; // Coches del tablero
    private char[][] board, boardDefault; // Mapa con la representación de los vehículos con letras
    private Pair<Integer, Integer> dimensions;
    private Stack<OldBoardData> history;

    public Level(char[][] board, Map<Character, Car> cars) {
        this.score = 0;
        this.board = deepCopy(board); // Copia profunda del tablero inicial
        this.cars = deepCopyCars(cars); // Copia profunda de los coches iniciales
        this.history = new Stack<>();
        this.boardDefault = deepCopy(board); // Guardar una copia profunda del estado inicial
        this.carsDefault = deepCopyCars(cars); // Guardar una copia profunda del estado inicial de los coches
        int numRows = board.length;
        int numCols = board[0].length;
        dimensions = new Pair<>(numRows, numCols);
    }

    private void increaseScore() {
        score++;
    }

    private void decreaseScore() {
        score--;
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
        return dimensions;
    }

    public char[][] getBoard() {
        return this.board;
    }

    public int getScore() {
        return score;
    }

    public OldBoardData undoMovement() throws CannotUndoMovementException {
        if( !history.isEmpty() /*&& !this.isLevelFinished(board)*/){

            OldBoardData restoredBoard = history.pop();
            this.board = deepCopy(restoredBoard.getBoard());
            this.cars = deepCopyCars(restoredBoard.getCars());
            decreaseScore();
            if(score == -1){
                score = oldScore;
            }
            return restoredBoard;
        }
        else{
            throw new CannotUndoMovementException();
        }
    }

    // Moves the car in the way and the length specified when its possible and
    // uploads the current board,
    // returns the new board or null if the car does not exist or its not possible
    // to move the car to the specified possition.
    public char[][] moveCar(char car, int length, char way) throws SameMovementException {
        addToHistory();
        char[][] newBoard = deepCopy(board); // Hacer una copia profunda de la matriz original
        Coordinates coord = null;
        int xCar = 0;
        int yCar = 0;
        if (cars.get(car) == null)
            return null;
        if (way == 'L' || way == 'R' || way == 'U' || way == 'D') {
            xCar = cars.get(car).getCoordinates().getX();
            yCar = cars.get(car).getCoordinates().getY();

            switch (way) {
                // Left
                case 'L':
                    coord = new Coordinates(xCar - Math.abs(length), yCar);
                    break;
                // Right
                case 'R':
                    coord = new Coordinates(xCar + Math.abs(length), yCar);
                    break;
                // Up
                case 'U':
                    coord = new Coordinates(xCar, yCar - Math.abs(length));
                    break;
                // Down
                case 'D':
                    coord = new Coordinates(xCar, yCar + Math.abs(length));
                    break;
            }

            // Verificar si las nuevas coordenadas están dentro de los límites del tablero
            if (coord.getX() >= 1 && coord.getX() < board[0].length - 1 && coord.getY() >= 1
                    && coord.getY() < board.length - 1) {

                if (checkMovementValidity(car, coord, way) && !this.isLevelFinished(newBoard)) {
                    try {
                        deleteCar(car, newBoard, cars);
                        addCar(car, newBoard, cars, coord);
                        this.cars.get(car).setCoordinates(coord.getX(), coord.getY());
                        /* System.out.println("COORD COCHE " + car + " X:" + this.cars.get(car).getCoordinates().getX()
                                + " Y: " + this.cars.get(car).getCoordinates().getY() + "   Longitud: "
                                + this.cars.get(car).getLength()); */

                        // Add the old map at the top of the stack

                        increaseScore();
                        board = newBoard; // No es necesario actualizar la matriz original
                    } catch (IllegalCarException e) {
                        e.printStackTrace();
                    }
                } else {
                    this.history.pop();
                    return null;
                }
            }
            else{
                return null;
            }
        }
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
        oldScore = score;
        addToHistory();
        this.score = 0;
        this.board = deepCopy(boardDefault); // Restaurar la copia profunda del tablero inicial
        this.cars = deepCopyCars(carsDefault); // Restaurar la copia profunda de los coches iniciales
    }

    private void deleteCar(char car, char[][] board, Map<Character, Car> cars) throws IllegalCarException {
        int xCar = 0;
        int yCar = 0;
        if (cars.get(car) == null)
            throw new IllegalCarException();

        xCar = cars.get(car).getCoordinates().getX();
        yCar = cars.get(car).getCoordinates().getY();
        for (int i = 1; i <= cars.get(car).getLength(); i++) {
            if (cars.get(car).getOrientation() == 'H')
                board[yCar][xCar + (i - 1)] = ' ';
            else
                board[yCar + (i - 1)][xCar] = ' ';
        }
    }

    private void addCar(char car, char[][] board, Map<Character, Car> cars, Coordinates coord)
            throws IllegalCarException {
        int xCar = 0;
        int yCar = 0;
        if (cars.get(car) == null)
            throw new IllegalCarException();

        xCar = coord.getX();
        yCar = coord.getY();
        for (int i = 1; i <= cars.get(car).getLength(); i++) {
            if (cars.get(car).getOrientation() == 'H')
                board[yCar][xCar + (i - 1)] = car;
            else
                board[yCar + (i - 1)][xCar] = car;
        }
    }

    private boolean checkMovementValidity(char carChar, Coordinates newCoordinates, char way)
            throws SameMovementException {
        Car car = cars.get(carChar);
        Coordinates carCoordinates = car.getCoordinates();
        int carLength = car.getLength();
        char carOrientation = car.getOrientation();
        int boardWidth = board[0].length;
        int boardHeight = board.length;

        /*
         * if (carCoordinates.getX() == newCoordinates.getX() && carCoordinates.getY()
         * == newCoordinates.getY())
         * throw new SameMovementException();
         */

        if (carOrientation == 'V') {
            if (carCoordinates.getX() != newCoordinates.getX() || newCoordinates.getY() < 0
                    || newCoordinates.getY() + carLength > boardHeight) {
                return false;
            }

            int currentY = carCoordinates.getY();

            if (way == 'U' && board[currentY - 1][carCoordinates.getX()] != ' '
                    && board[currentY - 1][carCoordinates.getX()] != '@') {
                return false;
            } else if (way == 'D' && board[currentY + carLength][carCoordinates.getX()] != ' '
                    && board[currentY + carLength][carCoordinates.getX()] != '@') {
                return false;
            }

            // }
        } else { // carOrientation == 'H'
            if (carCoordinates.getY() != newCoordinates.getY() || newCoordinates.getX() < 0
                    || newCoordinates.getX() + carLength > boardWidth) {
                return false;
            }

            int currentX = carCoordinates.getX();

            if (way == 'L' && board[carCoordinates.getY()][currentX - 1] != ' '
                    && board[carCoordinates.getY()][currentX - 1] != '@') {
                return false;
            } else if (way == 'R' && board[carCoordinates.getY()][currentX + carLength] != ' '
                    && board[carCoordinates.getY()][currentX + carLength] != '@') {
                return false;
            }
        }
        return true;
    }

    public Map<Character, Car> getCars() {
        return this.cars;
    }

    private boolean isLevelFinished(char[][] board) {
        boolean res = true;
        for (int i = 0; i < board.length && res; i++) {
            for (int j = 0; j < board[i].length && res; j++) {
                char elem = board[i][j];
                if (elem == '@') {
                    res = false;
                }
            }
        }
        System.out.println(res);
        return res;
    }
}
