package es.upm.pproject.parkingjam.parking_jam;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import es.upm.pproject.parkingjam.parking_jam.model.exceptions.*;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.Level;

public class GameTest {
	Game game;

	@DisplayName ("Tests related to testing the actions related to level")
	@Nested
	class GameLevelTests {
		
		@Test
		void getDimensionsTest1() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(-1);
			
			int x = game.getDimensions().getLeft();
			int y = game.getDimensions().getRight();
			assertEquals(8,x);
			assertEquals(8,y);
		}

		@Test
		void getDimensionsTest2() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(-13);
			
			int x = game.getDimensions().getLeft();
			int y = game.getDimensions().getRight();
			assertEquals(8,x);
			assertEquals(10,y);
		}
		
		@Test
		void getInitialBoard() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
		    game = new Game();
		    char level1Board[][] = {
		            {'+', '+', '+', '+', '+', '+', '+', '+'},
		            {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
		            {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
		            {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
		            {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
		            {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
		            {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
		            {'+', '+', '+', '+', '@', '+', '+', '+'}
		    };
		    
		    char[][] realBoard = game.getBoard();
		    assertArrayEquals(level1Board, realBoard);
		}


		@Test
		void getBoardTest1() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(-1);
			
			char expectedBoard[][] =	
				{{'+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
				 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
				 {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
				 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
				 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
				 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
				 {'+', '+', '+', '+', '@', '+', '+', '+'}};
			char [][] realBoard = game.getBoard();
			assertArrayEquals(expectedBoard,realBoard);
		}
		
		@Test
		void getBoardTest2() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(-13);

			char expectedBoard[][] =	
				{{'+', '+', '+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', ' ', ' ', ' ', ' ', ' ', 'f', 'f', 'f', '+'},
				 {'+', 'a', 'a', 'b', '*', ' ', ' ', ' ', ' ', '+'},
				 {'+', 'c', ' ', 'b', '*', ' ', ' ', ' ', ' ', '+'},
				 {'+', 'c', 'd', 'd', 'd', ' ', ' ', ' ', ' ', '+'},
				 {'+', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '+'},
				 {'+', 'e', 'e', 'e', ' ', ' ', ' ', ' ', ' ', '+'},
				 {'+', '+', '+', '+', '@', '+', '+', '+', '+', '+'}};
			char [][] realBoard = game.getBoard();
			assertArrayEquals(expectedBoard,realBoard);
		}

		@Test
		void loadNewLevelTest() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			int result = 1;
			game = new Game(result);
			result = game.loadNewLevel();

			assertEquals(2, result);
			char expectedBoard[][] =	
				{{'+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', 'a', ' ', ' ', 'b', 'b', 'b', '+'},
				 {'+', 'a', ' ', ' ', 'd', ' ', 'c', '+'},
				 {'+', '*', '*', ' ', 'd', 'e', 'c', '@'},
				 {'+', 'j', 'j', 'j', ' ', 'e', 'c', '+'},
				 {'+', ' ', ' ', 'h', ' ', 'f', 'f', '+'},
				 {'+', 'i', 'i', 'h', 'g', 'g', ' ', '+'},
				 {'+', '+', '+', '+', '+', '+', '+', '+'}};
			char [][] realBoard = game.getBoard();
			assertArrayEquals(expectedBoard,realBoard);
		}
		
		@Test
		void getLevelNumberTest() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(1);
			assertEquals(1, game.getLevelNumber());
		}
		
		@Test
		void getLevelTest() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(1);
			Level level = game.getLevel();
			assertNotNull(level);
		}
		@Test
		void getCarsTest() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
            game = new Game(1);
            assertNotNull(game.getCars());
        }
		@Test
		 void resetLevelTest() throws IllegalExitsNumberException, IllegalCarDimensionException, IllegalDirectionException, NullBoardException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException {
	            game = new Game(1);
	            game.moveCar('c', 1, 'D');
	            game.resetLevel();
	            char[][] initialBoard = {
	                {'+', '+', '+', '+', '+', '+', '+', '+'},
	                {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
	                {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
	                {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
	                {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
	                {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
	                {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
	                {'+', '+', '+', '+', '@', '+', '+', '+'}
	            };
	            assertArrayEquals(initialBoard, game.getBoard());
	        }

	}

	@DisplayName ("Tests related to testing the movement of the cars")
	@Nested
	class GameMovementTests {	

		@BeforeEach
		void runBeforeEach()  throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(-1);
		}
		
		@Test
		void testMove1TileLeft() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			char[][] newBoard = game.moveCar('g',1,'L');

			char expectedBoard[][] =	{
					{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
					{'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', 'e', 'g', 'g', 'g', ' ', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};
			assertArrayEquals(expectedBoard, newBoard);
		}

		@Test
		void testMove1TileRight() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			char[][] newBoard = game.moveCar('f',1,'R');

			char expectedBoard[][] =	{
					{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
					{'+', 'd', ' ', ' ', 'f', 'f', 'f', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};

			assertArrayEquals(expectedBoard, newBoard);
		}

		@Test
		void testMove1TileUp() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			char[][] newBoard = game.moveCar('e',1,'U');

			char expectedBoard[][] =	{
					{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
					{'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', ' ', ' ', 'g', 'g', 'g', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};

			assertArrayEquals(expectedBoard, newBoard);
		}
		@Test
        void testMove1TileDown() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
            char[][] newBoard = game.moveCar('c', 1, 'D');

			char expectedBoard[][] =	{
					{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', ' ', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};
     
            assertArrayEquals(expectedBoard, newBoard);
        }
		@Test
		void testMove2TilesLeft() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			// To do this tests we need to move another vehicle first to make space for the 2-tiles left movement
			game.moveCar('e',1,'U');
			char[][] newBoard = game.moveCar('g', 2, 'L');


			char expectedBoard[][] =	{
					{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
					{'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', 'g', 'g', 'g', ' ', ' ', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};

			assertArrayEquals(expectedBoard, newBoard);
		}

		@Test
		void testMove2TilesRight() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			// To do this tests we need to move t vehicle first to make space for the 2-tiles left movement
			game.moveCar('f',1,'L');
			char[][] newBoard = game.moveCar('f', 2, 'R');


			char expectedBoard[][] =	{
					{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
					{'+', 'd', ' ', ' ', 'f', 'f', 'f', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};

			assertArrayEquals(expectedBoard, newBoard);
		}

		@Test
		void testMove2TilesUp() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			char[][] newBoard = game.moveCar('e', 2, 'U');

			char expectedBoard[][] =	{
					{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', 'e', ' ', '*', ' ', ' ', '+'},
					{'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', ' ', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', ' ', ' ', 'g', 'g', 'g', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};

			assertArrayEquals(expectedBoard, newBoard);
		}

		@Test
		void testMove2TilesDown() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			char[][] newBoard = game.moveCar('c', 2, 'D');

			char expectedBoard[][] =	{
					{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', ' ', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', ' ', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', 'f', 'f', 'f', 'c', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};

			assertArrayEquals(expectedBoard, newBoard);
		}

		@Test
		void testMoveAgainstLeftWall() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			
			assertThrows(MovementOutOfBoundariesException.class, () -> game.moveCar('c', 1, 'U'));
		}

		@Test
		void testMoveAgainstRightWall() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			
			assertThrows(MovementOutOfBoundariesException.class, () -> game.moveCar('g', 5, 'R'));
		}

		@Test
		void testMoveAgainstUpperWall() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {

			assertThrows(MovementOutOfBoundariesException.class, () -> game.moveCar('a', 1, 'L'));
		}

		@Test
		void testMoveAgainstBottomWall() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {

			assertThrows(MovementOutOfBoundariesException.class, () -> game.moveCar('d', 5, 'D'));
		}

		@Test
		void testMoveInvalidDirection() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(InvalidMovementException.class, () -> game.moveCar('d', 1, 'R'));
		}
		
		@Test
		void testMoveInvalidDirection2() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(InvalidMovementException.class, () -> game.moveCar('a', 1, 'D'));
		}

		@Test
		void testMoveAgainstOtherCar() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(InvalidMovementException.class, () -> game.moveCar('a', 1, 'R'));
		}

		@Test
		void testMoveAgainstOtherCar2() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			assertThrows(InvalidMovementException.class, () -> game.moveCar('*', 1, 'D'));
		}
	}
	
	@DisplayName ("Tests related to testing the undo movement of the cars")
	@Nested
	class GameUndoMovementTests {	
		private char levelBaseBoard[][];

		@BeforeEach
		void runBeforeEach() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(-1);
			levelBaseBoard = game.getBoard();
		}
		
		@Test
		void testUndoMovement() throws CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {

			game.moveCar('c', 2, 'D');
			game.undoMovement();

			assertArrayEquals(levelBaseBoard, game.getBoard());
		}

		@Test
		void testUndoMovement2() throws CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {

			game.moveCar('g', 1, 'L');
			game.undoMovement();

			assertArrayEquals(levelBaseBoard, game.getBoard());
		}

		@Test
		void testUndoMovement3() throws CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			game.moveCar('e', 1, 'U');
			game.moveCar('g', 3, 'L');
			game.moveCar('c', 1, 'D');
			game.undoMovement();

			char expectedBoard[][] =	{{'+', '+', '+', '+', '+', '+', '+', '+'},
					{'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
					{'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
					{'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
					{'+', 'd', 'e', 'f', 'f', 'f', ' ', '+'},
					{'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
					{'+', 'g', 'g', 'g', ' ', ' ', ' ', '+'},
					{'+', '+', '+', '+', '@', '+', '+', '+'}};

			assertArrayEquals(expectedBoard, game.getBoard());
		}

		@Test
		void testUndoMovement4() throws CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			game.moveCar('e', 1, 'U');
			game.moveCar('g', 3, 'L');
			game.moveCar('c', 1, 'D');
			game.undoMovement();
			game.undoMovement();
			game.undoMovement();

			assertArrayEquals(levelBaseBoard, game.getBoard());
		}

		@Test
		void testUndoMovementInvalid() {
			// If there are no moves recorded, you can't undo
			assertThrows(CannotUndoMovementException.class, () -> game.undoMovement());
			
		}

		@Test
		void testUndoMovementInvalid2() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			game.moveCar('e', 1, 'U');
			assertDoesNotThrow(() -> game.undoMovement());
			assertThrows(CannotUndoMovementException.class, () -> game.undoMovement());
		}
	}

	@DisplayName ("Tests related to testing the games scores")
	@Nested
	class GameScoreTests {

		@BeforeEach
		void runBeforeEach() throws IllegalExitsNumberException, IllegalCarDimensionException, NullBoardException {
			game = new Game(2);
		}

		@Test
		void testInitialScore() {
			assertEquals(0, game.getLevelScore());
			assertEquals(0, game.getGameScore());
		}

		@Test
		void testScoreAfter1Move() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			game.moveCar('b', 1, 'L');
			assertEquals(1, game.getLevelScore());
			assertEquals(1, game.getGameScore());
		}

		@Test
		void testScoreAfterSomeMoves() throws IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
			game.moveCar('*', 1, 'R');
			game.moveCar('a', 1, 'D');
			game.moveCar('b', 2, 'L');
			assertEquals(3, game.getLevelScore());
			assertEquals(3, game.getGameScore());
		}


		@Test
		void testGameScoreAfter2Levels() throws IllegalExitsNumberException, IllegalCarDimensionException, IllegalDirectionException, NullBoardException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException{
			game.moveCar('*', 1, 'R');
			game.moveCar('a', 1, 'D');
			game.moveCar('b', 1, 'L');
			game.loadNewLevel();
			game.moveCar('d', 2, 'R');
			game.moveCar('e', 3, 'R');
			game.moveCar('c', 2, 'D');
			assertEquals(6, game.getGameScore());
		}
		
		@Test
        void testScoreAfterUndo() throws CannotUndoMovementException, IllegalDirectionException, LevelAlreadyFinishedException, IllegalCarException, InvalidMovementException, MovementOutOfBoundariesException, NullBoardException {
            game.moveCar('*', 1, 'R');
            game.undoMovement();
            assertEquals(0, game.getLevelScore());
        }
	}
}
