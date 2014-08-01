package org.etb.app.base;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.SetupRender;

/**
 * 
 * @author Alex Huang
 * @email 102233492@qq.com
 */
@MixinAfter
public class BaseMixin extends BaseInject {

	@InjectContainer
	protected BaseClientElement clientElement;

	protected String clientId;

	@SetupRender
	public final void baseSetupRender() {
		clientId = clientElement.getClientId();
	}

	public String getClientId() {
		return clientId;
	}

}
