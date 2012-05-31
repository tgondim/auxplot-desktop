package br.com.tg.gui.util;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class BackgroundedTabbedPane extends JTabbedPane {
	
	private Image img;
	private String backgroundImgFile;

	public BackgroundedTabbedPane() throws FileNotFoundException, IOException {
		super(SwingConstants.TOP);
		File file = new File("auxPlot.properties");
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		backgroundImgFile = prop.getProperty("backgroundImg");
		File caminho = new File(backgroundImgFile);
		img = ImageIO.read(caminho);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) {
			g.drawImage(img, (getSize().width-img.getWidth(this))/2, (getSize().height-img.getHeight(this))/2, this.getBackground(), this);
		}
	}
}