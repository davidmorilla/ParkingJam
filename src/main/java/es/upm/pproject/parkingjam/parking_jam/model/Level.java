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
    // returns the new board or null if the car does not exist or its not possible
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
                    System.out.println("-------------------DERECHA");
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
            if (coord.getX() >= 1 && coord.getX() < board[0].length - 1 && coord.getY() >= 1 && coord.getY() < board.length - 1) {

                if (checkMovementValidity(car, coord, way)) {
                    try {
                        System.out.println("--------------------------------------MOVING CAR------------------------------------");
                        deleteCar(car, newBoard, cars);
                        addCar(car, newBoard, cars, coord);
                        this.cars.get(car).setCoordinates(coord.getX(), coord.getY());
                        System.out.println("COORD COCHE " + car + " X:" +this.cars.get(car).getCoordinates().getX()
                        + " Y: " + this.cars.get(car).getCoordinates().getY() + "   Longitud: " +this.cars.get(car).getLength() );
                        score++;
                        // Add the old map at the top of the stack
                        
                        history.add(0, copy);
                        board = newBoard;
                    } catch (IllegalCarException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Me deja");
                } else {
                    System.out.println("NO me deja");
                    return null;
                }
            }
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


    private boolean checkMovementValidity(char carChar, Coordinates newCoordinates, char way) throws SameMovementException {
        Car car = cars.get(carChar);
        Coordinates carCoordinates = car.getCoordinates();
        int carLength = car.getLength();
        char carOrientation = car.getOrientation();
        int boardWidth = board[0].length;
        int boardHeight = board.length;

        /* if (carCoordinates.getX() == newCoordinates.getX() && carCoordinates.getY() == newCoordinates.getY()) 
            throw new SameMovementException(); */

        if (carOrientation == 'V') {
            if (carCoordinates.getX() != newCoordinates.getX() || newCoordinates.getY() < 0
                    || newCoordinates.getY() + carLength > boardHeight) {
                return false;
            }
            
            int currentY = carCoordinates.getY() ;
                
            if (way =='U' && board[currentY-1][carCoordinates.getX()] != ' ' && board[currentY-1][carCoordinates.getX()] != '@' ) {
                System.out.println("----> UP COMPROBANDO CELDA: X=" +  carCoordinates.getX() + "   Y= " + (currentY-1));
                return false;
            }
            else if(way =='D' && board[currentY+ carLength][carCoordinates.getX()] != ' ' && board[currentY+carLength][carCoordinates.getX()] != '@'){
                int aux = currentY + carLength;
                System.out.println("----> DOWN COMPROBANDO CELDA: X=" +  carCoordinates.getX() + "   Y= " + aux +"\nCONTENIDO: " + board[currentY+carLength][carCoordinates.getX()]);
                return false;
            }
                
            //}
        } else { // carOrientation == 'H'
        System.out.println("HORIZONTAAAAAAAAAL");
            if (carCoordinates.getY() != newCoordinates.getY() || newCoordinates.getX() < 0
                    || newCoordinates.getX() + carLength > boardWidth) {
                return false;
            }
            
            int currentX = carCoordinates.getX();
                
            if (way =='L' && board[carCoordinates.getY()][currentX-1] != ' ' && board[carCoordinates.getY()][currentX-1] != '@' ) {
                System.out.println("----> LEFT COMPROBANDO CELDA: X=" +  (currentX-1) + "   Y= " + carCoordinates.getY());
                return false;
            }
            else if(way =='R' && board[carCoordinates.getY()][currentX+carLength] != ' ' && board[carCoordinates.getY()][currentX+carLength] != '@'){
                int aux = currentX + carLength;
                System.out.println("----> RIGHT COMPROBANDO CELDA: X=" +  aux + "   Y= " + carCoordinates.getY() +"\nCONTENIDO: " + board[carCoordinates.getY()][currentX+carLength]);
                return false;
            }
        }
        return true;
    }

    public Map<Character, Car> getCars() {
        return this.cars;
    }

}
