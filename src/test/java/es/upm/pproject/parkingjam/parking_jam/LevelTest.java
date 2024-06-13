package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.pproject.parkingjam.parking_jam.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.Level;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

public class LevelTest {
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

		level = new Level(level1Board, cars, null);
	}

	@DisplayName ("Tests related to testing the movement of the cars")
	@Nested
	class MovementTests {
		
		@Test
		void testMove1TileLeft() throws SameMovementException {
			char[][] newBoard = level.moveCar('g',1,'L', false);

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
			char[][] newBoard = level.moveCar('f',1,'R', false);

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
			char[][] newBoard = level.moveCar('e',1,'U', false);

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
			level.moveCar('e',1,'U', false);
			char[][] newBoard = level.moveCar('g', 2, 'L', false);


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
			level.moveCar('f',1,'L', false);
			char[][] newBoard = level.moveCar('f', 2, 'R', false);


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
			char[][] newBoard = level.moveCar('e', 2, 'U', false);

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
			char[][] newBoard = level.moveCar('c', 2, 'D', false);

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
			char[][] newBoard = level.moveCar('c', 1, 'U', false);

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstRightWall() throws SameMovementException {
			char[][] newBoard = level.moveCar('g', 5, 'R', false);

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstUpperWall() throws SameMovementException {
			char[][] newBoard = level.moveCar('a', 1, 'L', false);

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstBottomWall() throws SameMovementException {
			char[][] newBoard = level.moveCar('d', 5, 'D', false);

			assertNull(newBoard);
		}

		@Test
		void testMoveInvalidDirection() throws SameMovementException {
			char[][] newBoard = level.moveCar('d', 1, 'R', false);

			assertNull(newBoard);
		}

		@Test
		void testMoveInvalidDirection2() throws SameMovementException {
			char[][] newBoard = level.moveCar('a', 1, 'D', false);

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstOtherCar() throws SameMovementException {
			char[][] newBoard = level.moveCar('a', 1, 'R', false);

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstOtherCar2() throws SameMovementException {
			char[][] newBoard = level.moveCar('*', 1, 'D', false);

			assertNull(newBoard);
		}
	}

	@DisplayName ("Tests related to testing the undo movement of the cars")
	@Nested
	class UndoMovementTests {	
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
			
			level.moveCar('c', 2, 'D', false);
			level.undoMovement();

			assertArrayEquals(level1Board, level.getBoard());
		}

		@Test
		void testUndoMovement2() throws SameMovementException, CannotUndoMovementException {
			
			level.moveCar('g', 1, 'L', false);
			level.undoMovement();

			assertArrayEquals(level1Board, level.getBoard());
		}

		@Test
		void testUndoMovement3() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('e', 1, 'U', false);
			level.moveCar('g', 3, 'L', false);
			level.moveCar('c', 1, 'D', false);
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
			level.moveCar('e', 1, 'U', false);
			level.moveCar('g', 3, 'L', false);
			level.moveCar('c', 1, 'D', false);
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
			level.moveCar('e', 1, 'U', false);
			assertDoesNotThrow(() -> level.undoMovement());
			assertThrows(CannotUndoMovementException.class, () -> level.undoMovement());
		}
	}

	@DisplayName ("Tests related to testing the level score")
	@Nested
	class ScoreTests {
		@Test
		void testInitialScore() {
			assertEquals(0, level.getScore());
		}

		@Test
		void testScoreAfter1Move() throws SameMovementException {
			level.moveCar('e', 1, 'U', false);

			assertEquals(1, level.getScore());
		}

		@Test
		void testScoreAfterSomeMoves() throws SameMovementException {
			level.moveCar('e', 1, 'U', false);
			level.moveCar('f', 1, 'R', false);
			level.moveCar('c', 1, 'D', false);

			assertEquals(3, level.getScore());
		}

		@Test
		void testScoreAfterMoveMoreThanOneTile() throws SameMovementException {
			level.moveCar('c', 3, 'D', false);

			assertEquals(1, level.getScore());
		}

		@Test
		void testScoreAfterInvalidMove() throws SameMovementException {
			level.moveCar('c', 1, 'U', false);

			assertEquals(0, level.getScore());
		}

		@Test
		void testScoreAfterSomeInvalidMoves() throws SameMovementException {
			level.moveCar('c', 1, 'U', false);
			level.moveCar('a', 1, 'R', false);
			assertEquals(0, level.getScore());
		}

		@Test
		void testScoreAfterSomeValidAndInvalidMoves() throws SameMovementException {
			level.moveCar('c', 1, 'U', false);
			level.moveCar('e', 1, 'U', false);
			level.moveCar('f', 1, 'R', false);
			level.moveCar('a', 1, 'L', false);
			assertEquals(2, level.getScore());
		}

		@Test
		void testScoreAfterUndo() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('e', 1, 'U', false);
			level.undoMovement();

			assertEquals(0, level.getScore());
		}

		@Test
		void testScoreAfterUndo2() throws SameMovementException, CannotUndoMovementException {
			level.moveCar('e', 1, 'U', false);
			level.moveCar('f', 1, 'R', false);
			level.moveCar('c', 1, 'D', false);

			level.undoMovement();

			assertEquals(2, level.getScore());
		}
	}

	@DisplayName ("Tests related to testing the dimensions level")
	@Nested
	class DimensionsTests {
		@Test
		void testGetDimensions() {
			Pair<Integer,Integer> dimensions = level.getDimensions();
			assertEquals(8, dimensions.getLeft());
			assertEquals(8, dimensions.getRight());
		}
	}
}
