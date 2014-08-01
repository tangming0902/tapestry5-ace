package org.etb.app.annotations;

import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.COMPONENT;
import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.MIXIN;
import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.PAGE;
import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.SERVICE;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.tapestry5.ioc.annotations.UseWith;

/**
 * @CatchRequestParameter 仅用于接收Request参数<br>
 * 与 @ActivationRequestParameter 相比，参数将不会持久化到URL上<br>
 * <b>现在可以用在Page,Component,Mixin上<b>
 * <b>注意:被标记的field不可以是基础数据类型，并且是只读的。<b>
 * @author Alex Huang
 * @email 102233492@qq.com
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@UseWith({ COMPONENT, MIXIN, PAGE, SERVICE })
public @interface CatchRequestParameter {
	
    /** The name of the query parameter, which defaults to the name of the field. */
	String value() default "";

}
