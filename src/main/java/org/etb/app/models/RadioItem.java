package org.etb.app.models;

public class RadioItem {

	private String value;

	private String labelText;

	private RadioItem() {
	}

	public static RadioItem of(String value, String labelText) {
		RadioItem item = new RadioItem();
		item.value = value;
		item.labelText = labelText;
		return item;
	}

	public String getValue() {
		return value;
	}

	public String getLabelText() {
		return labelText;
	}

}
