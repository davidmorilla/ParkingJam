package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.*;
import java.util.ArrayList;

import java.util.List;


import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

public class GameSaver {
    private File level, punctuation, history;
    private String levelName;
    private String dimensions;

    public GameSaver() {
        level = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/level.txt");
        punctuation = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/punctuation.txt");
        history = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/history.txt");
        try {
            if (!level.exists()) {
                level.getParentFile().mkdirs();
                level.createNewFile();
            }
            if (!punctuation.exists()) {
                punctuation.getParentFile().mkdirs();
                punctuation.createNewFile();
            }
            if (!history.exists()) {
                history.getParentFile().mkdirs();
                history.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveGame(List<Pair<Character, Pair<Integer, Character>>> history, int total, int level, char[][] board) {
    	saveHistory(history);
        savePunctuation(total, level);
        saveBoard(board);
    }

    public void saveLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void saveLevelDimensions(String dimensions) {
        this.dimensions = dimensions;
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

    public void savePunctuation(int total, int level) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(punctuation))) {
            writer.write("" + total);
            writer.newLine();
            writer.write("" + level);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHistory(List<Pair<Character, Pair<Integer, Character>>> list) {
        // Save history
        try (BufferedWriter movWriter = new BufferedWriter(new FileWriter(history))) {

            for (Pair<Character, Pair<Integer, Character>> mov : list) {
            	movWriter.write(mov.getLeft() + " " + mov.getRight().getLeft() + " " + mov.getRight().getRight());
                movWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Pair<Character, Pair<Integer,Character>>> loadHistory(int cols, int rows) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
