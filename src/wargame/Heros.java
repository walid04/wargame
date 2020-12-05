package wargame;

public class Heros extends Soldat {
	private static final long serialVersionUID = 1L;
	private final String NOM;
	private final TypesH TYPE;
	private boolean dejaJoue = false;

	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;
	}

	public String getNOM() {
		return NOM;
	}

	public TypesH getTYPE() {
		return TYPE;
	}

	public boolean getDejaJoue() {
		return dejaJoue;
	}

	public void setDejaJoue(boolean bool) {
		dejaJoue = bool;
	}

	public int getTour() {
		return 1;
	}

	public void joueTour(int tour) {

	}
}
