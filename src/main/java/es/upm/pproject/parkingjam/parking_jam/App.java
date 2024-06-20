package es.upm.pproject.parkingjam.parking_jam;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Game;
/**
 * Main App class.
 */
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	/**
	 * main method for the game start.
	 * @param args unused parameter.
	 */
	public static void main(String[] args) {
		logger.info("The app has been started.");
	    SwingUtilities.invokeLater(() -> {
	        try {
	            new Controller();
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null, e.getMessage());
	        }
	    });
	}
}
