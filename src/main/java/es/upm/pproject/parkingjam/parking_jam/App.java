package es.upm.pproject.parkingjam.parking_jam;

import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
import es.upm.pproject.parkingjam.parking_jam.model.Level;
import es.upm.pproject.parkingjam.parking_jam.model.LevelConverter;
import es.upm.pproject.parkingjam.parking_jam.model.LevelReader;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalCarDimensionException;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.IllegalExitsNumberException;
import es.upm.pproject.parkingjam.parking_jam.utilities.Coordinates;
import es.upm.pproject.parkingjam.parking_jam.view.MainFrame;

public class App {
     
    public static void main(String [] args){
        LevelReader lr = new LevelReader();
        char[][] board = lr.readMap(1);

        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board[i].length; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        LevelConverter lc = new LevelConverter();
        Map<Character, Car>cars = null;
        try {
            cars = lc.convertLevel(board);
            int i = 1;
            for (Car c : cars.values()) {

                System.out.println("Coche nº: " + i +" Coordenadas: X=" + c.getCoordinates().getX() + " Y=" + c.getCoordinates().getY() 
                + " Longitud: " + c.getLength() + " Orientacion: " + c.getOrientation() + "\n----------------------------------------\n");
                i++;
            }

            System.err.println("\nAHORA LAS LETRAS DE LOS COCHES EN ORDEN ALFABÉTICO (LAS ORDENA SOLAS EL PUT DEL MAP)\n");
            for (char letra : cars.keySet()) {
                if (letra=='*') {
                    System.out.println("Soy el coche rojo!!!");
                }
                else{
                    System.out.println("Coche: "+letra);
                }
            }

            Level lvl = new Level(board, cars);
            


            System.out.println("\n------MOVIENDO COCHE F 1 POSICIÓN A LA DERECHA------\n");
            lvl.moveCar('a', 1, 'R');
            for(i = 0; i<board.length; i++){
                for(int j = 0; j<board[i].length; j++){
                    System.out.print(board[i][j]);
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	
    	/* SwingUtilities.invokeLater(new Runnable() {
			public void run() {		
				try {
					

					new Controller();
					
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				} 
			}
		}); */
        try {
            new Controller();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
