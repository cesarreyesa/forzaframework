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

package org.forzaframework.web.servlet.mvc.annotation;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.metadata.SystemConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.validation.Validator;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.ControllerAdviceBean;
import org.forzaframework.web.bind.annotation.support.HandlerMethod;
import org.springframework.web.method.annotation.ErrorsMethodArgumentResolver;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.annotation.ModelMethodProcessor;
import org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.annotation.SessionAttributesHandler;
import org.springframework.web.method.annotation.SessionStatusMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
import org.springframework.web.servlet.mvc.method.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

/**
 * Extension of {@link AbstractHandlerMethodAdapter} that supports
 * {@link RequestMapping @RequestMapping} annotated {@link HandlerMethod HandlerMethods}.
 *
 * <p>Support for custom argument and return value types can be added via
 * {@link #setCustomArgumentResolvers} and {@link #setCustomReturnValueHandlers},
 * or alternatively, to re-configure all argument and return value types,
 * use {@link #setArgumentResolvers} and {@link #setReturnValueHandlers}.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @since 3.1
 * @see HandlerMethodArgumentResolver
 * @see HandlerMethodReturnValueHandler
 */
public abstract class RequestMappingHandlerAdapter extends org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter {

	/**
	 * MethodFilter that matches {@link InitBinder @InitBinder} methods.
	 */
	public static final MethodFilter INIT_BINDER_METHODS = method ->
			AnnotatedElementUtils.hasAnnotation(method, InitBinder.class);

	/**
	 * MethodFilter that matches {@link ModelAttribute @ModelAttribute} methods.
	 */
	public static final MethodFilter MODEL_ATTRIBUTE_METHODS = method ->
			(!AnnotatedElementUtils.hasAnnotation(method, RequestMapping.class) &&
					AnnotatedElementUtils.hasAnnotation(method, ModelAttribute.class));


	@Nullable
	private List<HandlerMethodArgumentResolver> customArgumentResolvers;

	@Nullable
	private HandlerMethodArgumentResolverComposite argumentResolvers;

	@Nullable
	private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;

	@Nullable
	private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;

	@Nullable
	private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

	@Nullable
	private List<ModelAndViewResolver> modelAndViewResolvers;

	private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();

	private List<HttpMessageConverter<?>> messageConverters;

	private List<Object> requestResponseBodyAdvice = new ArrayList<>();

	@Nullable
	private WebBindingInitializer webBindingInitializer;

	private AsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("MvcAsync");

	@Nullable
	private Long asyncRequestTimeout;

	private CallableProcessingInterceptor[] callableInterceptors = new CallableProcessingInterceptor[0];

	private DeferredResultProcessingInterceptor[] deferredResultInterceptors = new DeferredResultProcessingInterceptor[0];

	private ReactiveAdapterRegistry reactiveAdapterRegistry = ReactiveAdapterRegistry.getSharedInstance();

	private boolean ignoreDefaultModelOnRedirect = false;

	private int cacheSecondsForSessionAttributeHandlers = 0;

	private boolean synchronizeOnSession = false;

	private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();

	private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	@Nullable
	private ConfigurableBeanFactory beanFactory;


	private final Map<Class<?>, SessionAttributesHandler> sessionAttributesHandlerCache = new ConcurrentHashMap<>(64);

	private final Map<Class<?>, Set<Method>> initBinderCache = new ConcurrentHashMap<>(64);

	private final Map<ControllerAdviceBean, Set<Method>> initBinderAdviceCache = new LinkedHashMap<>();

	private final Map<Class<?>, Set<Method>> modelAttributeCache = new ConcurrentHashMap<>(64);

	private final Map<ControllerAdviceBean, Set<Method>> modelAttributeAdviceCache = new LinkedHashMap<>();

	/// FORZA ///
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	private SystemConfiguration systemConfiguration;

	public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
		this.systemConfiguration = systemConfiguration;
	}

	private Validator[] validators;

	public void setValidators(Validator[] validators) {
		this.validators = validators;
	}
	/// FORZA ///


	private void initControllerAdviceCache() {
		if (getApplicationContext() == null) {
			return;
		}

		List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());

		List<Object> requestResponseBodyAdviceBeans = new ArrayList<>();

		for (ControllerAdviceBean adviceBean : adviceBeans) {
			Class<?> beanType = adviceBean.getBeanType();
			if (beanType == null) {
				throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
			}
			Set<Method> attrMethods = MethodIntrospector.selectMethods(beanType, MODEL_ATTRIBUTE_METHODS);
			if (!attrMethods.isEmpty()) {
				this.modelAttributeAdviceCache.put(adviceBean, attrMethods);
			}
			Set<Method> binderMethods = MethodIntrospector.selectMethods(beanType, INIT_BINDER_METHODS);
			if (!binderMethods.isEmpty()) {
				this.initBinderAdviceCache.put(adviceBean, binderMethods);
			}
			if (RequestBodyAdvice.class.isAssignableFrom(beanType) || ResponseBodyAdvice.class.isAssignableFrom(beanType)) {
				requestResponseBodyAdviceBeans.add(adviceBean);
			}
		}

		if (!requestResponseBodyAdviceBeans.isEmpty()) {
			this.requestResponseBodyAdvice.addAll(0, requestResponseBodyAdviceBeans);
		}

		if (logger.isDebugEnabled()) {
			int modelSize = this.modelAttributeAdviceCache.size();
			int binderSize = this.initBinderAdviceCache.size();
			int reqCount = getBodyAdviceCount(RequestBodyAdvice.class);
			int resCount = getBodyAdviceCount(ResponseBodyAdvice.class);
			if (modelSize == 0 && binderSize == 0 && reqCount == 0 && resCount == 0) {
				logger.debug("ControllerAdvice beans: none");
			}
			else {
				logger.debug("ControllerAdvice beans: " + modelSize + " @ModelAttribute, " + binderSize +
						" @InitBinder, " + reqCount + " RequestBodyAdvice, " + resCount + " ResponseBodyAdvice");
			}
		}
	}

	// Count all advice, including explicit registrations..

	private int getBodyAdviceCount(Class<?> adviceType) {
		List<Object> advice = this.requestResponseBodyAdvice;
		return RequestBodyAdvice.class.isAssignableFrom(adviceType) ?
				getAdviceByType(advice, RequestBodyAdvice.class).size() :
				getAdviceByType(advice, ResponseBodyAdvice.class).size();
	}

	private static <T> List<T> getAdviceByType(@Nullable List<Object> requestResponseBodyAdvice, Class<T> adviceType) {
		if (requestResponseBodyAdvice != null) {
			List<T> result = new ArrayList<>();
			for (Object advice : requestResponseBodyAdvice) {
				Class<?> beanType = (advice instanceof ControllerAdviceBean ?
						((ControllerAdviceBean) advice).getBeanType() : advice.getClass());
				if (beanType != null && adviceType.isAssignableFrom(beanType)) {
					result.add((T) advice);
				}
			}
			return result;
		}
		return Collections.emptyList();
	}

	/**
	 * Return the list of argument resolvers to use including built-in resolvers
	 * and custom resolvers provided via {@link #setCustomArgumentResolvers}.
	 */
	private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

		// Annotation-based argument resolution
		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
		resolvers.add(new RequestParamMapMethodArgumentResolver());
		resolvers.add(new PathVariableMethodArgumentResolver());
		resolvers.add(new PathVariableMapMethodArgumentResolver());
		resolvers.add(new MatrixVariableMethodArgumentResolver());
		resolvers.add(new MatrixVariableMapMethodArgumentResolver());
		resolvers.add(new ServletModelAttributeMethodProcessor(false));
		resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
		resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters(), this.requestResponseBodyAdvice));
		resolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
		resolvers.add(new RequestHeaderMapMethodArgumentResolver());
		resolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
		resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
		resolvers.add(new SessionAttributeMethodArgumentResolver());
		resolvers.add(new RequestAttributeMethodArgumentResolver());

		// Type-based argument resolution
		resolvers.add(new ServletRequestMethodArgumentResolver());
		resolvers.add(new ServletResponseMethodArgumentResolver());
		resolvers.add(new HttpEntityMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
		resolvers.add(new RedirectAttributesMethodArgumentResolver());
		resolvers.add(new ModelMethodProcessor());
		resolvers.add(new MapMethodProcessor());
		resolvers.add(new ErrorsMethodArgumentResolver());
		resolvers.add(new SessionStatusMethodArgumentResolver());
		resolvers.add(new UriComponentsBuilderMethodArgumentResolver());

		// Custom arguments
		if (getCustomArgumentResolvers() != null) {
			resolvers.addAll(getCustomArgumentResolvers());
		}

		// Catch-all
		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
		resolvers.add(new ServletModelAttributeMethodProcessor(true));

		return resolvers;
	}

	/**
	 * Return the list of argument resolvers to use for {@code @InitBinder}
	 * methods including built-in and custom resolvers.
	 */
	private List<HandlerMethodArgumentResolver> getDefaultInitBinderArgumentResolvers() {
		List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

		// Annotation-based argument resolution
		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
		resolvers.add(new RequestParamMapMethodArgumentResolver());
		resolvers.add(new PathVariableMethodArgumentResolver());
		resolvers.add(new PathVariableMapMethodArgumentResolver());
		resolvers.add(new MatrixVariableMethodArgumentResolver());
		resolvers.add(new MatrixVariableMapMethodArgumentResolver());
		resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
		resolvers.add(new SessionAttributeMethodArgumentResolver());
		resolvers.add(new RequestAttributeMethodArgumentResolver());

		// Type-based argument resolution
		resolvers.add(new ServletRequestMethodArgumentResolver());
		resolvers.add(new ServletResponseMethodArgumentResolver());

		// Custom arguments
		if (getCustomArgumentResolvers() != null) {
			resolvers.addAll(getCustomArgumentResolvers());
		}

		// Catch-all
		resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));

		return resolvers;
	}

	/**
	 * Return the list of return value handlers to use including built-in and
	 * custom handlers provided via {@link #setReturnValueHandlers}.
	 */
	private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();

		// Single-purpose return value types
		handlers.add(new ModelAndViewMethodReturnValueHandler());
		handlers.add(new ModelMethodProcessor());
		handlers.add(new ViewMethodReturnValueHandler());
		handlers.add(new ResponseBodyEmitterReturnValueHandler(getMessageConverters(),
				this.reactiveAdapterRegistry, this.taskExecutor, this.contentNegotiationManager));
		handlers.add(new StreamingResponseBodyReturnValueHandler());
		handlers.add(new HttpEntityMethodProcessor(getMessageConverters(),
				this.contentNegotiationManager, this.requestResponseBodyAdvice));
		handlers.add(new HttpHeadersReturnValueHandler());
		handlers.add(new CallableMethodReturnValueHandler());
		handlers.add(new DeferredResultMethodReturnValueHandler());
		handlers.add(new AsyncTaskMethodReturnValueHandler(this.beanFactory));

		// Annotation-based return value types
		handlers.add(new ModelAttributeMethodProcessor(false));
		handlers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(),
				this.contentNegotiationManager, this.requestResponseBodyAdvice));

		// Multi-purpose return value types
		handlers.add(new ViewNameMethodReturnValueHandler());
		handlers.add(new MapMethodProcessor());

		// Custom return value types
		if (getCustomReturnValueHandlers() != null) {
			handlers.addAll(getCustomReturnValueHandlers());
		}

		// Catch-all
		if (!CollectionUtils.isEmpty(getModelAndViewResolvers())) {
			handlers.add(new ModelAndViewResolverMethodReturnValueHandler(getModelAndViewResolvers()));
		}
		else {
			handlers.add(new ModelAttributeMethodProcessor(true));
		}

		return handlers;
	}

	/**
	 * Return the {@link SessionAttributesHandler} instance for the given handler type
	 * (never {@code null}).
	 */
	private SessionAttributesHandler getSessionAttributesHandler(HandlerMethod handlerMethod) {
		Class<?> handlerType = handlerMethod.getBeanType();
		SessionAttributesHandler sessionAttrHandler = this.sessionAttributesHandlerCache.get(handlerType);
		if (sessionAttrHandler == null) {
			synchronized (this.sessionAttributesHandlerCache) {
				sessionAttrHandler = this.sessionAttributesHandlerCache.get(handlerType);
				if (sessionAttrHandler == null) {
					sessionAttrHandler = new SessionAttributesHandler(handlerType, this.sessionAttributeStore);
					this.sessionAttributesHandlerCache.put(handlerType, sessionAttrHandler);
				}
			}
		}
		return sessionAttrHandler;
	}

	private ModelFactory getModelFactory(HandlerMethod handlerMethod, WebDataBinderFactory binderFactory) {
		SessionAttributesHandler sessionAttrHandler = getSessionAttributesHandler(handlerMethod);
		Class<?> handlerType = handlerMethod.getBeanType();
		Set<Method> methods = this.modelAttributeCache.get(handlerType);
		if (methods == null) {
			methods = MethodIntrospector.selectMethods(handlerType, MODEL_ATTRIBUTE_METHODS);
			this.modelAttributeCache.put(handlerType, methods);
		}
		List<InvocableHandlerMethod> attrMethods = new ArrayList<>();
		// Global methods first
		this.modelAttributeAdviceCache.forEach((clazz, methodSet) -> {
			if (clazz.isApplicableToBeanType(handlerType)) {
				Object bean = clazz.resolveBean();
				for (Method method : methodSet) {
					attrMethods.add(createModelAttributeMethod(binderFactory, bean, method));
				}
			}
		});
		for (Method method : methods) {
			Object bean = handlerMethod.getBean();
			attrMethods.add(createModelAttributeMethod(binderFactory, bean, method));
		}
		return new ModelFactory(attrMethods, binderFactory, sessionAttrHandler);
	}

	private InvocableHandlerMethod createModelAttributeMethod(WebDataBinderFactory factory, Object bean, Method method) {
		InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(bean, method);
		if (this.argumentResolvers != null) {
			attrMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
		}
		attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
		attrMethod.setDataBinderFactory(factory);
		return attrMethod;
	}

	private WebDataBinderFactory getDataBinderFactory(HandlerMethod handlerMethod) throws Exception {
		Class<?> handlerType = handlerMethod.getBeanType();
		Set<Method> methods = this.initBinderCache.get(handlerType);
		if (methods == null) {
			methods = MethodIntrospector.selectMethods(handlerType, INIT_BINDER_METHODS);
			this.initBinderCache.put(handlerType, methods);
		}
		List<InvocableHandlerMethod> initBinderMethods = new ArrayList<>();
		// Global methods first
		this.initBinderAdviceCache.forEach((clazz, methodSet) -> {
			if (clazz.isApplicableToBeanType(handlerType)) {
				Object bean = clazz.resolveBean();
				for (Method method : methodSet) {
					initBinderMethods.add(createInitBinderMethod(bean, method));
				}
			}
		});
		for (Method method : methods) {
			Object bean = handlerMethod.getBean();
			initBinderMethods.add(createInitBinderMethod(bean, method));
		}
		return createDataBinderFactory(initBinderMethods);
	}

	private InvocableHandlerMethod createInitBinderMethod(Object bean, Method method) {
		InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(bean, method);
		if (this.initBinderArgumentResolvers != null) {
			binderMethod.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);
		}
		binderMethod.setDataBinderFactory(new DefaultDataBinderFactory(this.webBindingInitializer));
		binderMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
		return binderMethod;
	}

	@Nullable
	private ModelAndView getModelAndView(ModelAndViewContainer mavContainer,
			ModelFactory modelFactory, NativeWebRequest webRequest) throws Exception {

		modelFactory.updateModel(webRequest, mavContainer);
		if (mavContainer.isRequestHandled()) {
			return null;
		}
		ModelMap model = mavContainer.getModel();
		ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model, mavContainer.getStatus());
		if (!mavContainer.isViewReference()) {
			mav.setView((View) mavContainer.getView());
		}
		if (model instanceof RedirectAttributes) {
			Map<String, ?> flashAttributes = ((RedirectAttributes) model).getFlashAttributes();
			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
			if (request != null) {
				RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
			}
		}
		return mav;
	}

}
