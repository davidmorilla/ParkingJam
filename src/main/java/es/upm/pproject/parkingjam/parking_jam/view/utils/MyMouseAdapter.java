package es.upm.pproject.parkingjam.parking_jam.view.utils;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import es.upm.pproject.parkingjam.parking_jam.view.MovableCar;

public class MyMouseAdapter extends MouseAdapter {
    private Point initialPoint;
    private boolean dragging;
    private int squareSize;
    private MovableCar movableSquare;
    private JPanel grid;
    

    public MyMouseAdapter(int squareSize, MovableCar movableSquare, JPanel grid) {
        this.squareSize = squareSize;
        this.movableSquare = movableSquare;
        this.grid = grid;
    }

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
            grid.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }
}
