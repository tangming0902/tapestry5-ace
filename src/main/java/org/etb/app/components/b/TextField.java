package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.etb.app.base.AbstractTextField;

/**
 * 文本输入控件
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
@SupportsInformalParameters
public class TextField extends AbstractTextField {

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	private boolean formgroup;

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	private boolean col;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerClass;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerStyle;

	@Parameter(allowNull = false, value = "text", defaultPrefix = BindingConstants.LITERAL)
	private String type;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String placeholder;

    @Parameter(value="false",defaultPrefix = BindingConstants.LITERAL)
    private boolean readOnly;

	@Override
	protected void writeFieldTag(MarkupWriter writer, String value) {
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

		"type", type,

		"name", getControlName(),

		"id", getClientId(),

		"value", value, "placeholder", placeholder);


        if ( readOnly)
            input.attribute("readonly", "true");


		resources.renderInformalParameters(writer);

		if (formgroup) {
			defaultAttribute(input, "class", "col-xs-12 col-sm-6");
		}
		writer.end();
	}

}
