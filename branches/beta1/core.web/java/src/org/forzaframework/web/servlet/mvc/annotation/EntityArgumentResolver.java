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

import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.forzaframework.core.persistance.EntityManager;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

/**
 * User: cesarreyes
 * Date: 25-jun-2009
 * Time: 23:02:40
 */
@SuppressWarnings({"unchecked"})
public class EntityArgumentResolver implements WebArgumentResolver {

    @Autowired
    private EntityManager em;

    private WebBindingInitializer bindingInitializer;

    public WebBindingInitializer getBindingInitializer() {
        return bindingInitializer;
    }

    public void setBindingInitializer(WebBindingInitializer bindingInitializer) {
        this.bindingInitializer = bindingInitializer;
    }

    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest request) throws Exception {
        if(methodParameter.getParameterType().getAnnotation(Entity.class) != null){
            Object bean;
            if(StringUtils.isNotBlank(request.getParameter("id"))){
                bean = em.get(methodParameter.getParameterType(), Long.valueOf(request.getParameter("id")));
            }else{
                bean = BeanUtils.instantiateClass(methodParameter.getParameterType());
            }
            WebRequestDataBinder binder = new WebRequestDataBinder(bean);
            if (this.bindingInitializer != null) {
                this.bindingInitializer.initBinder(binder, request);
            }
            ServletWebRequest webRequest = new ServletWebRequest((HttpServletRequest) request.getNativeRequest());
            binder.bind(webRequest);
            return bean;
        }
        
        return WebArgumentResolver.UNRESOLVED;
    }
}
