package es.upm.pproject.parkingjam.parking_jam.utilities;

import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.Car;

public class OldBoardData {
	private char [][]board;
    private Map<Character,Car>cars;
    public OldBoardData(char[][]board, Map<Character,Car>cars){
        this.board = board;
        this.cars = cars;
    }

    public char[][] getBoard(){
        return this.board;
    }

    public Map<Character,Car> getCars(){
        return this.cars;
    }
}
