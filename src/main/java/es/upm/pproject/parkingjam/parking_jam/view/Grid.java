package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class Grid extends JPanel{
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Crear una instancia de Rectangle
        Rectangle rect = new Rectangle(65, 50, 1050, 600);

        
        // Dibujar el rectángulo usando las propiedades del Rectangle
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
}
