package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseClientElement;
import org.etb.app.enums.ButtonColor;
import org.etb.app.enums.ButtonSize;

@SupportsInformalParameters
public class SubmitButton extends BaseClientElement {

	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String elementName;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String formId;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private ButtonSize size;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private ButtonColor color;

	@Parameter(value = "false")
	private boolean disabled;

	String defaultElementName() {
		return resources.getElementName("a");
	}

	ButtonColor defaultColor() {
		return ButtonColor.DEFAULT;
	}

	void beginRender(MarkupWriter writer) {
		Element element = writer.element(elementName, "id", getClientId());
		resources.renderInformalParameters(writer);
		String buttonClass = element.getAttribute("class");

		if (buttonClass == null) {
			buttonClass = "btn";
		} else {
			buttonClass += " btn";
		}

		if (size != null) {
			buttonClass += " " + size.value;
		}

		if (color != null) {
			buttonClass += " " + color.value;
		}

		if (disabled) {
			buttonClass += " disabled";
		}

		writer.attributes("class", buttonClass);
	}

	void afterRender(MarkupWriter writer) {
		writer.end();

		if (!disabled) {
			JSONObject spec = new JSONObject();
			spec.put("clientId", getClientId());
			spec.put("formId", formId);
			jsSupport.addInitializerCall("FormSubmit", spec);
		}
	}

}
