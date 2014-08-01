package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.etb.app.base.BaseClientElement;
import org.etb.app.utils.ComponentUtil;

@SupportsInformalParameters
public class Dialog extends BaseClientElement {

	@SuppressWarnings("unused")
	@Parameter
	@Property
	private RenderCommand title;

	@SuppressWarnings("unused")
	@Parameter
	@Property
	private RenderCommand buttons;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String width;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String height;

	/**
	 * 移动端的宽度
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String phoneWidth;

	/**
	 * 移动端的高度
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String phoneHeight;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
	private boolean resize;

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("width", width);
		spec.put("height", height);
		spec.put("phoneWidth", phoneWidth);
		spec.put("phoneHeight", phoneHeight);
		spec.put("resize", resize);
		jsSupport.addInitializerCall(InitializationPriority.EARLY, "jQDialog2", spec);
	}

	public void ajaxClose() {
		ComponentUtil.checkIdInvariable(resources, idParameter);
		ajaxClose(idParameter);
	}

	public void ajaxClose(final String clientId) {
		if (request.isXHR()) {
			ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

				@Override
				public void run(JavaScriptSupport javascriptSupport) {
					JSONObject spec = new JSONObject();
					spec.put("clientId", clientId + "_content");
					javascriptSupport.addInitializerCall("jQDialogClose", spec);
				}
			});
		}
	}
}
