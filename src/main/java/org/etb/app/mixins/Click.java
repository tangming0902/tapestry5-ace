package org.etb.app.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseMixin;
import org.etb.app.constants.EtbEventConstants;

/**
 * Click mixin
 * 
 * @author Alex Huang
 * @email 102233492@qq.com
 */
@Events({ EtbEventConstants.CLICK })
public class Click extends BaseMixin {

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String zone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String params;

	@Parameter(value = "false")
	private boolean bubbleEvent;

	@Parameter
	private Object[] context;

	void afterRender(MarkupWriter writer) {
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

	Object onClick(Object[] context) {
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources.triggerEvent(EtbEventConstants.CLICK, context, callback);
		return callback.getResult();
	}
}
