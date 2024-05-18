package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.LevelConverter;
import es.upm.pproject.parkingjam.parking_jam.model.LevelReader;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;

public class AppTest {
	private LevelReader lr;
	private LevelConverter lc;
	
	@BeforeEach
	void runBeforeEach () {
		lr = new LevelReader();
		lc = new LevelConverter();
	}
	
	@DisplayName ("Tests related to testing the class LevelReader")
	@Nested
	class LevelReaderTests {
		@Test
		public void readOKLevelTest() {
			char[][] board = lr.readMap(-1);
			char[][] expectedBoard={{'+','+','+','+','+','+','+','+'},
									{'+','a','a','b','b','b','c','+'},
									{'+',' ',' ',' ','*',' ','c','+'},
									{'+','d',' ',' ','*',' ',' ','+'},
									{'+','d',' ','f','f','f',' ','+'},
									{'+','d','e',' ',' ',' ',' ','+'},
									{'+',' ','e',' ','g','g','g','+'},
									{'+','+','+','+','@','+','+','+'}};
			assertArrayEquals(board, expectedBoard);
		}
	}
		
		class LevelConverterTests {
			@Test
			public void convertOKLevelTest() {
				try {
					char[][] board = lr.readMap(-1);
					Map<Character, Car>	map = lc.convertLevel(board);
					Map<Character, Car> expectedMap = new<Character, Car> HashMap();;
			        expectedMap.put('a', new Car('a', 1, 1, 2, 'H'));
			        expectedMap.put('b', new Car('b', 1, 3, 3, 'H'));
			        expectedMap.put('c', new Car('c', 1, 6, 2, 'V'));
			        expectedMap.put('d', new Car('d', 3, 1, 3, 'V'));
			        expectedMap.put('*', new Car('*', 3, 4, 2, 'V'));
			        expectedMap.put('f', new Car('f', 4, 3, 3, 'H'));
			        expectedMap.put('e', new Car('e', 5, 2, 2, 'V'));
			        expectedMap.put('g', new Car('g', 6, 4, 3, 'H'));
					assertEquals(map, expectedMap);
				} catch (Exception e) {} 
				
			}
			public void convertLevelWithNoExitsTest() {
				char[][] board = lr.readMap(-2);
				Assertions.assertThrows(IllegalExitsNumberException.class, () -> lc.convertLevel(board));
			}
			public void convertLevelWithTwoExitsTest() {
				char[][] board = lr.readMap(-3);
				Assertions.assertThrows(IllegalExitsNumberException.class, () -> lc.convertLevel(board));
			}
			public void convertLevelWithInvalidCarDimensionsTest() {
				char[][] board = lr.readMap(-4);
				Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
			}
			public void convertLevelWithInvalidRedCarDimensionsTest1() {
				char[][] board = lr.readMap(-5);
				Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
			}
			public void convertLevelWithInvalidRedCarDimensionsTest2() {
				char[][] board = lr.readMap(-6);
				Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
			}
		}
	}
