package org.etb.app.components.b;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.etb.app.annotations.CatchRequestParameter;
import org.etb.app.base.BaseClientElement;
import org.etb.app.components.Params.Param;
import org.etb.app.constants.EtbEventConstants;

/**
 * 分页组件<br>
 * 为了保证数据一致性, 当alwaysCheck为true时，total会被重复读取，否则仅会在第一次渲染时读取。<br>
 * (在数据总数被增加或者减少时可以及时更新，避免翻页错误)
 * 
 * @author Linkin
 * @email yhuang@sophia.com.cn
 */
@Events(value = { EtbEventConstants.MOVE })
public class Pager extends BaseClientElement {

	/**
	 * 总数
	 */
	@Parameter(required = true, allowNull = false)
	private long total;

	/**
	 * 每页显示数
	 */
	@Parameter(required = true, allowNull = false)
	private Integer limit;

	/**
	 * 最大显示页码
	 */
	@Parameter(value = "5", allowNull = false)
	private Integer maxPageNumber;

	/**
	 * 当前页数
	 */
	@Parameter(value = "1", allowNull = false)
	private Integer currentPage;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
	private boolean alwaysCheck;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String zone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String params;

	@SuppressWarnings("unused")
	@Parameter(value = "", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String style;

	@InjectComponent
	private Zone pagerZone;

	@Property
	private long clientTotal;

	@Property
	private Integer clientLimit;

	@Property
	private Integer clientCurrentPage;

	@Property
	private String clientMoveZone;

	@Property
	private String clientMoveParams;

	@Property
	private Integer clientMaxPageNumber;

	@Property
	private Integer loopPageNum;

	@CatchRequestParameter
	private String requestClientId;

	@CatchRequestParameter
	private long requestTotal;

	@CatchRequestParameter
	private Integer requestLimit;

	@CatchRequestParameter
	private String requestClickZone;

	@CatchRequestParameter
	private String requestClickParams;

	@CatchRequestParameter
	private Integer requestMaxPageNumber;

	@CatchRequestParameter
	private Integer requestCurrentPage;

	@CatchRequestParameter
	private String requestCurrentPage2;

	@CatchRequestParameter
	private Integer requestPagerSize;

	@Persist(PersistenceConstants.CLIENT)
	private Integer defaultLimitOption;

	public void setupRender() {
		this.clientTotal = total;
		this.clientLimit = limit;
		if (defaultLimitOption == null) {
			this.defaultLimitOption = limit;
		}
		this.clientMoveZone = resources.isBound("onMoveZone") ? zone
				: getClientId();
		this.clientMoveParams = params;
		this.clientMaxPageNumber = maxPageNumber;
		validate();

		if (clientCurrentPage == null)
			clientCurrentPage = currentPage;

		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("zone", clientMoveZone);
		spec.put("paramsId", clientMoveParams);
		spec.put("enterUrl", resources.createEventLink("clickEnter")
				.toAbsoluteURI());
		spec.put("selectUrl", resources.createEventLink("selected")
				.toAbsoluteURI());
		spec.put("limit", this.limit);
		spec.put("defaultLimit", this.limit);
		spec.put("defaultLimitOption", defaultLimitOption);

		jsSupport.addInitializerCall("Pager", spec);
	}

	@OnEvent(component = "goToFirst", value = EtbEventConstants.CLICK)
	Object goToFirst() {
		prepareForRender(1);
		return goToPage();
	}

	@OnEvent(component = "goToLast", value = EtbEventConstants.CLICK)
	Object goToLast() {
		prepareForRender((int) Math
				.ceil(requestTotal / ((double) requestLimit)));
		return goToPage();
	}

	@OnEvent(component = "goToAllocate", value = EtbEventConstants.CLICK)
	Object goToAllocate(Integer pageNum) {
		prepareForRender(pageNum);
		return goToPage();
	}

	Object onSelected() {
		clientCurrentPage = 1;

		clientId = requestClientId;

		if (alwaysCheck) {
			clientTotal = total;
		} else {
			clientTotal = requestTotal;
		}

		clientLimit = requestPagerSize;
		clientMoveZone = requestClickZone;
		clientMoveParams = requestClickParams;
		clientMaxPageNumber = requestMaxPageNumber;

		ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

			public void run(JavaScriptSupport javascriptSupport) {
				JSONObject spec = new JSONObject();
				spec.put("clientId", clientId);
				spec.put("zone", clientMoveZone);
				spec.put("paramsId", clientMoveParams);
				spec.put("enterUrl", resources.createEventLink("clickEnter")
						.toAbsoluteURI());
				spec.put("selectUrl", resources.createEventLink("selected")
						.toAbsoluteURI());
				spec.put("limit", limit);
				spec.put("defaultLimitOption", defaultLimitOption);
				jsSupport.addInitializerCall("Pager", spec);
				jsSupport.addScript("jQuery('#%s_one_pager_size').val('%s')",
						requestClientId, requestPagerSize);
			}
		});
		return goToPage();
	}

	private Integer current;

	Object onClickEnter() {
		try {
			current = Integer.valueOf(requestCurrentPage2);
			if (current > (int) Math.ceil(requestTotal / (requestLimit + 0.00))
					|| current < 1) {
				current = requestCurrentPage;
			}
		} catch (Exception e) {
			current = requestCurrentPage;
		} finally {
			ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

				public void run(JavaScriptSupport jsSupport) {
					jsSupport.addScript(
							"jQuery('#%s input.input-small').val('%s')",
							requestClientId, current);
				}
			});
		}
		prepareForRender(current);
		return goToPage();
	}

	private Object goToPage() {
		ajaxResponseRenderer.addRender(requestClientId, pagerZone.getBody());

		int maxPage = (int) Math.ceil((double) clientTotal / clientLimit);
		if (clientCurrentPage > maxPage) {
			clientCurrentPage = maxPage;
		}

		int offset = clientLimit * (clientCurrentPage - 1);
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources
				.triggerEvent(EtbEventConstants.MOVE, new Object[] {
						clientTotal, clientLimit, offset, clientCurrentPage },
						callback);
		return callback.getResult();
	}

	private void validate() {
		if (clientTotal < 0 || clientLimit < 0)
			throw new RuntimeException("总数和每页显示数不能小于0");
	}

	public List<Param> getParams() {
		return Arrays.asList(
				Param.of("requestClientId", "'" + getClientId() + "'"),
				Param.of("requestTotal", "'" + clientTotal + "'"),
				Param.of("requestLimit", "'" + clientLimit + "'"),
				Param.of("requestClickZone", "'" + clientMoveZone + "'"),
				Param.of("requestMaxPageNumber", "'" + clientMaxPageNumber
						+ "'"),
				Param.of("requestCurrentPage", "'" + clientCurrentPage + "'"),
				Param.of("requestClickParams", "'" + clientMoveParams + "'"));
	}

	public boolean isGoToFirstEnable() {
		return clientCurrentPage > 1;
	}

	public boolean isGoToLastEnable() {
		return clientCurrentPage < maxPageNum;
	}

	public boolean isActivePage() {
		return loopPageNum + startNum == clientCurrentPage;
	}

	private Integer startNum;

	private Integer endNum;

	@Property
	private Integer maxPageNum;

	private Integer betweenDiff;

	public int getCurrentPage() {
		if (clientCurrentPage == null)
			return 0;
		return clientCurrentPage;
	}

	private void prepareForRender(Integer num) {
		clientCurrentPage = num;

		clientId = requestClientId;

		if (alwaysCheck) {
			clientTotal = total;
		} else {
			clientTotal = requestTotal;
		}

		clientLimit = requestLimit;
		clientMoveZone = requestClickZone;
		clientMoveParams = requestClickParams;
		clientMaxPageNumber = requestMaxPageNumber;

		ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

			public void run(JavaScriptSupport javascriptSupport) {
				JSONObject spec = new JSONObject();
				spec.put("clientId", clientId);
				spec.put("zone", clientMoveZone);
				spec.put("paramsId", clientMoveParams);
				spec.put("enterUrl", resources.createEventLink("clickEnter")
						.toAbsoluteURI());
				spec.put("selectUrl", resources.createEventLink("selected")
						.toAbsoluteURI());
				spec.put("limit", limit);
				spec.put("defaultLimitOption", defaultLimitOption);
				jsSupport.addInitializerCall("Pager", spec);
				jsSupport.addScript("jQuery('#%s_one_pager_size').val('%s')",
						requestClientId, clientLimit);
			}
		});
	}

	public Integer getStartNum() {
		maxPageNum = (int) Math.ceil(clientTotal / (clientLimit + 0.00));
		betweenDiff = (int) Math.ceil(clientMaxPageNumber / 2.00);

		startNum = 1;
		endNum = 1;
		if (maxPageNum <= clientMaxPageNumber) {
			startNum = 1;

			if (maxPageNum == 0) {
				endNum = 1;
				clientCurrentPage = 0;
			} else {
				endNum = maxPageNum;
			}
		} else if (clientCurrentPage - 1 < betweenDiff) {
			startNum = 1;
			endNum = clientMaxPageNumber;
		} else if (clientCurrentPage + betweenDiff >= maxPageNum) {
			startNum = maxPageNum - (betweenDiff - 1) * 2;
			endNum = maxPageNum;
		} else {
			startNum = clientCurrentPage - betweenDiff + 1;
			endNum = clientCurrentPage + betweenDiff - 1;
		}
		return startNum;
	}

	public Integer getEndNum() {
		return endNum;
	}

	public Integer getRenderingPageNum() {
		return startNum + loopPageNum;
	}

	public boolean isAlwaysCheck() {
		return alwaysCheck;
	}

	public Integer getLimit() {
		return limit;
	}
}
