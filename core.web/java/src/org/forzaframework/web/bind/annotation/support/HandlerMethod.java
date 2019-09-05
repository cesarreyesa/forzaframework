/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.web.bind.annotation.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.metadata.SystemConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Encapsulates information about a handler method consisting of a
 * {@linkplain #getMethod() method} and a {@linkplain #getBean() bean}.
 * Provides convenient access to method parameters, the method return value,
 * method annotations, etc.
 *
 * <p>The class may be created with a bean instance or with a bean name
 * (e.g. lazy-init bean, prototype bean). Use {@link #createWithResolvedBean()}
 * to obtain a {@code HandlerMethod} instance with a bean instance resolved
 * through the associated {@link BeanFactory}.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 3.1
 */
public class HandlerMethod extends org.springframework.web.method.HandlerMethod {

	/// FORZA ///
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private SystemConfiguration systemConfiguration;

	public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
		this.systemConfiguration = systemConfiguration;
	}

	protected Validator[] validators;

	public void setValidators(Validator[] validators) {
		this.validators = validators;
	}
	/// FORZA ///

	/**
	 * Create an instance from a bean instance and a method.
	 */
	public HandlerMethod(Object bean, Method method) {
		super(bean, method);
	}

	/**
	 * Create an instance from a bean instance, method name, and parameter types.
	 * @throws NoSuchMethodException when the method cannot be found
	 */
	public HandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
		super(bean, methodName, parameterTypes);
	}

	/**
	 * Create an instance from a bean name, a method, and a {@code BeanFactory}.
	 * The method {@link #createWithResolvedBean()} may be used later to
	 * re-create the {@code HandlerMethod} with an initialized bean.
	 */
	public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
		super(beanName, beanFactory, method);
	}

	/**
	 * Copy constructor for use in subclasses.
	 */
	protected HandlerMethod(HandlerMethod handlerMethod) {
		super(handlerMethod);
	}

}
