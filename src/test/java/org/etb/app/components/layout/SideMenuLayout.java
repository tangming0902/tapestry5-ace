package org.etb.app.components.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.services.PageSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.etb.app.base.BaseInject;
import org.etb.app.menu.MenuModel;
import org.etb.app.menu.MenuService;
import org.etb.app.menu.impl.MenuServiceImpl;
import org.etb.app.pages.Test;

@Import(library = { "res/back.js" })
public class SideMenuLayout extends BaseInject {

	@Property
	@Parameter(value = "Tapestry5-ace 示例", defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Property
	@Parameter
	private RenderCommand head;

	@Parameter
	private Object page;

	private Class<?> pageClass;

	@Inject
	private PageRenderLinkSource linkSource;

	@Property
	private Collection<MenuModel> menus;

	@Property
	private MenuModel menu;

	@Property
	private MenuModel secondMenu;

	@Inject
	private PageSource pageSource;

	private MenuService menuService;

	void setupRender() {
		// 这里只是演示demo页面，提供参考用
		// MenuService理应作为一个服务存在，在module注册
		// 菜单绑定过程也应该在module里执行
		List<MenuModel> menus = new ArrayList<MenuModel>();
		menus.add(MenuModel
				.of("home", "主页", null)
				.setIconClass("icon-home")
				.addChild(
						MenuModel.of("manage_messagelistpage", "测试模块1",
								Test.class)));

		menuService = new MenuServiceImpl(menus);

	}

	void beginRender() {
		if (page != null) {
			if (String.class.isAssignableFrom(page.getClass())) {
				pageClass = pageSource.getPage(page.toString())
						.getRootComponent().getClass();
			} else {
				pageClass = page.getClass();
			}
		}

		JSONObject spec = new JSONObject();
		jsSupport.addInitializerCall("SideMenuLayout", spec);

		menus = menuService.getMenuModels();
	}

	public String menuUrl(Class<?> page) {
		if (page == null)
			return "#";

		return linkSource.createPageRenderLink(page).toAbsoluteURI();
	}

	public String getMenuClass() {
		if (pageClass == null)
			return "";

		if (isChild(menu, menuService.findMenuModel(pageClass)))
			return "active open";

		return "";
	}

	public String getSecondMenuClass() {
		if (pageClass == null)
			return "";

		if (secondMenu.getId().equals(
				menuService.findMenuModel(pageClass).getId()))
			return "active";

		return "";
	}

	private boolean isChild(MenuModel parent, MenuModel child) {
		if (child.getParent() == null)
			return false;

		if (parent.getId().equals(child.getParent().getId())) {
			return true;
		}
		return isChild(parent, child.getParent());
	}

	private final String assetPath = findAsset(
			"classpath:org/etb/app/res/bootstrap").toClientURL();

	public String getAssetPath() {
		return assetPath;
	}

}
