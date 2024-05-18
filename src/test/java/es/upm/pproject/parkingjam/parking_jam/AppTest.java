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
	@DisplayName ("Tests related to testing the class LevelConverter")
	@Nested
	class LevelConverterTests {
		@Test
		public void convertOKLevelTest() {
			try { 
				char[][] board = lr.readMap(-1);
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
			char[][] board = lr.readMap(-2);
			Assertions.assertThrows(IllegalExitsNumberException.class, () -> lc.convertLevel(board));
		}
		@Test
		public void convertLevelWithTwoExitsTest() {
			char[][] board = lr.readMap(-3);
			Assertions.assertThrows(IllegalExitsNumberException.class, () -> lc.convertLevel(board));
		}
		@Test
		public void convertLevelWithInvalidCarDimensionsTest() {
			char[][] board = lr.readMap(-4);
			Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
		}
		@Test
		public void convertLevelWithInvalidRedCarDimensionsTest1() {
			char[][] board = lr.readMap(-5);
			Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
		}
		@Test
		public void convertLevelWithInvalidRedCarDimensionsTest2() {
			char[][] board = lr.readMap(-6);
			Assertions.assertThrows(IllegalCarDimensionException.class, () -> lc.convertLevel(board));
		}
	}
	@DisplayName ("Tests related to testing the class Car")
	@Nested
	class CarTests {
		
		@Test
		public void getSymbolTest() {
			Car car = new Car('a', 1, 5, 3, 'V');
			assertEquals('a', car.getSymbol());
		}
		@Test
		public void getOrientationTest() {
			Car car = new Car('a', 1, 5, 3, 'V');
			assertEquals('V', car.getOrientation());
		}
		@Test
		public void getCoordinatesTest() {
			Car car = new Car('a', 1, 5, 3, 'V');
			assertEquals(1, car.getCoordinates().getX());
			assertEquals(5, car.getCoordinates().getY());
		}
		@Test
		public void getLengthTest() {
			Car car = new Car('a', 1, 5, 3, 'V');
			assertEquals(3, car.getLength());
		}
		public void setSymbolTest() {
			Car car = new Car('a', 1, 5, 3, 'V');
			car.setSymbol('b');
			assertEquals('b', car.getSymbol());
		}
		@Test
		public void setOrientationTest() {
			Car car = new Car('a', 1, 5, 3, 'V');
			car.setOrientation('H');
			assertEquals('H', car.getOrientation());
		}
		@Test
		public void setCoordinatesTest() {
			Car car = new Car('a', 1, 5, 3, 'V');
			car.setCoordinates(3, 2);
			assertEquals(3, car.getCoordinates().getX());
			assertEquals(2, car.getCoordinates().getY());
		}
		@Test
		public void setLengthTest() {
			Car car = new Car('a', 1, 5, 3, 'V');
			car.setLength(5);
			assertEquals(5, car.getLength());
		}
		@Test
		public void equalsTest() {
			Car car1 = new Car('a', 1, 5, 3, 'V');
			Car car2 = new Car('a', 1, 5, 3, 'V');
			assertTrue(car1.equals(car2));
			car2.setLength(2);
			assertFalse(car1.equals(car2));
		}
		public void hashCodeTest() {
			Car car1 = new Car('a', 1, 5, 3, 'V');
			Car car2 = new Car('a', 1, 5, 3, 'V');
			assertEquals(car1.hashCode(), car2.hashCode());
			car2.setLength(2);
			assertNotEquals(car1.hashCode(), car2.hashCode());
		}
	}
}

