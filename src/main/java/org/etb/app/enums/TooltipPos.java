package org.etb.app.enums;

public enum TooltipPos {
	LEFT("left"), RIGHT("right"), TOP("top"), BOTTOM("bottom");

	public final String value;

	private TooltipPos(String value) {
		this.value = value;
	}
}
