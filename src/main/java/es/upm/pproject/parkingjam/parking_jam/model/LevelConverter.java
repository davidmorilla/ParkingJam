package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;

public class LevelConverter {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    
	public Map<Character, Car> convertLevel(char[][]board) throws IllegalExitsNumberException, IllegalCarDimensionException{
        logger.info("Converting level...");
        Map<Character, Car> cars = new HashMap<>();
        int numExits=0;
        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                if (board[i][j]!='+' && board[i][j]!='@' && board[i][j]!=' ') {
                    if(cars.containsKey(board[i][j])){
                        Car c = cars.get(board[i][j]);
                        c.setLength(c.getLength()+1);
                        if(c.getCoordinates().getY()!=i && c.getLength()==2){
                            c.setOrientation('V');
                        }
                        else if(c.getCoordinates().getY()!=i && c.getLength()>2 && c.getOrientation()=='H'){
                            logger.error("Car '{}' has invalid dimensions.", board[i][j]);
                            throw new IllegalCarDimensionException();
                        }
                    }
                    
                    else{
                        //valores por defecto
                        Car c = new Car(board[i][j], j, i, 1, 'H');
                        cars.put(board[i][j], c);
                    }
                }
                else if(board[i][j]=='@'){
                    numExits++;
                }
            }
        } 
        //If the level has only 1 exit and the dimensions of the car are 2x1 or 1x2 then the level is OK, IOC returns null
        if(numExits!=1){
            logger.error("There are {} exits. The number of exits must be exactly 1.",numExits);
            cars= null;
            throw new IllegalExitsNumberException();
        }
        if(cars.get('*').getLength()!=2){
            logger.error("There red car length is {}. The length of the red car must be exactly 2.",cars.get('*').getLength());
            cars= null;
            throw new IllegalCarDimensionException();
        }
        logger.info("Level has been converted.");
        return cars;
    }
}
