package org.etb.app.enums;

public enum ButtonColor {
	DEFAULT("btn-default"), BLUE("btn-primary"), GREEN("btn-success"), ORANGE(
			"btn-warning"), LIGHTBLUE("btn-info"), RED("btn-danger"), PURPLE(
			"btn-purple"), PINK("btn-pink"), BLACK("btn-inverse"), GREY(
			"btn-grey"), LIGHT("btn-light"), YELLOW("btn-yellow"), WHITE(
			"btn-white"), LINK("btn-link");

	public final String value;

	private ButtonColor(String value) {
		this.value = value;
	}
}
