package es.upm.pproject.parkingjam.parking_jam.view.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;
    
    public BackgroundPanel(BufferedImage backgroundImage){
        this.backgroundImage = backgroundImage;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            g2d.dispose();
        }
    }
}
