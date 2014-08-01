package org.etb.app.components.b;

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
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.etb.app.annotations.CatchRequestParameter;
import org.etb.app.base.AbstractField;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.models.RadioItem;
import org.etb.app.utils.StringUtils;

@Events({ EtbEventConstants.CLICK })
@SupportsInformalParameters
public class RadioGroup extends AbstractField {

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private boolean formgroup;
	
	@SuppressWarnings("unused")
	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private boolean col;

	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private boolean inline;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerClass;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerStyle;

	@SuppressWarnings("unused")
	@Property
	@Parameter(required = true, autoconnect = true, principal = true)
	private List<RadioItem> data;

	@Parameter(required = true, autoconnect = true, principal = true)
	private String value;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String zone;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String params;

	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	private FieldValidator<Object> validate;

	final Binding defaultValidate() {
		return defaultProvider.defaultValidatorBinding("value", resources);
	}

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Environmental
	private ValidationTracker tracker;

	@Property
	private RadioItem item;

	@SuppressWarnings("unused")
	@Property
	private RenderCommand rdRender;

	void beginRender(MarkupWriter writer) {
		writer.element("div", "class", "radiogroup"
				+ (formgroup ? " form-group" : "")
				+ (containerClass == null ? "" : " " + containerClass),
				"style", containerStyle, "id", getClientId());

		if (validate != null)
			validate.render(writer);

		rdRender = new RenderCommand() {

			@Override
			public void render(MarkupWriter writer, RenderQueue queue) {
				writer.element("input", "class", "ace", "type", "radio",
						"autocomplete", "off", "name", getControlName(),
						"value", item.getValue());
				if (StringUtils.isNotEmpty(value)
						&& value.equals(item.getValue())) {
					writer.attributes("checked", "checked");
				}
				writer.end();
			}
		};
	}

	void afterRender(MarkupWriter writer) {
		writer.end();

		if (resources.isBound("zone")) {
			JSONObject spec = new JSONObject();
			spec.put("clientId", getClientId());
			spec.put("zone", zone);
			spec.put("params", params);
			spec.put("url", resources.createEventLink("radioGroupClickEvent")
					.toAbsoluteURI());
			jsSupport.addInitializerCall("BRadioGroup", spec);
		}
	}

	@CatchRequestParameter("_radio_value")
	private String requestValue;

	Object onRadioGroupClickEvent() {
		CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
		resources.triggerEvent(EtbEventConstants.CLICK,
				new Object[] { requestValue }, callback);
		return callback.getResult();
	}

	@Override
	protected void processSubmission(String controlName) {
		String value = request.getParameter(controlName);

		try {
			fieldValidationSupport.validate(value, resources, validate);
			this.value = value;
		} catch (ValidationException ex) {
			tracker.recordError(this, ex.getMessage());
		}
	}
}
