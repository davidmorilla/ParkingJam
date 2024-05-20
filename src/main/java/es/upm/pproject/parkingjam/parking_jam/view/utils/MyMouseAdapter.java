package es.upm.pproject.parkingjam.parking_jam.view.utils;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import es.upm.pproject.parkingjam.parking_jam.view.Grid;
import es.upm.pproject.parkingjam.parking_jam.view.MovableCar;

public class MyMouseAdapter extends MouseAdapter {
    private boolean dragging;
    private int squareSize;
    private Grid grid;
    private MovableCar movableSquare;
    private Point click;
    private int lastDx;
    private int lastDy;
    private int initialRow;
    private int initialCol;

    public MyMouseAdapter(int squareSize, Grid grid) {
        this.squareSize = squareSize;
        this.grid = grid;
        this.lastDx = 0;
        this.lastDy = 0;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();
        movableSquare = grid.getMovableCarAt(point);
        if (movableSquare != null) {
        	movableSquare.startDrag();
            initialRow = movableSquare.getRow();
            initialCol = movableSquare.getCol();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (movableSquare != null) {
            Point point = e.getPoint();
            int dx = (point.x / squareSize) - initialCol;
            int dy = (point.y / squareSize) - initialRow;
            movableSquare.drag(dx, dy);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (movableSquare != null) {
            movableSquare.endDrag();
            movableSquare = null;
        }
    }
}