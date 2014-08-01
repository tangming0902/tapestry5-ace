package org.etb.app.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.etb.app.base.BaseMixin;

/**
 * 省略号组件，为过长文本加入省略号
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class Ellipsis extends BaseMixin {

	@Parameter(required = true, allowNull = false, value = "13", defaultPrefix = BindingConstants.LITERAL)
	private int fontSize;

	@Parameter(required = true, allowNull = false, value = "0", defaultPrefix = BindingConstants.LITERAL)
	private int marginright;

	void afterRender(MarkupWriter writer) {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("fontSize", fontSize);
		spec.put("marginright", marginright);
		jsSupport.addInitializerCall(InitializationPriority.LATE, "Ellipsis", spec);
	}
}
