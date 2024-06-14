package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.*;
import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

/**
 * The GameSaver class is responsible for saving and loading the game state, including the level,
 * score, and move history. It handles the creation and management of necessary files.
 */
public class GameSaver {
	private File level;       // File for saving the current level state
	private File score; 	  // File for saving the punctuation (score)
	private File history;     // File for saving the move history
	private String levelName; // The name of the current level
	private String dimensions;// The dimensions of the current level
	private static final Logger logger = LoggerFactory.getLogger(GameSaver.class);
	/**
	 * Initializes file objects for saving level, score, and history.
	 * Creates the files if they do not exist.
	 */
	public GameSaver() {
		logger.info("Creating new GameSaver...");
		level = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/level.txt");
		score = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/punctuation.txt");
		history = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/history.txt");
		try {
			if (!level.exists()) {
				logger.info("Creating level.txt file...");
				level.getParentFile().mkdirs();
				level.createNewFile();
				logger.info("The file level.txt has been created.");
			}
			if (!score.exists()) {
				logger.info("Creating punctuation.txt file...");
				score.getParentFile().mkdirs();
				score.createNewFile();
				logger.info("The file punctuation.txt has been created.");
			}
			if (!history.exists()) {
				logger.info("Creating history.txt file...");
				history.getParentFile().mkdirs();
				history.createNewFile();
				logger.info("The file history.txt has been created.");
			}
			logger.info("GameSaver created.");
		} catch (IOException e) {
			logger.error("There was a problem creating GameSaver: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Saves the entire game state including history, score, and board configuration.
	 * 
	 * @param history the list of move history.
	 * @param total the total score.
	 * @param level the current level number.
	 * @param board the current board configuration.
	 */
	public void saveGame(List<Pair<Character, Pair<Integer, Character>>> history, int total, int level, char[][] board, String levelName) {
		logger.info("Saving game...");
		saveLevelName(levelName);
		saveHistory(history);
		saveScore(total, level);
		saveBoard(board);
		logger.info("The game has been saved.");
	}

	/**
	 * Saves the level name to be used when saving the board.
	 * 
	 * @param levelName the name of the level.
	 */
	public void saveLevelName(String levelName) {
		this.levelName = levelName;
		logger.info("The name of the level has been saved.");
	}

	/**
	 * Saves the level dimensions to be used when saving the board.
	 * 
	 * @param dimensions the dimensions of the level.
	 */
	public void saveLevelDimensions(String dimensions) {
		this.dimensions = dimensions;
		logger.info("The dimensions of the level have been saved.");
	}

	/**
	 * Saves the current board configuration to a file.
	 * 
	 * @param board the current board configuration.
	 */
	public void saveBoard(char[][] board) {
		logger.info("Saving level board...");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(level))) {
			writer.write(this.levelName);
			writer.newLine();
			writer.write(this.dimensions);
			writer.newLine();
			for (char[] row : board) {
				writer.write(row);
				writer.newLine();
			}
			logger.info("The level board has been saved.");
		} catch (IOException e) {
			logger.error("There was an error saving the board: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Saves the current score and level number to a file.
	 * 
	 * @param total the total score.
	 * @param level the current level number.
	 */
	public void saveScore(int total, int level) {
		logger.info("Saving level score...");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(score))) {
			writer.write("" + total);
			writer.newLine();
			writer.write("" + level);
			writer.newLine();
			logger.info("The level score has been saved.");
		} catch (IOException e) {
			logger.error("There was an error saving the score: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Saves the movements history to a file.
	 * 
	 * @param list the list of move history.
	 */
	public void saveHistory(List<Pair<Character, Pair<Integer, Character>>> list) {
		logger.info("Saving level history...");
		// Save history
		try (BufferedWriter movWriter = new BufferedWriter(new FileWriter(history))) {

			for (Pair<Character, Pair<Integer, Character>> mov : list) {
				movWriter.write(mov.getLeft() + " " + mov.getRight().getLeft() + " " + mov.getRight().getRight());
				movWriter.newLine();
			}
			logger.info("The level history has been saved.");
		} catch (IOException e) {
			logger.error("There was an error saving the history: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Loads the move history from a file.
	 * 
	 * @param cols the number of columns in the board (unused).
	 * @param rows the number of rows in the board (unused).
	 * @return the list of move history.
	 */
	public List<Pair<Character, Pair<Integer,Character>>> loadHistory(int cols, int rows) {
		logger.info("Loading history...");
		List<Pair<Character, Pair<Integer,Character>>> list = new ArrayList<>();
		try (BufferedReader boardReader = new BufferedReader(new FileReader(history))) {
			String line;
			while ((line = boardReader.readLine()) != null) {
				String[] moveData = line.split(" ");
				char car = moveData[0].charAt(0);
				int length = Integer.parseInt(moveData[1]);
				char way = moveData[2].charAt(0);
				list.add(new Pair<>(car, new Pair<>(length, way)));
			}
			logger.info("The level history has been loaded.");
		} catch (IOException e) {
			logger.error("There was an error loading the history: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Retrieves the dimensions of the board.
	 * @returns the board dimensions (NxM)
	 */
	public String getDimensions() {
		logger.info("Getting dimensions...");
		logger.info("The dimensions: [{}] have been given.", dimensions );
		return dimensions;
	}
	/**
	 * Retrieves the name of the level.
	 * @returns the name of the level
	 */
	public String getLevelName() {
		logger.info("Getting level name...");
		logger.info("The level name: [{}] has been given.", levelName );
		return levelName;
	}
}
