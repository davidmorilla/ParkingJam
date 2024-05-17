package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;

// Clase interna para representar el cuadrado movible
public class MovableCar {
    private int row;
    private int col;
    private int rows;
    private int cols;
    private int squareSize;
    private Color color;
    private int initialCol; // Columna inicial al empezar a arrastrar
    private int initialRow; // Fila inicial al empezar a arrastrar
    private char orientation, car;
    private int length;
    private Controller controller;

    public MovableCar(char car ,int row, int col, int rows, int cols, int squareSize, int length, char orientation, Controller controller) {
        this.row = row;
        this.col = col;
        this.color = Color.RED;
        this.rows = rows;
        this.cols = cols;
        this.squareSize = squareSize;
        this.orientation = orientation;
        this.length = length;
        this.controller = controller;
        this.car = car;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        if (orientation == 'V') {
            // Dibujar un solo rectángulo vertical
            g.fillRect(col * squareSize, row * squareSize, squareSize, length * squareSize);
            g.setColor(Color.BLACK);
            g.drawRect(col * squareSize, row * squareSize, squareSize, length * squareSize);
        } else {
            // Dibujar un solo rectángulo horizontal
            g.fillRect(col * squareSize, row * squareSize, length * squareSize, squareSize);
            g.setColor(Color.BLACK);
            g.drawRect(col * squareSize, row * squareSize, length * squareSize, squareSize);
        }
    }

    public boolean contains(Point p) {
        if (orientation == 'V') {
            return p.x >= col * squareSize && p.x < (col + 1) * squareSize &&
                   p.y >= row * squareSize && p.y < (row + length) * squareSize;
        } else {
            return p.x >= col * squareSize && p.x < (col + length) * squareSize &&
                   p.y >= row * squareSize && p.y < (row + 1) * squareSize;
        }
    }

    public void startDrag() {
        initialCol = col;
        initialRow = row;
    }

    public void drag(int dx, int dy) {
        try{
            if (orientation == 'V') {
                if(controller.move(car, dy, dy>0?'D':'U')!=null){
                    row = Math.max(0, Math.min(rows - length, initialRow + dy));
                    System.out.println("DRAG X: "+ dx + "   Y: " + dy);
                }
                
            } else {
                if(controller.move(car, dx, dx>0?'R':'L')!=null){
                    col = Math.max(0, Math.min(cols - length, initialCol + dx));
                    System.out.println("DRAG X: "+ dx + "   Y: " + dy);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
