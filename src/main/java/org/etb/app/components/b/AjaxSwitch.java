package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.annotations.CatchRequestParameter;
import org.etb.app.base.BaseClientElement;
import org.etb.app.constants.EtbEventConstants;

@Events(value = { EtbEventConstants.CLICK })
@SupportsInformalParameters
public class AjaxSwitch extends BaseClientElement {

	@Parameter
	private boolean value;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String zone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String params;

	void beginRender(MarkupWriter writer) {
		writer.element("label");
		resources.renderInformalParameters(writer);
		Element input = writer.element("input", "id", getClientId(), "class",
				"ace ace-switch", "type", "checkbox", "autocomplete", "off");
		if (value) {
			input.attribute("checked", "checked");
		}

		writer.end();
		writer.element("span", "class", "lbl");
		writer.end();
		writer.end();
	}

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		if (resources.isBound("zone")) {
			spec.put("url", resources.createEventLink("click").toAbsoluteURI());
			spec.put("zoneId", zone);
			spec.put("paramsId", params);
		}
		jsSupport.addInitializerCall("BSwitch", spec);
	}

	@CatchRequestParameter("_switchValue")
	private Boolean switchValue;

	Object onClick() {
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources.triggerEvent(EtbEventConstants.CLICK,
				new Object[] { switchValue }, callback);
		return callback.getResult();
	}
}
