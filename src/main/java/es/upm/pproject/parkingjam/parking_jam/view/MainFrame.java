package es.upm.pproject.parkingjam.parking_jam.view;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.model.Car;
import es.upm.pproject.parkingjam.parking_jam.model.exceptions.CannotUndoMovementException;
import es.upm.pproject.parkingjam.parking_jam.utilities.OldBoardData;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
	private Controller controller;
	private ImageIcon icon;
	private JPanel mainPanel;
	private DataPanel dataPanel;
	private Grid gridPanel;
	private JButton actionButton;

	public MainFrame(Controller controller) {
		super("Parking Jam - Programming Project");
		// setLayout(null);

		this.controller = controller;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setBounds(0, 0, 600, 600);
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH); Pantalla maximizada

		try {
			icon = new ImageIcon(new URL(
					"https://store-images.s-microsoft.com/image/apps.32576.13852498558802281.02407fd2-7c4b-4af9-a1f0-3b18d41974a0.70dbf666-990b-481d-b089-01bbce54de27?mode=scale&q=90&h=200&w=200&background=%23FFFFFF"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.setIconImage(icon.getImage());
		mainPanel = new JPanel(new BorderLayout());

		Pair<Integer, Integer> dimensions = controller.getBoardDimensions();
		System.out.println(dimensions.getLeft());
		System.out.println(dimensions.getRight());

		dataPanel = new DataPanel();
		mainPanel.add(dataPanel, BorderLayout.LINE_END);
		
		gridPanel = new Grid(dimensions, controller.getCars(), controller.getBoard(), controller,dataPanel);

		mainPanel.add(gridPanel, BorderLayout.CENTER);
		
		
		// Crear un JPanel para el botón
        JPanel buttonPanel = new JPanel();
        actionButton = new JButton("UNDO");
        buttonPanel.add(actionButton);

        // Añadir el JPanel del botón al mainPanel en la parte inferior
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Añadir ActionListener al botón
        actionButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					OldBoardData old = controller.undoMovement();
					char[][]oldBoard = old.getBoard();
					System.out.println("\n\n-----------EN MAINFRAME----------\n");
					for (int i = 0; i < oldBoard.length; i++) {
						for (int j = 0; j < oldBoard[i].length; j++) {
							System.out.print(oldBoard[i][j]);
						}
						System.out.println();
					}
					
					for(Car c : old.getCars().values()){ 
						System.out.println("Car: " + c.getSymbol() + "	X: " +c.getCoordinates().getX() + "	Y: " +  c.getCoordinates().getY() + " LENGTH: "+ c.getLength());
					}
					gridPanel.setCars(old.getCars());
					gridPanel.setBoard(oldBoard);
					gridPanel.repaint();
				} catch (CannotUndoMovementException e) {
					
					e.printStackTrace();
				}; // Ejemplo de llamada a un método en el controlador
                updateDataPanel();
			}
        });

		


		add(mainPanel);


		this.setVisible(true);
	}


	public Grid getGrid() {
		return this.gridPanel;
	}

	public void setGrid(Grid grid) {
		mainPanel.add(gridPanel, BorderLayout.CENTER);
	}

	public void updateDataPanel() {
		dataPanel.updateData(controller.getLevelNumber(), controller.getGameScore(), controller.getLevelScore());
	}

}
