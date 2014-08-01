package org.etb.app.components.b;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.etb.app.base.BaseClientElement;
import org.etb.app.enums.ButtonColor;
import org.etb.app.enums.ButtonSize;
import org.etb.app.utils.StringUtils;

/**
 * 支持了Button样式的PageLink
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class PageLink extends BaseClientElement {

	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String page;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String anchor;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private ButtonSize size;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private ButtonColor color;

	@Parameter(value = "false")
	private boolean disabled;

	@Parameter
	private Object[] context;
	
	ButtonSize defaultSize() {
		return ButtonSize.SMALL;
	}

	ButtonColor defaultColor() {
		return ButtonColor.DEFAULT;
	}

	String defaultElementName() {
		return resources.getElementName("a");
	}

	@SuppressWarnings("deprecation")
	void beginRender(MarkupWriter writer) {
		Element element = writer.element("a", "id", getClientId());
		resources.renderInformalParameters(writer);
		String buttonClass = element.getAttribute("class");

		if (buttonClass == null) {
			buttonClass = "btn";
		} else {
			buttonClass += " btn";
		}

		if (size != null) {
			buttonClass += " " + size.value;
		}

		if (color != null) {
			buttonClass += " " + color.value;
		}

		if (disabled) {
			buttonClass += " disabled";
		}

		writer.attributes("class", buttonClass);

		if (StringUtils.isNotEmpty(page) && !disabled) {
			Link link = resources.createPageLink(page,
					resources.isBound("context"), context);
			writer.attributes("href", buildHref(link));
		}
	}

	void afterRender(MarkupWriter writer) {
		writer.end();
	}

	private String buildHref(Link link) {
		String href = link.toURI();

		if (anchor == null)
			return href;

		return href + "#" + anchor;
	}
}
