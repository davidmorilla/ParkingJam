package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.Map;
import java.util.Stack;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.InvalidMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;

public class Level {
    private int score;                  // Puntuación del n
    //Stack movements;
    private Map<Character, Car> cars;   // Coches del tablero
    private char[][]board;              //Mapa con la representación de los vehículos con letras
    private int nrows;
    private int ncolumns;

    public Level (char[][] board,  int nrows, int ncolumns, Map<Character, Car> cars){ // necesitamos guardar el tam del tablero para checkear la validez de un movimiento mas adelante.
        this.score=0;
        this.board= board;
        this.cars= cars;
        this.nrows=nrows;
        this.ncolumns=ncolumns;
    }

    public void moveCar(char car, Coordinates newCoordinates) throws InvalidMovementException, SameMovementException {
        checkMovementValidity(car, newCoordinates);
        cars.get(car).setCoordinates(newCoordinates.getX(),newCoordinates.getY());

    }

	private boolean checkMovementValidity(char carChar, Coordinates newCoordinates, int carLength) throws SameMovementException{
        //TODO checkear validez del movimiento (free tiles)

        Car car =cars.get(carChar);
        Coordinates carCoordinates = car.getCoordinates();
        int carLength = car.getLength();
        char carOrientation = car.getOrientation();
        if(carCoordinates.getX()== newCoordinates.getX() && carCoordinates.getY()== newCoordinates.getY()) // no se debe computar como movimiento si se quiere mover al mismo lugar (de momento lanzo excepcion porque tecnicamente es valido pero no se debe sumar 1 al contador de movimientos, es como una accion vacia, ya veremos como tratamos este caso especifico
            throw new SameMovementException();
        if(carOrientation=='V' ){
            if(carCoordinates.getX()!= newCoordinates.getX() || newCoordinates.getY()<0 || newCoordinates.getY()>= nrows){ //si quiere moverse a una X diferente estaria moviendose lateralmente == invalido, no puede moverse a una fila menor a 0 o mayor igual que el numero de filas (null pointer exc)
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
                    if( (board[carCoordinates.getX()][currentY - i] != ' ') && (board[carCoordinates.getX()][currentY + i] != '@'))
                        valid=false;
                    i++;
                }
            } else{ //goal is down
                int i = carLength;
                int limit = goalY - currentY;
                while(valid && i<limit + carLength){
                    if(board[carCoordinates.getX()][currentY + i] != '@')
                        return true;
                    if(board[carCoordinates.getX()][currentY + i] != ' ') && (board[carCoordinates.getX()][currentY + i] != '@') ){)
                        valid=false;
                    }
                    i++;
                }
            }
            return valid;
        } else { // caso orientation == 'H'
            if(carCoordinates.getY()!= newCoordinates.getY() || newCoordinates.getX()<0 || newCoordinates.getX()>= ncolumns){ //si quiere moverse a una Y diferente estaria moviendose verticalmente == invalido, no puede moverse a una columna menor a 0 o mayor igual que el numero de columnas (null pointer exc)
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
                    if( (board[currentX - i][carCoordinates.getY()] != ' ') && (board[currentX - i][carCoordinates.getY()] != '@'))
                        valid=false;
                    i++;
                }
            } else{ //goal is right
                int i = carLength;
                int limit = goalX - currentX;
                while(valid && i<limit + carLength){
                    if(board[currentX + i][carCoordinates.getY()] != '@')
                        return true;
                    if(board[currentX + i][carCoordinates.getY()] != ' ') && (board[currentX + i][carCoordinates.getY()] != '@') ){)
                        valid=false;
                    }
                    i++;
                }
            }
            return valid;
        }
	}
    
    

}

