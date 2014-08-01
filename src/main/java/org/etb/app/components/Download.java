package org.etb.app.components;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.etb.app.base.BaseInject;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.utils.ContentTypeUtil;

/**
 * 下载组件
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
@Events({ EtbEventConstants.DOWNLOAD + "下载事件，返回参数必须为InputStream" })
public class Download extends BaseInject {

	public static final String IFRAME_ID = "_iframe_download";

	void beginRender() {
		JSONObject spec = new JSONObject();
		spec.put("iframeId", IFRAME_ID);
		jsSupport.addInitializerCall("Download", spec);
	}

	public void ajaxTriggerDownload(String fileName, Object... serializeObjects) {
		if (fileName == null)
			throw new RuntimeException("fileName不能为null");

		int size = 1;
		if (serializeObjects != null) {
			size = serializeObjects.length + 1;
		}
		Object[] context = new Object[size];
		context[0] = fileName;

		if (serializeObjects != null) {
			int index = 1;
			for (Object so : serializeObjects) {
				context[index++] = so;
			}
		}

		Link link = resources.createEventLink("downloadEvent", context);
		final JSONObject spec = new JSONObject();
		spec.put("iframeId", IFRAME_ID);
		spec.put("url", link.toRedirectURI());
		ajaxResponseRenderer.addCallback(new JavaScriptCallback() {

			public void run(JavaScriptSupport javascriptSupport) {
				javascriptSupport.addInitializerCall("DownloadTrigger", spec);
			}
		});
	}

	Object onDownloadEvent(Object[] object) {
		final String fileName = (String) object[0];
		final String fileType = fileName.substring(fileName.lastIndexOf('.'));
		final String contentType = ContentTypeUtil.getContentTypes(fileType);

		Object[] context = new Object[object.length - 1];
		for (int i = 1; i < object.length; i++) {
			context[i - 1] = object[i];
		}

		CaptureResultCallback<InputStream> callback = new CaptureResultCallback<InputStream>();
		resources.triggerEvent(EtbEventConstants.DOWNLOAD, context, callback);

		final InputStream in = callback.getResult();
		if (in == null)
			return null;

		return new StreamResponse() {

			public void prepareResponse(Response response) {
				String name;
				try {
					name = new String(fileName.getBytes(), "ISO8859-1");
				} catch (UnsupportedEncodingException e) {
					name = fileName;
				}
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + name + "\"");
			}

			public InputStream getStream() throws IOException {
				return in;
			}

			public String getContentType() {
				return contentType;
			}
		};
	}
}
