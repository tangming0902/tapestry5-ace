package org.etb.app.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;
import org.apache.tapestry5.services.javascript.StylesheetOptions;
import org.etb.app.res.ResourcePlaceHolder;

public abstract class BaseStack implements JavaScriptStack {

	protected final AssetSource assetSource;
	protected final SymbolSource symbolSource;
	protected final List<Asset> javaScripts;
	protected final List<StylesheetLink> styleSheets;
	protected final String resourcesPath;

	public BaseStack(AssetSource assetSource, SymbolSource symbolSource) {
		String path = ResourcePlaceHolder.class.getPackage().getName();
		this.resourcesPath = "classpath:" + path.replace('.', '/');
		this.assetSource = assetSource;
		this.symbolSource = symbolSource;

		javaScripts = new ArrayList<Asset>();
		styleSheets = new ArrayList<StylesheetLink>();
	}

	public List<Asset> getJavaScriptLibraries() {
		return javaScripts;
	}

	public List<StylesheetLink> getStylesheets() {
		return styleSheets;
	}

	public String getInitialization() {
		return null;
	}

	public List<String> getStacks() {
		return new ArrayList<String>();
	}

	protected final Asset pathToAsset(String path) {
		return assetSource.getExpandedAsset(path);
	}

	protected final StylesheetLink assetToStylesheetlink(Asset asset) {
		return new StylesheetLink(asset);
	}

	protected final StylesheetLink pathToStylesheetlink(String path) {
		return assetToStylesheetlink(pathToAsset(path));
	}

	protected final StylesheetLink pathToStylesheetlink(String path,
			String condition) {
		return new StylesheetLink(pathToAsset(path),
				new StylesheetOptions().withCondition(condition));
	}

}
