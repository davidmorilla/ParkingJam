package es.upm.pproject.parkingjam.parking_jam;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;

public class App {
     
    public static void main(String [] args){
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {		
				try {
					

					new Controller();
					
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				} 
			}
		});
    } 
    
}
