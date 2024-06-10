package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelReader {
    public final String LEVEL_FILE_NAME_FORMAT = "src/main/java/es/upm/pproject/parkingjam/parking_jam/levels/level_%d.txt";
    public final String LEVEL_SAVED_FILE_NAME_FORMAT = "src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/level.txt";

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private GameSaver gameSaver = new GameSaver();
    private int levelNumber; // Añadido para guardar el número del nivel

    public int getLevelNumber() {
        return levelNumber;
    }

    public char[][] readMap(int level, boolean loadSaved) {
        String fileName = loadSaved ? LEVEL_SAVED_FILE_NAME_FORMAT : LEVEL_FILE_NAME_FORMAT;
        logger.info("Reading map from file: '{}'...", String.format(fileName, level));
        BufferedReader reader = null;
        char[][] board = null;

        try {
            // Abrir el archivo
            reader = new BufferedReader(new FileReader(String.format(fileName, level)));

            // Leer la primera línea que contiene el nombre del nivel
            String levelName = reader.readLine();
            if (levelName != null) {
                gameSaver.saveLevelName(levelName);
                levelNumber = extractLevelNumber(levelName); // Extraer y guardar el número de nivel

                // Leer la segunda línea que contiene las dimensiones del tablero
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
                            // Leer las siguientes nRows líneas que contienen los elementos del tablero
                            board = new char[nRows][nColumns];
                            for (int i = 0; i < nRows; i++) {
                                String row = reader.readLine();
                                realCols = row.length();
                                for (int j = 0; j < nColumns; j++) {
                                    board[i][j] = row.charAt(j);
                                }
                                realRows++;
                            }
                            if (realCols != nColumns || realRows != nRows) {
                                logger.error("The dimensions of the board does not match the ones specified in the file.");
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

            // Cerrar el archivo
            reader.close();

        } catch (Exception e) {
            logger.error("There was an error while reading the file: {}.", e.getMessage());
            try {
                reader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        if (board != null)
            logger.info("Map has been read: \n {}", charMatrixToString(board));
        return board;
    }

    private int extractLevelNumber(String levelName) {
		if(levelName.equals("Initial level"))
			return 1;
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

    public GameSaver getGameSaver() {
        return gameSaver;
    }
}
