package es.upm.pproject.parkingjam.parking_jam.model;

import java.util.HashMap;
import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;

public class LevelConverter {
	public Map<Character, Car> convertLevel(char[][]board) throws IllegalExitsNumberException, IllegalCarDimensionException{
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
            cars= null;
            throw new IllegalExitsNumberException();
        }
        if(cars.get('*').getLength()!=2){
            cars= null;
            throw new IllegalCarDimensionException();
        }
        return cars;
    }
}
