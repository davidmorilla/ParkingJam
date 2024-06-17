package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
/**
 * The 'LevelConverter' class is responsible for converting a 2D character array representation
 * of a game level into a map of 'Car' objects. It validates the level by checking the number
 * of exits and the dimensions of the cars.
 *  */
public class LevelConverter {
	private Map<Character, Car> cars;
	
    private static final Logger logger = LoggerFactory.getLogger(LevelConverter.class);
    
    
    /**
     * Converts a 2D character array board representation into a map of 'Car' objects.
     *
     * @param board the 2D character array representing the level.
     * @return a map where keys are characters representing cars and values are 'Car' objects.
     * @throws IllegalExitsNumberException if the number of exits in the level is not exactly one.
     * @throws IllegalCarDimensionException if any car has invalid dimensions.
     *
     * Example Board:
     * <pre>{@code
     * char[][] board = {
     *     {'+', '+', '+', '+', '+', '+', '+'},
     *     {'+', 'A', ' ', ' ', ' ', 'B', '+'},
     *     {'+', 'A', ' ', '*', '*', 'B', '@'},
     *     {'+', 'A', ' ', ' ', ' ', ' ', '+'},
     *     {'+', '+', '+', '+', '+', '+', '+'}
     * };
     * }</pre>
     *
     * Example Result:
     * <pre>{@code
     * {
     *     'A' -> Car{symbol='A', x=1, y=1, l=3, o='V'},
     *     'B' -> Car{symbol='B', x=5, y=1, l=2, o='V'},
     *     '*' -> Car{symbol='*', x=3, y=2, l=2, o='H'}
     * }
     * }</pre>
     */
	public Map<Character, Car> convertLevel(char[][]board) throws IllegalExitsNumberException, IllegalCarDimensionException{
        logger.info("Converting level...");
        cars = new HashMap<>();
        int numExits=0;
        
        // Iterate through the board to identify cars and exits
        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                if (board[i][j]!='+' && board[i][j]!='@' && board[i][j]!=' ') {
                	// Process car
                	processCar(board, i, j);
                }
                else if(board[i][j]=='@'){
                	// Process exit
                    numExits++;
                }
            }
        } 
        // Validate the number of exits
        if(numExits!=1){
            logger.error("There are {} exits. The number of exits must be exactly 1.",numExits);
            throw new IllegalExitsNumberException();
        }
        
        // Validate the red car dimensions
        if(cars.get('*').getLength()!=2){
            logger.error("There red car length is {}. The length of the red car must be exactly 2.",cars.get('*').getLength());
            throw new IllegalCarDimensionException();
        }
        logger.info("Level has been converted.");
        return cars;
    }
	
	/**
	 * Process board square as a part of a car and checks the validity of that car
	 * 
	 * @param board the board containing the specific square
	 * @param x the horizontal position of the square
	 * @param y the vertical position of the square
	 * @throws IllegalCarDimensionException if any car has invalid dimensions
	 */
	private void processCar(char[][] board, int x, int y) throws IllegalCarDimensionException{
    	if(cars.containsKey(board[x][y])){
    		
    		// Update existing car
            Car c = cars.get(board[x][y]);
            c.setLength(c.getLength()+1);
            
            // Determine orientation if length is 2
            if(c.getCoordinates().getY()!=x && c.getLength()==2){
                c.setOrientation('V');
            }
            
            // Validate car dimensions
            if(c.getCoordinates().getY()!=x && c.getLength()>=2 && c.getOrientation()=='H'){
                logger.error("Car '{}' has invalid dimensions.", board[x][y]);
                throw new IllegalCarDimensionException();
            }
            if(c.getCoordinates().getX()!=y && c.getLength()>=2 && c.getOrientation()=='V'){
                logger.error("Car '{}' has invalid dimensions.", board[x][y]);
                throw new IllegalCarDimensionException();
            }
        }
        
        else{
        	// Add new car with default values
            Car c = new Car(board[x][y], y, x, 1, 'H');
            cars.put(board[x][y], c);
        }
	}
}
