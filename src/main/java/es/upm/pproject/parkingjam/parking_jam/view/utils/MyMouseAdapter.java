package es.upm.pproject.parkingjam.parking_jam.view.utils;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import es.upm.pproject.parkingjam.parking_jam.view.IGrid;
import es.upm.pproject.parkingjam.parking_jam.view.MovableCar;

/**
 * MyMouseAdapter extends MouseAdapter for managing mouse events on
 * a game grid of movable cars
 */
public class MyMouseAdapter extends MouseAdapter {
    private int squareSize;
    private IGrid grid;
    private MovableCar movableSquare;
    private int initialRow;
    private int initialCol;

    /**
     * MyMouseAdapter constructor
     *
     * @param grid the game grid where the cars visually move
     */
    public MyMouseAdapter(IGrid grid) {
        this.grid = grid;
        this.squareSize = grid.getSquareSize();
    }

    /**
     * Starts the dragging of a car if found on the mouse click position
     *
     * @param e mouse event which contains the click info
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();
        movableSquare = (MovableCar) grid.getMovableCarAt(point);
        if (movableSquare != null) {
            movableSquare.startDrag();
            initialRow = movableSquare.getRow();
            initialCol = movableSquare.getCol();
        }
    }

    /**
     * Updates the movable car's position according to the mouse drag
     *
     * @param e mouse event which contains the drag info
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (movableSquare != null) {
            Point point = e.getPoint();
            int dx = (point.x / squareSize) - initialCol;
            int dy = (point.y / squareSize) - initialRow;
            movableSquare.drag(dx, dy);
        }
    }

    /**
     * Ends the movable car's drag and puts it on its new position
     *
     * @param e mouse event which contains the mouse release info
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (movableSquare != null) {
            movableSquare.endDrag();
            movableSquare = null;
        }
    }
}
