package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalDirectionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.InvalidMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.LevelAlreadyFinishedException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.MovementOutOfBoundariesException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.NullBoardException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.model.Level;
import es.upm.pproject.parkingjam.parking_jam.utilities.Car;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

public class LevelTest {
	private Level level;

	@BeforeEach
	void runBeforeEach () throws NullBoardException, IllegalCarDimensionException {
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
	
	@DisplayName ("Tests related to testing the contructor of  Level")
	@Nested
	class ContructorTests {
		@Test
		void testConstructor() throws NullBoardException, IllegalCarDimensionException {
			char board[][] =	{{'+', '+', '+', '+'},
					{'+', 'a', 'a', '+'},
					{'+', '+', '@', '+'}};
			
			Map<Character, Car> cars = new HashMap<>();
			cars.put('a', new Car('a',1,1,2,'H'));
			
			Level level = new Level(board, cars, null);
			
			assertNotNull(level);
		}
		
		@Test
		void testConstructorNotOk() throws NullBoardException {
			assertThrows(NullBoardException.class, () -> new Level(null,null,null));
		}
	}

	@DisplayName ("Tests related to testing the movement of the cars")
	@Nested
	class MovementTests {
		
		@Test
		void testMove1TileLeft() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
		void testMove1TileRight() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
		void testMove1TileUp() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
		void testMove2TilesLeft() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
		void testMove2TilesRight() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
		void testMove2TilesUp() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
		void testMove2TilesDown() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
		void testMoveAgainstLeftWall() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(MovementOutOfBoundariesException.class, () -> level.moveCar('c', 1, 'U', false));
		}

		@Test
		void testMoveAgainstRightWall() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(MovementOutOfBoundariesException.class, () -> level.moveCar('g', 5, 'R', false));
		}

		@Test
		void testMoveAgainstUpperWall() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(MovementOutOfBoundariesException.class, () -> level.moveCar('a', 1, 'L', false));
		}

		@Test
		void testMoveAgainstBottomWall() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(MovementOutOfBoundariesException.class, () -> level.moveCar('d', 5, 'D', false));
		}

		@Test
		void testMoveInvalidDirection() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(InvalidMovementException.class, () -> level.moveCar('d', 1, 'R', false));
		}

		@Test
		void testMoveInvalidDirection2() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(InvalidMovementException.class, () -> level.moveCar('a', 1, 'D', false));
		}

		@Test
		void testMoveAgainstOtherCar() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(InvalidMovementException.class, () -> level.moveCar('a', 1, 'R', false));
		}

		@Test
		void testMoveAgainstOtherCar2() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(InvalidMovementException.class, () -> level.moveCar('*', 1, 'D', false));
		}
		
		@Test
		void testInvalidDirection() {
			assertThrows(IllegalDirectionException.class, () -> level.moveCar('*', 1, 'Q', false));
		}
		
		@Test
		void testInvalidCar() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(IllegalCarException.class, () -> level.moveCar('-', 1, 'D', false));
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
		void testUndoMovement() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			
			level.moveCar('c', 2, 'D', false);
			level.undoMovement();

			assertArrayEquals(level1Board, level.getBoard());
		}

		@Test
		void testUndoMovement2() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			
			level.moveCar('g', 1, 'L', false);
			level.undoMovement();

			assertArrayEquals(level1Board, level.getBoard());
		}

		@Test
		void testUndoMovement3() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
		void testUndoMovement4() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('e', 1, 'U', false);
			level.moveCar('g', 3, 'L', false);
			level.moveCar('c', 1, 'D', false);
			level.undoMovement();
			level.undoMovement();
			level.undoMovement();

			assertArrayEquals(level1Board, level.getBoard());
		}

		@Test
		void testUndoMovementInvalid() {
			// If there are no moves recorded, you can't undo
			assertThrows(CannotUndoMovementException.class, () -> level.undoMovement());
		}

		@Test
		void testUndoMovementInvalid2() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('e', 1, 'U', false);
			assertDoesNotThrow(() -> level.undoMovement());
			assertThrows(CannotUndoMovementException.class, () -> level.undoMovement());
		}
		
		@Test
		void testUndoAfterReset() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('e', 1, 'U', false);
			level.moveCar('g', 1, 'L', false);
			level.moveCar('c', 1, 'D', false);
			
			level.resetLevel();
			level.undoMovement(); // this should revoke the reset
			
			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', ' ', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', 'c', ' '},
					{'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', ' ', 'g', 'g', 'g', ' ', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};

			assertArrayEquals(expectedBoard, level.getBoard());
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
		void testScoreAfter1Move() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('e', 1, 'U', false);

			assertEquals(1, level.getScore());
		}

		@Test
		void testScoreAfterSomeMoves() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('e', 1, 'U', false);
			level.moveCar('f', 1, 'R', false);
			level.moveCar('c', 1, 'D', false);

			assertEquals(3, level.getScore());
		}

		@Test
		void testScoreAfterMoveMoreThanOneTile() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('c', 3, 'D', false);

			assertEquals(1, level.getScore());
		}

		@Test
		void testScoreAfterUndo() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('e', 1, 'U', false);
			level.undoMovement();

			assertEquals(0, level.getScore());
		}

		@Test
		void testScoreAfterUndo2() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
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
	
	@DisplayName ("Tests related to testing the getter of the movements' history")
	@Nested
	class HistoryTests {
		@Test
		void testGetHistoryOneMovement() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('g',1,'L', false);
			List<Pair<Character,Pair<Integer,Character>>> movements = level.getHistory();
			assertEquals(1, movements.size());
			Pair<Character,Pair<Integer,Character>> movement = movements.get(0);
			
			// When saving the movement, we save the opposite direction to the move done (if 'L' we save 'R')
			assertTrue('g' == movement.getLeft() && 1 == movement.getRight().getLeft() && 'R' == movement.getRight().getRight());
		}
		
		@Test
		void testGetHistoryVariousMovements() throws SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			level.moveCar('e', 1, 'U', false);
			level.moveCar('g', 3, 'L', false);
			List<Pair<Character,Pair<Integer,Character>>> movements = level.getHistory();
			
			assertTrue('e' == movements.get(0).getLeft() && 1 == movements.get(0).getRight().getLeft() && 'D' == movements.get(0).getRight().getRight());
			assertTrue('g' == movements.get(1).getLeft() && 3 == movements.get(1).getRight().getLeft() && 'R' == movements.get(1).getRight().getRight());

		}
		
		@Test
		void testGetHistoryNoMovements(){
			List<Pair<Character,Pair<Integer,Character>>> movements = level.getHistory();
			assertEquals(0, movements.size());
		}
	}
	
	@DisplayName ("Tests related to testing the reset system")
	@Nested
	class ResetTests {
		@Test
		void resetLevelNoMovements() throws NullBoardException { // reset with no movements should leave the board as it was
			level.resetLevel();
			
			char level1Board[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
					{'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(level1Board, level.getBoard());
		}
		
		@Test
		void resetLevel() throws NullBoardException, SameMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException { // reset with no movements should leave the board as it was
			level.moveCar('e', 1, 'U', false);
			level.moveCar('g', 3, 'L', false);
			
			level.resetLevel();
			
			char level1Board[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', ' ', ' '},
					{'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};
			
			assertArrayEquals(level1Board, level.getBoard());
		}
	}
}
