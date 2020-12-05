package wargame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PanneauJeu extends JPanel implements IConfig {
	private static final long serialVersionUID = 1L;
	private Font font;
	private Carte carte;
	private Carte carte2;
	private JToolBar barreOutils;
	private JButton tour;
	private JButton redemarrer;
	private JButton sauvegarder;
	private JButton restaurer;
	private JButton son;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JPanel panel1;
	private Image img;
	private boolean tourHumain = true;
	private boolean deplacement = false;
	private int xDep, yDep;
	private Position pos;
	private Position pos2;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String file;
	private Clip clip1;
	private Clip clip;
	private AudioInputStream audioIn;
	int option;
	public static int test_son = 1;
	String nomSauvegarde;

	public PanneauJeu(int test) {

	}

	public PanneauJeu() {
		font = new Font("Tahoma", Font.ITALIC, 20);
		carte = new Carte();
		file = new String();

		barreOutils = new JToolBar();
		barreOutils.setOpaque(true);
		panel1 = new JPanel();
		panel1.setLayout(null);

		label1 = new JLabel(new ImageIcon("pictures/label.png"));
		label1.setFont(font);
		label1.setOpaque(true);
		label1.setForeground(Color.white);
		label1.setBackground(Color.DARK_GRAY);
		label1.setVerticalTextPosition(SwingConstants.CENTER);
		label1.setHorizontalTextPosition(SwingConstants.CENTER);

		label2 = new JLabel(new ImageIcon("pictures/label.png"));
		label2.setText("<html><center>Les messages d'informations sur le jeu seront affichés ici<center/><html>");
		label2.setFont(font);
		label2.setOpaque(true);
		label2.setForeground(Color.white);
		label2.setBackground(Color.DARK_GRAY);
		label2.setVerticalTextPosition(SwingConstants.CENTER);
		label2.setHorizontalTextPosition(SwingConstants.CENTER);

		label3 = new JLabel(new ImageIcon("pictures/background.jpg"));
		label3.setFont(font);
		label3.setOpaque(true);
		label3.setBackground(new Color(0, 103, 132));
		
		carte2 = carte;
		
		JPanel panel5 = new JPanel() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
				try {
					img = ImageIO.read(new File("pictures/label.png"));
				} catch (IOException e) {
					System.out.println("Un problème est survenu lors du chargement de label.png !");
				}
				g.drawImage(img, 3, 0, 274, 191, this);
				carte2.toutDessiner2(g);
		    }
		};
		
		panel5.setOpaque(true);
		panel5.setForeground(Color.white);
		panel5.setBackground(Color.DARK_GRAY);
		
		son = new JButton(new ImageIcon("pictures/son_on.png"));
		son.setForeground(Color.white);
		son.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (test_son == 1) {
					son.setIcon(new ImageIcon("pictures/son_off.png"));
					clip1.stop();
					test_son = 0;
				} else if (test_son == 0) {
					son.setIcon(new ImageIcon("pictures/son_on.png"));
					clip1.loop(50);
					test_son = 1;
				}
			}
		});

		/* Traitement du son */
		if (test_son == 1) {
			try {
				/* Ouverture fichier son */
				File fichier = new File("sounds/music.wav");
				audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
				clip1 = AudioSystem.getClip();
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					System.out.println("Un problème est survenu lors de Thread sleep en fenetreJeu !");
				}
				/* Lancer et boucler le son */
				clip1.open(audioIn);
				clip1.loop(50);
			} catch (UnsupportedAudioFileException e) {
				System.out.println("Un problème est survenu lors de chargement de music.wav !");
			} catch (IOException e) {
				System.out.println("Un problème est survenu lors de chargement de music.wav !");
			} catch (LineUnavailableException e) {
				System.out.println("Un problème est survenu lors de chargement de music.wav !");
			}
		}
		/* Fin du traitement de son */

		tour = new JButton(new ImageIcon("pictures/button.png"));
		tour.setFont(font);
		tour.setForeground(Color.white);
		tour.setText("Fin du tour");
		tour.setVerticalTextPosition(SwingConstants.CENTER);
		tour.setHorizontalTextPosition(SwingConstants.CENTER);
		tour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}

				tourHumain = false;
				PanneauJeu.this.setEnabled(false);
				barreOutils.setEnabled(false);
				tour.setEnabled(false);

				carte.jouerSoldats(PanneauJeu.this);
				carte2=carte;
				panel5.repaint();

				tour.setEnabled(true);
				barreOutils.setEnabled(true);
				PanneauJeu.this.setEnabled(true);
				tourHumain = true;

				if (carte.getNbHeros() == 0) {
					if (test_son == 1) {
						try {
							File fichier = new File("sounds/victoir.wav");
							audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
							clip = AudioSystem.getClip();
							clip.open(audioIn);
							clip.start();
						} catch (UnsupportedAudioFileException e1) {
							System.out.println("Un problème est survenu lors de chargement de victoir.wav !");
						} catch (IOException e1) {
							System.out.println("Un problème est survenu lors de chargement de victoir.wav !");
						} catch (LineUnavailableException e1) {
							System.out.println("Un problème est survenu lors de chargement de victoir.wav !");
						}
					}
					label2.setText(
							"<html><center> Désolé vous n'avez plus de soldat, vous avez perdu la partie <center/><html/>");
					JOptionPane.showMessageDialog(null,
							" Désolé vous n'avez plus de soldat, vous avez perdu la partie ", ":( Fin de Partie",
							JOptionPane.INFORMATION_MESSAGE);
					tour.setEnabled(false);
					PanneauJeu.this.setEnabled(false);
				}
			}
		});
		tour.setToolTipText("Clicker ici pour terminer votre tour");

		redemarrer = new JButton(new ImageIcon("pictures/button.png"));
		redemarrer.setFont(font);
		redemarrer.setForeground(Color.white);
		redemarrer.setText("Redémarrer");
		redemarrer.setVerticalTextPosition(SwingConstants.CENTER);
		redemarrer.setHorizontalTextPosition(SwingConstants.CENTER);
		redemarrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}

				option = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment Redémarrer une nouvelle partie ?",
						"Redémarrage de la partie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}

				if (option == JOptionPane.YES_OPTION) {
					carte = new Carte();
					repaint();
					carte2 = carte;
					panel5.repaint();
				}
			}
		});
		redemarrer.setToolTipText("Clicker ici pour démarrer une nouvelle partie");

		sauvegarder = new JButton(new ImageIcon("pictures/button.png"));
		sauvegarder.setFont(font);
		sauvegarder.setForeground(Color.white);
		sauvegarder.setText("Sauvegarder");
		sauvegarder.setVerticalTextPosition(SwingConstants.CENTER);
		sauvegarder.setHorizontalTextPosition(SwingConstants.CENTER);
		sauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}

				nomSauvegarde = JOptionPane.showInputDialog(null, "Veuillez saisir un nom pour votre sauvegarde",
						"Sauvegarde de la partie", JOptionPane.QUESTION_MESSAGE);
				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}

				if (nomSauvegarde != null) {
					try {
						oos = new ObjectOutputStream(new BufferedOutputStream(
								new FileOutputStream(new File("sauvegardes/" + nomSauvegarde + ".ser"))));
					} catch (FileNotFoundException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Un problème est survenu lors de la sauvegarde !\n Veuillez réessayer",
								"Erreur Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Un problème est survenu lors de la sauvegarde !\n Veuillez réessayer",
								"Erreur Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
					}

					try {
						oos.writeObject(carte);
					} catch (IOException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Un problème est survenu lors de la sauvegarde !\n Veuillez réessayer",
								"Erreur Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
					}

					try {
						oos.close();
					} catch (IOException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Un problème est survenu lors de la sauvegarde !\n Veuillez réessayer",
								"Erreur Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		sauvegarder.setToolTipText("Clicker ici pour sauvegarder la partie en cours");

		restaurer = new JButton(new ImageIcon("pictures/button.png"));
		restaurer.setFont(font);
		restaurer.setForeground(Color.white);
		restaurer.setText("Restaurer");
		restaurer.setVerticalTextPosition(SwingConstants.CENTER);
		restaurer.setHorizontalTextPosition(SwingConstants.CENTER);
		restaurer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}

				nomSauvegarde = JOptionPane.showInputDialog(null, "Veuillez saisir le nom de votre sauvegarde",
						"Restauration d'une partie", JOptionPane.QUESTION_MESSAGE);

				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e1) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}

				// On rÃ©cupÃ¨re maintenant les donnÃ©es !
				if (nomSauvegarde != null) {
					try {
						ois = new ObjectInputStream(new BufferedInputStream(
								new FileInputStream(new File("sauvegardes/" + nomSauvegarde + ".ser"))));
					} catch (FileNotFoundException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Sauvegarde \"" + nomSauvegarde
										+ "\" inexistante\n Veuillez réessayer avec un autre nom",
								"Erreur Restauration", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Un problème est survenu lors de la restauration !\n Veuillez réessayer",
								"Erreur Restauration", JOptionPane.INFORMATION_MESSAGE);
					}
					try {
						carte = (Carte) ois.readObject();
					} catch (ClassNotFoundException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Un problème est survenu lors de la restauration !\n Veuillez réessayer",
								"Erreur Restauration", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Un problème est survenu lors de la restauration !\n Veuillez réessayer",
								"Erreur Restauration", JOptionPane.INFORMATION_MESSAGE);
					} catch (NullPointerException e1) {
						if (test_son == 1) {
							try {
								File fichier = new File("sounds/error.wav");
								audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
								clip = AudioSystem.getClip();
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

						JOptionPane.showMessageDialog(null,
								"Un problème est survenu lors de la restauration !\n Veuillez réessayer",
								"Erreur Restauration", JOptionPane.INFORMATION_MESSAGE);
					}

					repaint();
					carte2=carte;
					panel5.repaint();
				}
			}
		});
		restaurer.setToolTipText("Clicker ici pour restaurer une partie sauvegardÃ©e");

		barreOutils.addSeparator();
		barreOutils.add(redemarrer);
		barreOutils.addSeparator();
		barreOutils.add(sauvegarder);
		barreOutils.addSeparator();
		barreOutils.add(restaurer);

		panel1.add(barreOutils);
		barreOutils.setBounds(10, 10, LARGEUR_CARTE * 40, 45);
		panel1.add(this);
		this.setBounds(10, 55, LARGEUR_CARTE * 40, HAUTEUR_CARTE * 40);
		panel1.add(tour);
		tour.setBounds(LARGEUR_CARTE * 40 + 75, 55, 170, 45);
		panel1.add(label1);
		label1.setBounds(LARGEUR_CARTE * 40 + 20, 495, 280, 155);
		panel1.add(label2);
		label2.setBounds(LARGEUR_CARTE * 40 + 20, 323, 280, 155);
		panel1.add(panel5);
		panel5.setBounds(LARGEUR_CARTE * 40 + 20, 115, 280, 191);
		son.setOpaque(false);
		son.setContentAreaFilled(false);
		son.setBorderPainted(false);
		son.setFocusPainted(false);
		panel1.add(son);
		son.setBounds(LARGEUR_CARTE * 40 + 265, 655, 35, 35);
		panel1.add(label3);
		label3.setBounds(0, 0, LARGEUR_CARTE * 40 + 310, HAUTEUR_CARTE * 40 + 65);

		addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent event) {
				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						Clip clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e2) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e2) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e2) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}
				if (tourHumain) {
					label2.setText(
							"<html><center>Les messages d'informations sur le jeu seront affichés ici<center/><html>");
					pos = new Position(event.getX() / 40, event.getY() / 40);
					if (carte.getElement(pos) instanceof Heros
							&& ((Heros) carte.getElement(pos)).getDejaJoue() == false) {
						deplacement = true;
						carte.getElement(pos).setVisible(false);
					}
				}
			}

			public void mouseReleased(MouseEvent event) {
				if (test_son == 1) {
					try {
						File fichier = new File("sounds/clickBoutton.wav");
						audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
						clip = AudioSystem.getClip();
						clip.open(audioIn);
						clip.start();
					} catch (UnsupportedAudioFileException e2) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (IOException e2) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					} catch (LineUnavailableException e2) {
						System.out.println("Un problème est survenu lors de chargement de clickBoutton.wav !");
					}
				}
				pos2 = new Position(event.getX() / 40, event.getY() / 40);
				if (tourHumain && deplacement == true) {
					deplacement = false;
					if (event.getX() >= 0 && event.getY() >= 0) {
						carte.actionHeros(pos, pos2, label2, PanneauJeu.this);
						if (carte.getNbMonstres() == 0) {
							if (test_son == 1) {
								try {
									File fichier = new File("sounds/victoir.wav");
									audioIn = AudioSystem.getAudioInputStream(fichier.toURI().toURL());
									clip = AudioSystem.getClip();
									clip.open(audioIn);
									clip.start();
								} catch (UnsupportedAudioFileException e1) {
									System.out.println("Un problème est survenu lors de chargement de victoir.wav !");
								} catch (IOException e1) {
									System.out.println("Un problème est survenu lors de chargement de victoir.wav !");
								} catch (LineUnavailableException e1) {
									System.out.println("Un problème est survenu lors de chargement de victoir.wav !");
								}
							}
							label2.setText("<html><center> Félicitations Vous avez gagné la partie <center/><html/>");
							JOptionPane.showMessageDialog(null, " Félicitations Vous avez gagné la partie ",
									":) Fin de Partie", JOptionPane.INFORMATION_MESSAGE);
							tour.setEnabled(false);
							PanneauJeu.this.setEnabled(false);
						}
					}
					repaint();
					carte2 = carte;
					panel5.repaint();
				}
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				if (tourHumain && carte.getElement(pos) instanceof Heros
						&& ((Heros) carte.getElement(pos)).getDejaJoue() == false) {
					xDep = event.getX() - 20;
					yDep = event.getY() - 20;
					repaint();
					carte2 = carte;
					panel5.repaint();
				}
			}

			public void mouseMoved(MouseEvent event) {
				Position pos = new Position(event.getX() / 40, event.getY() / 40);
				if (pos.estValide()) {
					if (carte.getElement(pos) instanceof Heros) {
						PanneauJeu.this.setToolTipText("<html>Position : (" + pos.getX() + "," + pos.getY() + ")<br>"
								+ ((Heros) carte.getElement(pos)).getTYPE() + " "
								+ ((Heros) carte.getElement(pos)).getNOM() + "<br>("
								+ ((Heros) carte.getElement(pos)).getPointsDeVie() + " PV / "
								+ ((Heros) carte.getElement(pos)).getPointsDeVieMAX() + ")<html/>");
						PanneauJeu.this.getToolTipLocation(event);
					} else if (carte.getElement(pos) instanceof Monstre) {
						PanneauJeu.this.setToolTipText("<html>Position : (" + pos.getX() + "," + pos.getY() + ")<br>"
								+ ((Monstre) carte.getElement(pos)).getTYPE() + " "
								+ ((Monstre) carte.getElement(pos)).getNOM() + "<br>("
								+ ((Monstre) carte.getElement(pos)).getPointsDeVie() + " PV / "
								+ ((Monstre) carte.getElement(pos)).getPointsDeVieMAX() + ")<html/>");
						PanneauJeu.this.getToolTipLocation(event);
					} else if (carte.getElement(pos) instanceof Obstacle) {
						PanneauJeu.this.setToolTipText("<html>Position : (" + pos.getX() + "," + pos.getY() + ")<br>"
								+ ((Obstacle) carte.getElement(pos)).getTYPE() + "<html/>");
						PanneauJeu.this.getToolTipLocation(event);
					} else {
						PanneauJeu.this.setToolTipText("Position : (" + pos.getX() + "," + pos.getY() + ")");
					}
				}
				repaint();
			}
		});
	}

	public JPanel getPanel1() {
		return panel1;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(font);
		try {
			img = ImageIO.read(new File("pictures/map.jpg"));
		} catch (IOException e) {
			System.out.println("Un problème est survenu lors du chargement de map.png !");
		}
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		label1.setText("<html><center>Il reste :<center/><br/>" + carte.getNbHeros() + " Héros et "
				+ carte.getNbMonstres() + " Monstres<html>");
		carte.toutDessiner(g);
		
		if (deplacement) {
			for (int i = pos.getX() - 1; i <= pos.getX() + 1; i++) {
				for (int j = pos.getY() - 1; j <= pos.getY() + 1; j++) {
					if (i != pos.getX() || j != pos.getY()) {
						if (new Position(i, j).estValide() && carte.getElement(new Position(i, j)) == null) {
							try {
								img = ImageIO.read(new File("pictures/position.png"));
							} catch (IOException e) {
								System.out.println("Un problème est survenu lors du chargement de position.png !");
							}
							g.drawImage(img, i * 40, j * 40, 40, 40, this);
						}
					}
				}
			}
			try {
				switch (((Heros) carte.getElement(pos)).getTYPE()) {
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
			g.drawImage(img, xDep, yDep, 40, 40, null);
		}
	}
}
