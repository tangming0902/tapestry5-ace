package org.etb.app.services.ui.impl;

import java.util.UUID;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.services.PageRenderQueue;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.runtime.RenderQueue;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.etb.app.enums.BgColor;
import org.etb.app.services.ui.AjaxBehavior;
import org.etb.app.services.ui.ContentHanlder;
import org.etb.app.utils.StringUtils;

public class AjaxBehaviorImpl implements AjaxBehavior {

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private PageRenderQueue queue;

	@Inject
	private TypeCoercer typeCoercer;

	@Override
	public void notify(final BgColor color, final Object title,
			final Object text, final String imageUrl, final boolean sticky) {
		final JSONObject spec = new JSONObject();
		spec.put("sticky", sticky);

		if (color != null) {
			spec.put("color", color.value);
			spec.put("dark", color.dark);
		}

		if (imageUrl != null)
			spec.put("image", imageUrl);

		queue.addPartialMarkupRendererFilter(new PartialMarkupRendererFilter() {

			@Override
			public void renderMarkup(MarkupWriter writer,
					final JSONObject reply, PartialMarkupRenderer renderer) {

				if (title != null) {

					addPartialRenderer(title, new ContentHanlder() {

						@Override
						public void hander(String content) {
							spec.put("title", content);
						}

					});
				}

				if (text != null) {
					addPartialRenderer(text, new ContentHanlder() {

						@Override
						public void hander(String content) {
							spec.put("text", content);
						}

					});
				}

				renderer.renderMarkup(writer, reply);
			}

		});

		ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

			@Override
			public void run(JavaScriptSupport javascriptSupport) {
				javascriptSupport.addInitializerCall(
						InitializationPriority.EARLY, "GritterNotify", spec);
			}
		});
	}

	@Override
	public void notify(BgColor color, Object title, Object text, String imageUrl) {
		this.notify(color, title, text, imageUrl, false);
	}

	@Override
	public void notify(BgColor color, Object title, Object text) {
		this.notify(color, title, text, null);
	}

	@Override
	public void showDialog(String dialogId, final Object title,
			final Object content, final Object buttons) {
		if (StringUtils.isEmpty(dialogId)) {
			dialogId = "dialog" + UUID.randomUUID().toString().replace("-", "");
		}

		final JSONObject spec = new JSONObject();
		spec.put("clientId", dialogId);

		queue.addPartialMarkupRendererFilter(new PartialMarkupRendererFilter() {

			@Override
			public void renderMarkup(MarkupWriter writer,
					final JSONObject reply, PartialMarkupRenderer renderer) {

				if (title != null) {

					addPartialRenderer(title, new ContentHanlder() {

						@Override
						public void hander(String content) {
							spec.put("title", content);
						}

					});
				}

				if (content != null) {
					addPartialRenderer(content, new ContentHanlder() {

						@Override
						public void hander(String content) {
							spec.put("content", content);
						}

					});
				}

				if (buttons != null) {
					addPartialRenderer(buttons, new ContentHanlder() {

						@Override
						public void hander(String content) {
							spec.put("buttons", content);
						}

					});
				}

				renderer.renderMarkup(writer, reply);
			}

		});

		ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

			@Override
			public void run(JavaScriptSupport javascriptSupport) {
				javascriptSupport.addInitializerCall(
						InitializationPriority.EARLY, "jQDialog", spec);
			}
		});

	}

	@Override
	public void closeDialog(String dialogId) {
		final JSONObject spec = new JSONObject();
		spec.put("clientId", dialogId);
		ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

			@Override
			public void run(JavaScriptSupport javascriptSupport) {
				javascriptSupport.addInitializerCall(
						InitializationPriority.EARLY, "jQDialogClose", spec);
			}
		});
	}

	private void addPartialRenderer(final Object content,
			final ContentHanlder handler) {
		final RenderCommand renderCommand = typeCoercer.coerce(content,
				RenderCommand.class);
		RenderCommand tempContentRenderCommand = new RenderCommand() {
			public void render(MarkupWriter writer, RenderQueue queue) {

				final Element tempElement = writer.element("temp");

				queue.push(new RenderCommand() {
					public void render(MarkupWriter writer, RenderQueue queue) {
						writer.end();

						String titleContent = tempElement.getChildMarkup();

						if (StringUtils.isNotEmpty(titleContent)) {
							handler.hander(titleContent);
						}

						tempElement.remove();
					}
				});

				queue.push(renderCommand);
			}
		};

		queue.addPartialRenderer(tempContentRenderCommand);
	}

	@Override
	public void error(Object title, Object text) {
		notify(BgColor.RED, title, text);
	}

	@Override
	public void success(Object title, Object text) {
		notify(BgColor.GREEN, title, text);
	}

	@Override
	public void info(Object title, Object text) {
		notify(BgColor.ORANGE, title, text);
	}

}
