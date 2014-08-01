package org.etb.app.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.etb.app.base.BaseClientElement;

@SupportsInformalParameters
public class EtbAny extends BaseClientElement {
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String element;

	String defaultElement() {
		return resources.getElementName("div");
	}

	void beginRender(MarkupWriter writer) {
		writer.element(element, "id", getClientId());

		resources.renderInformalParameters(writer);
	}

	void afterRender(MarkupWriter writer) {
		writer.end(); // the element
	}
}
