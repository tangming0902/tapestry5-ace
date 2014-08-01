package org.etb.app.utils;

import org.apache.tapestry5.services.Request;

public class BrowserUtil {

	public static final String USER_AGENT = "User-Agent";

	public static final String TAG_IE = "MSIE";

	public static final String TAG_WEIXIN = "MicroMessenger";

	public static int getIEVersion(String agent) {
		if (agent.indexOf(TAG_IE) == -1)
			return -1;

		String version = StringUtils.substringBetween(agent, TAG_IE, ".")
				.trim();
		try {
			return Integer.valueOf(version);
		} catch (Exception e) {
			return -1;
		}
	}

	public static boolean isWeiXinBrowser(String agent) {
		return agent.indexOf(TAG_WEIXIN) != -1;
	}

	public static String getUserAgent(Request request) {
		return request.getHeader(USER_AGENT);
	}
}
