package org.etb.app.components.b;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.NullFieldStrategy;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.etb.app.base.AbstractField;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.utils.StringUtils;

/**
 * 富文本编辑组件(基于xhEditor)
 */
@Import(library = { "res/editor/xheditor/xheditor-all.js" }, stylesheet = { "res/editor/xheditor/xheditor_skin/default/ui.css" })
@Events(EtbEventConstants.VALUE_CHANGE + "文本内容替换事件")
public class Editor extends AbstractField {

	public final static String TOOLS_FULL = "full";// 全部功能
	public final static String TOOLS_MFULL = "mfull";// 多行完全
	public final static String TOOLS_SIMPLE = "simple";// 简单
	public final static String TOOLS_MINI = "mini";// 迷你

	@Parameter(required = true, principal = true, autoconnect = true)
	private Object value;

	@Parameter(value = TOOLS_FULL, allowNull = true, defaultPrefix = BindingConstants.LITERAL)
	private String tools;

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	private boolean formgroup;

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	private boolean col;

	@Parameter(value = "100%", defaultPrefix = BindingConstants.LITERAL)
	private String width;

	@Parameter(value = "260px", defaultPrefix = BindingConstants.LITERAL)
	private String height;

	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	private FieldValidator<Object> validate;

	/**
	 * The object which will perform translation between server-side and
	 * client-side representations. If not specified, a value will usually be
	 * generated based on the type of the value parameter.
	 */
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.TRANSLATE)
	private FieldTranslator<Object> translate;

	@Parameter(value = "false")
	private boolean ignoreBlankInput;

	/**
	 * Defines how nulls on the server side, or sent from the client side, are
	 * treated. The selected strategy may replace the nulls with some other
	 * value. The default strategy leaves nulls alone. Another built-in
	 * strategy, zero, replaces nulls with the value 0.
	 */
	@Parameter(defaultPrefix = BindingConstants.NULLFIELDSTRATEGY, value = "default")
	private NullFieldStrategy nulls;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String containerStyle;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String containerClass;

	@SuppressWarnings("unused")
	@Property
	private RenderCommand richEditorRender;

	@Environmental
	private ValidationTracker tracker;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	private String clientValue;

	final Binding defaultValidate() {
		return defaultProvider.defaultValidatorBinding("value", resources);
	}

	void beginRender(MarkupWriter writer) {

		if (formgroup) {
			writer.element("div", "class", "form-group"
					+ (containerClass == null ? "" : " " + containerClass),
					"style", containerStyle);
			writer.element("label", "class",
					col ? "control-label col-xs-12 col-sm-3 no-padding-right"
							: "control-label", "for", getClientId());
			writer.writeRaw(getLabel());
			writer.end();
			writer.element("div", "class", col ? "col-xs-12 col-sm-9" : null);
			writer.element("div", "class", "clearfix");
		}

		clientValue = tracker.getInput(this);

		if (clientValue == null) {
			clientValue = fieldValidationSupport.toClient(this.value,
					resources, translate, nulls);
		}

		richEditorRender = new RenderCommand() {
			@Override
			public void render(MarkupWriter writer, RenderQueue queue) {
				String tmpStyle = "width:" + width + "; height:" + height + ";";
				Element divContainer = writer.element("div");
				Element inputDiv = divContainer.element("div");
				Element input = inputDiv.element("textarea",

				"name", getControlName(),

				"id", getClientId(),

				// "class", "xheditor-simple", [*]edit by
				// Linkin设置class后，所有参数不起作用了
						"style", "width:" + width + ";");

				if (isDisabled()) {
					input.attributes("disabled", "disabled");
					inputDiv.attribute("style", "display:none;");
					tmpStyle += "border:1px solid #CCC;";
					divContainer.attribute("style", tmpStyle);
					writer.writeRaw(clientValue);
				}
				if (StringUtils.isNotEmpty(clientValue)) {
					input.text(clientValue);
				}
				translate.render(writer);
				validate.render(writer);

				resources.renderInformalParameters(writer);

				writer.end();
			}
		};

		decorateInsideField();

		JSONObject spec = new JSONObject();
		spec.put("richTextId", getClientId());
		spec.put("tools", tools);
		spec.put("width", width);
		spec.put("height", height);
		spec.put("disabled", isDisabled());
		jsSupport.addInitializerCall("XHTextEditor", spec);
	}

	final void afterRender(MarkupWriter writer) {
		if (formgroup) {
			writer.end();
			writer.end();
			writer.end();
		}
	}

	@Override
	protected void processSubmission(String controlName) {
		String rawValue = request.getParameter(controlName);
		CaptureResultCallback<String> callback = new CaptureResultCallback<String>();
		resources.triggerEvent(EtbEventConstants.VALUE_CHANGE,
				new Object[] { rawValue }, callback);
		rawValue = callback.getResult();

		tracker.recordInput(this, rawValue);

		try {

			Object translated = fieldValidationSupport.parseClient(rawValue,
					resources, translate, nulls);

			fieldValidationSupport.validate(rawValue, resources, validate);

			if (!(ignoreBlankInput && StringUtils.isEmpty(rawValue))) {
				value = translated;
			}

		} catch (ValidationException ex) {
			tracker.recordError(this, ex.getMessage());
		}
	}

	@Override
	public boolean isRequired() {
		return validate.isRequired();
	}

	final Binding defaultTranslate() {
		return defaultProvider.defaultTranslatorBinding("value", resources);
	}
}