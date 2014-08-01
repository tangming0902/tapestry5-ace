package org.etb.app.enums;

public enum WidgetHeadColor {
	DEFAULT(""), BLUE("header-color-blue"), BLUE2("header-color-blue2"), BLUE3(
			"header-color-blue3"), GREEN("header-color-green"), GREEN2(
			"header-color-green2"), GREEN3("header-color-green3"), RED(
			"header-color-red"), RED2("header-color-red2"), RED3(
			"header-color-red3"), PURPLE("header-color-purple"), PINK(
			"header-color-pink"), ORANGE("header-color-orange"), DARK(
			"header-color-dark"), GREY("header-color-grey");
	
	public final String value;

	private WidgetHeadColor(String value) {
		this.value = value;
	}
}
