package org.etb.app.models.grid;

import java.util.Collection;
import java.util.List;

import org.etb.app.utils.StringUtils;

public class GridParams<T> {

	private List<GridColumn> columns;

	private CachedGridModel<T> model;

	private int pageSize;

	private int pageNum;

	private String sortName;

	private String sortOrder;

	public GridParams(List<GridColumn> columns, CachedGridModel<T> model,
			int pageSize, int pageNum, String sortName, String sortOrder) {
		this.columns = columns;
		this.model = model;
		this.pageSize = pageSize;
		this.pageNum = pageNum;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
	}

	/**
	 * 取出已初始化的labels集合
	 */
	public List<GridColumn> getColumns() {
		return columns;
	}

	public static class MyOrder {
		public boolean asc;
		public String sortName;
	}

	/*
	 * 取得排序对象
	 */
	public MyOrder getOrderBy() {

		MyOrder order = new MyOrder();

		String sort = getSortName(sortName, getColumns());
		if (StringUtils.isNotEmpty(sort)) {
			order.sortName = sort;
			order.asc = "asc".equals(sortOrder);
		}
		return order;
	}

	private String getSortName(String sortName, Collection<GridColumn> columns) {
		if (null == sortName) {
			return null;
		}
		for (GridColumn c : columns) {
			if (c.getField().equals(sortName)) {
				String dbField = c.getDbField();
				return StringUtils.isEmpty(dbField) ? sortName : dbField;
			}
		}
		return sortName;
	}

	/**
	 * 取得查询记录结果集
	 */
	public List<T> getQueryData() {
		int offset = pageSize * (pageNum - 1);
		MyOrder order = getOrderBy();
		return model.query(getTotalCount(), pageSize, offset, pageNum,
				order.sortName, order.asc);
	}

	/**
	 * 取得实体class
	 */
	public Class<T> getEntityClass() {
		return model.getEntityClass();
	}

	/**
	 * 取得查询总数
	 */
	public long getTotalCount() {
		return model.getTotalCount();
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
}
