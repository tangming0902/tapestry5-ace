package org.etb.app.enums;

public enum BgColor {
	DEFAULT("", false), BLUE("gritter-info", false), 
	GREEN("gritter-success", false), ORANGE("gritter-warning", false), 
	RED("gritter-error", false), DEFAULT_DARK("", true), 
	BLUE_DARK("gritter-info", true), GREEN_DARK("gritter-success", true), 
	ORANGE_DARK("gritter-warning", true), RED_DARK("gritter-error", true);

	public final String value;

	public final boolean dark;

	private BgColor(String value, boolean dark) {
		this.value = value;
		this.dark = dark;
	}
}
