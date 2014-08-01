package org.etb.app.components.b;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.Asset2;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.etb.app.base.AbstractField;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.models.StringKV;
import org.etb.app.utils.StringUtils;

@Events(EtbEventConstants.UPLOAD_FILE)
public class Upload extends AbstractField {

	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private boolean formgroup;

	@SuppressWarnings("unused")
	@Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
	@Property
	private boolean col;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerClass;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String containerStyle;

	@Parameter(required = true, principal = true, autoconnect = true)
	private List<StringKV> value;

	/**
	 * example : jpg,png,gif
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String whitelist;

	/**
	 * 文件大小
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "500K")
	@Property
	private String limitSize;

	/**
	 * 文件数量大小，0为不限制
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "1")
	private int limit;

	@Parameter(defaultPrefix = BindingConstants.VALIDATE)
	private FieldValidator<Object> validate;

	@Environmental
	private ValidationTracker tracker;

	@Inject
	private FieldValidationSupport fieldValidationSupport;

	@Inject
	private ComponentDefaultProvider defaultProvider;

	@Inject
	@Path("res/upload/plupload.full.min.js")
	private Asset2 pluploadJs;

	@Inject
	@Path("res/upload/zh_CN.js")
	private Asset2 i18nJs;

	@Inject
	@Path("res/upload/Moxie.swf")
	private Asset2 swf;

	@Inject
	@Path("res/upload/Moxie.xap")
	private Asset2 xap;

	/* debug resources start */
	@SuppressWarnings("unused")
	@Inject
	@Path("res/upload/moxie.js")
	private Asset2 debugJs1;

	@SuppressWarnings("unused")
	@Inject
	@Path("res/upload/plupload.dev.js")
	private Asset2 debugJs2;
	/* debug resources end */

	@Inject
	private MultipartDecoder decoder;

	final Binding defaultValidate() {
		return defaultProvider.defaultValidatorBinding("value", resources);
	}

	void beginRender(MarkupWriter writer) {
		writer.element("div", "class", "uploader"
				+ (formgroup ? " form-group" : "")
				+ (containerClass == null ? "" : " " + containerClass),
				"style", containerStyle);
	}

	void afterRender(MarkupWriter writer) {
		writer.end();

		validate.render(writer);

		jsSupport.importJavaScriptLibrary(pluploadJs);
		jsSupport.importJavaScriptLibrary(i18nJs);

		// jsSupport.importJavaScriptLibrary(debugJs1);
		// jsSupport.importJavaScriptLibrary(debugJs2);

		JSONObject spec = new JSONObject();
		spec.put("clientId", getClientId());
		spec.put("whitelist", whitelist);
		spec.put("limitSize", limitSize);
		spec.put("limit", limit);
		spec.put("url", resources.createEventLink("uploadFile").toAbsoluteURI());
		spec.put("swfUrl", swf.toClientURL());
		spec.put("xapUrl", xap.toClientURL());

		if (value != null) {
			JSONArray array = new JSONArray();
			for (StringKV item : value) {
				JSONObject json = new JSONObject();
				json.put("id", item.getK());
				json.put("text", item.getV());
				array.put(json);
			}
			spec.put("data", array);
		}

		jsSupport.addInitializerCall("Upload", spec);
	}

	private String fileId;

	void onUploadFile() {
		UploadedFile uploaded = decoder.getFileUpload("file");
		ComponentEventCallback<String> callback = new ComponentEventCallback<String>() {
			public boolean handleResult(String result) {
				fileId = result;

				return true;
			}
		};
		resources.triggerEvent(EtbEventConstants.UPLOAD_FILE,
				new Object[] { uploaded }, callback);

		if (fileId == null) {
			throw new RuntimeException("上传文件事件必须返回文件id!");
		}
		JSONObject json = new JSONObject();
		json.put("fileId", fileId);

		HttpServletResponse response = requestGlobals.getHTTPServletResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.print(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			InternalUtils.close(writer);
		}
	}

	@Override
	protected void processSubmission(String controlName) {
		String idStr = request.getParameter(controlName);
		String textStr = request.getParameter(controlName + "_text");

		try {
			List<StringKV> rawValue = null;
			if (StringUtils.isNotEmpty(idStr)) {
				String[] idArr = idStr.split("\\?");
				String[] textArr = textStr.split("\\?");

				rawValue = new ArrayList<StringKV>();
				for (int i = 0; i < idArr.length; i++) {
					rawValue.add(StringKV.of(idArr[i], textArr[i]));
				}
			}

			fieldValidationSupport.validate(rawValue, resources, validate);

			this.value = rawValue;
		} catch (ValidationException ex) {
			tracker.recordError(this, ex.getMessage());
		}
	}
}
