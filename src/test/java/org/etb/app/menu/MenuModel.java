package org.etb.app.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 菜单项模型
 */
public class MenuModel implements Serializable {

	private static final long serialVersionUID = 434314538267751020L;

	/**
	 * 菜单项id
	 */
	private String id;

	private String iconClass;

	/**
	 * 菜单项名称
	 */
	private String text;

	/**
	 * 菜单项点击时跳转的页面
	 */
	private Class<?> page;

	/**
	 * 菜单项的子菜单
	 */
	private List<MenuModel> children;

	/**
	 * 菜单项的父菜单
	 */
	private MenuModel parent;

	/**
	 * 菜单项的排序次序
	 */
	private float index;

	/*
	 * 一个菜单下有多个页面
	 */
	private Class<?>[] pages;

	/**
	 * 得到菜单项名称
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	public String getId() {
		return id;
	}

	private MenuModel() {
	}

	public static MenuModel of(String id, String text, Class<?> page,
			Class<?>... pages) {
		MenuModel t = new MenuModel();
		t.id = id;
		t.text = text;
		t.page = page;
		t.pages = pages;
		return t;
	}

	/**
	 * 设置菜单项名称
	 */
	public MenuModel setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * 得到菜单项点击时跳转的页面
	 */
	public Class<?> getPage() {
		return page;
	}

	public List<MenuModel> getChildren() {
		if (null == children) {
			return Collections.emptyList();
		}
		return children;
	}

	public MenuModel setChildren(List<MenuModel> children) {
		this.children = children;
		return this;
	}

	public String getIconClass() {
		return iconClass;
	}

	public MenuModel setIconClass(String iconClass) {
		this.iconClass = iconClass;
		return this;
	}

	public float getIndex() {
		return index;
	}

	public void setIndex(float index) {
		this.index = index;
	}

	public MenuModel getParent() {
		return parent;
	}

	/**
	 * 是否根菜单项
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return this.parent == null;
	}

	/**
	 * 是否有子菜单项
	 * 
	 * @return
	 */
	public boolean hasChild() {
		return children != null && !children.isEmpty();
	}

	public Class<?>[] getPages() {
		return pages;
	}

	public void setPages(Class<?>[] pages) {
		this.pages = pages;
	}

	public MenuModel addChild(MenuModel child) {
		if (null == children) {
			children = new ArrayList<MenuModel>();
		}
		child.parent = this;
		children.add(child);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((page == null) ? 0 : page.getName().hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MenuModel other = (MenuModel) obj;
		if (other != null && other.page != null)
			if (page == null) {
				return false;
			} else if (page != null
					&& !page.getName().equals(other.page.getName())) {
				return false;
			}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		if (text == null) {
			if (other.text != null) {
				return false;
			}
		} else if (!text.equals(other.text)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("[%s|%s]", text,
				page == null ? "" : page.getCanonicalName());
	}
}
