package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

@DisplayName ("Tests related to testing the class LevelConverter")
@Nested
class LevelConverterTest {
	private LevelReader lr;
	private LevelConverter lc;

	@BeforeEach
	void runBeforeEach () {
		lr = new LevelReader();
		lc = new LevelConverter();
	}
	@Test
	public void convertOKLevelTest() {
		try { 
			char[][] board = lr.readMap(-1,false);
			Map<Character, Car>	map = lc.convertLevel(board);
			Map<Character, Car> expectedMap = new<Character, Car> HashMap();;
			expectedMap.put('a', new Car('a', 1, 1, 2, 'H'));
			expectedMap.put('b', new Car('b', 3, 1, 3, 'H'));
			expectedMap.put('c', new Car('c', 6, 1, 2, 'V'));
			expectedMap.put('d', new Car('d', 1, 3, 3, 'V'));
			expectedMap.put('*', new Car('*', 4, 2, 2, 'V'));
			expectedMap.put('f', new Car('f', 3, 4, 3, 'H'));
			expectedMap.put('e', new Car('e', 2, 5, 2, 'V'));
			expectedMap.put('g', new Car('g', 4, 6, 3, 'H'));
			for (char a : expectedMap.keySet()) {
				Car expectedCar = expectedMap.get(a);
				Car actualCar = map.get(a);
				assertNotNull(actualCar, "Car with symbol '" + a + "' is missing in actual map");
				assertEquals(expectedCar.getSymbol(), actualCar.getSymbol(), "Symbol mismatch for car with symbol '" + a + "'");
				assertEquals(expectedCar.getCoordinates().getX(), actualCar.getCoordinates().getX(), "X coordinate mismatch for car with symbol '" + a + "'");
				assertEquals(expectedCar.getCoordinates().getY(), actualCar.getCoordinates().getY(), "Y coordinate mismatch for car with symbol '" + a + "'");
				assertEquals(expectedCar.getLength(), actualCar.getLength(), "Length mismatch for car with symbol '" + a + "'");
				assertEquals(expectedCar.getOrientation(), actualCar.getOrientation(), "Orientation mismatch for car with symbol '" + a + "'");
			}

		} catch (Exception e) {} 
	}
	@Test
	public void convertLevelWithNoExitsTest() {
		char[][] board = lr.readMap(-2,false);
		Assertions.assertThrows(IllegalExitsNumberException.class, () -> lc.convertLevel(board));
	}
	@Test
	public void convertLevelWithTwoExitsTest() {
		char[][] board = lr.readMap(-3,false);
		Assertions.assertThrows(IllegalExitsNumberException.class, () -> lc.convertLevel(board));
	}
	@Test
	public void convertLevelWithInvalidCarDimensionsTest() {
		char[][] board = lr.readMap(-4,false);
		Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
	}
	@Test
	public void convertLevelWithInvalidRedCarDimensionsTest1() {
		char[][] board = lr.readMap(-5,false);
		Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
	}
	@Test
	public void convertLevelWithInvalidRedCarDimensionsTest2() {
		char[][] board = lr.readMap(-6,false);
		Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
	}
	@Test
	public void convertLevelWithInvalidRedCarDimensionsTest3() {
		char[][] board = lr.readMap(-7,false);
		Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
	}
}
