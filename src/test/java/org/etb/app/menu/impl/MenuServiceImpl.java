package org.etb.app.menu.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etb.app.menu.MenuModel;
import org.etb.app.menu.MenuService;

public class MenuServiceImpl implements MenuService {

	private List<MenuModel> menus;

	private final Map<String, MenuModel> pageToDefs = new HashMap<String, MenuModel>();

	private final Map<String, MenuModel> idToDefs = new HashMap<String, MenuModel>();

	private final static Comparator<MenuModel> DEFAULT_COMPARATOR = new Comparator<MenuModel>() {
		public int compare(final MenuModel o1, final MenuModel o2) {
			return (int) (o1.getIndex() - o2.getIndex());
		}
	};

	public MenuServiceImpl(final List<MenuModel> menus) {
		this.menus = menus;

		Collections.sort(this.menus, DEFAULT_COMPARATOR);
		for (final MenuModel menu : menus) {
			init(menu);
		}
	}

	private void init(final MenuModel parent) {
		for (final MenuModel child : parent.getChildren()) {
			Class<?> page = child.getPage();
			if (page != null) {
				pageToDefs.put(page.getCanonicalName(), child);
				idToDefs.put(child.getId(), child);
			}

			// 同一个菜单项注册多个page类
			Class<?>[] pages = child.getPages();
			for (int i = 0; i < pages.length; i++) {
				Class<?> p = pages[i];
				pageToDefs.put(p.getCanonicalName(), child);
			}

			init(child);
		}
	}

	@Override
	public Collection<MenuModel> getMenuModels() {
		return Collections
				.unmodifiableCollection((List<MenuModel>) cloneObject(menus));
	}

	@Override
	public MenuModel findMenuModelById(String id) {
		return idToDefs.get(id);
	}

	@Override
	public MenuModel findMenuModel(Class<?> page) {
		return pageToDefs.get(page.getCanonicalName());
	}

	/**
	 * 复制对象obj，类似于值传递，非引用
	 */
	public List<MenuModel> cloneObject(Object obj) {
		ByteArrayOutputStream byteOut = null;
		ObjectOutputStream out = null;
		ByteArrayInputStream byteIn = null;
		ObjectInputStream in = null;
		try {
			byteOut = new ByteArrayOutputStream();
			out = new ObjectOutputStream(byteOut);
			out.writeObject(obj);
			byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			in = new ObjectInputStream(byteIn);
			return (List<MenuModel>) in.readObject();
		} catch (Exception e) {
			return new ArrayList<MenuModel>();
		} finally {
			try {
				if (byteOut != null) {
					byteOut.close();
				}
				if (out != null) {
					out.close();
				}
				if (byteIn != null) {
					byteIn.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				return new ArrayList<MenuModel>();
			}
		}
	}

}
