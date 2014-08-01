package org.etb.app.components.b;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.etb.app.base.BaseClientElement;

@Import(library = "res/jplayer/jquery.jplayer.min.js")
public class AudioPlayer extends BaseClientElement {

	@SuppressWarnings("unused")
	@Parameter(value = "200px", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String width;

	@Parameter(required = true)
	private String url;

	@Inject
	@Path("res/jplayer/Jplayer.swf")
	private Asset swf;

	void beginRender() {

	}

	void afterRender() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("swfPath", swf.toClientURL());
		spec.put("url", url);
		jsSupport.addInitializerCall("AudioPlayer", spec);
	}
}
