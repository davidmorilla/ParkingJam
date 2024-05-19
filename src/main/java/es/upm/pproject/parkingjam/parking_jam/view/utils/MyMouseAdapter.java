package es.upm.pproject.parkingjam.parking_jam.view.utils;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import es.upm.pproject.parkingjam.parking_jam.view.Grid;
import es.upm.pproject.parkingjam.parking_jam.view.MainFrame;
import es.upm.pproject.parkingjam.parking_jam.view.MovableCar;

public class MyMouseAdapter extends MouseAdapter {
    private boolean dragging;
    private int squareSize;
    private Grid grid;
    private MovableCar movableSquare;
    private Point click;
    private int lastDx;
    private int lastDy;
    private boolean movementSuccess;
    private MainFrame mf;


    public MyMouseAdapter(int squareSize, Grid grid, MainFrame mf) {
        this.squareSize = squareSize;
        this.grid = grid;
        this.lastDx = 0;
        this.lastDy = 0;
        this.mf = mf;
        movementSuccess = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = e.getPoint();
        movableSquare = grid.getMovableCarAt(click);
        if (movableSquare != null) {
            movableSquare.startDrag();
            dragging = true;
            lastDx = 0;
            lastDy = 0;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            Point p = e.getPoint();
            int dx = (p.x - click.x) / squareSize;
            int dy = (p.y - click.y) / squareSize;

            // Mover solo una posición a la vez
            if (dx != lastDx) {
                movementSuccess = movableSquare.drag(dx - lastDx, 0);
                lastDx = dx;
                lastDy = 0;
            } else if (dy != lastDy) {
                movementSuccess = movableSquare.drag(0, dy - lastDy);
                lastDx = 0;
                lastDy = dy;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
        if(movementSuccess){
            mf.getController().increaseScore();;
            mf.increaseScore();
        }
    }
}