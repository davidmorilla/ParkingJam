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
import es.upm.pproject.parkingjam.parking_jam.model.Level;
import es.upm.pproject.parkingjam.parking_jam.model.LevelConverter;
import es.upm.pproject.parkingjam.parking_jam.model.LevelReader;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

public class AppTest {
	

	@DisplayName ("Tests related to testing the class LevelReader")
	@Nested
	class LevelReaderTests {
		private LevelReader lr;
		@BeforeEach
		void runBeforeEach () {
			lr = new LevelReader();
			new LevelConverter();
		}
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
		@Test
		public void readLevelWithNoNameTest() {
			Assertions.assertNull(lr.readMap(-8));
		}
		@Test
		public void readLevelWithNoDimensionsTest() {
			Assertions.assertNull(lr.readMap(-9));
		}
		@Test
		public void readLevelWithWrongDimensionsFormatTest() {
			Assertions.assertNull(lr.readMap(-10));
		}
		@Test
		public void readLevelWithMissingLinesTest() {
			Assertions.assertNull(lr.readMap(-11));
		}
		@Test
		public void readTooSmallLevelTest() {
			Assertions.assertNull(lr.readMap(-12));
		}
		
	}
	@DisplayName ("Tests related to testing the class LevelConverter")
	@Nested
	class LevelConverterTests {
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
		@Test
		public void convertLevelWithInvalidRedCarDimensionsTest3() {
			char[][] board = lr.readMap(-7);
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
	
	@DisplayName ("Tests related to testing the movement of the cars")
	@Nested
	class MovementTests {
		private Level level;
		
		@BeforeEach
		void runBeforeEach () {
			/* Note: In the game board and cars are given by LevelReader and LevelConverter classes,
			 * but here they are introduced manually in order to test only the Level class*/
			char level1Board[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
									 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
									 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
									 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
									 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
									 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
									 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
									 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			Map<Character, Car> cars = new HashMap<>();
			cars.put('a', new Car('a',1,1,2,'H'));
			cars.put('b', new Car('b',3,1,3,'H'));
			cars.put('c', new Car('c',6,1,2,'V'));
			cars.put('d', new Car('d',1,3,3,'V'));
			cars.put('e', new Car('e',2,5,2,'V'));
			cars.put('f', new Car('f',3,4,3,'H'));
			cars.put('g', new Car('g',4,6,3,'H'));
			cars.put('*', new Car('*',4,2,2,'V'));
			
			level = new Level(level1Board, cars);
		}
		@Test
		void testMove1TileLeft() throws SameMovementException {
			char[][] newBoard = level.moveCar('g',1,'L');
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
					 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
					 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					 {'+', ' ', 'e', 'g', 'g', 'g', ' ', '+'},
					 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			assertArrayEquals(expectedBoard, newBoard);
		}
		
		@Test
		void testMove1TileRight() throws SameMovementException {
			char[][] newBoard = level.moveCar('f',1,'R');
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
					 {'+', 'd', ' ', ' ', 'f', 'f', 'f', '+'},
					 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(expectedBoard, newBoard);
		}
		
		@Test
		void testMove1TileUp() throws SameMovementException {
			char[][] newBoard = level.moveCar('e',1,'U');
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
					 {'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					 {'+', ' ', ' ', ' ', 'g', 'g', 'g', '+'},
					 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(expectedBoard, newBoard);
		}
		
		@Test
		void testMove2TilesLeft() throws SameMovementException {
			// To do this tests we need to move another vehicle first to make space for the 2-tiles left movement
			level.moveCar('e',1,'U');
			char[][] newBoard = level.moveCar('g', 2, 'L');
			
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
					 {'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					 {'+', ' ', 'g', 'g', 'g', ' ', ' ', '+'},
					 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(expectedBoard, newBoard);
		}
		
		@Test
		void testMove2TilesRight() throws SameMovementException {
			// To do this tests we need to move t vehicle first to make space for the 2-tiles left movement
			level.moveCar('f',1,'L');
			char[][] newBoard = level.moveCar('f', 2, 'R');
			
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
					 {'+', 'd', ' ', ' ', 'f', 'f', 'f', '+'},
					 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(expectedBoard, newBoard);
		}
		
		@Test
		void testMove2TilesUp() throws SameMovementException {
			char[][] newBoard = level.moveCar('e', 2, 'U');
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					 {'+', 'd', 'e', ' ', '*', ' ', ' ', ' '},
					 {'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					 {'+', 'd', ' ', ' ', ' ', ' ', ' ', '+'},
					 {'+', ' ', ' ', ' ', 'g', 'g', 'g', '+'},
					 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(expectedBoard, newBoard);
		}
		
		@Test
		void testMove2TilesDown() throws SameMovementException {
			char[][] newBoard = level.moveCar('c', 2, 'D');
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					 {'+', 'a', 'a', 'b', 'b', 'b', ' ', '+'},
					 {'+', ' ', ' ', ' ', '*', ' ', ' ', '+'},
					 {'+', 'd', ' ', ' ', '*', ' ', 'c', ' '},
					 {'+', 'd', ' ', 'f', 'f', 'f', 'c', '+'},
					 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(expectedBoard, newBoard);
		}
		
		@Test
		void testMoveAgainstLeftWall() throws SameMovementException {
			char[][] newBoard = level.moveCar('c', 1, 'U');

			assertNull(newBoard);
		}
		
		@Test
		void testMoveAgainstRightWall() throws SameMovementException {
			char[][] newBoard = level.moveCar('g', 5, 'R');

			assertNull(newBoard);
		}
		
		@Test
		void testMoveAgainstUpperWall() throws SameMovementException {
			char[][] newBoard = level.moveCar('a', 1, 'L');

			assertNull(newBoard);
		}
		
		@Test
		void testMoveAgainstBottomWall() throws SameMovementException {
			char[][] newBoard = level.moveCar('d', 5, 'D');

			assertNull(newBoard);
		}
		
		@Test
		void testMoveInvalidDirection() throws SameMovementException {
			char[][] newBoard = level.moveCar('d', 1, 'R');

			assertNull(newBoard);
		}
		
		@Test
		void testMoveInvalidDirection2() throws SameMovementException {
			char[][] newBoard = level.moveCar('a', 1, 'D');

			assertNull(newBoard);
		}
		
		@Test
		void testMoveAgainstOtherCar() throws SameMovementException {
			char[][] newBoard = level.moveCar('a', 1, 'R');

			assertNull(newBoard);
		}
		
		@Test
		void testMoveAgainstOtherCar2() throws SameMovementException {
			char[][] newBoard = level.moveCar('*', 1, 'D');

			assertNull(newBoard);
		}
	}
	
	@DisplayName ("Tests related to testing the undo movement of the cars")
	@Nested
	class UndoMovementTests {
		private Level level;
		
		@BeforeEach
		void runBeforeEach () {
			/* Note: In the game board and cars are given by LevelReader and LevelConverter classes,
			 * but here they are introduced manually in order to test only the Level class*/
			char level1Board[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
									 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
									 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
									 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
									 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
									 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
									 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
									 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			Map<Character, Car> cars = new HashMap<>();
			cars.put('a', new Car('a',1,1,2,'H'));
			cars.put('b', new Car('b',3,1,3,'H'));
			cars.put('c', new Car('c',6,1,2,'V'));
			cars.put('d', new Car('d',1,3,3,'V'));
			cars.put('e', new Car('e',2,5,2,'V'));
			cars.put('f', new Car('f',3,4,3,'H'));
			cars.put('g', new Car('g',4,6,3,'H'));
			cars.put('*', new Car('*',4,2,2,'V'));
			
			level = new Level(level1Board, cars);
		}
		private char level1Board[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
				 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
				 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
				 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
				 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
				 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
				 {'+', '+', '+', '+', '@', '+', '+', '+'}};
		
		@Test
		void testUndoMovement() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('c', 2, 'D');
			level.undoMovement();
			
			assertArrayEquals(level1Board, level.getBoard());
		}
		
		@Test
		void testUndoMovement2() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('g', 1, 'L');
			level.undoMovement();
			
			assertArrayEquals(level1Board, level.getBoard());
		}
		
		@Test
		void testUndoMovement3() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('e', 1, 'U');
			level.moveCar('g', 3, 'L');
			level.moveCar('c', 1, 'D');
			level.undoMovement();
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
					 {'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					 {'+', 'g', 'g', 'g', ' ', ' ', ' ', '+'},
					 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(expectedBoard, level.getBoard());
		}
		
		@Test
		void testUndoMovement4() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('e', 1, 'U');
			level.moveCar('g', 3, 'L');
			level.moveCar('c', 1, 'D');
			level.undoMovement();
			level.undoMovement();
			level.undoMovement();
			
			assertArrayEquals(level1Board, level.getBoard());
		}
		
		@Test
		void testUndoMovementInvalid() throws SameMovementException, CannotUndoMovementException {
			// If there are no moves recorded, you can't undo
			assertThrows(CannotUndoMovementException.class, () -> level.undoMovement());
		}
		
		@Test
		void testUndoMovementInvalid2() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('e', 1, 'U');
			assertDoesNotThrow(() -> level.undoMovement());
			assertThrows(CannotUndoMovementException.class, () -> level.undoMovement());
		}
	}
	
	@DisplayName ("Tests related to testing the level score")
	@Nested
	class ScoreTests {
		private Level level;
		
		@BeforeEach
		void runBeforeEach () {
			/* Note: In the game board and cars are given by LevelReader and LevelConverter classes,
			 * but here they are introduced manually in order to test only the Level class*/
			char level1Board[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
									 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
									 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
									 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
									 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
									 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
									 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
									 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			Map<Character, Car> cars = new HashMap<>();
			cars.put('a', new Car('a',1,1,2,'H'));
			cars.put('b', new Car('b',3,1,3,'H'));
			cars.put('c', new Car('c',6,1,2,'V'));
			cars.put('d', new Car('d',1,3,3,'V'));
			cars.put('e', new Car('e',2,5,2,'V'));
			cars.put('f', new Car('f',3,4,3,'H'));
			cars.put('g', new Car('g',4,6,3,'H'));
			cars.put('*', new Car('*',4,2,2,'V'));
			
			level = new Level(level1Board, cars);
		}
		@Test
		void testInitialScore() {
			assertEquals(0, level.getScore());
		}
		
		@Test
		void testScoreAfter1Move() throws SameMovementException {
			level.moveCar('e', 1, 'U');
			
			assertEquals(1, level.getScore());
		}
		
		@Test
		void testScoreAfterSomeMoves() throws SameMovementException {
			level.moveCar('e', 1, 'U');
			level.moveCar('f', 1, 'R');
			level.moveCar('c', 1, 'D');
			
			assertEquals(3, level.getScore());
		}
		
		@Test
		void testScoreAfterMoveMoreThanOneTile() throws SameMovementException {
			level.moveCar('c', 3, 'D');
			
			assertEquals(1, level.getScore());
		}
		
		@Test
		void testScoreAfterInvalidMove() throws SameMovementException {
			level.moveCar('c', 1, 'U');
			
			assertEquals(0, level.getScore());
		}
		
		@Test
		void testScoreAfterSomeInvalidMoves() throws SameMovementException {
			level.moveCar('c', 1, 'U');
			level.moveCar('a', 1, 'R');
			assertEquals(0, level.getScore());
		}
		
		@Test
		void testScoreAfterSomeValidAndInvalidMoves() throws SameMovementException {
			level.moveCar('c', 1, 'U');
			level.moveCar('e', 1, 'U');
			level.moveCar('f', 1, 'R');
			level.moveCar('a', 1, 'L');
			assertEquals(2, level.getScore());
		}
		
		@Test
		void testScoreAfterUndo() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('e', 1, 'U');
			level.undoMovement();
			
			assertEquals(0, level.getScore());
		}
		
		@Test
		void testScoreAfterUndo2() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('e', 1, 'U');
			level.moveCar('f', 1, 'R');
			level.moveCar('c', 1, 'D');
			level.undoMovement();
			
			assertEquals(2, level.getScore());
		}
	}
	
	@DisplayName ("Tests related to testing the dimensions level")
	@Nested
	class DimensionsTests {
		private Level level;
		
		@BeforeEach
		void runBeforeEach () {
			/* Note: In the game board and cars are given by LevelReader and LevelConverter classes,
			 * but here they are introduced manually in order to test only the Level class*/
			char level1Board[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
									 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
									 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
									 {'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
									 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
									 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
									 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
									 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			Map<Character, Car> cars = new HashMap<>();
			cars.put('a', new Car('a',1,1,2,'H'));
			cars.put('b', new Car('b',3,1,3,'H'));
			cars.put('c', new Car('c',6,1,2,'V'));
			cars.put('d', new Car('d',1,3,3,'V'));
			cars.put('e', new Car('e',2,5,2,'V'));
			cars.put('f', new Car('f',3,4,3,'H'));
			cars.put('g', new Car('g',4,6,3,'H'));
			cars.put('*', new Car('*',4,2,2,'V'));
			
			level = new Level(level1Board, cars);
		}
		@Test
		void testGetDimensions() {
			Pair<Integer,Integer> dimensions = level.getDimensions();
			assertEquals(8, dimensions.getLeft());
			assertEquals(8, dimensions.getRight());
		}
	}
}

