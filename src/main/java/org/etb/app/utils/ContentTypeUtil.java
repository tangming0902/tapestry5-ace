package org.etb.app.utils;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeUtil {

	private static Map<String, String> contentTypes = new HashMap<String, String>();

	static {
		contentTypes.put(".xls", "application/vnd.ms-excel; charset=utf-8");
		contentTypes.put(".pdf", "application/pdf; charset=utf-8");
		contentTypes.put(".doc", "application/msword; charset=utf-8");
		contentTypes.put(".zip", "application/octet-stream");
		contentTypes.put(".jpg", "image/jpeg");
		contentTypes.put(".png", "image/png");
		contentTypes.put(".gif", "image/gif");
		contentTypes.put(".bmp", "image/bmp");
		contentTypes.put(".gif", "image/gif");
		contentTypes.put(".txt", "text/plain");
	}

	public static String getContentTypes(String fileType) {
		if (StringUtils.isNotEmpty(fileType)) {
			if (!fileType.startsWith("."))
				fileType = "." + fileType;

			String contextType = contentTypes.get(fileType);
			if (contextType != null) {
				return contextType;
			}
		}
		return "application/octet-stream";
	}

}
