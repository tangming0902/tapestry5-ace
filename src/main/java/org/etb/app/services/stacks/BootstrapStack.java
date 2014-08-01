package org.etb.app.services.stacks;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Request;
import org.etb.app.base.BaseStack;
import org.etb.app.utils.BrowserUtil;

public class BootstrapStack extends BaseStack {

	public BootstrapStack(AssetSource assetSource, SymbolSource symbolSource) {
		super(assetSource, symbolSource);
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.mobile.custom.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/ace-extra.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/bootstrap.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/typeahead-bs2.min.js"));

		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.dataTables.js"));

		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/jquery.dataTables.bootstrap.js"));

		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/fuelux/fuelux.wizard.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.validate.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/additional-methods.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/bootbox.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.maskedinput.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/select2.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.gritter.js"));

		// 1.10.3在dialog的position定位上有BUG,降级为1.9.2
		// 1.9.2
		// 去掉了tooltip的定义，避免覆盖bootstrap.js的内容,但是仍然有dialog内部radiogroup和checkbox点击不生效的问题
		// javaScripts.add(pathToAsset(resourcesPath
		// + "/bootstrap/js/uncompressed/jquery-ui-1.9.2.custom.js"));
		// javaScripts.add(pathToAsset(resourcesPath
		// + "/bootstrap/js/jquery-ui-1.10.3.full.min.js"));

		// 使用1.10.4，去掉了tooltip的定义，修正了dialog的定位问题，修正了dialog resize的问题
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery-ui-1.10.4.full.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.ui.touch-punch.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.slimscroll.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.easy-pie-chart.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.sparkline.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/flot/jquery.flot.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/flot/jquery.flot.pie.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/flot/jquery.flot.resize.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/bootstrap-tag.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/jquery.hotkeys.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/bootstrap-wysiwyg.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/ace-elements.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/bootstrap/js/uncompressed/ace.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/core/js/jquery.showLoading.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/core/js/jquery.timeago.js"));
		javaScripts.add(pathToAsset(resourcesPath
				+ "/core/js/jquery.ellipsis.js"));
		javaScripts.add(pathToAsset(resourcesPath + "/core/js/ajax.js"));
		javaScripts.add(pathToAsset(resourcesPath + "/etb/js/common.js"));
		javaScripts.add(pathToAsset(resourcesPath + "/etb/js/b.js"));

		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/bootstrap.min.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/select2.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/jquery.gritter.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/font-awesome.min.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/jquery-ui-1.10.3.full.min.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/ace-fonts.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/ace.min.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/ace-rtl.min.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/ace-skins.min.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/font-awesome-ie7.min.css", "IE 7"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/ace-ie.min.css", "lte IE 8"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/datepicker.css", "IE 7"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/bootstrap/css/ui.jqgrid.css", "IE 7"));

		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/core/css/responsive-nav.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/core/css/showLoading.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath + "/etb/css/b.css"));
		styleSheets.add(pathToStylesheetlink(resourcesPath
				+ "/etb/css/common.css"));
	}

	@Override
	public List<Asset> getJavaScriptLibraries() {
		List<Asset> newJavaScripts = new ArrayList<Asset>();
		int version = getIEVersion();
		if (version == -1) {
			newJavaScripts.add(pathToAsset(resourcesPath
					+ "/bootstrap/js/jquery-2.0.3.min.js"));
		} else {
			newJavaScripts.add(pathToAsset(resourcesPath
					+ "/bootstrap/js/jquery-1.10.2.min.js"));
		}

		newJavaScripts.addAll(javaScripts);
		return newJavaScripts;
	}

	@Inject
	private Request request;

	private int getIEVersion() {
		String agent = BrowserUtil.getUserAgent(request);
		return BrowserUtil.getIEVersion(agent);
	}

}
