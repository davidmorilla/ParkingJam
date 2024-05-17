package es.upm.pproject.parkingjam.parking_jam.view.utils;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import es.upm.pproject.parkingjam.parking_jam.view.MovableCar;

public class MyMouseAdapter extends MouseAdapter {
    private boolean dragging;
    private int squareSize;
    private MovableCar movableSquare;
    private JPanel grid;
    private Point click;
    private int lastDx;
    private int lastDy;

    public MyMouseAdapter(int squareSize, MovableCar movableSquare, JPanel grid) {
        this.squareSize = squareSize;
        this.movableSquare = movableSquare;
        this.grid = grid;
        this.lastDx = 0;
        this.lastDy = 0;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = e.getPoint();
        if (movableSquare.contains(click)) {
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
                movableSquare.drag(dx - lastDx, 0);
                lastDx = dx;
                lastDy = 0;
            } else if (dy != lastDy) {
                movableSquare.drag(0, dy - lastDy);
                lastDx = 0;
                lastDy = dy;
            }

            grid.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }
}
