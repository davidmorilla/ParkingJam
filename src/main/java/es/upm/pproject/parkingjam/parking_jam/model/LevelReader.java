package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedReader;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelReader {
	public final String LEVEL_FILE_NAME_FORMAT = "src/main/java/es/upm/pproject/parkingjam/parking_jam/levels/level_%d.txt";
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    
    
    public char[][] readMap(int level) {
        String fileName = LEVEL_FILE_NAME_FORMAT;
    	logger.info("Reading map from file: '{}'...",String.format(fileName, level));
		BufferedReader reader;
		char[][] board = null;
		
        try {
            // Abrir el archivo
            reader = new BufferedReader(new FileReader(String.format(fileName, level)));		

            // Leer la primera línea que contiene el nombre del nivel
            String levelName = reader.readLine();
			
			if(levelName != null) {
				// Leer la segunda línea que contiene las dimensiones del tablero
				String secondLine = reader.readLine();
				if(secondLine != null) {
				String[] dimensiones = secondLine.split(" ");
				if(dimensiones.length == 2) {
					int nRows = Integer.parseInt(dimensiones[0]);
					int nColumns = Integer.parseInt(dimensiones[1]);
					if(nRows >=3 && nColumns >= 3) {
						// Leer las siguientes nRows líneas que contienen los elementos del tablero
						board = new char[nRows][nColumns];
						for (int i = 0; i < nRows; i++) {
							String row = reader.readLine();
							if(row!=null) {
							for (int j = 0; j < nColumns; j++) {
								board[i][j] = row.charAt(j);
							}
							}else {
								logger.error("The board is missing lines.");
							}
						}
					}else {
						logger.error("The board is too small.");
					}
				}else {
					logger.error("The dimensions of the board are not in the correct format.");
				}
			}else {
				logger.error("The dimensions of the board are not specified.");
			}
		} else {
			logger.error("The name of the board is not specified.");
		}

            // Cerrar el archivo
        reader.close();

        } catch (Exception e) {
        	logger.error("There was an error while reading the file: {}.", e.getMessage());
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        logger.info("Map has been read: \n {}", charMatrixToString(board));
		return board;
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

}
