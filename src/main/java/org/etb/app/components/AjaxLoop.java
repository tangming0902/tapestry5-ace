package org.etb.app.components;

import java.util.Collections;
import java.util.Iterator;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.services.PageRenderQueue;
import org.apache.tapestry5.internal.services.RequestConstants;
import org.apache.tapestry5.internal.services.ajax.AjaxFormUpdateController;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;
import org.apache.tapestry5.services.ajax.JSONCallback;
import org.etb.app.annotations.CatchRequestParameter;
import org.etb.app.base.BaseClientElement;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.utils.StringUtils;

@Events({ EtbEventConstants.ADD_ROW })
public class AjaxLoop extends BaseClientElement {

	public final static String ROW_CLASS = "etb-loop-row";

	public final static String TAIL_CLASS = "etb-loop-tail";

	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property(write = false)
	private String element;

	@Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL)
	private String elementClass;

	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String style;

	@SuppressWarnings("rawtypes")
	@Parameter(required = true, autoconnect = true)
	private Iterable source;

	@SuppressWarnings("unused")
	@Parameter(required = true)
	private Object value;

	@Parameter
	private int index = 1;

	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String show;

	@SuppressWarnings("unused")
	@Parameter
	@Property
	private RenderCommand nulls;

	@Parameter
	private Object[] context;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private int max;

	/**
	 * js方法名，当删除后会调用此js方法，传入参数1.index
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String removeCallback;

	@Inject
	private PageRenderQueue pageRenderQueue;

	@Inject
	private AjaxFormUpdateController ajaxFormUpdateController;

	@Inject
	private TypeCoercer typeCoercer;

	@Inject
	private Block more;

	@Inject
	private Block tail;

	@InjectComponent
	private EtbAny tailElement;

	@SuppressWarnings("rawtypes")
	private Iterator iterator;

	private boolean hasItem;

	String defaultElement() {
		return resources.getElementName("div");
	}

	public String getTailClass() {
		return TAIL_CLASS;
	}

	@SuppressWarnings("unused")
	@Property
	private String rowClass;

	void setupRender() {
		iterator = source == null ? Collections.EMPTY_LIST.iterator() : source
				.iterator();

		hasItem = iterator.hasNext();
		this.rowClass = StringUtils.isEmpty(elementClass) ? ROW_CLASS
				: ROW_CLASS + " " + elementClass;
	}

	boolean beginRender(MarkupWriter writer) {
		if (!iterator.hasNext())
			return false;

		value = iterator.next();
		index = index + 1;
		return true;
	}

	Object afterRender(MarkupWriter writer) {

		if (iterator.hasNext()) {
			return false;
		}
		return tail;
	}

	void cleanupRender() {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());

		if (max > 0 && index > max) {
			throw new RuntimeException(String.format("[%s]的条数必须小于等于%s",
					AjaxLoop.class.getSimpleName(), max));
		}

		if (max > 0) {
			spec.put("max", max);
		}

		Link link = resources.createEventLink("addRow", context);

		FormSupport formSupport = environment.peek(FormSupport.class);
		if (formSupport != null) {
			link.addParameter(RequestConstants.FORM_CLIENTID_PARAMETER,
					formSupport.getClientId());
			link.addParameter(RequestConstants.FORM_COMPONENTID_PARAMETER,
					formSupport.getFormComponentId());
		}

		spec.put("rowNum", index);
		spec.put("index", index);
		spec.put("addUrl", link.toAbsoluteURI());
		spec.put("tailId", tailElement.getClientId());
		spec.put("rowClass", ROW_CLASS);
		spec.put("removeCallback", removeCallback);
		jsSupport.addInitializerCall("AjaxLoop", spec);
	}

	@CatchRequestParameter("_index")
	private Integer requestIndex;

	@SuppressWarnings("rawtypes")
	void onAddrow(Object[] context) {
		this.index = requestIndex + 1;
		this.rowClass = StringUtils.isEmpty(elementClass) ? ROW_CLASS
				: ROW_CLASS + " " + elementClass;

		ComponentEventCallback callback = new ComponentEventCallback() {
			public boolean handleResult(Object result) {
				value = result;

				return true;
			}
		};

		resources.triggerEvent(EtbEventConstants.ADD_ROW, context, callback);

		// if (value == null)
		// throw new IllegalArgumentException(
		// String.format(
		// "Event handler for event 'addRow' from %s should have returned a non-null value.",
		// resources.getCompleteId()));

		final RenderCommand renderCommand = typeCoercer.coerce(more,
				RenderCommand.class);

		final JSONObject spec = new JSONObject();

		pageRenderQueue
				.addPartialMarkupRendererFilter(new PartialMarkupRendererFilter() {
					public void renderMarkup(MarkupWriter writer,
							JSONObject reply, PartialMarkupRenderer renderer) {
						RenderCommand tempContentRenderCommand = new RenderCommand() {
							public void render(MarkupWriter writer,
									RenderQueue queue) {

								final Element tempElement = writer
										.element("temp");
								ajaxFormUpdateController
										.setupBeforePartialZoneRender(writer);
								queue.push(new RenderCommand() {
									public void render(MarkupWriter writer,
											RenderQueue queue) {
										writer.end();

										ajaxFormUpdateController
												.cleanupAfterPartialZoneRender();
										String content = tempElement
												.getChildMarkup();

										if (StringUtils.isNotEmpty(content)) {
											spec.put("content", content);
										}

										tempElement.remove();
									}
								});

								queue.push(renderCommand);
							}
						};

						pageRenderQueue
								.addPartialRenderer(tempContentRenderCommand);

						renderer.renderMarkup(writer, reply);
					}
				});

		ajaxResponseRenderer.addCallback(new JSONCallback() {

			@Override
			public void run(JSONObject reply) {
				reply.put("addRowContent", spec);
			}
		});
	}

	public boolean isNeedNulls() {
		return resources.isBound("nulls") && !hasItem;
	}

}
