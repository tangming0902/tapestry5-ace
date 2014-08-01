package org.etb.app.utils;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.LiteralBinding;
import org.apache.tapestry5.ioc.AnnotationProvider;

public class ComponentUtil {

	/**
	 * 验证组件id值不可变
	 * 
	 * @param resources
	 * @param idValue
	 */
	public static void checkIdInvariable(ComponentResources resources,
			String idValue) {
		checkNotNull(resources, "ComponentResources参数不能为null！");

		AnnotationProvider provider = resources.getAnnotationProvider("id");
		checkNotNull(provider, "id参数没有被绑定！");

		if (!(provider instanceof LiteralBinding))
			throw new RuntimeException(String.format(
					"[t:id=%s]必须强指定id(绑定方式为literal)", resources.getId()));

		if (StringUtils.isEmpty(idValue))
			throw new RuntimeException(String.format("[t:id=%s]id值不能为空",
					resources.getId()));
	}

	/**
	 * 验证非null
	 * 
	 * @param target
	 *            验证对象
	 * @param msg
	 *            错误信息
	 */
	public static void checkNotNull(Object target, String msg) {
		if (target == null)
			throw new NullPointerException(msg);
	}
}
