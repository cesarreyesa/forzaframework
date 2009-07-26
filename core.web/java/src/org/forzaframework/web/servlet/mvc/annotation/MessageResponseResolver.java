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

import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.ui.ExtendedModelMap;
import org.forzaframework.web.servlet.view.XmlView;
import org.forzaframework.web.servlet.view.TextView;
import org.forzaframework.validation.Information;

import java.lang.reflect.Method;

/**
 *
 *
 * User: cesarreyes
 * Date: 14-jun-2009
 * Time: 23:29:37
 */
public class MessageResponseResolver implements ModelAndViewResolver {

    public ModelAndView resolveModelAndView(Method handlerMethod, Class handlerType, Object returnValue, ExtendedModelMap implicitModel, NativeWebRequest webRequest) {
        ResponseType responseType = handlerMethod.getAnnotation(ResponseType.class);
        if(responseType != null && returnValue == null){
            if(responseType.value().equals(ResponseTypes.XML)){
                return new XmlView(new Information().toXml());
            }
            else{
                return new TextView(new Information().toJSONString());
            }
        }
        return ModelAndViewResolver.UNRESOLVED;
    }
}
