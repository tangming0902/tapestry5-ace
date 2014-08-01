package org.etb.app.components.b;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.etb.app.base.AbstractField;

public class Checkbox extends AbstractField {

	/**
	 * 是否被勾选中
	 */
	@Parameter(required = true, autoconnect = true)
	private boolean value;

	/**
	 * 如果有文本值，可绑定
	 */
	@Parameter
	private String text;

	@Environmental
	private ValidationTracker tracker;

	void beginRender(MarkupWriter writer) {
		if (resources.isBound("text")) {
			writer.element("div", "style", "display:none;");
			writer.element("input", "id", getClientId() + "_text", "name",
					getControlName() + "_text", "type", "text", "value", text);
			writer.end();
			writer.end();
		}
		
		writer.element("input", "id", getClientId(), "name", getControlName(),
				"class", "ace", "type", "checkbox");

		if (value) {
			writer.attributes("checked", "checked");
		}
		
		if (resources.isBound("text")){
			writer.attributes("value", text);
		}

		resources.renderInformalParameters(writer);
		writer.end();
	}

	@Override
	protected void processSubmission(String controlName) {
		String postedValue = request.getParameter(controlName);

		tracker.recordInput(this, Boolean.toString(postedValue != null));

		value = postedValue != null;

		if (resources.isBound("text")) {
			text = request.getParameter(controlName + "_text");
		}
	}

}
