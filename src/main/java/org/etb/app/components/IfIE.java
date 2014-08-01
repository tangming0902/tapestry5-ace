package org.etb.app.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;

/**
 * IE判断
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class IfIE {

	@Parameter(required = true, defaultPrefix = "literal", allowNull = false)
	private String test;

	private boolean notEquals;

	public void beginRender(MarkupWriter writer) {
		// write out a dummy tag to prevent T5 from writing it's script tags
		// 'inside' the IE comment!
		// writer.element("script", "type", "text/javascript");
		// writer.end();

		notEquals = "!IE".equals(test.trim());

		writer.writeRaw("<!--[if ");
		writer.writeRaw(test);

		if (notEquals) {
			writer.writeRaw("]> -->");
		} else {
			writer.writeRaw("]>");
		}
	}

	public void afterRender(MarkupWriter writer) {
		if (notEquals) {
			writer.writeRaw("<!-- ");
		}
		writer.writeRaw("<![endif]-->");
	}
}
