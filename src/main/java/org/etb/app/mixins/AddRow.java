package org.etb.app.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseMixin;

public class AddRow extends BaseMixin {
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String addRowValue;
	
	public static final String NAME = "_addRow_value";

	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String loopId;

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("loopId", loopId);
		
		if(resources.isBound("value")){
			spec.put("name", NAME);
			spec.put("value", addRowValue);
		}
		
		jsSupport.addInitializerCall("AddRow", spec);
	}
}
