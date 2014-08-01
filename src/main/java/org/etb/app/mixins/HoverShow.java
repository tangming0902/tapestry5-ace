package org.etb.app.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseMixin;

/**
 * 当hover target时，被mixin附上的元素会显示出来
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class HoverShow extends BaseMixin {

	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
	private String target;

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("target", target);
		jsSupport.addInitializerCall("HoverShow", spec);
	}
}
