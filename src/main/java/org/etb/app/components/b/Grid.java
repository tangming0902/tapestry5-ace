package org.etb.app.components.b;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.etb.app.base.BaseClientElement;
import org.etb.app.components.EtbZone;
import org.etb.app.components.Params.Param;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.models.grid.CachedGridModel;
import org.etb.app.models.grid.ColumnAlign;
import org.etb.app.models.grid.GridColumn;
import org.etb.app.models.grid.GridModel;
import org.etb.app.models.grid.GridParams;
import org.etb.app.utils.StringUtils;

@SupportsInformalParameters
public class Grid<T> extends BaseClientElement {

	@SuppressWarnings("unused")
	@Property
	@Parameter(value = "table table-striped table-bordered table-hover dataTable", defaultPrefix = BindingConstants.LITERAL)
	private String tableClassName;

	/**
	 * 表格的列
	 */
	@Parameter(required = true, autoconnect = true, allowNull = false)
	private List<GridColumn> columns;

	/**
	 * 数据源
	 */
	@Parameter(required = true, autoconnect = true, allowNull = false)
	private GridModel<T> model;

	@SuppressWarnings("unused")
	@Parameter(value = "true")
	@Property
	private boolean showTableHead;

	@Parameter
	@Property
	private ColumnAlign headAlign;

	/**
	 * 排序/分页的ajax事件所带的参数
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String onLoadDataParams;

	/**
	 * 每页显示记录数
	 */
	@Parameter(value = "10", autoconnect = true)
	@Property
	private int pageSize;

	/**
	 * 行数据渲染时数据载体
	 */
	@Parameter
	@Property
	private T value;

	@Parameter
	@Property
	private boolean enableCheckbox;

	@SuppressWarnings("unused")
	@Parameter
	@Property
	private boolean enableNumber;

	@SuppressWarnings("unused")
	@Parameter
	@Property
	private RenderCommand rowBlock;

	@SuppressWarnings("unused")
	@Parameter
	@Property
	private RenderCommand detailBlock;

	@SuppressWarnings("unused")
	@Parameter
	@Property
	private RenderCommand emptyBlock;

	@SuppressWarnings("unused")
	@Property
	private Block headRightBlock;

	@Component(publishParameters = "alwaysCheck")
	private Pager pager;

	/**************** BEGIN 小图标资源 ***********************/
	@Inject
	@Path("res/images/sort-asc.png")
	@Property
	private Asset ascImg;

	@Inject
	@Path("res/images/sort-desc.png")
	@Property
	private Asset descImg;

	@Inject
	@Path("res/images/sortable.png")
	@Property
	private Asset sortableImg;
	/**************** END 小图标资源 ***********************/

	@Property
	private List<GridColumn> gridColumns;

	@Property
	private GridColumn gridColumn;

	@SuppressWarnings("unused")
	@Property
	private List<T> data;

	@Property
	private RenderCommand cellRenderCommand;

	@SuppressWarnings("unused")
	@Property
	private String orderByImg;

	@Property
	private int index;

	private GridParams<T> gridParams;

	private CachedGridModel<T> cachedGridModel;

	@Persist(PersistenceConstants.CLIENT)
	private String sortName;

	@Persist(PersistenceConstants.CLIENT)
	private Integer sortOrder;

	@Persist(PersistenceConstants.CLIENT)
	@Property
	private Integer curPageSize;

	void beginRender(MarkupWriter writer) {
		cachedGridModel = new CachedGridModel<T>(model);
		gridParams = new GridParams<T>(columns, cachedGridModel, pageSize, 1,
				null, null);
		data = gridParams.getQueryData();
		curPageSize = pageSize;
		gridColumns = gridParams.getColumns();
		orderByImg = sortableImg.toClientURL();
	}

	@OnEvent(component = "rednerHeadRight", value = "rednerHeadRight")
	void rednerHeadRight() {
		headRightBlock = resources.getBlockParameter("_"
				+ gridColumn.getField() + "_headRight");
	}

	@OnEvent(value = "prepareRenderValue")
	void renderCell() throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException {
		String name = gridColumn.getField();
		cellRenderCommand = resources.getInformalParameter("_" + name,
				RenderCommand.class);

		if (cellRenderCommand == null) {
			if (name.contains(".")) {
				String[] properties = StringUtils.splitIgnoreBlank(name, "\\.");
				Object v = value;
				for (String property : properties) {
					try {
						v = BeanUtils.getProperty(v, property);
					} catch (Exception e) {
						v = null;
					}
					if (v == null)
						break;
				}

				final String cellText = v == null ? "" : v.toString();
				cellRenderCommand = new RenderCommand() {

					public void render(MarkupWriter writer, RenderQueue queue) {
						writer.write(cellText);
					}
				};

			} else {
				String row = BeanUtils.getProperty(value, name);
				final String v = row == null ? "" : row;
				cellRenderCommand = new RenderCommand() {

					public void render(MarkupWriter writer, RenderQueue queue) {
						writer.write(v);
					}
				};
			}
		}
	}

	public String getRowId() throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		return BeanUtils.getProperty(value, "id");
	}

	@OnEvent(component = "renderDataEvent", value = "renderDataEvent")
	void renderDataEvent() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());

		jsSupport.addInitializerCall("RenderGridCheckbox", spec);
	}

	@InjectComponent
	private EtbZone dataZone;

	@InjectComponent
	private EtbZone outZone;

	/**
	 * 分页
	 * 
	 * @param lp
	 *            限制条件
	 * @return Zone的区域
	 */
	@OnEvent(component = "pager", value = EtbEventConstants.MOVE)
	void onMove(long total, int limit, int offset, int currentPage) {
		this.curPageSize = limit;
		this.index = offset;

		if (cachedGridModel == null)
			cachedGridModel = new CachedGridModel<T>(model);

		if (!pager.isAlwaysCheck()) {
			cachedGridModel.setTotalCount(total);
		}

		int so = 0;
		if (sortOrder != null)
			so = sortOrder;

		switch (so) {
		case 0:
			orderByImg = sortableImg.toClientURL();
			gridParams = new GridParams<T>(columns, cachedGridModel, limit,
					currentPage, null, null);
			if (sortName != null) {
				gridParams.setSortName(sortName);
				gridParams.setSortOrder(so == 2 ? "desc" : "asc");
			}
			break;
		case 1:
			orderByImg = ascImg.toClientURL();
			gridParams = new GridParams<T>(columns, cachedGridModel, limit,
					currentPage, sortName, "asc");
			break;
		case 2:
			gridParams = new GridParams<T>(columns, cachedGridModel, limit,
					currentPage, sortName, "desc");
			orderByImg = descImg.toClientURL();
		}

		data = gridParams.getQueryData();
		gridColumns = gridParams.getColumns();
		ajaxResponseRenderer.addRender(dataZone);
	}

	// 排序
	@OnEvent(component = "sort", value = EtbEventConstants.CLICK)
	Object sortZone(String sortField) {

		sortName = sortField;

		if (cachedGridModel == null)
			cachedGridModel = new CachedGridModel<T>(model);

		if (sortOrder == null) {
			sortOrder = 0;
		}
		switch (sortOrder) {
		case 1:
			sortOrder = 2;
			orderByImg = descImg.toClientURL();
			gridParams = new GridParams<T>(columns, cachedGridModel,
					curPageSize, 1, sortField, "desc");
			break;
		case 2:
			sortOrder = 0;
			orderByImg = sortableImg.toClientURL();
			gridParams = new GridParams<T>(columns, cachedGridModel,
					curPageSize, 1, null, null);
			break;
		default:
			sortOrder = 1;
			orderByImg = ascImg.toClientURL();
			gridParams = new GridParams<T>(columns, cachedGridModel,
					curPageSize, 1, sortField, "asc");
			break;
		}

		data = gridParams.getQueryData();
		gridColumns = gridParams.getColumns();

		return outZone.getBody();
	}

	@OnEvent(component = "renderRowEvent", value = "renderRowEvent")
	void renderRowEvent() {
		index++;
	}

	public long getTotal() {
		if (cachedGridModel == null)
			cachedGridModel = new CachedGridModel<T>(model);
		return cachedGridModel.getTotalCount();
	}

	public boolean isShowChangeSortIcon() {
		return sortName != null && sortName.equals(gridColumn.getField());
	}

	public String getColumnStyle() {
		StringBuffer style = new StringBuffer();
		if (gridColumn.getAlign() != null) {
			switch (gridColumn.getAlign()) {
			case LEFT:
				style.append("text-align:left;");
				break;
			case CENTER:
				style.append("text-align:center;");
				break;
			case RIGHT:
				style.append("text-align:right;");
				break;
			}
		}

		return style.toString();
	}

	public String getHeadStyle() {
		StringBuffer style = new StringBuffer();

		if (gridColumn.getWidth() != null) {
			style.append("width:" + gridColumn.getWidth() + "px;");
		}

		if (gridColumn.getMinWidth() != null) {
			style.append("min-width:" + gridColumn.getMinWidth() + "px;");
		}

		if (headAlign != null) {
			switch (headAlign) {
			case LEFT:
				style.append("text-align:left;");
				break;
			case CENTER:
				style.append("text-align:center;");
				break;
			case RIGHT:
				style.append("text-align:right;");
				break;
			}
		}

		return style.toString();
	}

	public boolean isHasItem() {
		return getTotal() > 0;
	}

	public String getParamsId() {
		if (resources.isBound("onLoadDataParams")) {
			return onLoadDataParams;
		}
		return "";
	}

	/**
	 * Grid选中行id，默认参数类型String[](当id被强指定时)
	 * 
	 * @param requestKey
	 * @return
	 */
	public Param paramChecked(String requestKey) {
		checkIdInvariable(resources, idParameter);
		return paramChecked(idParameter, requestKey);
	}

	/**
	 * Grid选中行id，默认参数类型String[](当id为动态值时)
	 * 
	 * @param requestKey
	 * @return
	 */
	public Param paramChecked(String clientId, String requestKey) {
		return Param.of(requestKey, "Tapestry.Initializer.GridCheckedRows('"
				+ clientId + "')");
	}

	public int getColumnCount() {
		return enableCheckbox ? gridColumns.size() + 2 : gridColumns.size() + 1;
	}

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String rowZone;

	@Property
	@Parameter
	private Object[] rowContext;

	public boolean isHasRowZone() {
		return StringUtils.isNotEmpty(rowZone);
	}

	public Object[] getAlterRowContext() throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		String id = (String) BeanUtils.getProperty(value, "id");

		Object[] alter = Arrays.copyOf(rowContext, rowContext.length + 1);
		alter[alter.length - 1] = id;

		return alter;
	}

}