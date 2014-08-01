package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.etb.app.base.AbstractTextField;

/**
 * 密码输入控件
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class PasswordField extends AbstractTextField {

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	private boolean formgroup;

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	private boolean col;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerClass;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerStyle;

	@Override
	protected final void writeFieldTag(MarkupWriter writer, String value) {
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
			writeInput(writer, value);
			writer.end();
			writer.end();
			writer.end();
		} else {
			writeInput(writer, value);
		}
	}

	private void writeInput(MarkupWriter writer, String value) {
		Element input = writer.element("input",

		"type", "password",

		"name", getControlName(),

		"id", getClientId(),

		"value", value);

		resources.renderInformalParameters(writer);

		if (formgroup) {
			defaultAttribute(input, "class", "col-xs-12 col-sm-4");
		}
		writer.end(); // input
	}

	/**
	 * Returns true, blank input should be ignored and not cause an update to
	 * the server-side property bound to the value parameter.
	 * 
	 * @return true
	 */
	@Override
	protected boolean ignoreBlankInput() {
		return true;
	}
}
