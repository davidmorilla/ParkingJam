package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The 'LevelReader' class is responsible for reading level data from text files.
 * It reads the level name, dimensions, and the board configuration from a specified file.
 */
public class LevelReader {
	
	// File name formats for level data and saved game data
    public static final String LEVEL_FILE_NAME_FORMAT = "src/main/java/es/upm/pproject/parkingjam/parking_jam/levels/level_%d.txt";
    public static final String LEVEL_SAVED_FILE_NAME_FORMAT = "src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/level.txt";

    private static final Logger logger = LoggerFactory.getLogger(LevelReader.class);
    private GameSaver gameSaver = new GameSaver();
    private int levelNumber;  // Added to store the level number

    /**
     * Gets the current level number.
     * 
     * @return the level number.
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Reads the map configuration for a given level.
     * 
     * @param level the level number to read.
     * @param loadSaved flag indicating whether to load a saved game.
     * @return a 2D char array representing the board configuration.
     */
    public char[][] readMap(int level, boolean loadSaved) {
        String fileName = loadSaved ? LEVEL_SAVED_FILE_NAME_FORMAT : LEVEL_FILE_NAME_FORMAT;
        logger.info("Reading map from file: '{}'...", String.format(fileName, level));
        BufferedReader reader = null;
        char[][] board = null;

        try {
        	// Open the file
            reader = new BufferedReader(new FileReader(String.format(fileName, level)));

            // Read the first line containing the level name
            String levelName = reader.readLine();
            if (levelName != null) {
                gameSaver.saveLevelName(levelName);
                levelNumber = extractLevelNumber(levelName); // Extract and store the level number
                if(levelNumber == -1) {
                	return null;
                }
             // Read the second line containing the board dimensions
                String secondLine = reader.readLine();
                if (secondLine != null) {
                    gameSaver.saveLevelDimensions(secondLine);
                    String[] dimensiones = secondLine.split(" ");
                    if (dimensiones.length == 2) {
                        int nRows = Integer.parseInt(dimensiones[0]);
                        int nColumns = Integer.parseInt(dimensiones[1]);
                        if (nRows >= 3 && nColumns >= 3) {
                        	
                            int realRows = 0;
                            int realCols = 0;
                            // Read the next nRows lines containing the board elements
                            board = new char[nRows][nColumns];
                            for (int i = 0; i < nRows; i++) {
                                String row = reader.readLine();
                                realCols = row.length();
                                for (int j = 0; j < nColumns; j++) {
                                	try {
                                    board[i][j] = row.charAt(j);
                                	}catch(IndexOutOfBoundsException e) {
                                		logger.error("The dimensions of the board do not match the ones specified in the file.");
                                        reader.close();
                                        return null;
                                	}
                                }
                                realRows++;
                            }
                            
                            // Check if each row matches the specified number of columns
                            if (realCols != nColumns || realRows != nRows) {
                                logger.error("The dimensions of the board do not match the ones specified in the file.");
                                reader.close();
                                return null;
                            }
                        } else {
                            logger.error("The board is too small.");
                        }
                    } else {
                        logger.error("The dimensions of the board are not in the correct format.");
                    }
                } else {
                    logger.error("The dimensions of the board are not specified.");
                }
            } else {
                logger.error("The name of the board is not specified.");
            }

            // Close the file
            reader.close();

        } catch (Exception e) {
            logger.error("There was an error while reading the file: {}.", e.getMessage());
            try {
                reader.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }
        
     // Log the read map if successfully read
        if (board != null)
            logger.info("Map has been read: \n {}", charMatrixToString(board));
        return board;
    }

    /**
     * Extracts the level number from the level name string.
     * 
     * @param levelName the name of the level.
     * @return the extracted level number, or -1 if it cannot be extracted.
     */
    private int extractLevelNumber(String levelName) {
		if(levelName.equals("Initial level"))
			return 1; // Special case for the initial level
        String[] parts = levelName.split(" ");
        if (parts.length > 1 && parts[0].equalsIgnoreCase("Level")) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                logger.error("Could not extract level number from: {}", levelName);
            }
        }
        return -1; // Error case
    }
    /**
     * Converts a 2D char array board to a string representation.
     * 
     * @param board the board to convert.
     * @return a string representation of the board.
     */
    private String charMatrixToString(char[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j]).append(' ');
            }
            sb.append('\n'); // New line after each row
        }
        return sb.toString();
    }
    /**
     * Gets the GameSaver instance.
     * 
     * @return the GameSaver instance.
     */
    public GameSaver getGameSaver() {
        return gameSaver;
    }
}
