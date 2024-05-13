import java.io.BufferedReader;
import java.io.FileReader;

public class LevelReader {
	public static final String LEVEL_FILE_NAME_FORMAT = "src/main/resources/levels/level_%d.txt";
	
    public static char[][] readMap(int level) {
        String fileName = LEVEL_FILE_NAME_FORMAT;
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
							for (int j = 0; j < nColumns; j++) {
								board[i][j] = row.charAt(j);
							}
						}
					}
					}
				}
			}

            // Cerrar el archivo
            reader.close();

        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
		return board;
    }

}
