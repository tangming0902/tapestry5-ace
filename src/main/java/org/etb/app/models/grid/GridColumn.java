package org.etb.app.models.grid;

/**
 * 简单的Grid列对象
 * 
 * @author Linkin
 * @email yhuang@sophia.cn
 */
public class GridColumn {

	/**
	 * 默认绑定至实体field
	 */
	private String field;

	/**
	 * 对应的数据库字段名
	 */
	private String dbField;

	/**
	 * 表格列标题
	 */
	private String display;

	/**
	 * 表格列宽度
	 */
	private Integer width;

	/**
	 * 表格列宽度
	 */
	private Integer minWidth;

	/**
	 * 是否允许排序
	 */
	private boolean sortable = true;

	/**
	 * 列对齐方式
	 */
	private ColumnAlign align;

	public static GridColumn of(String display, String field) {
		GridColumn c = new GridColumn();
		c.setDisplay(display);
		c.setField(field);
		return c;
	}

	public static GridColumn of(String display, String field, String dbField) {
		GridColumn c = new GridColumn();
		c.setDisplay(display);
		c.setField(field);
		c.setDbField(dbField);
		return c;
	}

	public String getField() {
		return field;
	}

	public GridColumn setField(String field) {
		this.field = field;
		return this;
	}

	public String getDbField() {
		return dbField;
	}

	public GridColumn setDbField(String dbField) {
		this.dbField = dbField;
		return this;
	}

	public String getDisplay() {
		return display;
	}

	public GridColumn setDisplay(String display) {
		this.display = display;
		return this;
	}

	public Integer getWidth() {
		return width;
	}

	public GridColumn setWidth(Integer width) {
		this.width = width;
		return this;
	}

	public boolean isSortable() {
		return sortable;
	}

	public GridColumn setSortable(boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	public ColumnAlign getAlign() {
		return align;
	}

	public GridColumn setAlign(ColumnAlign align) {
		this.align = align;
		return this;
	}

	public Integer getMinWidth() {
		return minWidth;
	}

	public GridColumn setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int h = 1;
		h = prime * h + ((display == null) ? 0 : display.hashCode());
		h = prime * h + ((field == null) ? 0 : field.hashCode());
		h = prime * h + ((dbField == null) ? 0 : dbField.hashCode());
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof GridColumn)) {
			return false;
		}
		GridColumn that = (GridColumn) obj;
		return equal(field, that.field) && equal(dbField, that.dbField)
				&& equal(display, that.display);
	}

	private boolean equal(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	@Override
	public String toString() {
		return display + "|" + field;
	}

}
