package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.LinkedList;
import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.*;

public class Level {
    private int score; // Puntuación del nivel
    private Map<Character, Car> cars; // Coches del tablero
    private char[][] board; // Mapa con la representación de los vehículos con letras
    private Pair<Integer, Integer> dimensions;

    LinkedList<OldBoardData> history;

    public Level(char[][] board, Map<Character, Car> cars) {
        this.score = 0;
        this.board = board;
        this.cars = cars;
        this.history = new LinkedList<>();
        int numRows = board.length;
        int numCols = board[0].length;
        dimensions = new Pair<>(numRows, numCols);

    }

    public Pair<Integer, Integer> getDimensions() {
        return dimensions;
    }

    public char[][] getBoard(){
        return this.board;
    }

    public int getScore() {
        return score;
    }

    public char[][] undoMovement() throws CannotUndoMovementException {
        if (this.history.size() == 1)
            throw new CannotUndoMovementException();
        OldBoardData restoredBoard = history.getFirst();
        history.removeFirst();
        this.board = restoredBoard.getBoard();
        this.cars = restoredBoard.getCars();
        this.score--;
        return board;
    }

    // Moves the car in the way and the length specified when its possible and
    // uploads the current board,
    // returns the old board or null if the car does not exist or its not possible
    // to move the car to the specified possition.
    public char[][] moveCar(char car, int length, char way) throws SameMovementException {
        char[][] newBoard = board;
        OldBoardData copy = new OldBoardData(board, cars);
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

            if (checkMovementValidity(car, coord)) {
                try {
                    System.out.println("--------------------------------------MOVING CAR------------------------------------");
                    deleteCar(car, newBoard, cars);
                    addCar(car, newBoard, cars, coord);
                    score++;
                    // Add the old map at the top of the stack
                    history.add(0, copy);
                    board = newBoard;
                } catch (IllegalCarException e) {
                    e.printStackTrace();
                }
            }
            else   
                return null;
        }
        return newBoard;
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

    /*private boolean checkMovementValidity(char carChar, Coordinates newCoordinates) throws SameMovementException {
        Car car = cars.get(carChar);
        Coordinates carCoordinates = car.getCoordinates();
        int carLength = car.getLength();
        char carOrientation = car.getOrientation();
        if (carCoordinates.getX() == newCoordinates.getX() && carCoordinates.getY() == newCoordinates.getY()) 
            throw new SameMovementException();
        if (carOrientation == 'V') {
            if (carCoordinates.getX() != newCoordinates.getX() || newCoordinates.getY() <= 0
                    || newCoordinates.getY() >= board[0].length) { // si quiere moverse a una X diferente estaria
                                                                   // moviendose lateralmente == invalido, no puede
                                                                   // moverse a una fila menor a 0 o mayor igual que el
                                                                   // numero de filas (null pointer exc)
                return false;
            }
            boolean valid = true;
            int goalY = newCoordinates.getY();
            int currentY = carCoordinates.getY();
            boolean goalIsUp = goalY - currentY < 0;
            if (goalIsUp) {
                int i = 1; // si inicializamos a 0 comprobaria su actual posicion tambien que ya sabemos
                           // que es valida
                int limit = currentY - goalY;
                while (valid && i < limit + 1) {
                    if ((board[currentY - i][carCoordinates.getX()] != ' ')
                            && (board[currentY + i][carCoordinates.getX()] != '@'))
                        valid = false;
                    i++;
                }
            } else { // goal is down
                int i = carLength;
                int limit = goalY - currentY;
                while (valid && i < limit + carLength) {
                    if (board[currentY + i][carCoordinates.getX()] == '@')
                        return true;
                    if ((board[currentY + i][carCoordinates.getX()] != ' ')
                            && (board[currentY + i][carCoordinates.getX()] != '@')) {
                        valid = false;
                    }
                    i++;
                }
            }
            return valid;
        } else { // caso orientation == 'H'
            if (carCoordinates.getY() != newCoordinates.getY() || newCoordinates.getX() <= 0
                    || newCoordinates.getX() >= board.length) { // si quiere moverse a una Y diferente estaria
                                                                // moviendose verticalmente == invalido, no puede
                                                                // moverse a una columna menor a 0 o mayor igual que el
                                                                // numero de columnas (null pointer exc)
                return false;
            }
            boolean valid = true;
            boolean goalIsLeft = newCoordinates.getX() - carCoordinates.getX() < 0;
            if (goalIsLeft) {
                int i = 1; // si inicializamos a 0 comprobaria su actual posicion tambien que ya sabemos
                           // que es valida
                int movementLength = carCoordinates.getX() - newCoordinates.getX();
                while (valid && i < movementLength + 1) {
                    if ((board[carCoordinates.getY()][carCoordinates.getX() - i] != ' ')
                            && (board[carCoordinates.getY()][carCoordinates.getX() - i] != '@'))
                        valid = false;
                    i++;
                }
            } else { // goal is right
                int i = carLength;
                int movementLength = newCoordinates.getX() - carCoordinates.getX();
                while (valid && i < movementLength + carLength) {
                    if (board[carCoordinates.getY()][carCoordinates.getX() + i] == '@')
                        return true;
                    if (board[carCoordinates.getY()][carCoordinates.getX() + i] != ' '&& board[carCoordinates.getY()][carCoordinates.getX() + i] != '@'
                            && board[carCoordinates.getY()][carCoordinates.getX() + i] != carChar ) {
                        valid = false;
                    }
                    i++;
                }
            }
            return valid;
        }
    }*/
    private boolean checkMovementValidity(char carChar, Coordinates newCoordinates) throws SameMovementException {
        Car car = cars.get(carChar);
        Coordinates carCoordinates = car.getCoordinates();
        int carLength = car.getLength();
        char carOrientation = car.getOrientation();
        int boardWidth = board[0].length;
        int boardHeight = board.length;

        if (carCoordinates.getX() == newCoordinates.getX() && carCoordinates.getY() == newCoordinates.getY()) 
            throw new SameMovementException();

        if (carOrientation == 'V') {
            if (carCoordinates.getX() != newCoordinates.getX() || newCoordinates.getY() < 0
                    || newCoordinates.getY() + carLength > boardHeight) {
                return false;
            }
            int step = newCoordinates.getY() > carCoordinates.getY() ? 1 : -1;
            for (int i = 0; i < Math.abs(newCoordinates.getY() - carCoordinates.getY()); i++) {
                int currentY = carCoordinates.getY() + (i + 1) * step;
                if (board[currentY][carCoordinates.getX()] != ' ' && board[currentY][carCoordinates.getX()] != '@') {
                    return false;
                }
            }
        } else { // carOrientation == 'H'
            if (carCoordinates.getY() != newCoordinates.getY() || newCoordinates.getX() < 0
                    || newCoordinates.getX() + carLength > boardWidth) {
                return false;
            }
            int step = newCoordinates.getX() > carCoordinates.getX() ? 1 : -1;
            for (int i = 0; i < Math.abs(newCoordinates.getX() - carCoordinates.getX()); i++) {
                int currentX = carCoordinates.getX() + (i + 1) * step;
                if (board[carCoordinates.getY()][currentX] != ' ' && board[carCoordinates.getY()][currentX] != '@') {
                    return false;
                }
            }
        }
        return true;
    }

    public Map<Character, Car> getCars() {
        return this.cars;
    }

}
