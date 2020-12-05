package wargame;

import java.io.Serializable;

public abstract class Element implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Position pos;
	protected boolean visible = true;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}