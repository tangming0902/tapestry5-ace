package org.etb.app.models;

import java.io.Serializable;

public class Option implements Serializable {

	private static final long serialVersionUID = 1L;

	private String value;

	private String label;

	private boolean disabled;

	public static Option of(String value, String label) {
		Option option = new Option();
		option.value = value;
		option.label = label;
		return option;
	}

	public String getValue() {
		return value;
	}

	public Option setValue(String value) {
		this.value = value;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public Option setLabel(String label) {
		this.label = label;
		return this;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public Option setDisabled(boolean disabled) {
		this.disabled = disabled;
		return this;
	}

}
