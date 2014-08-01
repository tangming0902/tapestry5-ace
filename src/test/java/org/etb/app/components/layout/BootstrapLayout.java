package org.etb.app.components.layout;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.etb.app.base.BaseInject;
import org.etb.app.constants.CoreSymbolConstants;
import org.etb.app.utils.BrowserUtil;

@Import(stack = { CoreSymbolConstants.ACE_CORE_STACK })
@SuppressWarnings("unused")
public class BootstrapLayout extends BaseInject {

	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Property
	@Parameter
	private RenderCommand head;

	@Property
	private final RenderCommand script3 = new RenderCommand() {

		@Override
		public void render(MarkupWriter writer, RenderQueue queue) {
			writer.writeRaw("if(\"ontouchend\" in document) document.write(\"<script src='"
					+ getAssetPath()
					+ "/js/jquery.mobile.custom.min.js'>\"+\"<\"+\"/script>\");");
		}
	};

	private final String assetPath = findAsset(
			"classpath:org/etb/app/res/bootstrap").toClientURL();

	public String getAssetPath() {
		return assetPath;
	}

	@Cached
	public int getIEVersion() {
		String agent = BrowserUtil.getUserAgent(request);
		return BrowserUtil.getIEVersion(agent);
	}

	public boolean isLtIE9() {
		return getIEVersion() != -1 && getIEVersion() < 9;
	}

	public boolean isIE() {
		return getIEVersion() != -1;
	}

}
