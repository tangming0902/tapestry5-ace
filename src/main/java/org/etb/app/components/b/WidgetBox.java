package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.etb.app.base.BaseClientElement;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.enums.WidgetHeadColor;

/**
 * 箱子container，带有刷新、伸缩、关闭按钮
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
@Events({ EtbEventConstants.RELOAD, EtbEventConstants.CLOSE })
public class WidgetBox extends BaseClientElement {

	@SuppressWarnings("unused")
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private WidgetHeadColor color;

	public WidgetHeadColor defaultColor() {
		return WidgetHeadColor.DEFAULT;
	}

	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private RenderCommand title;

	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private RenderCommand toolbar;

	@SuppressWarnings("unused")
	@Parameter(allowNull = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
	@Property
	private boolean collapse;

	/*--关闭事件-- 开始*/

	@Parameter(allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private boolean close;

	@Parameter
	private Object[] closeContext;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String closeZone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String closeParams;

	/*--关闭事件-- 结束*/

	/*--重载事件-- 开始*/

	@Parameter(allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private boolean reload;

	@Parameter
	private Object[] reloadContext;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String reloadZone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String reloadParams;

	/*--重载事件-- 结束*/

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());

		if (reload && resources.isBound("reloadZone")) {
			spec.put("reloadUrl",
					resources.createEventLink("reload", reloadContext)
							.toAbsoluteURI());
			spec.put("reloadZone", reloadZone);
			spec.put("reloadParams", reloadParams);
		}

		if (close && resources.isBound("closeZone")) {
			spec.put("closeUrl",
					resources.createEventLink("close", closeContext)
							.toAbsoluteURI());
			spec.put("closeZone", closeZone);
			spec.put("closeParams", closeParams);
		}

		jsSupport.addInitializerCall("WidgetBox", spec);
	}

	Object onReload(Object[] context) {
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources.triggerEvent(EtbEventConstants.RELOAD, context, callback);
		return callback.getResult();
	}

	Object onClose(Object[] context) {
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources.triggerEvent(EtbEventConstants.CLOSE, context, callback);
		return callback.getResult();
	}

}
