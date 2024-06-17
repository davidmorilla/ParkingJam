package es.upm.pproject.parkingjam.parking_jam;

import es.upm.pproject.parkingjam.parking_jam.model.GameSaver;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSaverTest {
    private GameSaver gameSaver;
    private final String levelFilePath = "src/main/resources/savedGame/level.txt";
    private final String scoreFilePath = "src/main/resources/savedGame/punctuation.txt";
    private final String historyFilePath = "src/main/resources/savedGame/history.txt";

    @BeforeEach
    void setUp() {
        gameSaver = new GameSaver();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(levelFilePath));
        Files.deleteIfExists(Paths.get(scoreFilePath));
        Files.deleteIfExists(Paths.get(historyFilePath));
    }

    @Test
    void testLevelFileCreation() {
        assertTrue(Files.exists(Paths.get(levelFilePath)));
    }

    @Test
    void testScoreFileCreation() {
        assertTrue(Files.exists(Paths.get(scoreFilePath)));
    }

    @Test
    void testHistoryFileCreation() {
        assertTrue(Files.exists(Paths.get(historyFilePath)));
    }

    @Test
    void testSaveLevelName() {
        String levelName = "Level 1";
        gameSaver.saveLevelName(levelName);
        assertEquals(levelName, gameSaver.getLevelName());
    }


    @Test
    void testSaveLevelDimensions() {
        String dimensions = "8x8";
        gameSaver.saveLevelDimensions(dimensions);
        assertEquals(dimensions, gameSaver.getDimensions());
    }

    @Test
    void testSaveBoardSaveLevelNameFromFile() throws IOException {
    	
        gameSaver.saveLevelName("Level 1");
        gameSaver.saveLevelDimensions("8x8");

        char[][] board = 
        		{{'+', '+', '+', '+', '+', '+', '+', '+'},
   				 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
   				 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
   				 {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
   				 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
   				 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
   				 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
   				 {'+', '+', '+', '+', '@', '+', '+', '+'}};
   

        gameSaver.saveBoard(board);

        List<String> lines = Files.readAllLines(Paths.get(levelFilePath));
        assertEquals("Level 1", lines.get(0));
    }

    @Test
    void testSaveBoardSaveLevelDimensionsFromFile() throws IOException {
        gameSaver.saveLevelName("Level 1");
        gameSaver.saveLevelDimensions("8x8");

        char[][] board = 	{{'+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
				 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
				 {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
				 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
				 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
				 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
				 {'+', '+', '+', '+', '@', '+', '+', '+'}};

        gameSaver.saveBoard(board);

        List<String> lines = Files.readAllLines(Paths.get(levelFilePath));
        assertEquals("8x8", lines.get(1));
    }

    @Test
    void testSaveBoardConfigurationFromFile() throws IOException {
        gameSaver.saveLevelName("Level 1");
        gameSaver.saveLevelDimensions("8x8");

        char[][] board = 	{{'+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
				 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
				 {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
				 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
				 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
				 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
				 {'+', '+', '+', '+', '@', '+', '+', '+'}};

        gameSaver.saveBoard(board);

        List<String> lines = Files.readAllLines(Paths.get(levelFilePath));
        assertEquals("+aabbbc+", lines.get(3));
        assertEquals("+d fff +", lines.get(6));
    }

    @Test
    void testSaveScoreFromFile() throws IOException {
        int totalScore = 100;
        int level = 1;

        gameSaver.saveScore(totalScore, level);

        List<String> lines = Files.readAllLines(Paths.get(scoreFilePath));
        assertEquals(String.valueOf(totalScore), lines.get(0));
        assertEquals(String.valueOf(level), lines.get(1));
    }


    @Test
    void testSaveHistoryFromFile() throws IOException {
        List<Pair<Character, Pair<Integer, Character>>> history = new ArrayList<>();
        history.add(new Pair<>('a', new Pair<>(1, 'U')));
        history.add(new Pair<>('b', new Pair<>(2, 'D')));

        gameSaver.saveHistory(history);

        List<String> lines = Files.readAllLines(Paths.get(historyFilePath));
        assertEquals("a 1 U", lines.get(0));
        assertEquals("b 2 D", lines.get(1));
    }

    @Test
    void testLoadHistoryFirstMove() {
        List<Pair<Character, Pair<Integer, Character>>> history = new ArrayList<>();
        history.add(new Pair<>('a', new Pair<>(1, 'U')));
        history.add(new Pair<>('b', new Pair<>(2, 'D')));

        gameSaver.saveHistory(history);
        List<Pair<Character, Pair<Integer, Character>>> loadedHistory = gameSaver.loadHistory(0, 0);

        assertEquals('a', loadedHistory.get(0).getLeft());
        assertEquals(1, loadedHistory.get(0).getRight().getLeft());
        assertEquals('U', loadedHistory.get(0).getRight().getRight());
    }

    @Test
    void testLoadHistorySecondMove() {
        List<Pair<Character, Pair<Integer, Character>>> history = new ArrayList<>();
        history.add(new Pair<>('a', new Pair<>(1, 'U')));
        history.add(new Pair<>('b', new Pair<>(2, 'D')));

        gameSaver.saveHistory(history);
        List<Pair<Character, Pair<Integer, Character>>> loadedHistory = gameSaver.loadHistory(0, 0);

        assertEquals('b', loadedHistory.get(1).getLeft());
        assertEquals(2, loadedHistory.get(1).getRight().getLeft());
        assertEquals('D', loadedHistory.get(1).getRight().getRight());
    }

    @Test
    void testSaveGameSaveHistoryFromFile() throws IOException {
        List<Pair<Character, Pair<Integer, Character>>> history = new ArrayList<>();
        history.add(new Pair<>('c', new Pair<>(1, 'D')));
        history.add(new Pair<>('g', new Pair<>(1, 'L')));
        int totalScore = 100;
        int level = 1;
        String levelName = "Level 1";
        char[][] board = {
        		 {'+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
				 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
				 {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
				 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
				 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
				 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
				 {'+', '+', '+', '+', '@', '+', '+', '+'}
				 };
        gameSaver.saveLevelName(levelName);
        gameSaver.saveLevelDimensions("8x8");

        gameSaver.saveGame(history, totalScore, level, board, levelName);

        List<String> historyLines = Files.readAllLines(Paths.get(historyFilePath));
        assertEquals("c 1 D", historyLines.get(0));
        assertEquals("g 1 L", historyLines.get(1));
    }

    @Test
    void testSaveGameSavesScoreFromFile() throws IOException {
        List<Pair<Character, Pair<Integer, Character>>> history = new ArrayList<>();
        history.add(new Pair<>('c', new Pair<>(1, 'D')));
        history.add(new Pair<>('g', new Pair<>(1, 'L')));
        int totalScore = 100;
        int level = 1;
        String levelName = "Level 1";
        char[][] board = {
        		 {'+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
				 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
				 {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
				 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
				 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
				 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
				 {'+', '+', '+', '+', '@', '+', '+', '+'}
				 };
        gameSaver.saveLevelName(levelName);
        gameSaver.saveLevelDimensions("8x8");

        gameSaver.saveGame(history, totalScore, level, board, levelName);
        List<String> scoreLines = Files.readAllLines(Paths.get(scoreFilePath));
        assertEquals(String.valueOf(totalScore), scoreLines.get(0));
        assertEquals(String.valueOf(level), scoreLines.get(1));
    }

    @Test
    void testSaveGameSavesBoardFromFile() throws IOException {
        List<Pair<Character, Pair<Integer, Character>>> history = new ArrayList<>();
        history.add(new Pair<>('c', new Pair<>(1, 'D')));
        history.add(new Pair<>('g', new Pair<>(1, 'L')));
        int totalScore = 100;
        int level = 1;
        String levelName = "Level 1";
        char[][] board = {
        		 {'+', '+', '+', '+', '+', '+', '+', '+'},
				 {'+', 'a', 'a', 'b', 'b', 'b', 'c', '+'},
				 {'+', ' ', ' ', ' ', '*', ' ', 'c', '+'},
				 {'+', 'd', ' ', ' ', '*', ' ', ' ', '+'},
				 {'+', 'd', ' ', 'f', 'f', 'f', ' ', '+'},
				 {'+', 'd', 'e', ' ', ' ', ' ', ' ', '+'},
				 {'+', ' ', 'e', ' ', 'g', 'g', 'g', '+'},
				 {'+', '+', '+', '+', '@', '+', '+', '+'}
				 };
        gameSaver.saveLevelName(levelName);
        gameSaver.saveLevelDimensions("8x8");

        gameSaver.saveGame(history, totalScore, level, board, levelName);

        List<String> boardLines = Files.readAllLines(Paths.get(levelFilePath));
        assertEquals("Level 1", boardLines.get(0));
        assertEquals("8x8", boardLines.get(1));
        assertEquals("+aabbbc+", boardLines.get(3));
        assertEquals("+   * c+", boardLines.get(4));
        assertEquals("++++@+++", boardLines.get(9));
    }
}
