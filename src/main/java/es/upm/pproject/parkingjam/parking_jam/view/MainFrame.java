package es.upm.pproject.parkingjam.parking_jam.view;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;
import es.upm.pproject.parkingjam.parking_jam.utilities.Pair;

import java.awt.*;

public class MainFrame extends JFrame {
	private Controller controller;
	private ImageIcon icon;
	private JPanel mainPanel;
	private DataPanel dataPanel;
	private Grid gridPanel;

	public MainFrame(Controller controller) {
		super("Parking Jam - Programming Project");
		// setLayout(null);

		this.controller = controller;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setBounds(0, 0, 1200, 800);
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
		gridPanel = new Grid(dimensions, controller.getCars(), controller.getBoard(), controller);

		mainPanel.add(gridPanel, BorderLayout.CENTER);
		
		dataPanel = new DataPanel();
		mainPanel.add(dataPanel, BorderLayout.LINE_END);


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
