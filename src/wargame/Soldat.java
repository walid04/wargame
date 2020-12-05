package wargame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public abstract class Soldat extends Element implements ISoldat {
	private static final long serialVersionUID = 1L;
	private final int POINTS_DE_VIE_MAX;
	private final int PORTEE_VISUELLE;
	private final int PUISSANCE;
	private final int TIR;
	private int pointsDeVie;
	private Carte carte;

	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINTS_DE_VIE_MAX = pointsDeVie = pts;
		PORTEE_VISUELLE = portee;
		PUISSANCE = puiss;
		TIR = tir;
		this.carte = carte;
		this.pos = pos;
	}

	public int getPointsDeVieMAX() {
		return POINTS_DE_VIE_MAX;
	}

	public int getPointsDeVie() {
		return pointsDeVie;
	}

	public void setPointsDeVie(int pv) {
		pointsDeVie = pv;
		if (pointsDeVie > POINTS_DE_VIE_MAX) {
			pointsDeVie = POINTS_DE_VIE_MAX;
		}
	}

	public int getPORTEE_VISUELLE() {
		return PORTEE_VISUELLE;
	}

	public void combat(Soldat soldat) {
		int puissanceCoup;
		if (this.pos.estVoisine(soldat.pos)) {
			if (PanneauJeu.test_son == 1) {
				try {
					File fichier = new File("sounds/frappe.wav");
					AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
					Clip clip = AudioSystem.getClip();
					clip.open(audioIn);
					clip.start();
				} catch (UnsupportedAudioFileException e2) {
					System.out.println("Un problème est survenu lors de chargement de frappe.wav !");
				} catch (IOException e2) {
					System.out.println("Un problème est survenu lors de chargement de frappe.wav !");
				} catch (LineUnavailableException e2) {
					System.out.println("Un problème est survenu lors de chargement de frappe.wav !");
				}
			}
			puissanceCoup = (int) (Math.random() * (this.PUISSANCE + 1));
			soldat.setPointsDeVie(soldat.getPointsDeVie() - puissanceCoup);
		} else {
			if (PanneauJeu.test_son == 1) {
				try {
					File fichier = new File("sounds/tir.wav");
					AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
					Clip clip = AudioSystem.getClip();
					clip.open(audioIn);
					clip.start();
				} catch (UnsupportedAudioFileException e2) {
					System.out.println("Un problème est survenu lors de chargement de tir.wav !");
				} catch (IOException e2) {
					System.out.println("Un problème est survenu lors de chargement de tir.wav !");
				} catch (LineUnavailableException e2) {
					System.out.println("Un problème est survenu lors de chargement de tir.wav !");
				}
			}
			puissanceCoup = (int) (Math.random() * (this.TIR + 1));
			soldat.setPointsDeVie(soldat.getPointsDeVie() - puissanceCoup);
		}
		if (soldat.getPointsDeVie() <= 0) {
			if (PanneauJeu.test_son == 1) {
				try {
					File fichier = new File("sounds/mort.wav");
					AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
					Clip clip = AudioSystem.getClip();
					clip.open(audioIn);
					clip.start();
				} catch (UnsupportedAudioFileException e2) {
					System.out.println("Un problème est survenu lors de chargement de mort.wav !");
				} catch (IOException e2) {
					System.out.println("Un problème est survenu lors de chargement de mort.wav !");
				} catch (LineUnavailableException e2) {
					System.out.println("Un problème est survenu lors de chargement de mort.wav !");
				}
			}
			carte.mort(soldat);
		}
	}

	public void seDeplace(Position newPos) {
		this.pos.setX(newPos.getX());
		this.pos.setY(newPos.getY());
	}
}
