/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.web.servlet.mvc.annotation;

import java.lang.annotation.*;

/**
 * Es usado por {@link org.forzaframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter} y por {@link @link org.forzaframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter}
 *
 * @see org.forzaframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter
 *
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

