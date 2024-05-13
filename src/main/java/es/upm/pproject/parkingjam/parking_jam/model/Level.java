import java.util.Map;
import java.util.Stack;

public class Level {
    int score;                  // Puntuación del n
    //Stack movements;
    Map<Character, Car> cars;   // Coches del tablero
    char[][]board;              //Mapa con la representación de los vehículos con letras

    public Level (char[][] board, Map<Character, Car> cars){
        this.score=0;
        this.board= board;
        this.cars= cars;
    }

    public void moveCar(char car, Coordinates newCoordinates) throws InvalidMovementException {
        checkMovementValidity(car, newCoordinates);
        cars.get(car).setCoordinates(newCoordinates.getX(),newCoordinates.getY());

    }

	private boolean checkMovementValidity(char carChar, Coordinates newCoordinates) throws InvalidMovementException{
        //TODO checkear validez del movimiento (free tiles)

        Car car =cars.get(carChar);
        Coordinates carCoordinates = car.getCoordinates();
        int carLength = car.getLength();
        char carOrientation = car.getOrientation();
        if(carCoordinates.getX()== newCoordinates.getX() && carCoordinates.getY()== newCoordinates.getY()) // no se debe co
            throw new SameMovementException();
        if(carOrientation=='v' ){
            if(carCoordinates.getX()!= newCoordinates.getX()){ //si quiere moverse a una X diferente estaria moviendose lateralmente == invalido
                throw new InvalidMovementException();
            }
            boolean valid=true;
            int goalY = newCoordinates.getY();
            int currentY= carCoordinates.getY();
            boolean goalIsUp = goalY - currentY < 0;
            boolean goalIsDown = goalY - currentY > 0;
            while(valid){

            }
        }
		throw new InvalidMovementException();
	}
    
    

}

