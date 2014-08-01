package org.etb.app.enums;

public enum TooltipColor {
	DEFAULT(""), BLUE("tooltip-info"), GREEN("tooltip-success"), ORANGE(
			"tooltip-warning"), RED("tooltip-error");

	public final String value;

	private TooltipColor(String value) {
		this.value = value;
	}
}
