package org.etb.app.base;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.bindings.LiteralBinding;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.etb.app.utils.StringUtils;

/**
 * 组件基础类，实现了ClientElement id的生成
 * 
 * @author Alex Huang
 * @email 102233492@qq.com
 */
public class BaseClientElement extends BaseInject implements ClientElement {

	@Parameter(name = "id", defaultPrefix = BindingConstants.LITERAL)
	protected String idParameter;

	@Persist(value = PersistenceConstants.CLIENT)
	private String persistenceClientId;

	protected String clientId;

	@SetupRender
	public final void baseSetupRender() {
		if (resources.isBound("id")) {
			clientId = idParameter;
		} else {
			clientId = jsSupport.allocateClientId(resources);
		}
		this.persistenceClientId = clientId;
	}

	@CleanupRender
	public final void baseClearRender() {
		if (!globalClientId())
			this.persistenceClientId = null;
	}

	public String getClientId() {
		return clientId != null ? clientId : persistenceClientId;
	}

	/**
	 * 可被覆写，component的clientId是否在全局可取
	 */
	protected boolean globalClientId() {
		return false;
	}

	protected static void checkIdInvariable(ComponentResources resources,
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
	protected static void checkNotNull(Object target, String msg) {
		if (target == null)
			throw new NullPointerException(msg);
	}

	/**
	 * 强制Element带上某个属性值
	 * 
	 * @param element
	 * @param name
	 * @param value
	 */
	protected void forceAddAttribute(Element element, String name, String value) {
		String nameValue = resources.getInformalParameter(name, String.class);
		String nameStr = value;
		if (nameValue != null) {
			nameStr += " " + nameValue;
		}
		element.forceAttributes(name, nameStr);
	}

	/**
	 * Element某个属性值如果为null，则定义为默认值
	 * 
	 * @param element
	 * @param name
	 * @param value
	 */
	protected void defaultAttribute(Element element, String name,
			String defaultValue) {
		String nameValue = resources.getInformalParameter(name, String.class);
		element.forceAttributes(name, nameValue == null ? defaultValue
				: nameValue);
	}
}
