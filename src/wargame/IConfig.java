package wargame;

import java.awt.Color;

public interface IConfig {
	int LARGEUR_CARTE = 22;
	int HAUTEUR_CARTE = 16; // en nombre de cases
	int NB_PIX_CASE = 20;
	int POSITION_X = 80;
	int POSITION_Y = 3; // Position de la fenètre
	int NB_HEROS = 15;
	int NB_MONSTRES = 25;
	int NB_OBSTACLES = 45;
	int NB_ROCHER = 10;
	int NB_FORET = 10;
	int NB_EAU = 10;
	int NB_AUTRE_OBSTACLE = 15;
	Color COULEUR_EAU = Color.blue; 
	Color COULEUR_FORET = Color.green;
	Color COULEUR_ROCHER = Color.gray;
	Color COULEUR_VIDE = Color.white;
	Color COULEUR_TEXTE_HEROS = Color.black;
	Color COULEUR_TEXTE_MONSTRES = Color.white;
	Color COULEUR_HEROS = Color.red;
	Color COULEUR_HEROS_DEJA_JOUE = Color.pink;
	Color COULEUR_MONSTRES = Color.black;
	Color COULEUR_MONSTRES_DEJA_JOUE = Color.magenta;
	Color COULEUR_INCONNU = Color.lightGray;
}