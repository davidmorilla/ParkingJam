package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameSaver {
    private File level, punctuation;
    private String levelName;
    private String dimensions;
    public GameSaver() {
        level = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/level.txt");
        punctuation = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/punctuation.txt");
        try {
            if (!level.exists()) {
                level.getParentFile().mkdirs();
                level.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void saveLevelDimensions(String dimensions) {
        this.dimensions=dimensions;
    }

    public void saveBoard(char[][] board) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(level))) {
            
            writer.write(this.levelName);
            writer.newLine();
            writer.write(this.dimensions);
            writer.newLine();

            for (char[] row : board) {
                writer.write(row);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePunctuation(int total, int level){
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(punctuation))) {
            writer.write(""+total);
            writer.newLine();
            writer.write(""+level);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
