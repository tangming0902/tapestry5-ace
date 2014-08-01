package org.etb.app.components.layout;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.etb.app.base.BaseInject;
import org.etb.app.constants.CoreSymbolConstants;
import org.etb.app.models.NavigationItem;

@Import(library = { "res/back.js" })
public class NavigationLayout extends BaseInject {

	@Property
	@Parameter(value = "Tapestry5-ace 示例", defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Property
	@Parameter
	private RenderCommand head;

	@Property
	@Parameter
	private List<NavigationItem> items;

	@Component(publishParameters = "page")
	private SideMenuLayout sideMenuLayout;

	@Property
	private NavigationItem item;

	void beginRender() {
		JSONObject spec = new JSONObject();
		jsSupport.addInitializerCall("NavigationLayout", spec);
	}

	private final String assetPath = findAsset(
			"classpath:org/etb/app/res/bootstrap").toClientURL();

	public String getAssetPath() {
		return assetPath;
	}

}
