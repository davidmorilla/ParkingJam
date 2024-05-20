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

import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.Level;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
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

		level = new Level(level1Board, cars);
	}


	@DisplayName ("Tests related to testing the movement of the cars")
	@Nested
	class MovementTests {
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
		@Test
		void testGetDimensions() {
			Pair<Integer,Integer> dimensions = level.getDimensions();
			assertEquals(8, dimensions.getLeft());
			assertEquals(8, dimensions.getRight());
		}
	}
}
