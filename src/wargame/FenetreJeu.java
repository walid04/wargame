package wargame;

import javax.swing.JFrame;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FenetreJeu implements IConfig {

	public static void main(String[] args) {
		JFrame fenetre = new JFrame();
		Image img = null;

		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		img = null;
//		try {
//			img = ImageIO.read(new File("pictures/cursor.png"));
//		} catch (IOException e1) {
//			System.out.println("Un probl�me est survenu lors de chargement de cursor.png !");
//		}
//		try {
//			Point hotspot = new Point(0, 0);
//			Cursor cursor = toolkit.createCustomCursor(img, hotspot, null);
//			fenetre.setCursor(cursor);
//		} catch (NullPointerException e1) {
//			System.out.println("Un probl�me est survenu lors de l'ouverture de cursor.png !");
//		}

		fenetre.setTitle("Wargame");
		try {
			img = ImageIO.read(new File("pictures/logo.png"));
		} catch (IOException e) {
			System.out.println("Un probl�me est survenu lors de chargement de logo.png !");
		}
		fenetre.setIconImage(img);
		fenetre.setSize(1194, 725);
		fenetre.setLocation(POSITION_X, POSITION_Y);
		fenetre.setResizable(false);
		PanneauJeu pj = new PanneauJeu();
		fenetre.setContentPane(pj.getPanel1());

		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setVisible(true);
	}

}