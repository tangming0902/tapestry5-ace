package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseClientElement;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.enums.ButtonColor;
import org.etb.app.enums.ButtonSize;

@Events({ EtbEventConstants.CLICK })
@SupportsInformalParameters
public class Button extends BaseClientElement {

	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String elementName;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String zone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String params;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private ButtonSize size;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private ButtonColor color;

	/**
	 * 点击是否冒泡
	 */
	@Parameter(value = "false")
	private boolean bubbleEvent;

	@Parameter(value = "false")
	private boolean disabled;

	@Parameter
	private Object[] context;

	String defaultElementName() {
		return resources.getElementName("a");
	}

	ButtonSize defaultSize() {
		return ButtonSize.SMALL;
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
			spec.put("bubbleEvent", bubbleEvent);
			if (resources.isBound("zone")) {
				spec.put("url", resources.createEventLink("click", context)
						.toAbsoluteURI());
				spec.put("zoneId", zone);
				spec.put("paramsId", params);
			}

			jsSupport.addInitializerCall("SimpleLink", spec);
		}
	}

	Object onClick(Object[] context) {
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources.triggerEvent(EtbEventConstants.CLICK, context, callback);
		return callback.getResult();
	}
}
