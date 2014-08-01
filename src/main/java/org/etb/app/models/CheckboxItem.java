package org.etb.app.models;

/**
 * 比较特殊的构造，用于Checkboxes组件渲染<br>
 * 同时也是提交值对象(当表单提交时，仅value会有值)<br>
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class CheckboxItem {

	private String value;

	private String labelText;

	private boolean selected;

	private CheckboxItem() {
	}

	public static CheckboxItem of(String value, String labelText) {
		CheckboxItem item = new CheckboxItem();
		item.value = value;
		item.labelText = labelText;
		return item;
	}

	public static CheckboxItem of(String value, boolean selected) {
		CheckboxItem item = new CheckboxItem();
		item.value = value;
		item.selected = selected;
		return item;
	}

	public String getValue() {
		return value;
	}

	public String getLabelText() {
		return labelText;
	}

	public boolean isSelected() {
		return selected;
	}

	public CheckboxItem setSelected(boolean selected) {
		this.selected = selected;
		return this;
	}

}
