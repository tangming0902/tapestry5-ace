package org.etb.app.models.grid;

import java.util.List;

/**
 * 用于在<b>一次请求内<b>GridModel参数值的缓存
 * 
 * @author Linkin
 * @email yhuang@sophia.cn
 */
public class CachedGridModel<T> implements GridModel<T> {

	private final GridModel<T> model;

	private long count;

	private List<T> items;

	private boolean countCached;

	private boolean itemsCached;

	public CachedGridModel(GridModel<T> model) {
		this.model = model;
	}

	public void setTotalCount(long totalCount) {
		countCached = true;
		count = totalCount;
	}

	public long getTotalCount() {
		if (countCached)
			return count;

		countCached = true;
		count = model.getTotalCount();
		return count;
	}

	public List<T> query(long total, int limit, int offset, int currentPage,
			String orderSortName, Boolean orderAsc) {
		if (itemsCached)
			return items;

		itemsCached = true;
		items = model.query(total, limit, offset, currentPage, orderSortName,
				orderAsc);
		return items;
	}

	public Class<T> getEntityClass() {
		return model.getEntityClass();
	}
}
