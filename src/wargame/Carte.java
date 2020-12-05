package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Carte implements ICarte, IConfig, Serializable {
	private static final long serialVersionUID = 1L;
	private Element grille[][] = new Element[LARGEUR_CARTE][HAUTEUR_CARTE];
	private String nom_heros[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O" };
	private String nom_monstres[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20", "21", "22", "23", "24", "25" };
	private int nuages[][] = new int[LARGEUR_CARTE][HAUTEUR_CARTE];
	private int nbHeros = 15;
	private int nbMonstres = 25;

	public Carte() {
		Position pos;
		int nb_heros = 0;
		int nb_monstres = 0;
		int nb_rocher = 0;
		int nb_foret = 0;
		int nb_eau = 0;
		int nb_autre_obstacle = 0;
		while (nb_heros < NB_HEROS) {
			pos = trouvePositionVide('h');
			Heros h = new Heros(this, Soldat.TypesH.getTypeHAlea(), nom_heros[nb_heros], pos);
			grille[pos.getX()][pos.getY()] = h;
			nb_heros++;
		}
		while (nb_monstres < NB_MONSTRES) {
			pos = trouvePositionVide('m');
			Monstre m = new Monstre(this, Soldat.TypesM.getTypeMAlea(), nom_monstres[nb_monstres], pos);
			grille[pos.getX()][pos.getY()] = m;
			nb_monstres++;
		}
		while (nb_rocher < NB_ROCHER) {
			pos = trouvePositionVide('o');
			Obstacle o = new Obstacle(Obstacle.TypeObstacle.getObstacleAlea(0, 9), pos);
			grille[pos.getX()][pos.getY()] = o;
			nb_rocher++;
		}
		while (nb_foret < NB_FORET) {
			pos = trouvePositionVide('o');
			Obstacle o = new Obstacle(Obstacle.TypeObstacle.getObstacleAlea(9, 14), pos);
			grille[pos.getX()][pos.getY()] = o;
			nb_foret++;
		}
		while (nb_autre_obstacle < NB_AUTRE_OBSTACLE) {
			pos = trouvePositionVide('o');
			Obstacle o = new Obstacle(Obstacle.TypeObstacle.getObstacleAlea(14, 25), pos);
			grille[pos.getX()][pos.getY()] = o;
			nb_autre_obstacle++;
		}
		while (nb_eau < NB_EAU) {
			pos = trouvePositionVide('o');
			Obstacle o = new Obstacle(Obstacle.TypeObstacle.getObstacleAlea(25, 26), pos);
			grille[pos.getX()][pos.getY()] = o;
			nb_eau++;
		}
	}

	public int getNbHeros() {
		return nbHeros;
	}

	public int getNbMonstres() {
		return nbMonstres;
	}

	public Element getElement(Position pos) {
		return grille[pos.getX()][pos.getY()];
	}

	public Position trouvePositionVide(char c) { // Trouve alà©atoirement une position vide sur la carte, c='h' pour
													// hà©ros et c='m' pour monstre
		Position P;
		do {
			if (c == 'h')
				P = new Position((int) (Math.random() * 14), (int) (Math.random() * HAUTEUR_CARTE));
			else if (c == 'm')
				P = new Position((int) (Math.random() * LARGEUR_CARTE), (int) (Math.random() * HAUTEUR_CARTE));
			else
				P = new Position((int) (Math.random() * LARGEUR_CARTE), (int) (Math.random() * HAUTEUR_CARTE));
		} while (grille[P.getX()][P.getY()] != null);
		return P;
	}

	public Position trouvePositionVide(Position pos) { // Trouve une position vide choisie alà©atoirement parmi les 8
														// positions adjacentes de pos
		Position P;
		do {
			P = new Position((int) (Math.random() * 3 + pos.getX() - 1), (int) (Math.random() * 3 + pos.getY() - 1));
		} while (!P.estValide() || grille[P.getX()][P.getY()] != null);
		return P;
	}

	public Heros trouveHeros() { // Trouve alà©atoirement un hà©ros sur la carte
		Position P;
		do {
			P = new Position((int) (Math.random() * 25), (int) (Math.random() * 15));
		} while (!(grille[P.getX()][P.getY()] instanceof Heros));
		return (Heros) grille[P.getX()][P.getY()];
	}

	public Heros trouveHeros(Position pos) { // Trouve un hà©ros choisi alà©atoirement parmi les 8 positions adjacentes
												// de
												// pos
		Position P;
		do {
			P = new Position((int) (Math.random() * 3 + pos.getX() - 1), (int) (Math.random() * 3 + pos.getY() - 1));
		} while (!(grille[P.getX()][P.getY()] instanceof Heros) || (P.getX() == pos.getX() && P.getY() == pos.getY()));
		return (Heros) grille[P.getX()][P.getY()];
	}

	public void deplaceSoldat(Position pos, Soldat soldat) {
		grille[pos.getX()][pos.getY()] = grille[soldat.pos.getX()][soldat.pos.getY()];
		grille[soldat.pos.getX()][soldat.pos.getY()] = null;
		soldat.seDeplace(pos);
	}

	public void mort(Soldat perso) {
		grille[perso.pos.getX()][perso.pos.getY()] = null;
		if (perso instanceof Heros)
			this.nbHeros--;
		else
			this.nbMonstres--;
	}

	public void actionHeros(Position pos, Position pos2, JLabel label, PanneauJeu pj) {
		if (pos2.estValide()) {
			if (grille[pos2.getX()][pos2.getY()] == null) {
				if (pos.estVoisine(pos2)) {
					if (PanneauJeu.test_son == 1) {
						try {
							File fichier = new File("sounds/deplacement.wav");
							AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
							Clip clip = AudioSystem.getClip();
							clip.open(audioIn);
							clip.start();
						} catch (UnsupportedAudioFileException e2) {
							System.out.println("Un problème est survenu lors de chargement de deplacement.wav !");
						} catch (IOException e2) {
							System.out.println("Un problème est survenu lors de chargement de deplacement.wav !");
						} catch (LineUnavailableException e2) {
							System.out.println("Un problème est survenu lors de chargement de deplacement.wav !");
						}
					}
					deplaceSoldat(pos2, (Soldat) getElement(pos));
					((Heros) getElement(pos2)).setDejaJoue(true);
					getElement(pos2).setVisible(true);
				} else {
					if (PanneauJeu.test_son == 1) {
						try {
							File fichier = new File("sounds/error.wav");
							AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
							Clip clip = AudioSystem.getClip();
							clip.open(audioIn);
							clip.start();
						} catch (UnsupportedAudioFileException e2) {
							System.out.println("Un problème est survenu lors de chargement de error.wav !");
						} catch (IOException e2) {
							System.out.println("Un problème est survenu lors de chargement de error.wav !");
						} catch (LineUnavailableException e2) {
							System.out.println("Un problème est survenu lors de chargement de error.wav !");
						}
					}
					label.setText(
							"<html><center>Vous ne pouvez pas vous déplacer à une position non voisine !<center/><html/>");
					getElement(pos).setVisible(true);
				}
			}

			else if (getElement(pos2) instanceof Heros) {
				if (PanneauJeu.test_son == 1) {
					try {
						File fichier = new File("sounds/error.wav");
						AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						Clip clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e2) {
						System.out.println("Un problème est survenu lors de chargement de error.wav !");
					} catch (IOException e2) {
						System.out.println("Un problème est survenu lors de chargement de error.wav !");
					} catch (LineUnavailableException e2) {
						System.out.println("Un problème est survenu lors de chargement de error.wav !");
					}
				}
				label.setText(
						"<html><center>Vous ne pouvez pas agir à une position qui contient déjà  un ami !<center/><html/>");
				getElement(pos).setVisible(true);
			}

			else if (getElement(pos2) instanceof Monstre) {
				((Heros) getElement(pos)).combat((Monstre) getElement(pos2));
				((Heros) getElement(pos)).setDejaJoue(true);
				getElement(pos).setVisible(true);
			} else if (getElement(pos2) instanceof Obstacle) {
				if (PanneauJeu.test_son == 1) {
					try {
						File fichier = new File("sounds/error.wav");
						AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						Clip clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e2) {
						System.out.println("Un problème est survenu lors de chargement de error.wav !");
					} catch (IOException e2) {
						System.out.println("Un problème est survenu lors de chargement de error.wav !");
					} catch (LineUnavailableException e2) {
						System.out.println("Un problème est survenu lors de chargement de error.wav !");
					}
				}
				label.setText(
						"<html><center>Vous ne pouvez pas agir à  une position qui contient un obstacle !<center/><html/>");
				getElement(pos).setVisible(true);
			}
		} else {
			if (PanneauJeu.test_son == 1) {
				try {
					File fichier = new File("sounds/error.wav");
					AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
					Clip clip = AudioSystem.getClip();
					clip.open(audioIn);
					clip.start();
				} catch (UnsupportedAudioFileException e2) {
					System.out.println("Un problème est survenu lors de chargement de error.wav !");
				} catch (IOException e2) {
					System.out.println("Un problème est survenu lors de chargement de error.wav !");
				} catch (LineUnavailableException e2) {
					System.out.println("Un problème est survenu lors de chargement de error.wav !");
				}
			}
			label.setText("<html><center>Vous ne pouvez pas agir à  une position non valide !<center/><html/>");
			getElement(pos).setVisible(true);
		}
	}

	public void jouerSoldats(PanneauJeu pj) {
		double distance;
		int porteeVis;
		int plusProche_X = 50, plusProche_Y = 50;
		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				if (grille[i][j] instanceof Heros) {
					if (((Heros) grille[i][j]).getDejaJoue()) {
						((Heros) grille[i][j]).setDejaJoue(false);
					} else {
						((Heros) grille[i][j]).setPointsDeVie(((Heros) grille[i][j]).getPointsDeVie() + 10);
					}
				}
			}
		}
		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				if (grille[i][j] instanceof Monstre && !((Monstre) grille[i][j]).getDejaJoue()) {
					distance = 50;
					porteeVis = ((Monstre) grille[i][j]).getPORTEE_VISUELLE();
					for (int k = i - porteeVis; k <= i + porteeVis; k++) {
						for (int l = j - porteeVis; l <= j + porteeVis; l++) {
							if (new Position(k, l).estValide() && grille[k][l] instanceof Heros) {
								if (Math.sqrt(Math.pow(k - i, 2) + Math.pow(l - j, 2)) < distance) {
									distance = Math.sqrt(Math.pow(k - i, 2) + Math.pow(l - j, 2));
									plusProche_X = k;
									plusProche_Y = l;
								}
							}
						}

					}
					if (distance != 50) {
						((Monstre) grille[i][j]).combat((Heros) grille[plusProche_X][plusProche_Y]);
						((Monstre) grille[i][j]).setDejaJoue(true);
					} else {
						Position pos = new Position(i, j);
						double distance2 = 50;
						for (int k = i - porteeVis; k <= i + porteeVis; k++) {
							for (int l = j - porteeVis; l <= j + porteeVis; l++) {
								if (new Position(k, l).estValide() && !(k == i && l == j)
										&& grille[k][l] instanceof Monstre) {
									if (Math.sqrt(Math.pow(k - i, 2) + Math.pow(l - j, 2)) < distance) {
										distance = Math.sqrt(Math.pow(k - i, 2) + Math.pow(l - j, 2));
										plusProche_X = k;
										plusProche_Y = l;
									}
								}
							}

						}
						if (distance != 50) {
							if (i <= 6) {
								for (int k = j - 1; k <= j + 1; k++) {
									if (new Position(i + 1, k).estValide() && grille[i + 1][k] == null
											&& Math.sqrt(Math.pow(i + 1 - plusProche_X, 2)
													+ Math.pow(k - plusProche_Y, 2)) < distance2) {
										distance2 = Math.sqrt(
												Math.pow(i + 1 - plusProche_X, 2) + Math.pow(k - plusProche_Y, 2));
										pos.setX(i + 1);
										pos.setY(k);
									}
								}
								if (distance2 != 50) {
									if (PanneauJeu.test_son == 1) {
										try {
											File fichier = new File("sounds/deplacement.wav");
											AudioInputStream audioIn = AudioSystem
													.getAudioInputStream(fichier.toURI().toURL());
											Clip clip = AudioSystem.getClip();
											clip.open(audioIn);
											clip.start();
										} catch (UnsupportedAudioFileException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (IOException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (LineUnavailableException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										}
									}
									deplaceSoldat(pos, (Soldat) grille[i][j]);
									((Monstre) getElement(pos)).setDejaJoue(true);
								} else {
									for (int k = i; k <= i + 1; k++) {
										for (int l = j - 1; l <= j + 1; l++) {
											if (new Position(k, l).estValide() && !(k == i && l == j)
													&& grille[k][l] == null && Math.sqrt(Math.pow(k - plusProche_X, 2)
															+ Math.pow(l - plusProche_Y, 2)) < distance2) {
												distance2 = Math.sqrt(
														Math.pow(k - plusProche_X, 2) + Math.pow(l - plusProche_Y, 2));
												pos.setX(k);
												pos.setY(l);
											}
										}
									}
									if (distance2 != 50) {
										if (PanneauJeu.test_son == 1) {
											try {
												File fichier = new File("sounds/deplacement.wav");
												AudioInputStream audioIn = AudioSystem
														.getAudioInputStream(fichier.toURI().toURL());
												Clip clip = AudioSystem.getClip();
												clip.open(audioIn);
												clip.start();
											} catch (UnsupportedAudioFileException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											} catch (IOException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											} catch (LineUnavailableException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											}
										}
										deplaceSoldat(pos, (Soldat) grille[i][j]);
										((Monstre) getElement(pos)).setDejaJoue(true);
									} else {
										((Monstre) getElement(pos)).setDejaJoue(true);
									}
								}
							} else if (i >= 22) {
								for (int k = j - 1; k <= j + 1; k++) {
									if (new Position(i - 1, k).estValide() && grille[i - 1][k] == null
											&& Math.sqrt(Math.pow(i - 1 - plusProche_X, 2)
													+ Math.pow(k - plusProche_Y, 2)) < distance2) {
										distance2 = Math.sqrt(
												Math.pow(i - 1 - plusProche_X, 2) + Math.pow(k - plusProche_Y, 2));
										pos.setX(i - 1);
										pos.setY(k);
									}
								}
								if (distance2 != 50) {
									if (PanneauJeu.test_son == 1) {
										try {
											File fichier = new File("sounds/deplacement.wav");
											AudioInputStream audioIn = AudioSystem
													.getAudioInputStream(fichier.toURI().toURL());
											Clip clip = AudioSystem.getClip();
											clip.open(audioIn);
											clip.start();
										} catch (UnsupportedAudioFileException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (IOException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (LineUnavailableException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										}
									}
									deplaceSoldat(pos, (Soldat) grille[i][j]);
									((Monstre) getElement(pos)).setDejaJoue(true);
								} else {
									for (int k = i - 1; k <= i; k++) {
										for (int l = j - 1; l <= j + 1; l++) {
											if (new Position(k, l).estValide() && !(k == i && l == j)
													&& grille[k][l] == null && Math.sqrt(Math.pow(k - plusProche_X, 2)
															+ Math.pow(l - plusProche_Y, 2)) < distance2) {
												distance2 = Math.sqrt(
														Math.pow(k - plusProche_X, 2) + Math.pow(l - plusProche_Y, 2));
												pos.setX(k);
												pos.setY(l);
											}
										}
									}
									if (distance2 != 50) {
										if (PanneauJeu.test_son == 1) {
											try {
												File fichier = new File("sounds/deplacement.wav");
												AudioInputStream audioIn = AudioSystem
														.getAudioInputStream(fichier.toURI().toURL());
												Clip clip = AudioSystem.getClip();
												clip.open(audioIn);
												clip.start();
											} catch (UnsupportedAudioFileException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											} catch (IOException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											} catch (LineUnavailableException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											}
										}
										deplaceSoldat(pos, (Soldat) grille[i][j]);
										((Monstre) getElement(pos)).setDejaJoue(true);
									} else {
										((Monstre) getElement(pos)).setDejaJoue(true);
									}
								}
							} else {
								for (int k = i - 1; k <= i + 1; k++) {
									for (int l = j - 1; l <= j + 1; l++) {
										if (new Position(k, l).estValide() && !(k == i && l == j)
												&& grille[k][l] == null && Math.sqrt(Math.pow(k - plusProche_X, 2)
														+ Math.pow(l - plusProche_Y, 2)) < distance2) {
											distance2 = Math.sqrt(
													Math.pow(k - plusProche_X, 2) + Math.pow(l - plusProche_Y, 2));
											pos.setX(k);
											pos.setY(l);
										}
									}
								}
								if (distance2 != 50) {
									if (PanneauJeu.test_son == 1) {
										try {
											File fichier = new File("sounds/deplacement.wav");
											AudioInputStream audioIn = AudioSystem
													.getAudioInputStream(fichier.toURI().toURL());
											Clip clip = AudioSystem.getClip();
											clip.open(audioIn);
											clip.start();
										} catch (UnsupportedAudioFileException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (IOException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (LineUnavailableException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										}
									}
									deplaceSoldat(pos, (Soldat) grille[i][j]);
									((Monstre) getElement(pos)).setDejaJoue(true);
								} else {
									((Monstre) getElement(pos)).setDejaJoue(true);
								}
							}
						} else {

							if (i <= 6) {
								for (int k = j - 1; k <= j + 1; k++) {
									if (new Position(i + 1, k).estValide() && grille[i + 1][k] == null) {
										distance = 40;
										pos.setX(i + 1);
										pos.setY(k);
									}
								}
								if (distance2 != 50) {
									if (PanneauJeu.test_son == 1) {
										try {
											File fichier = new File("sounds/deplacement.wav");
											AudioInputStream audioIn = AudioSystem
													.getAudioInputStream(fichier.toURI().toURL());
											Clip clip = AudioSystem.getClip();
											clip.open(audioIn);
											clip.start();
										} catch (UnsupportedAudioFileException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (IOException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (LineUnavailableException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										}
									}
									deplaceSoldat(pos, (Soldat) grille[i][j]);
									((Monstre) getElement(pos)).setDejaJoue(true);
								} else {
									for (int k = i; k <= i + 1; k++) {
										for (int l = j - 1; l <= j + 1; l++) {
											if (new Position(k, l).estValide() && !(k == i && l == j)
													&& grille[k][l] == null) {
												distance2 = 40;
												pos.setX(k);
												pos.setY(l);
											}
										}
									}
									if (distance2 != 50) {
										if (PanneauJeu.test_son == 1) {
											try {
												File fichier = new File("sounds/deplacement.wav");
												AudioInputStream audioIn = AudioSystem
														.getAudioInputStream(fichier.toURI().toURL());
												Clip clip = AudioSystem.getClip();
												clip.open(audioIn);
												clip.start();
											} catch (UnsupportedAudioFileException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											} catch (IOException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											} catch (LineUnavailableException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											}
										}
										deplaceSoldat(pos, (Soldat) grille[i][j]);
										((Monstre) getElement(pos)).setDejaJoue(true);
									} else {
										((Monstre) getElement(pos)).setDejaJoue(true);
									}
								}
							} else if (i >= 22) {
								for (int k = j - 1; k <= j + 1; k++) {
									if (new Position(i - 1, k).estValide() && grille[i - 1][k] == null) {
										distance2 = 40;
										pos.setX(i - 1);
										pos.setY(k);
									}
								}
								if (distance2 != 50) {
									if (PanneauJeu.test_son == 1) {
										try {
											File fichier = new File("sounds/deplacement.wav");
											AudioInputStream audioIn = AudioSystem
													.getAudioInputStream(fichier.toURI().toURL());
											Clip clip = AudioSystem.getClip();
											clip.open(audioIn);
											clip.start();
										} catch (UnsupportedAudioFileException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (IOException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (LineUnavailableException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										}
									}
									deplaceSoldat(pos, (Soldat) grille[i][j]);
									((Monstre) getElement(pos)).setDejaJoue(true);
								} else {
									for (int k = i - 1; k <= i; k++) {
										for (int l = j - 1; l <= j + 1; l++) {
											if (new Position(k, l).estValide() && !(k == i && l == j)
													&& grille[k][l] == null) {
												distance2 = 40;
												pos.setX(k);
												pos.setY(l);
											}
										}
									}
									if (distance2 != 50) {
										if (PanneauJeu.test_son == 1) {
											try {
												File fichier = new File("sounds/deplacement.wav");
												AudioInputStream audioIn = AudioSystem
														.getAudioInputStream(fichier.toURI().toURL());
												Clip clip = AudioSystem.getClip();
												clip.open(audioIn);
												clip.start();
											} catch (UnsupportedAudioFileException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											} catch (IOException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											} catch (LineUnavailableException e1) {
												System.out.println(
														"Un problème est survenu lors de chargement de deplacement.wav !");
											}
										}
										deplaceSoldat(pos, (Soldat) grille[i][j]);
										((Monstre) getElement(pos)).setDejaJoue(true);
									} else {
										((Monstre) getElement(pos)).setDejaJoue(true);
									}
								}
							} else {
								for (int k = i - 1; k <= i + 1; k++) {
									for (int l = j - 1; l <= j + 1; l++) {
										if (new Position(k, l).estValide() && !(k == i && l == j)
												&& grille[k][l] == null) {
											distance2 = 40;
											pos.setX(k);
											pos.setY(l);
										}
									}
								}
								if (distance2 != 50) {
									if (PanneauJeu.test_son == 1) {
										try {
											File fichier = new File("sounds/deplacement.wav");
											AudioInputStream audioIn = AudioSystem
													.getAudioInputStream(fichier.toURI().toURL());
											Clip clip = AudioSystem.getClip();
											clip.open(audioIn);
											clip.start();
										} catch (UnsupportedAudioFileException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (IOException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										} catch (LineUnavailableException e1) {
											System.out.println(
													"Un problème est survenu lors de chargement de deplacement.wav !");
										}
									}
									deplaceSoldat(pos, (Soldat) grille[i][j]);
									((Monstre) getElement(pos)).setDejaJoue(true);
								} else {
									((Monstre) getElement(pos)).setDejaJoue(true);
								}
							}
						}
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							System.out.println("Un problème est survenu lors de Thread sleep en fenetreJeu !");
						}
						pj.paintImmediately(0, 0, pj.getWidth(), pj.getHeight());
					}
				}
			}
		}
		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				if (grille[i][j] instanceof Monstre) {
					if (((Monstre) grille[i][j]).getDejaJoue()) {
						((Monstre) grille[i][j]).setDejaJoue(false);
					} else {
						((Monstre) grille[i][j]).setPointsDeVie(((Monstre) grille[i][j]).getPointsDeVie() + 10);
					}
				}
			}
		}
		pj.repaint();
	}

	// tout Dessiner qui ne dessine pas les nuages :

	/*
	 * public void toutDessiner(Graphics g) { Image img = null; int dessineVie; int
	 * pourcentageVie; int x, y; String file = new String(); int porteeVis;
	 * 
	 * for (int i = 0; i < LARGEUR_CARTE; i++) { for (int j = 0; j < HAUTEUR_CARTE;
	 * j++) { nuages[i][j] = 0; } }
	 * 
	 * for (int i = 0; i < LARGEUR_CARTE; i++) { for (int j = 0; j < HAUTEUR_CARTE;
	 * j++) { x = i * 40; y = j * 40; if (grille[i][j] instanceof Heros) {
	 * 
	 * porteeVis = ((Heros) grille[i][j]).getPORTEE_VISUELLE();
	 * 
	 * for (int k = i - porteeVis; k <= i + porteeVis; k++) { for (int l = j -
	 * porteeVis; l <= j + porteeVis; l++) { if (new Position(k, l).estValide()) {
	 * nuages[k][l] = 1; } } }
	 * 
	 * if (grille[i][j].isVisible()) { if (((Heros) grille[i][j]).getDejaJoue() ==
	 * true) { try { img = ImageIO.read(new File("pictures/dejaJoue.png")); } catch
	 * (IOException e) { System.out.
	 * println("Un problème est survenu lors du chargement de dejaJoue.png !"); }
	 * g.drawImage(img, x, y + 25, 40, 15, null); } try { switch (((Heros)
	 * grille[i][j]).getTYPE()) { case HUMAIN: file = "pictures/humain.png"; break;
	 * case NAIN: file = "pictures/nain.png"; break; case ELF: file =
	 * "pictures/elf.png"; break; case HOBBIT: file = "pictures/hobbit.png"; break;
	 * case MAGICIEN: file = "pictures/magicien.png"; break; } img =
	 * ImageIO.read(new File(file)); } catch (IOException e) {
	 * System.out.println("Un problème est survenu lors du chargement d'une photo !"
	 * ); } g.drawImage(img, x, y, 40, 40, null); g.setColor(COULEUR_VIDE);
	 * g.drawRect(x, y, 4, 30); dessineVie = ((Heros) grille[i][j]).getPointsDeVie()
	 * * 29 / ((Heros) grille[i][j]).getPointsDeVieMAX(); pourcentageVie = ((Heros)
	 * grille[i][j]).getPointsDeVie() * 100 / ((Heros)
	 * grille[i][j]).getPointsDeVieMAX(); if (pourcentageVie < 20) {
	 * g.setColor(Color.RED); } else if (pourcentageVie < 50) {
	 * g.setColor(Color.YELLOW); } else { g.setColor(Color.GREEN); } g.fillRect(x +
	 * 1, y + 1 + (29 - dessineVie), 3, dessineVie); } } else if (grille[i][j]
	 * instanceof Monstre) { if (((Monstre) grille[i][j]).getDejaJoue() == true) {
	 * try { img = ImageIO.read(new File("pictures/dejaJoue.png")); } catch
	 * (IOException e) { System.out.
	 * println("Un problème est survenu lors du chargement de dejaJoue.png !"); }
	 * g.drawImage(img, x, y + 25, 40, 15, null); } try { switch (((Monstre)
	 * grille[i][j]).getTYPE()) { case TROLL: file = "pictures/troll.png"; break;
	 * case ORC: file = "pictures/orc.png"; break; case GOBELIN: file =
	 * "pictures/gobelin.png"; break; case CATAPULTE: file =
	 * "pictures/catapulte.png"; break; } img = ImageIO.read(new File(file)); }
	 * catch (IOException e) {
	 * System.out.println("Un problème est survenu lors du chargement d'une photo !"
	 * ); } g.drawImage(img, x, y, 40, 40, null); g.setColor(COULEUR_VIDE);
	 * g.drawRect(x, y, 4, 30); dessineVie = ((Monstre)
	 * grille[i][j]).getPointsDeVie() * 29 / ((Monstre)
	 * grille[i][j]).getPointsDeVieMAX(); pourcentageVie = ((Monstre)
	 * grille[i][j]).getPointsDeVie() * 100 / ((Monstre)
	 * grille[i][j]).getPointsDeVieMAX(); if (pourcentageVie < 20) {
	 * g.setColor(Color.RED); } else if (pourcentageVie < 50) {
	 * g.setColor(Color.YELLOW); } else { g.setColor(Color.GREEN); } g.fillRect(x +
	 * 1, y + 1 + (29 - dessineVie), 3, dessineVie); } else if (grille[i][j]
	 * instanceof Obstacle) { int eau = 0; try { switch (((Obstacle)
	 * grille[i][j]).getTYPE()) { case ROCHER1: file = "pictures/rocher1.png";
	 * break; case ROCHER2: file = "pictures/rocher2.png"; break; case ROCHER3: file
	 * = "pictures/rocher3.png"; break; case ROCHER4: file = "pictures/rocher4.png";
	 * break; case ROCHER5: file = "pictures/rocher5.png"; break; case ROCHER6: file
	 * = "pictures/rocher6.png"; break; case ROCHER7: file = "pictures/rocher7.png";
	 * break; case ROCHER8: file = "pictures/rocher8.png"; break; case ROCHER9: file
	 * = "pictures/rocher9.png"; break; case FORET1: file = "pictures/foret1.png";
	 * break; case FORET2: file = "pictures/foret2.png"; break; case FORET3: file =
	 * "pictures/foret3.png"; break; case FORET4: file = "pictures/foret4.png";
	 * break; case FORET5: file = "pictures/foret5.png"; break; case
	 * AUTRE_OBSTACLE1: file = "pictures/autreObstacle1.png"; break; case
	 * AUTRE_OBSTACLE2: file = "pictures/autreObstacle2.png"; break; case
	 * AUTRE_OBSTACLE3: file = "pictures/autreObstacle3.png"; break; case
	 * AUTRE_OBSTACLE4: file = "pictures/autreObstacle4.png"; break; case
	 * AUTRE_OBSTACLE5: file = "pictures/autreObstacle5.png"; break; case
	 * AUTRE_OBSTACLE6: file = "pictures/autreObstacle6.png"; break; case
	 * AUTRE_OBSTACLE7: file = "pictures/autreObstacle7.png"; break; case
	 * AUTRE_OBSTACLE8: file = "pictures/autreObstacle8.png"; break; case
	 * AUTRE_OBSTACLE9: file = "pictures/autreObstacle9.png"; break; case
	 * AUTRE_OBSTACLE10: file = "pictures/autreObstacle10.png"; break; case
	 * file = "pictures/eau.png"; eau = 1; break; } img = ImageIO.read(new
	 * File(file)); } catch (IOException e) {
	 * System.out.println("Un problème est survenu lors du chargement d'une photo !"
	 * ); } if (eau != 1) { g.drawImage(img, x, y, 40, 40, null); } else {
	 * g.drawImage(img, x, y + 20, 40, 20, null); } } } } }
	 */

	// toutDessiner qui dessine aussi les nuages :

	public void toutDessiner(Graphics g) {
		Image img = null;
		int dessineVie;
		int pourcentageVie;
		int x, y;
		String file = new String();
		int porteeVis;

		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				nuages[i][j] = 0;
			}
		}

		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				if (grille[i][j] instanceof Heros) {

					porteeVis = ((Heros) grille[i][j]).getPORTEE_VISUELLE();

					for (int k = i - porteeVis; k <= i + porteeVis; k++) {
						for (int l = j - porteeVis; l <= j + porteeVis; l++) {
							if (new Position(k, l).estValide()) {
								nuages[k][l] = 1;
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				x = i * 40;
				y = j * 40;
				if (nuages[i][j] == 0) {
					try {
						img = ImageIO.read(new File("pictures/nuage.png"));
					} catch (IOException e) {
						System.out.println("Un problème est survenu lors du chargement de nuage.png !");
					}
					g.drawImage(img, x, y, 40, 40, null);
				}
			}
		}

		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				x = i * 40;
				y = j * 40;
				if (nuages[i][j] == 1) {
					if (grille[i][j] instanceof Heros) {

						if (grille[i][j].isVisible()) {
							if (((Heros) grille[i][j]).getDejaJoue() == true) {
								try {
									img = ImageIO.read(new File("pictures/dejaJoue.png"));
								} catch (IOException e) {
									System.out.println("Un problème est survenu lors du chargement de dejaJoue.png !");
								}
								g.drawImage(img, x, y + 25, 40, 15, null);
							}
							try {
								switch (((Heros) grille[i][j]).getTYPE()) {
								case HUMAIN:
									file = "pictures/humain.png";
									break;
								case NAIN:
									file = "pictures/nain.png";
									break;
								case ELF:
									file = "pictures/elf.png";
									break;
								case HOBBIT:
									file = "pictures/hobbit.png";
									break;
								case MAGICIEN:
									file = "pictures/magicien.png";
									break;
								}
								img = ImageIO.read(new File(file));
							} catch (IOException e) {
								System.out.println("Un problème est survenu lors du chargement d'une photo !");
							}
							g.drawImage(img, x, y, 40, 40, null);
							g.setColor(COULEUR_VIDE);
							g.drawRect(x, y, 4, 30);
							dessineVie = ((Heros) grille[i][j]).getPointsDeVie() * 29
									/ ((Heros) grille[i][j]).getPointsDeVieMAX();
							pourcentageVie = ((Heros) grille[i][j]).getPointsDeVie() * 100
									/ ((Heros) grille[i][j]).getPointsDeVieMAX();
							if (pourcentageVie < 20) {
								g.setColor(Color.RED);
							} else if (pourcentageVie < 50) {
								g.setColor(Color.YELLOW);
							} else {
								g.setColor(Color.GREEN);
							}
							g.fillRect(x + 1, y + 1 + (29 - dessineVie), 3, dessineVie);
						}
					} else if (grille[i][j] instanceof Monstre) {
						if (((Monstre) grille[i][j]).getDejaJoue() == true) {
							try {
								img = ImageIO.read(new File("pictures/dejaJoue.png"));
							} catch (IOException e) {
								System.out.println("Un problème est survenu lors du chargement de dejaJoue.png !");
							}
							g.drawImage(img, x, y + 25, 40, 15, null);
						}
						try {
							switch (((Monstre) grille[i][j]).getTYPE()) {
							case TROLL:
								file = "pictures/troll.png";
								break;
							case ORC:
								file = "pictures/orc.png";
								break;
							case GOBELIN:
								file = "pictures/gobelin.png";
								break;
							case CATAPULTE:
								file = "pictures/catapulte.png";
								break;
							}
							img = ImageIO.read(new File(file));
						} catch (IOException e) {
							System.out.println("Un problème est survenu lors du chargement d'une photo !");
						}
						g.drawImage(img, x, y, 40, 40, null);
						g.setColor(COULEUR_VIDE);
						g.drawRect(x, y, 4, 30);
						dessineVie = ((Monstre) grille[i][j]).getPointsDeVie() * 29
								/ ((Monstre) grille[i][j]).getPointsDeVieMAX();
						pourcentageVie = ((Monstre) grille[i][j]).getPointsDeVie() * 100
								/ ((Monstre) grille[i][j]).getPointsDeVieMAX();
						if (pourcentageVie < 20) {
							g.setColor(Color.RED);
						} else if (pourcentageVie < 50) {
							g.setColor(Color.YELLOW);
						} else {
							g.setColor(Color.GREEN);
						}
						g.fillRect(x + 1, y + 1 + (29 - dessineVie), 3, dessineVie);
					} else if (grille[i][j] instanceof Obstacle) {
						int eau = 0;
						try {
							switch (((Obstacle) grille[i][j]).getTYPE()) {
							case ROCHER1:
								file = "pictures/rocher1.png";
								break;
							case ROCHER2:
								file = "pictures/rocher2.png";
								break;
							case ROCHER3:
								file = "pictures/rocher3.png";
								break;
							case ROCHER4:
								file = "pictures/rocher4.png";
								break;
							case ROCHER5:
								file = "pictures/rocher5.png";
								break;
							case ROCHER6:
								file = "pictures/rocher6.png";
								break;
							case ROCHER7:
								file = "pictures/rocher7.png";
								break;
							case ROCHER8:
								file = "pictures/rocher8.png";
								break;
							case ROCHER9:
								file = "pictures/rocher9.png";
								break;
							case FORET1:
								file = "pictures/foret1.png";
								break;
							case FORET2:
								file = "pictures/foret2.png";
								break;
							case FORET3:
								file = "pictures/foret3.png";
								break;
							case FORET4:
								file = "pictures/foret4.png";
								break;
							case FORET5:
								file = "pictures/foret5.png";
								break;
							case AUTRE_OBSTACLE1:
								file = "pictures/autreObstacle1.png";
								break;
							case AUTRE_OBSTACLE2:
								file = "pictures/autreObstacle2.png";
								break;
							case AUTRE_OBSTACLE3:
								file = "pictures/autreObstacle3.png";
								break;
							case AUTRE_OBSTACLE4:
								file = "pictures/autreObstacle4.png";
								break;
							case AUTRE_OBSTACLE5:
								file = "pictures/autreObstacle5.png";
								break;
							case AUTRE_OBSTACLE6:
								file = "pictures/autreObstacle6.png";
								break;
							case AUTRE_OBSTACLE7:
								file = "pictures/autreObstacle7.png";
								break;
							case AUTRE_OBSTACLE8:
								file = "pictures/autreObstacle8.png";
								break;
							case AUTRE_OBSTACLE9:
								file = "pictures/autreObstacle9.png";
								break;
							case AUTRE_OBSTACLE10:
								file = "pictures/autreObstacle10.png";
								break;
							case AUTRE_OBSTACLE11:
								file = "pictures/autreObstacle11.png";
								break;
							case EAU:
								file = "pictures/eau.png";
								eau = 1;
								break;
							}
							img = ImageIO.read(new File(file));
						} catch (IOException e) {
							System.out.println("Un problème est survenu lors du chargement d'une photo !");
						}
						if (eau != 1) {
							g.drawImage(img, x, y, 40, 40, null);
						} else {
							g.drawImage(img, x, y + 20, 40, 20, null);
						}
					}
				}
			}
		}
	}
	
	public void toutDessiner2(Graphics g) {
		Image img = null;
		int x, y;
		String file = new String();
		int porteeVis;

		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				nuages[i][j] = 0;
			}
		}

		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				if (grille[i][j] instanceof Heros) {

					porteeVis = ((Heros) grille[i][j]).getPORTEE_VISUELLE();

					for (int k = i - porteeVis; k <= i + porteeVis; k++) {
						for (int l = j - porteeVis; l <= j + porteeVis; l++) {
							if (new Position(k, l).estValide()) {
								nuages[k][l] = 1;
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				x = i * 11;
				y = j * 11;
				if (nuages[i][j] == 0) {
					try {
						img = ImageIO.read(new File("pictures/nuage.png"));
					} catch (IOException e) {
						System.out.println("Un problème est survenu lors du chargement de nuage.png !");
					}
					//g.drawImage(img, x, y, 40, 40, null);
				}
			}
		}

		for (int i = 0; i < LARGEUR_CARTE; i++) {
			for (int j = 0; j < HAUTEUR_CARTE; j++) {
				x = i * 11;
				y = j * 11;
				if (nuages[i][j] == 1) {
					if (grille[i][j] instanceof Heros) {

						if (grille[i][j].isVisible()) {
							try {
								switch (((Heros) grille[i][j]).getTYPE()) {
								case HUMAIN:
									file = "pictures/map_hero.png";
									break;
								case NAIN:
									file = "pictures/map_hero.png";
									break;
								case ELF:
									file = "pictures/map_hero.png";
									break;
								case HOBBIT:
									file = "pictures/map_hero.png";
									break;
								case MAGICIEN:
									file = "pictures/map_hero.png";
									break;
								}
								img = ImageIO.read(new File(file));
							} catch (IOException e) {
								System.out.println("Un problème est survenu lors du chargement d'une photo !");
							}
							g.drawImage(img, x, y, 40, 40, null);
						}
					} else if (grille[i][j] instanceof Monstre) {
						try {
							switch (((Monstre) grille[i][j]).getTYPE()) {
							case TROLL:
								file = "pictures/map_monstre.png";
								break;
							case ORC:
								file = "pictures/map_monstre.png";
								break;
							case GOBELIN:
								file = "pictures/map_monstre.png";
								break;
							case CATAPULTE:
								file = "pictures/map_monstre.png";
								break;
							}
							img = ImageIO.read(new File(file));
						} catch (IOException e) {
							System.out.println("Un problème est survenu lors du chargement d'une photo !");
						}
						g.drawImage(img, x, y, 40, 40, null);
					} else if (grille[i][j] instanceof Obstacle) {
						int eau = 0;
						try {
							switch (((Obstacle) grille[i][j]).getTYPE()) {
							case ROCHER1:
								file = "pictures/rocher1.png";
								break;
							case ROCHER2:
								file = "pictures/rocher2.png";
								break;
							case ROCHER3:
								file = "pictures/rocher3.png";
								break;
							case ROCHER4:
								file = "pictures/rocher4.png";
								break;
							case ROCHER5:
								file = "pictures/rocher5.png";
								break;
							case ROCHER6:
								file = "pictures/rocher6.png";
								break;
							case ROCHER7:
								file = "pictures/rocher7.png";
								break;
							case ROCHER8:
								file = "pictures/rocher8.png";
								break;
							case ROCHER9:
								file = "pictures/rocher9.png";
								break;
							case FORET1:
								file = "pictures/foret1.png";
								break;
							case FORET2:
								file = "pictures/foret2.png";
								break;
							case FORET3:
								file = "pictures/foret3.png";
								break;
							case FORET4:
								file = "pictures/foret4.png";
								break;
							case FORET5:
								file = "pictures/foret5.png";
								break;
							case AUTRE_OBSTACLE1:
								file = "pictures/autreObstacle1.png";
								break;
							case AUTRE_OBSTACLE2:
								file = "pictures/autreObstacle2.png";
								break;
							case AUTRE_OBSTACLE3:
								file = "pictures/autreObstacle3.png";
								break;
							case AUTRE_OBSTACLE4:
								file = "pictures/autreObstacle4.png";
								break;
							case AUTRE_OBSTACLE5:
								file = "pictures/autreObstacle5.png";
								break;
							case AUTRE_OBSTACLE6:
								file = "pictures/autreObstacle6.png";
								break;
							case AUTRE_OBSTACLE7:
								file = "pictures/autreObstacle7.png";
								break;
							case AUTRE_OBSTACLE8:
								file = "pictures/autreObstacle8.png";
								break;
							case AUTRE_OBSTACLE9:
								file = "pictures/autreObstacle9.png";
								break;
							case AUTRE_OBSTACLE10:
								file = "pictures/autreObstacle10.png";
								break;
							case AUTRE_OBSTACLE11:
								file = "pictures/autreObstacle11.png";
								break;
							case EAU:
								file = "pictures/eau.png";
								eau = 1;
								break;
							}
							img = ImageIO.read(new File(file));
						} catch (IOException e) {
							System.out.println("Un problème est survenu lors du chargement d'une photo !");
						}
						if (eau != 1) {
							//g.drawImage(img, x, y, 40, 40, null);
						} else {
							//g.drawImage(img, x, y + 20, 40, 20, null);
						}
					}
				}
			}
		}
	}
}