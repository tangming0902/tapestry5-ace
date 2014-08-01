package org.etb.app.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class FormSubmit {

	@InjectContainer
	private ClientElement clientElement;

	@Environmental
	private JavaScriptSupport jsSupport;

	void afterRender(MarkupWriter writer) {
		JSONObject spec = new JSONObject();
		spec.put("clientId", clientElement.getClientId());
		jsSupport.addInitializerCall("FormSubmit", spec);
	}
}
