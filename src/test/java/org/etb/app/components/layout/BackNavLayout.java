package org.etb.app.components.layout;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.etb.app.base.BaseInject;

@Import(library = { "res/back.js" })
public class BackNavLayout extends BaseInject {

	@Property
	@Parameter(value = "知微管理平台", defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Property
	@Parameter
	private RenderCommand head;

	void beginRender() {
		JSONObject spec = new JSONObject();
		jsSupport.addInitializerCall("BackNavLayout", spec);
	}

	private final String assetPath = findAsset(
			"classpath:org/etb/app/res/bootstrap").toClientURL();

	public String getAssetPath() {
		return assetPath;
	}

}
