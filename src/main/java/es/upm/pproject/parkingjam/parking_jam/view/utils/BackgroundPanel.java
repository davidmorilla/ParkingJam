package es.upm.pproject.parkingjam.parking_jam.view.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the panel which will sit on the back of the app and
 * will show the background image display
 *
 */
public class BackgroundPanel extends JPanel {
    private transient BufferedImage backgroundImage;
	private static final Logger logger = LoggerFactory.getLogger(BackgroundPanel.class);
    /**
     * BackgroundPanel constructor
     *
     * @param backgroundImage the background image to be painted
     */
    public BackgroundPanel(BufferedImage backgroundImage){
    	logger.info("Creating background panel...");
        this.backgroundImage = backgroundImage;
        logger.info("Background panel created.");
    }


    /**
     * Paints the full background with an image transparency of 50%
     *
     * @param g the graphical context used to paint the background
     */
    @Override
    protected void paintComponent(Graphics g) {
    	logger.info("Painting background...");
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            g2d.dispose();
            logger.info("Painting background...");
        } else {
        	logger.error("Cannot paint the background. The background image does not exist.");
        }
    }
}
