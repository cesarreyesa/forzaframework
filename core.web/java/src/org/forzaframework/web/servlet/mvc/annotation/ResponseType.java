package org.forzaframework.web.servlet.mvc.annotation;

import java.lang.annotation.*;

/**
 * User: cesarreyes
 * Date: 05-dic-2007
 * Time: 9:54:53
 * Description:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseType {

	/**
	 * The primary mapping expressed by this annotation.
	 * <p>In a Servlet environment: the path mapping URLs (e.g. "/myPath.do")
	 * <p>In a Portlet environment: the mapped portlet modes (e.g. "EDIT")
	 * <p><b>Supported at the type level as well as at the method level!</b>
	 * When used at the type level, all method level mappings inherit
	 * this primary mapping, narrowing it for a specific handler method.
	 */
	ResponseTypes value() default ResponseTypes.JSON;

}

