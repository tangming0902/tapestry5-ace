package org.etb.app.menu;

import java.util.Collection;

public interface MenuService {

	public Collection<MenuModel> getMenuModels();

	public MenuModel findMenuModelById(String id);

	public MenuModel findMenuModel(Class<?> page);

}
