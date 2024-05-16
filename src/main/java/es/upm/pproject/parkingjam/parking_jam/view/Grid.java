package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;

public class Grid extends JPanel{
	    /*private int rows;
        private int cols;
        private int squareSize = 50;

        public Grid(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * squareSize, i * squareSize, squareSize, squareSize);
                }
            }
        }*/
    private int rows;
        private int cols;
        private int squareSize = 50;
        private MovableSquare movableSquare;

        public Grid(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
            movableSquare = new MovableSquare(1, 1);

            // Añadir el MouseAdapter para mover el cuadrado
            MyMouseAdapter mouseAdapter = new MyMouseAdapter();
            this.addMouseListener(mouseAdapter);
            this.addMouseMotionListener(mouseAdapter);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(j * squareSize, i * squareSize, squareSize, squareSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * squareSize, i * squareSize, squareSize, squareSize);
                }
            }

            // Dibujar el cuadrado movible
            movableSquare.draw(g);
        }

        // Clase interna para representar el cuadrado movible
        private class MovableSquare {
            private int row;
            private int col;
            private Color color;
            private int initialCol; // Columna inicial al empezar a arrastrar
            private int initialRow; // Fila inicial al empezar a arrastrar

            public MovableSquare(int row, int col) {
                this.row = row;
                this.col = col;
                this.color = Color.RED;
            }

            public void draw(Graphics g) {
                g.setColor(color);
                g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
                g.setColor(Color.BLACK);
                g.drawRect(col * squareSize, row * squareSize, squareSize, squareSize);
            }

            public boolean contains(Point p) {
                return p.x >= col * squareSize && p.x < (col + 1) * squareSize &&
                       p.y >= row * squareSize && p.y < (row + 1) * squareSize;
            }

            public void startDrag() {
                initialCol = col;
                initialRow = row;
            }

            public void drag(int dx, int dy) {
                col = Math.max(0, Math.min(cols - 1, initialCol + dx)); //Horizontal
                row = Math.max(0, Math.min(rows - 1, initialRow + dy)); //Vertical
            }
        }

        // Clase interna para manejar los eventos del mouse
        private class MyMouseAdapter extends MouseAdapter {
            private Point initialPoint;
            private boolean dragging;

            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (movableSquare.contains(p)) {
                    initialPoint = p;
                    movableSquare.startDrag();
                    dragging = true;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    Point p = e.getPoint();
                    int dx = (p.x - initialPoint.x) / squareSize;
                    int dy = (p.y - initialPoint.y) / squareSize;
                    movableSquare.drag(dx, dy);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        }
    }