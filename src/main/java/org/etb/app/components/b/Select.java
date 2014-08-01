package org.etb.app.components.b;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.etb.app.annotations.CatchRequestParameter;
import org.etb.app.base.AbstractField;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.models.StringKV;
import org.etb.app.models.Option;
import org.etb.app.models.SelectModel;
import org.etb.app.utils.StringUtils;

/**
 * 下拉框<br>
 * api见http://ivaynberg.github.io/select2/
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
@Events(EtbEventConstants.SELECT)
@SupportsInformalParameters
public class Select extends AbstractField {

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	private boolean formgroup;

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	private boolean col;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerClass;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerStyle;

	@Parameter(required = true, allowNull = false, principal = true)
	private SelectModel model;

	@Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
	private boolean multiple;

	/**
	 * 单选场景的值
	 */
	@Parameter
	private StringKV value;

	/**
	 * 多选场景的值
	 */
	@Parameter
	private List<StringKV> values;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String zone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String params;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "200px")
	private String width;

	@Parameter(value = "", defaultPrefix = BindingConstants.LITERAL)
	private String placeholder;

	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	private FieldValidator<Object> validate;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Environmental
	private ValidationTracker tracker;

	protected Binding defaultValidate() {
		if (multiple) {
			return defaultProvider.defaultValidatorBinding("values", resources);
		}

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
			renderInput(writer);
			writer.end();
			writer.end();
		} else {
			renderInput(writer);
		}
	}

	void afterRender(MarkupWriter writer) {
		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("placeholder", placeholder);
		spec.put("multiple", multiple);

		if (zone != null) {
			spec.put("onSelectZone", zone);
			spec.put("selectUrl", resources.createEventLink("selected").toURI());
			if (params != null)
				spec.put("onSelectParams", params);
		}

		spec.put("data", convertOptions(model.getOptions()));
		jsSupport.addInitializerCall("Select2", spec);
	}

	private void renderInput(MarkupWriter writer) {
		writer.element("input", "id", getClientId() + "_text", "name",
				getControlName() + "_text", "type", "hidden");
		if (multiple) {
			if (values != null) {
				List<String> ids = new ArrayList<String>();
				for (StringKV value : values) {
					ids.add(value.getV());
				}
				writer.attributes("value",
						StringUtils.toDelimitedString(ids, ","));
			}
		} else {
			if (value != null) {
				writer.attributes("value", value.getV());
			}
		}
		writer.end();

		Element input = writer.element("input", "id", getClientId(), "name",
				getControlName(), "type", "text");

		if (multiple) {
			if (values != null) {
				List<String> ids = new ArrayList<String>();
				for (StringKV value : values) {
					ids.add(value.getK());
				}
				writer.attributes("value",
						StringUtils.toDelimitedString(ids, ","));
			}
		} else {
			if (value != null) {
				writer.attributes("value", value.getK());
			}
		}

		resources.renderInformalParameters(writer);
		forceAddAttribute(input, "style", "width:" + width + ";");
		forceAddAttribute(input, "class", "select2");

		validate.render(writer);

		decorateInsideField();

		writer.end();
	}

	private JSONArray convertOptions(List<Option> options) {
		JSONArray array = new JSONArray();

		// if (!multiple) {
		// JSONObject blank = new JSONObject();
		// blank.put("id", "");
		// blank.put("text", "&nbsp;");
		// array.put(blank);
		// }

		if (options == null)
			return array;

		for (Option option : options) {
			JSONObject o = new JSONObject();
			o.put("id", option.getValue());
			o.put("text", option.getLabel());
			o.put("disabled", option.isDisabled());
			array.put(o);
		}
		return array;
	}

	@Override
	protected void processSubmission(String controlName) {
		String submittedValue = request.getParameter(controlName);
		String submittedText = request.getParameter(controlName + "_text");

		tracker.recordInput(this, submittedValue);

		try {

			if (multiple) {
				List<StringKV> selectedValues = null;
				if (!StringUtils.isEmpty(submittedValue)) {
					selectedValues = new ArrayList<StringKV>();
					String[] valueArr = submittedValue.split(",");
					String[] textArr = submittedText.split(",");

					for (int i = 0; i < valueArr.length; i++) {
						selectedValues.add(StringKV.of(valueArr[i], textArr[i]));
					}
					fieldValidationSupport.validate(selectedValues, resources,
							validate);
					values = selectedValues;
				}
			} else {
				StringKV selectedValue = null;
				if (!StringUtils.isEmpty(submittedValue)) {
					selectedValue = StringKV.of(submittedValue, submittedText);
				}
				fieldValidationSupport.validate(selectedValue, resources,
						validate);
				value = selectedValue;
			}
		} catch (ValidationException ex) {
			tracker.recordError(this, ex.getMessage());
		}
	}

	@CatchRequestParameter("_select_value")
	private String selectValue;

	@CatchRequestParameter("_select_text")
	private String selectText;

	Object onSelected() {
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources.triggerEvent(EtbEventConstants.SELECT, new Object[] {
				selectValue, selectText }, callback);
		return callback.getResult();
	}

}