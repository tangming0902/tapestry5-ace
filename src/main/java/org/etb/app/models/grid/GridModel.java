package org.etb.app.models.grid;

import java.util.List;

public interface GridModel<T> {

	/**
	 * 得到总数
	 */
	long getTotalCount();

	/**
	 * 查询记录
	 */
	List<T> query(long total, int limit, int offset, int currentPage,
			String orderSortName, Boolean orderAsc);

	/**
	 * 取得实体class
	 */
	Class<T> getEntityClass();

}
