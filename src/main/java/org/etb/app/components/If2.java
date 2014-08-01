package org.etb.app.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.mvel2.MVEL;

public class If2 extends AbstractConditional
{
    /**
     * If true, then the body of the If component is rendered. If false, the body is omitted.
     */
    @Parameter(required = true)
    private String test;

    /**
     * @return test parameter (if negate is false), or test parameter inverted (if negate is true)
     */
    protected boolean test()
    {
    	if (InternalUtils.isBlank(test)) return false;
    	
    	if (test.equals("false")) return false;
    	
    	return (Boolean) MVEL.eval(test);
    }

}
