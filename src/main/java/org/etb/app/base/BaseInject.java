package org.etb.app.base;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.internal.plastic.PlasticInternalUtils;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.URLEncoder;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * 注入基础服务的基类
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class BaseInject {

	/**
	 * 组件资源对象<br>
	 * 用于取得或调用与t:id或当前tml相关的信息(包括EventUrl创建等等)<br>
	 */
	@Inject
	protected ComponentResources resources;

	/**
	 * 处理ajax响应的服务类，比如ajax刷新zone，执行js，返回json<br>
	 * Street的ajax请求响应都是基于json数据交互的
	 */
	@Inject
	protected AjaxResponseRenderer ajaxResponseRenderer;

	/**
	 * 创建page访问链接类
	 */
	@Inject
	protected PageRenderLinkSource linkSource;

	/**
	 * 环境上下文服务类，用于支持@Environmental的写法<br>
	 * 使用场景比较少，可参考RadioGroup的实现。
	 */
	@Inject
	protected Environment environment;

	/**
	 * T5包装过的request对象<br>
	 */
	@Inject
	protected Request request;

	/**
	 * T5包装过的response对象<br>
	 */
	@Inject
	protected Response response;

	/**
	 * T5包装过的cookie对象<br>
	 */
	@Inject
	protected Cookies cookies;

	/**
	 * 全局请求对线（包装当前request对象）<br>
	 * 可以通过此对象取得HttpServletRequest、HttpServletResponse<br>
	 */
	@Inject
	protected RequestGlobals requestGlobals;

	/**
	 * 资源服务<br>
	 * 取得classpath、context下的资源用<br>
	 * getUnlocalizedAsset可以用于取得自定义路径的资源（比如udl配置路径、数据库大文本字段等）<br>
	 */
	@Inject
	protected AssetSource assetSource;

	/**
	 * T5封装的URLEncoder
	 */
	@Inject
	protected URLEncoder urlEncoder;
	
	@Inject
	protected SymbolSource symbolSource;

	/**
	 * JavaScript服务类，执行js、加载js/css文件、生成唯一clientId。<br>
	 * 与AjaxResponseRenderer不同在于:<br>
	 * 1.JavaScriptSupport仅在渲染内容时才可被调用<br>
	 * 2.AjaxResponseRenderer实际上以callback的形式调用了JavaScriptSupport去做js操作<br>
	 */
	@Environmental(value = false)
	protected JavaScriptSupport jsSupport;

	/**
	 * 取得Asset资源文件通用方法
	 * 
	 * @param path
	 * @return
	 */
	protected Asset findAsset(String path) {
		Asset asset = null;
		if (path.startsWith("classpath:")) {
			asset = assetSource.getClasspathAsset(path);
		} else if (path.startsWith("context:")) {
			asset = assetSource.getContextAsset(path, null);
		} else if (path.indexOf(":") != -1) {
			asset = assetSource.getUnlocalizedAsset(path);
		} else {
			Class<?> scope = this.getClass();
			Resource baseResource = new ClasspathResource(
					scope.getClassLoader(),
					PlasticInternalUtils.toClassPath(scope.getName()));
			asset = assetSource.getAsset(baseResource, path, null);
		}
		return asset;
	}

	/**
	 * 导入js
	 * 
	 * @param path
	 */
	protected void importLibrary(String path) {
		final Asset asset = findAsset(path);
		if (request.isXHR()) {
			ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

				@Override
				public void run(JavaScriptSupport javascriptSupport) {
					jsSupport.importJavaScriptLibrary(asset);
				}
			});
		} else {
			jsSupport.importJavaScriptLibrary(asset);
		}
	}

	/**
	 * 导入css
	 * 
	 * @param path
	 */
	protected void importStylesheet(String path) {
		final Asset asset = findAsset(path);
		if (request.isXHR()) {
			ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

				@Override
				public void run(JavaScriptSupport javascriptSupport) {
					jsSupport.importStylesheet(asset);
				}
			});
		} else {
			jsSupport.importStylesheet(asset);
		}
	}
}
