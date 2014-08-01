package org.etb.app.components.b;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseClientElement;

@SupportsInformalParameters
public class Errors extends BaseClientElement {

	void beginRender(MarkupWriter writer) {
		writer.element("div", "id", getClientId(), "class",
				"alert alert-warning", "style", "display:none;");
		resources.renderInformalParameters(writer);
		writer.element("a", "class", "close");
		writer.element("i", "class", "icon-remove");
		writer.end();
		writer.end();
		writer.element("strong");
		writer.element("i", "class", "icon-remove");
		writer.end();
		writer.end();
		writer.writeRaw(" 请修正以下错误后继续.<br/><div class='error-content'><ul class='list-unstyled'></ul></div>");
		writer.end();

		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		jsSupport.addInitializerCall("BErrors", spec);
	}
}
