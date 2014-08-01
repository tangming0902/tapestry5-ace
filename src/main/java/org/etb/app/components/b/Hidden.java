package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.NullFieldStrategy;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.FormSupport;
import org.etb.app.base.BaseClientElement;

@SupportsInformalParameters
public class Hidden extends BaseClientElement {

	@Parameter(required = true, autoconnect = true, principal = true)
	private Object value;

	@Parameter(defaultPrefix = BindingConstants.NULLFIELDSTRATEGY, value = "default")
	private NullFieldStrategy nulls;

	@SuppressWarnings("rawtypes")
	@Parameter(required = true)
	private ValueEncoder encoder;

	@Environmental(false)
	private FormSupport formSupport;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@SuppressWarnings("rawtypes")
	ValueEncoder defaultEncoder() {
		return defaultProvider.defaultValueEncoder("value", resources);
	}

	@SuppressWarnings("serial")
	static class ProcessSubmission implements ComponentAction<Hidden> {
		private final String controlName;

		public ProcessSubmission(String controlName) {
			this.controlName = controlName;
		}

		public void execute(Hidden component) {
			component.processSubmission(controlName);
		}
	}

	@SuppressWarnings("unchecked")
	boolean beginRender(MarkupWriter writer) {
		if (formSupport == null) {
			throw new RuntimeException(
					"The Hidden component must be enclosed by a Form component.");
		}

		String controlName = formSupport.allocateControlName(resources.getId());

		formSupport.store(this, new ProcessSubmission(controlName));

		Object toEncode = value == null ? nulls.replaceToClient() : value;

		String encoded = toEncode == null ? "" : encoder.toClient(toEncode);

		writer.element("input", "id", getClientId(), "type", "hidden", "name",
				controlName, "value", encoded);

		resources.renderInformalParameters(writer);

		writer.end();

		return false;
	}

	private void processSubmission(String controlName) {
		String encoded = request.getParameter(controlName);

		String toDecode = InternalUtils.isBlank(encoded) ? nulls
				.replaceFromClient() : encoded;

		Object decoded = toDecode == null ? null : encoder.toValue(toDecode);

		value = decoded;
	}

}
