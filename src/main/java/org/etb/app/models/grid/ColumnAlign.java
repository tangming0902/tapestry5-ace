package org.etb.app.models.grid;

public enum ColumnAlign {

	LEFT, RIGHT, CENTER;

	@Override
	public String toString() {
		switch (this) {
		case LEFT:
			return "left";
		case RIGHT:
			return "right";
		}
		return "center";
	}
}
