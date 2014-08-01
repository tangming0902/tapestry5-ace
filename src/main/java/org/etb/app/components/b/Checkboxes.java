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
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.etb.app.base.AbstractField;
import org.etb.app.models.CheckboxItem;

@SupportsInformalParameters
public class Checkboxes extends AbstractField {

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
	private List<CheckboxItem> data;

	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	private FieldValidator<Object> validate;
	
	final Binding defaultValidate()
    {
        return defaultProvider.defaultValidatorBinding("value", resources);
    }

	@Inject
	private FieldValidationSupport fieldValidationSupport;
	
	@Inject
    private ComponentDefaultProvider defaultProvider;

	@Environmental
	private ValidationTracker tracker;

	@Property
	private CheckboxItem item;

	@SuppressWarnings("unused")
	@Property
	private RenderCommand cbRender;

	void beginRender(MarkupWriter writer) {
		writer.element("div", "class", "checkboxes"
				+ (formgroup ? " form-group" : "")
				+ (containerClass == null ? "" : " " + containerClass),
				"style", containerStyle, "id", getClientId());

		if (validate != null)
			validate.render(writer);

		cbRender = new RenderCommand() {

			@Override
			public void render(MarkupWriter writer, RenderQueue queue) {
				writer.element("input", "class", "ace", "type", "checkbox",
						"name", getControlName(), "value", item.getValue());
				if (item.isSelected()) {
					writer.attributes("checked", "checked");
				}
				writer.end();
			}
		};
	}

	void afterRender(MarkupWriter writer) {
		writer.end();
	}

	@Override
	protected void processSubmission(String controlName) {
		String[] values = request.getParameters(controlName);

		try {
			fieldValidationSupport.validate(values, resources, validate);

			List<CheckboxItem> submitData = new ArrayList<CheckboxItem>();
			for (String value : values) {
				submitData.add(CheckboxItem.of(value, true));
			}

			this.data = submitData;

		} catch (ValidationException ex) {
			tracker.recordError(this, ex.getMessage());
		}
	}
}
