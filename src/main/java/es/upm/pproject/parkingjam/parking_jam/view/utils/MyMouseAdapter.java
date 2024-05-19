package es.upm.pproject.parkingjam.parking_jam.view.utils;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import es.upm.pproject.parkingjam.parking_jam.model.Car;
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
    private char[][] board;
    private Map<Character,Car> cars;


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
        board = grid.getBoard();
        cars = grid.getCars();
        mf.getController().addToHistory();
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
            mf.getController().increaseScore();
            mf.increaseScore();
        }
        else{
            if(boardHasChanged()){
                mf.getController().increaseScore();
                mf.increaseScore();
            }
            System.out.println("NO MOVEMENT SUCCESS: " + movementSuccess);

            System.out.println("\nGRID BOARD");

            for(int i = 0; i<grid.getBoard().length ; i++){
                for(int j = 0; j<grid.getBoard()[i].length; j++){
                    System.out.print( grid.getBoard()[i][j]);
                }
                System.out.println();
            }


            System.out.println("\nBOARD");

            for(int i = 0; i<board.length ; i++){
                for(int j = 0; j<board[i].length; j++){
                    System.out.print( board[i][j]);
                }
                System.out.println();
            }
        } 
    }

    private boolean boardHasChanged(){
        boolean equals = true;
        for(int i = 0; i<grid.getBoard().length && equals; i++){
            for(int j = 0; j<grid.getBoard()[i].length && equals; j++){
                equals = board[i][j]==grid.getBoard()[i][j];
            }
        }
        return !equals;
    }

}