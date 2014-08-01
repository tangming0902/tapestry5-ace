package org.etb.app.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseMixin;
import org.etb.app.components.AjaxLoop;

public class RemoveRow extends BaseMixin {

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String loopId;

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("loopId", loopId);
		spec.put("rowClass", AjaxLoop.ROW_CLASS);
		spec.put("tailClass", AjaxLoop.TAIL_CLASS);
		jsSupport.addInitializerCall("RemoveRow", spec);
	}
}
