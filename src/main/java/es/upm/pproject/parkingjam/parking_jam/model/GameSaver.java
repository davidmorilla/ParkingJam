package es.upm.pproject.parkingjam.parking_jam.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import es.upm.pproject.parkingjam.parking_jam.utilities.OldBoardData;
import es.upm.pproject.parkingjam.parking_jam.model.Car;

public class GameSaver {
    private File level, punctuation, history, cars;
    private String levelName;
    private String dimensions;

    public GameSaver() {
        level = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/level.txt");
        punctuation = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/punctuation.txt");
        history = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/history.txt");
        cars = new File("src/main/java/es/upm/pproject/parkingjam/parking_jam/Games saved/cars.txt");
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
            if (!cars.exists()) {
                cars.getParentFile().mkdirs();
                cars.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void saveHistory(Stack<OldBoardData> stack) {
        // Save history
        try (BufferedWriter boardWriter = new BufferedWriter(new FileWriter(history));
             BufferedWriter carsWriter = new BufferedWriter(new FileWriter(cars))) {

            for (OldBoardData data : stack) {
                // Save board
                char[][] board = data.getBoard();
                for (char[] row : board) {
                    boardWriter.write(row);
                    boardWriter.newLine();
                }
                boardWriter.write("END_BOARD");
                boardWriter.newLine();

                // Save cars
                Map<Character, Car> carMap = data.getCars();
                for (Car car : carMap.values()) {
                    carsWriter.write(car.getSymbol() + "," + car.getCoordinates().getX() + "," +
                                     car.getCoordinates().getY() + "," + car.getLength() + "," + car.getOrientation());
                    carsWriter.newLine();
                }
                carsWriter.write("END_CARS");
                carsWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stack<OldBoardData> loadHistory(int cols, int rows) {
        Stack<OldBoardData> stack = new Stack<>();

        try (BufferedReader boardReader = new BufferedReader(new FileReader(history));
             BufferedReader carsReader = new BufferedReader(new FileReader(cars))) {

            String line;
            while ((line = boardReader.readLine()) != null) {
                // Load board
                char[][] board = new char[cols][rows];
                int row = 0; 
                while (!line.equals("END_BOARD")) {
                    board[row++] = line.toCharArray();
                    line = boardReader.readLine();
                }

                // Load cars
                Map<Character, Car> carMap = new HashMap<>();
                while ((line = carsReader.readLine()) != null && !line.equals("END_CARS")) {
                    String[] carData = line.split(",");
                    char symbol = carData[0].charAt(0);
                    int x = Integer.parseInt(carData[1]);
                    int y = Integer.parseInt(carData[2]);
                    int length = Integer.parseInt(carData[3]);
                    char orientation = carData[4].charAt(0);
                    Car car = new Car(symbol, x, y, length, orientation);
                    carMap.put(symbol, car);
                }

                stack.push(new OldBoardData(board, carMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stack;
    }
}
