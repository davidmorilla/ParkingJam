package es.upm.pproject.parkingjam.parking_jam;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
/**
 * Main App class.
 */
public class App {
	/**
	 * main method for the game start.
	 * @param args unused parameter.
	 */
	public static void main(String[] args) {
	    SwingUtilities.invokeLater(() -> {
	        try {
	            new Controller();
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null, e.getMessage());
	        }
	    });
	}
}
