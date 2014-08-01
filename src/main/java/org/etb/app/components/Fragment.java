package org.etb.app.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.internal.ComponentActionSink;
import org.apache.tapestry5.corelib.internal.FormSupportAdapter;
import org.apache.tapestry5.corelib.internal.HiddenFieldPositioner;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.ClientDataEncoder;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.HiddenFieldLocationRules;
import org.etb.app.base.BaseClientElement;
import org.slf4j.Logger;

/**
 * 类似于FormFragment的功能，区别在于： 1.可以放在非Form组件内部 2.删掉了所有的多余功能，仅作为隔断Form隐藏input用
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
@SupportsInformalParameters
public class Fragment extends BaseClientElement {

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String element;

	@Environmental
	private ClientBehaviorSupport clientBehaviorSupport;

	private ComponentActionSink componentActions;

	@Inject
	private Logger logger;

	@Inject
	private HiddenFieldLocationRules rules;

	private HiddenFieldPositioner hiddenFieldPositioner;

	@Inject
	private ClientDataEncoder clientDataEncoder;

	String defaultElement() {
		return resources.getElementName("div");
	}

	private boolean insideForm;

	void beginRender(MarkupWriter writer) {
		writer.element(this.element, "id", getClientId());

		resources.renderInformalParameters(writer);

		FormSupport formSupport = environment.peek(FormSupport.class);

		if (formSupport != null) {
			insideForm = true;

			hiddenFieldPositioner = new HiddenFieldPositioner(writer, rules);

			clientBehaviorSupport.addFormFragment(getClientId(), false, null,
					null, null);

			componentActions = new ComponentActionSink(logger,
					clientDataEncoder);

			FormSupport override = new FormSupportAdapter(formSupport) {
				@Override
				public <T> void store(T component, ComponentAction<T> action) {
					componentActions.store(component, action);
				}

				@Override
				public <T> void storeAndExecute(T component,
						ComponentAction<T> action) {
					componentActions.store(component, action);

					action.execute(component);
				}
			};
			environment.push(FormSupport.class, override);
		}

	}

	void afterRender(MarkupWriter writer) {
		if (insideForm) {
			hiddenFieldPositioner.getElement().attributes("type", "hidden",

			"name", EtbForm.FORM_DATA,

			"id", getClientId() + "-hidden",

			"value", componentActions.getClientData());

			environment.pop(FormSupport.class);
		}

		writer.end(); // div
	}
}
