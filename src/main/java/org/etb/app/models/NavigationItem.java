package org.etb.app.models;

public class NavigationItem {

	private String url;
	private String text;
	private String iconClass;

	public static NavigationItem of(String url, String text, String iconClass) {
		NavigationItem item = new NavigationItem();
		item.url = url;
		item.text = text;
		item.iconClass = iconClass;
		return item;
	}

	public String getUrl() {
		return url;
	}

	public NavigationItem setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getText() {
		return text;
	}

	public NavigationItem setText(String text) {
		this.text = text;
		return this;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

}
