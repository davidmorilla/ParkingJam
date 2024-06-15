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
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.GameSaver;
import es.upm.pproject.parkingjam.parking_jam.model.Level;

public class GameTest {
	Game game;

	@DisplayName ("Tests related to testing the actions related to level")
	@Nested
	class GameLevelTests {
		
		@Test
		void getDimensionsTest1() throws IllegalExitsNumberException, IllegalCarDimensionException {
			game = new Game(-1);
			
			int x = game.getDimensions().getLeft();
			int y = game.getDimensions().getRight();
			assertEquals(8,x);
			assertEquals(8,y);
		}

		@Test
		void getDimensionsTest2() throws IllegalExitsNumberException, IllegalCarDimensionException {
			game = new Game(-13);
			
			int x = game.getDimensions().getLeft();
			int y = game.getDimensions().getRight();
			assertEquals(8,x);
			assertEquals(10,y);
		}
		
		@Test
		void getInitialBoard() throws IllegalExitsNumberException, IllegalCarDimensionException {
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
		void getBoardTest1() throws IllegalExitsNumberException, IllegalCarDimensionException {
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
		void getBoardTest2() throws IllegalExitsNumberException, IllegalCarDimensionException {
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
		void loadNewLevelTest() throws IllegalExitsNumberException, IllegalCarDimensionException {
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
		void getLevelNumberTest() throws IllegalExitsNumberException, IllegalCarDimensionException {
			game = new Game(1);
			assertEquals(1, game.getLevelNumber());
		}
		
		@Test
		void getLevelTest() throws IllegalExitsNumberException, IllegalCarDimensionException {
			game = new Game(1);
			Level level = game.getLevel();
			assertNotNull(level);
		}
		@Test
		void getCarsTest() throws IllegalExitsNumberException, IllegalCarDimensionException {
            game = new Game(1);
            assertNotNull(game.getCars());
        }
		@Test
		 void resetLevelTest() throws IllegalExitsNumberException, IllegalCarDimensionException, SameMovementException, IllegalDirectionException {
	            game = new Game(1);
	            game.moveCar('c', 1, 'L');
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
		void runBeforeEach()  throws IllegalExitsNumberException, IllegalCarDimensionException {
			game = new Game(-1);
		}
		
		@Test
		void testMove1TileLeft() throws SameMovementException, IllegalDirectionException {
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
		void testMove1TileRight() throws SameMovementException, IllegalDirectionException {
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
		void testMove1TileUp() throws SameMovementException, IllegalDirectionException {
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
        void testMove1TileDown() throws SameMovementException, IllegalDirectionException {
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
		void testMove2TilesLeft() throws SameMovementException, IllegalDirectionException {
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
		void testMove2TilesRight() throws SameMovementException, IllegalDirectionException {
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
		void testMove2TilesUp() throws SameMovementException, IllegalDirectionException {
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
		void testMove2TilesDown() throws SameMovementException, IllegalDirectionException {
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
		void testMoveAgainstLeftWall() throws SameMovementException, IllegalDirectionException {
			char[][] newBoard = game.moveCar('c', 1, 'U');

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstRightWall() throws SameMovementException, IllegalDirectionException {
			char[][] newBoard = game.moveCar('g', 5, 'R');

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstUpperWall() throws SameMovementException, IllegalDirectionException {
			char[][] newBoard = game.moveCar('a', 1, 'L');

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstBottomWall() throws SameMovementException, IllegalDirectionException {
			char[][] newBoard = game.moveCar('d', 5, 'D');

			assertNull(newBoard);
		}

		@Test
		void testMoveInvalidDirection() throws SameMovementException, IllegalDirectionException {
			char[][] newBoard = game.moveCar('d', 1, 'R');

			assertNull(newBoard);
		}

		@Test
		void testMoveInvalidDirection2() throws SameMovementException, IllegalDirectionException {
			char[][] newBoard = game.moveCar('a', 1, 'D');

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstOtherCar() throws SameMovementException, IllegalDirectionException {
			char[][] newBoard = game.moveCar('a', 1, 'R');

			assertNull(newBoard);
		}

		@Test
		void testMoveAgainstOtherCar2() throws SameMovementException, IllegalDirectionException {
			char[][] newBoard = game.moveCar('*', 1, 'D');

			assertNull(newBoard);
		}
	}
	
	@DisplayName ("Tests related to testing the undo movement of the cars")
	@Nested
	class GameUndoMovementTests {	
		private char levelBaseBoard[][];

		@BeforeEach
		void runBeforeEach() throws IllegalExitsNumberException, IllegalCarDimensionException {
			game = new Game(-1);
			levelBaseBoard = game.getBoard();
		}
		
		@Test
		void testUndoMovement() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException {

			game.moveCar('c', 2, 'D');
			game.undoMovement();

			assertArrayEquals(levelBaseBoard, game.getBoard());
		}

		@Test
		void testUndoMovement2() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException {

			game.moveCar('g', 1, 'L');
			game.undoMovement();

			assertArrayEquals(levelBaseBoard, game.getBoard());
		}

		@Test
		void testUndoMovement3() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException {
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
		void testUndoMovement4() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException {
			game.moveCar('e', 1, 'U');
			game.moveCar('g', 3, 'L');
			game.moveCar('c', 1, 'D');
			game.undoMovement();
			game.undoMovement();
			game.undoMovement();

			assertArrayEquals(levelBaseBoard, game.getBoard());
		}

		@Test
		void testUndoMovementInvalid() throws SameMovementException, CannotUndoMovementException {
			// If there are no moves recorded, you can't undo
			assertThrows(CannotUndoMovementException.class, () -> game.undoMovement());
		}

		@Test
		void testUndoMovementInvalid2() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException {
			game.moveCar('e', 1, 'U');
			assertDoesNotThrow(() -> game.undoMovement());
			assertThrows(CannotUndoMovementException.class, () -> game.undoMovement());
		}
	}

	@DisplayName ("Tests related to testing the games scores")
	@Nested
	class GameScoreTests {

		@BeforeEach
		void runBeforeEach() throws IllegalExitsNumberException, IllegalCarDimensionException {
			game = new Game(2);
		}

		@Test
		void testInitialScore() {
			assertEquals(0, game.getLevelScore());
			assertEquals(0, game.getGameScore());
		}

		@Test
		void testScoreAfter1Move() throws SameMovementException, IllegalDirectionException {
			game.moveCar('b', 1, 'L');
			assertEquals(1, game.getLevelScore());
			assertEquals(1, game.getGameScore());
		}

		@Test
		void testScoreAfterSomeMoves() throws SameMovementException, IllegalDirectionException {
			game.moveCar('*', 1, 'R');
			game.moveCar('a', 1, 'D');
			game.moveCar('b', 2, 'L');
			assertEquals(3, game.getLevelScore());
			assertEquals(3, game.getGameScore());
		}

		@Test
		void testScoreAfterSomeInvalidMoves() throws SameMovementException, IllegalDirectionException {
			game.moveCar('c', 2, 'D');
			game.moveCar('h', 1, 'U');
			assertEquals(0, game.getLevelScore());
			assertEquals(0, game.getGameScore());
		}

		@Test
		void testScoreAfterSomeValidAndInvalidMoves() throws SameMovementException, IllegalDirectionException {
			game.moveCar('c', 2, 'D');
			game.moveCar('b', 1, 'L');
			game.moveCar('i', 1, 'R');
			game.moveCar('*', 1, 'R');
			assertEquals(2, game.getLevelScore());
			assertEquals(2, game.getGameScore());
		}

		@Test
		void testGameScoreAfter2Levels() throws SameMovementException, IllegalExitsNumberException, IllegalCarDimensionException, IllegalDirectionException{
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
        void testScoreAfterUndo() throws SameMovementException, CannotUndoMovementException, IllegalDirectionException {
            game.moveCar('*', 1, 'R');
            game.undoMovement();
            assertEquals(0, game.getLevelScore());
        }
	}
	
	@DisplayName("Tests related to saving and loading game state")
    @Nested
    class GameSaveLoadTests {
	    GameSaver gameSaver;
	    
	    @BeforeEach
	    void setUp() throws IllegalExitsNumberException, IllegalCarDimensionException, SameMovementException, IllegalDirectionException {
	        // Crear un juego nuevo para cada prueba
	        game = new Game(1);
	        gameSaver = new GameSaver();
	        game.moveCar('g',1,'L');
	        game.saveGame(gameSaver);
	    }
	    
	    @Test
	    void testSameLevelNumber() throws IllegalExitsNumberException, IllegalCarDimensionException {
	    	Game loadedGame = new Game();
	        loadedGame.loadSavedLevel(gameSaver);

	        assertEquals(game.getLevelNumber(), loadedGame.getLevelNumber());
	    }
	    
	    @Test
	    void testSameBoard() throws IllegalExitsNumberException, IllegalCarDimensionException {
	    	Game loadedGame = new Game();
	        loadedGame.loadSavedLevel(gameSaver);
	        
	        char[][] originalBoard = game.getBoard();
	        char[][] loadedBoard = loadedGame.getBoard();
	        assertArrayEquals(originalBoard, loadedBoard);
	    }
	    
	    @Test
	    void testSameAcumulatedScore() throws IllegalExitsNumberException, IllegalCarDimensionException {
	    	Game loadedGame = new Game();
	        loadedGame.loadSavedLevel(gameSaver);
	        
	        assertEquals(game.getAccumulatedScore(), loadedGame.getAccumulatedScore());
	    }
	    
	    @Test
	    void testSameDimensions() throws IllegalExitsNumberException, IllegalCarDimensionException {
	    	Game loadedGame = new Game();
	        loadedGame.loadSavedLevel(gameSaver);
	        
	        Pair<Integer, Integer> originalDimensions = game.getDimensions();
	        Pair<Integer, Integer> loadedDimensions = loadedGame.getDimensions();
	        assertEquals(originalDimensions.getLeft(), loadedDimensions.getLeft());
	        assertEquals(originalDimensions.getRight(), loadedDimensions.getRight());	    
	    }
    }
}
