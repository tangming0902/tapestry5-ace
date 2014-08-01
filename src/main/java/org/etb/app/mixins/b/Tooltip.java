package org.etb.app.mixins.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.etb.app.enums.TooltipColor;
import org.etb.app.enums.TooltipPos;

public class Tooltip {

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private TooltipPos tooltipPos;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private TooltipColor tooltipColor;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String tooltipTitle;

	@InjectContainer
	private ClientElement clientElement;

	@Environmental
	private JavaScriptSupport jsSupport;

	public TooltipPos defaultTooltipPos() {
		return TooltipPos.TOP;
	}

	public TooltipColor defaultTooltipColor() {
		return TooltipColor.DEFAULT;
	}

	void afterRender(MarkupWriter writer) {
		JSONObject spec = new JSONObject();
		spec.put("clientId", clientElement.getClientId());
		spec.put("pos", tooltipPos.value);
		spec.put("title", tooltipTitle);
		spec.put("color", tooltipColor.value);
		jsSupport.addInitializerCall("Tooltip", spec);
	}
}
