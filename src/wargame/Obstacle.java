package wargame;

import java.awt.Color;

public class Obstacle extends Element {
	private static final long serialVersionUID = 1L;

	public static enum TypeObstacle {
		ROCHER1(IConfig.COULEUR_ROCHER), ROCHER2(IConfig.COULEUR_ROCHER), ROCHER3(IConfig.COULEUR_ROCHER), ROCHER4(
				IConfig.COULEUR_ROCHER), ROCHER5(IConfig.COULEUR_ROCHER), ROCHER6(IConfig.COULEUR_ROCHER), ROCHER7(
						IConfig.COULEUR_ROCHER), ROCHER8(IConfig.COULEUR_ROCHER), ROCHER9(
								IConfig.COULEUR_ROCHER), FORET1(IConfig.COULEUR_FORET), FORET2(
										IConfig.COULEUR_FORET), FORET3(IConfig.COULEUR_FORET), FORET4(
												IConfig.COULEUR_FORET), FORET5(IConfig.COULEUR_FORET), AUTRE_OBSTACLE1(
														IConfig.COULEUR_FORET), AUTRE_OBSTACLE2(
																IConfig.COULEUR_FORET), AUTRE_OBSTACLE3(
																		IConfig.COULEUR_FORET), AUTRE_OBSTACLE4(
																				IConfig.COULEUR_FORET), AUTRE_OBSTACLE5(
																						IConfig.COULEUR_FORET), AUTRE_OBSTACLE6(
																								IConfig.COULEUR_FORET), AUTRE_OBSTACLE7(
																										IConfig.COULEUR_FORET), AUTRE_OBSTACLE8(
																												IConfig.COULEUR_FORET), AUTRE_OBSTACLE9(
																														IConfig.COULEUR_FORET), AUTRE_OBSTACLE10(
																																IConfig.COULEUR_FORET), AUTRE_OBSTACLE11(
																																		IConfig.COULEUR_FORET), EAU(
																																				IConfig.COULEUR_EAU);
		private final Color COULEUR;

		TypeObstacle(Color couleur) {
			COULEUR = couleur;
		}

		public Color getCouleur() {
			return COULEUR;
		}

		public static TypeObstacle getObstacleAlea(int inf, int sup) {
			return values()[(int) (Math.random() * (sup - inf) + inf)];
		}
	}

	private final TypeObstacle TYPE;

	Obstacle(TypeObstacle type, Position pos) {
		TYPE = type;
		this.pos = pos;
	}

	public TypeObstacle getTYPE() {
		return TYPE;
	}

	public String toString() {
		return "" + TYPE;
	}
}