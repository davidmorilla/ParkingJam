package es.upm.pproject.parkingjam.parking_jam.view.utils;

import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Insets;
import java.awt.FontMetrics;

public class LabelUtils {

	private LabelUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static JLabel createCustomLabel(String text, Color foregroundColor, int shadowOffset) {
		return new JLabel(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g.create();
				Insets insets = getInsets();
				FontMetrics fontMetrics = getFontMetrics(getFont());

				// Draw shadow
				g2d.setColor(Color.GRAY);
				g2d.drawString(getText(), insets.left + shadowOffset, insets.top + fontMetrics.getAscent() + shadowOffset);

				// Draw main text
				g2d.setColor(foregroundColor);
				g2d.drawString(getText(), insets.left, insets.top + fontMetrics.getAscent());
				g2d.dispose();
			}
		};
	}
}
