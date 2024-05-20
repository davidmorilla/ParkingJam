package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.SameMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MyMouseAdapter;

public class Grid extends JPanel {
    private int rows;
    private int cols;
    private int squareSize = 50;
    private Map<Character, MovableCar> movableCars; // Almacenar las instancias de MovableCar
    private Controller controller;
    private char[][] board;
    private boolean levelCompleted;
    private MainFrame mf;
    private Map<Integer, String[]> carImages;
    private Image wallImage;

    public Grid(Pair<Integer, Integer> dimensions, Map<Character, Car> cars, char[][] board, Controller controller,
            MainFrame mf) {
        this.rows = dimensions.getLeft();
        this.cols = dimensions.getRight();
        this.board = board;
        this.controller = controller;
        this.levelCompleted = false;
        this.mf = mf;
        this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
        this.movableCars = new HashMap<>();
        this.carImages = new HashMap<>();
        carImages.put(2, new String[]{"car2","car4","car7","car9","car10","car11","car12"});
        carImages.put(3, new String[]{"car3","car5","car6","car8"});

        // Crear instancias de MovableCar y almacenarlas en el mapa
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
            Car car = entry.getValue();
            String[] imagePaths = carImages.get(car.getLength());
            String imagePath;
            if(imagePaths == null) { // if is null it means that the length is more than 3
                imagePath = "longCar";
            }
            else {
                imagePath = imagePaths[new Random().nextInt(imagePaths.length)];
            }
            MovableCar movableCar = new MovableCar(car, rows, cols, squareSize, this, controller, this.mf, imagePath);
            movableCars.put(entry.getKey(), movableCar);
        }

        // Cargar la imagen del muro
        wallImage = new ImageIcon("path/to/wall/image.png").getImage();

        // Añadir un solo MouseAdapter para toda la cuadrícula
        MyMouseAdapter mouseAdapter = new MyMouseAdapter(squareSize, this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    // Método para obtener el coche en un punto específico
    public MovableCar getMovableCarAt(Point point) {
        for (MovableCar car : movableCars.values()) {
            if (car.contains(point)) {
                return car;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar el tablero
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char elem = getElemAt(board, j, i);
                if (elem == '+') {
                    try {
                        String image;
                        BufferedImage imageToDraw;
                        String cornerType = getCornerType(j, i);
                        switch (cornerType) {
                            case "topLeft":
                                image = "wall_left_corners.png";
                                imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize,null);
                                break;
                            case "topRight":
                                image = "wall_right_corners.png";
                                imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize,null);
                                break;
                            case "bottomLeft":
                                image = "wall_left_corners.png";
                                imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize,null);
                                break;
                            case "bottomRight":
                                image = "wall_right_corners.png";
                                imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize,null);
                                break;
                            default:
                                if (isVerticalWall(j, i)) {
                                    image = "wall_vertical.png";
                                    imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                    g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize, this);
                                } else {
                                    image = "wall_horizontal.png";
                                    imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                    g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize, this);
                                }
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (elem == '@') {
                    levelCompleted = false;
                    String image;
                    BufferedImage imageToDraw;
                    String side = getExitSide(j,i);
                    try {
                        switch (side) {
                            case "top":
                                image = "exits/exitTop.png";
                                imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize,null);  
                                break;
                            case "bottom":
                                image = "exits/exitBottom.png";
                                imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize,null);
                                break;
                            case "left":
                                image = "exits/exitLeft.png";
                                imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize,null);
                                break;
                            case "right":
                                image = "exits/exitRight.png";
                                imageToDraw = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
                                g.drawImage(imageToDraw, j * squareSize, i * squareSize, squareSize, squareSize,null);
                                break;
                            default:
                                break;
                        }
                    } catch (IOException e) {
                        // TODO: handle exception
                    }
                } else {
                    g.setColor(Color.GRAY);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                }
            }
        }

        // Dibujar todos los coches movibles
        for (MovableCar movableCar : movableCars.values()) {
            movableCar.draw(g);
        }

        if (isLevelCompleted()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30)); // Cambiar la fuente y el tamaño del texto
            g.drawString("LEVEL COMPLETED", (cols * squareSize) / 2 - 100, (rows * squareSize) / 2);
        }

    }

    private String getCornerType(int x, int y) {
        if (x == 0 && y == 0) {
            return "topLeft";
        } else if (x == cols - 1 && y == 0) {
            return "topRight";
        } else if (x == 0 && y == rows - 1) {
            return "bottomLeft";
        } else if (x == cols - 1 && y == rows - 1) {
            return "bottomRight";
        } else {
            return "";
        }
    }

    private boolean isVerticalWall(int x, int y) {
        return x == 0 || x == cols - 1;
    }

    private String getExitSide(int x, int y) {
        if (y == 0) {
            return "top";
        } else if (y == rows - 1) {
            return "bottom";
        } else if (x == 0) {
            return "left";
        } else if (x == cols - 1) {
            return "right";
        } else {
            return "";
        }
    }

    private char getElemAt(char[][] board, int row, int col) {
        return board[col][row];
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public char[][] getBoard() {
        return this.board;
    }

    public void setCars(Map<Character, Car> cars) {
        // Actualizar las instancias de MovableCar
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
            MovableCar movableCar = movableCars.get(entry.getKey());
            if (movableCar != null) {
                movableCar.updateCar(entry.getValue());
            }
        }
    }

    public void setCarsMap(Map<Character, Car> cars) {
        // Actualizar las instancias de MovableCar
        this.movableCars = new HashMap<>();
        
        for (Map.Entry<Character, Car> entry : cars.entrySet()) {
        	Car car = entry.getValue();
        	String[] imagePaths = carImages.get(car.getLength());
            String imagePath;
            if(imagePaths == null) { // if is null it means that the length is more than 3
                imagePath = "longCar";
            }
            else {
                imagePath = imagePaths[new Random().nextInt(imagePaths.length)];
            }
            MovableCar movableCar = new MovableCar(car, rows, cols, squareSize, this, controller, this.mf, imagePath);
            movableCars.put(entry.getKey(), movableCar);
        }
    }

    public char[][] moveCar(char car, int length, char way) throws SameMovementException {

        return controller.moveCar(car, length, way);
    }

    public boolean isLevelCompleted() {
        levelCompleted = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char elem = getElemAt(board, j, i);
                if (elem == '@')
                    levelCompleted = false;
            }
        }
        return this.levelCompleted;
    }

}