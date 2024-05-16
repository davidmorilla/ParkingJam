package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.upm.pproject.parkingjam.parking_jam.controller.Controller;

public class MainFrame extends JFrame{
	private Controller controller;
	private ImageIcon icon;
	private Image image;
	private JPanel elements;
	
	public MainFrame(Controller controller) {
		super("Parking Jam - Programming Project");
		//setLayout(null);
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.controller = controller;
		this.setResizable(false);
		
		
		try {
			icon = new ImageIcon(new URL("https://store-images.s-microsoft.com/image/apps.32576.13852498558802281.02407fd2-7c4b-4af9-a1f0-3b18d41974a0.70dbf666-990b-481d-b089-01bbce54de27?mode=scale&q=90&h=200&w=200&background=%23FFFFFF"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		image = icon.getImage();
		this.setIconImage(image); 
		
		//this.add(new Grid());
		//------------------------
		
        /*// Crear el panel principal con un GridLayout
		int rows = 8;
		int cols = 10;
        JPanel panel = new JPanel(new GridLayout(rows, cols));
        JButton [][]buttons = new JButton[rows][cols];

        // Crear y agregar botones al panel
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50)); // Establecer tamaño de botón
                buttons[i][j] = button;
                panel.add(button);
            }
        }

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);*/
		int rows = 8;
        int cols = 10;
        Grid panel = new Grid(rows, cols);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
		//-------------
		
		setBounds(0,0,1200,800);
		this.setVisible(true);
		
	}
	
}
