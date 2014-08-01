package org.etb.app.enums;

public enum ButtonSize {
	MINIER("btn-minier"),MINI("btn-xs"),
	SMALL("btn-sm"),DEFAULT(""),
	LARGE("btn-lg");

	public final String value;

	private ButtonSize(String value) {
		this.value = value;
	}
}
