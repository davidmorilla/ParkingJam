package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.*; 

public class Level {
    int score;                  // Puntuación del n
    Map<Character, Car> cars;   // Coches del tablero
    char[][]board;              //Mapa con la representación de los vehículos con letras

    LinkedList<OldBoardData> history;

    public Level (char[][] board, Map<Character, Car> cars){
        this.score=0;
        this.board= board;
        this.cars= cars;
        this.history = new LinkedList<>();
    }


    //Moves the car in the way and the length specified when its possible and uploads the current board, 
    //returns the old board or null if the car does not exist or its not possible to move the car to the specified possition. 
    public char[][] moveCar(char car, int length, char way) throws SameMovementException{
        char[][] newBoard = board;
        OldBoardData copy = new OldBoardData(board, cars);
        Coordinates coord = null;
        int xCar = 0;
        int yCar = 0;
        if(cars.get(car)==null)
            return null;
        switch (way) {
            //Left
            case 'L':
                xCar = cars.get(car).getCoordinates().getX();
                yCar = cars.get(car).getCoordinates().getY();
                coord = new Coordinates(xCar - length, yCar);
                if(checkMovementValidity(car, coord)){
                    try {
                        deleteCar(car, newBoard, cars);
                        addCar(car, newBoard, cars, coord);
                        score ++;
                        //Add the old map at the top of the stack
                        history.add(0,copy);        
                        board = newBoard;
                    } catch (IllegalCarException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //Right
            case 'R':
                xCar = cars.get(car).getCoordinates().getX();
                yCar = cars.get(car).getCoordinates().getY();
                coord = new Coordinates(xCar + length, yCar);
                if(checkMovementValidity(car, coord)){
                    try {
                        deleteCar(car, newBoard, cars);
                        addCar(car, newBoard, cars, coord);
                        score ++;
                        //Add the old map at the top of the stack
                        history.add(0,copy);        
                        board = newBoard;
                    } catch (IllegalCarException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //Up
            case 'U':
                xCar = cars.get(car).getCoordinates().getX();
                yCar = cars.get(car).getCoordinates().getY();
                coord = new Coordinates(xCar, yCar-length);
                if(checkMovementValidity(car, coord)){
                    try {
                        deleteCar(car, newBoard, cars);
                        addCar(car, newBoard, cars, coord);
                        score ++;
                        //Add the old map at the top of the stack
                        history.add(0,copy);        
                        board = newBoard;
                    } catch (IllegalCarException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //Down
            case 'D':
                xCar = cars.get(car).getCoordinates().getX();
                yCar = cars.get(car).getCoordinates().getY();
                coord = new Coordinates(xCar, yCar+length);
                if(checkMovementValidity(car, coord)){
                    try {
                        deleteCar(car, newBoard, cars);
                        addCar(car, newBoard, cars, coord);
                        score ++;
                        //Add the old map at the top of the stack
                        history.add(0,copy);        
                        board = newBoard;
                    } catch (IllegalCarException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        
        return newBoard;
    }

    
    public void deleteCar(char car, char[][]board, Map<Character,Car>cars) throws IllegalCarException{
        int xCar = 0;
        int yCar = 0;
        if(cars.get(car)==null)
            throw new IllegalCarException();
        
        xCar = cars.get(car).getCoordinates().getX();
        yCar = cars.get(car).getCoordinates().getY();
        for(int i = 1; i<=cars.get(car).getLength(); i++){ 
            if(cars.get(car).getOrientation()=='H')
                board[yCar][xCar+(i-1)]=' ';
            else
            board[yCar +(i-1)][xCar]=' ';
        }
    }

    public void addCar(char car, char[][]board, Map<Character,Car>cars, Coordinates coord) throws IllegalCarException{
        int xCar = 0;
        int yCar = 0;
        if(cars.get(car)==null)
            throw new IllegalCarException();
        
        xCar = coord.getX();
        yCar = coord.getY();
        for(int i = 1; i<=cars.get(car).getLength(); i++){ 
            if(cars.get(car).getOrientation()=='H')
                board[yCar][xCar+(i-1)]=car;
            else
            board[yCar +(i-1)][xCar]=car;
        }
    }


	private boolean checkMovementValidity(char carChar, Coordinates newCoordinates) throws SameMovementException{
        Car car =cars.get(carChar);
        Coordinates carCoordinates = car.getCoordinates();
        int carLength = car.getLength();
        char carOrientation = car.getOrientation();
        if(carCoordinates.getX()== newCoordinates.getX() && carCoordinates.getY()== newCoordinates.getY()) // no se debe computar como movimiento si se quiere mover al mismo lugar (de momento lanzo excepcion porque tecnicamente es valido pero no se debe sumar 1 al contador de movimientos, es como una accion vacia, ya veremos como tratamos este caso especifico
            throw new SameMovementException();
        if(carOrientation=='V' ){
            if(carCoordinates.getX()!= newCoordinates.getX() || newCoordinates.getY()<0 || newCoordinates.getY()>= board[0].length){ //si quiere moverse a una X diferente estaria moviendose lateralmente == invalido, no puede moverse a una fila menor a 0 o mayor igual que el numero de filas (null pointer exc)
                return false;
            }
            boolean valid=true;
            int goalY = newCoordinates.getY();
            int currentY= carCoordinates.getY();
            boolean goalIsUp = goalY - currentY < 0;
            if(goalIsUp){
                int i = 1; // si inicializamos a 0 comprobaria su actual posicion tambien que ya sabemos que es valida 
                int limit = currentY - goalY;
                while(valid && i<limit + 1){
                    if( (board[currentY - i][carCoordinates.getX()] != ' ') && (board[currentY + i][carCoordinates.getX()] != '@'))
                        valid=false;
                    i++;
                }
            } else{ //goal is down
                int i = carLength;
                int limit = goalY - currentY;
                while(valid && i<limit + carLength){
                    if(board[currentY + i][carCoordinates.getX()] == '@')
                        return true;
                    if((board[currentY + i][carCoordinates.getX()] != ' ') && (board[currentY + i][carCoordinates.getX()] != '@') ){
                        valid=false;
                    }
                    i++;
                }
            }
            return valid;
        } else { // caso orientation == 'H'
            if(carCoordinates.getY()!= newCoordinates.getY() || newCoordinates.getX()<0 || newCoordinates.getX()>= board.length){ //si quiere moverse a una Y diferente estaria moviendose verticalmente == invalido, no puede moverse a una columna menor a 0 o mayor igual que el numero de columnas (null pointer exc)
                return false;
            }
            boolean valid=true;
            int goalX = newCoordinates.getX();
            int currentX= carCoordinates.getX();
            boolean goalIsLeft = goalX - currentX < 0;
            if(goalIsLeft){
                int i = 1; // si inicializamos a 0 comprobaria su actual posicion tambien que ya sabemos que es valida 
                int limit = currentX - goalX;
                while(valid && i<limit + 1){
                    if( (board[carCoordinates.getY()][currentX - i] != ' ') && (board[carCoordinates.getY()][currentX - i] != '@'))
                        valid=false;
                    i++;
                }
            } else{ //goal is right
                int i = carLength;
                int limit = goalX - currentX;
                while(valid && i<limit + carLength){
                    if(board[carCoordinates.getY()][currentX + i] == '@')
                        return true;
                    if((board[carCoordinates.getY()][currentX + i] != ' ') && (board[carCoordinates.getY()][currentX + i] != '@') ){
                        valid=false;
                    }
                    i++;
                }
            }
            return valid;
        }
	}
    
    public void undoMovement() throws CannotUndoMovementException{
        if(this.history.size()==1)
            throw new CannotUndoMovementException();
        OldBoardData restoredBoard = history.getFirst();
        history.removeFirst();
        this.board = restoredBoard.getBoard();
        this.cars = restoredBoard.getCars();
        this.score--;
    }

}
