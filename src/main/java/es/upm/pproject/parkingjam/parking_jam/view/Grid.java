package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.*;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.view.utils.MyMouseAdapter;

public class Grid extends JPanel {
    private int rows;
    private int cols;
    private int squareSize = 50;
    private MovableCar movableSquare;
    private Map<Character,Car> cars;
    private Controller controller;
    private char[][] board;

    public Grid(int rows, int cols,Map<Character,Car> cars, char[][] board, Controller controller) {
        this.rows = rows;
        this.cols = cols;
        this.cars = cars;
        this.board = board;
        this.controller = controller;
        this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
        int row = cars.get('a').getCoordinates().getY();
        int col = cars.get('a').getCoordinates().getX();
        System.out.println("Fila: " + row + "   Col: " + col);
        movableSquare = new MovableCar('a',row,col,rows, cols, squareSize, cars.get('a').getLength(),cars.get('a').getOrientation(), controller );

        // Añadir el MouseAdapter para mover el cuadrado
        MyMouseAdapter mouseAdapter = new MyMouseAdapter(squareSize,movableSquare,this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char elem = getElemAt(board, i, j);
                if(elem=='+'){
                    g.setColor(Color.BLACK);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * squareSize, i * squareSize, squareSize, squareSize);
                }
                else{
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * squareSize, i * squareSize, squareSize, squareSize);
                }
            }
        }
        // Dibujar el cuadrado movible
        movableSquare.draw(g);
    }


    private char getElemAt(char[][]board, int row, int col){
        return board[col][row];
    }
}