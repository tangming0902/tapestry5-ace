package org.etb.app.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseClientElement;

/**
 * TimeAgo
 * 
 * @author Alex Huang
 * @email 102233492@qq.com
 */
@SupportsInformalParameters
public class TimeAgo extends BaseClientElement {

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String elementName;

	@Parameter(required = true, allowNull = false)
	private Date date;

	String defaultElementName() {
		return resources.getElementName("span");
	}

	void beginRender(MarkupWriter writer) {
		writer.element(elementName, "id", getClientId(), "title",
				getDateString());
		resources.renderInformalParameters(writer);
	}

	void afterRender(MarkupWriter writer) {
		writer.end();
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		jsSupport.addInitializerCall("TimeAgo", spec);
	}

	private String getDateString() {
		return SDF.format(date);
	}

}
