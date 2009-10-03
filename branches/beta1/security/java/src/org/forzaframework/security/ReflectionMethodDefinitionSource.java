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

package org.forzaframework.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.util.Assert;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aopalliance.intercept.MethodInvocation;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Method;

/**
 * @author cesar.reyes
 *         Date: 22/10/2008
 *         Time: 10:17:50 AM
 */
public class ReflectionMethodDefinitionSource {}
//extends AbstractMethodDefinitionSource {
//
//    protected static Log logger = LogFactory.getLog(ReflectionMethodDefinitionSource.class);
//    private org.forzaframework.security.service.SecurityManager securityManager;
//
//    public void setSecurityManager(org.forzaframework.security.service.SecurityManager securityManager) {
//        this.securityManager = securityManager;
//    }
//
//    public ConfigAttributeDefinition getAttributes(Object object)
//        throws IllegalArgumentException {
//        Assert.notNull(object, "Object cannot be null");
//
//        if (object instanceof MethodInvocation) {
//            return this.lookupAttributes(((MethodInvocation) object));
//        }
//        return super.getAttributes(object);
//    }
//
//    protected ConfigAttributeDefinition lookupAttributes(MethodInvocation methodInvocation) {
//        try {
//            Method method = methodInvocation.getMethod();
//            logger.debug("autenticating method: " + method.getName());
//            List<String> roles = null;
//            if(method.getDeclaringClass().getName().equals("com.nopalsoft.service.Manager")){
//                if(methodInvocation.getArguments().length > 0){
//                    if(methodInvocation.getArguments()[0] instanceof Class){
//                        String targetClass = ((Class) methodInvocation.getArguments()[0]).getName();
//                        roles = securityManager.getAuthorizedRolesForMethod(method.getDeclaringClass().getName(), method.getName(), targetClass);
//                    }
//                }
//            }else{
//                roles = securityManager.getAuthorizedRolesForMethod(method.getDeclaringClass().getName(), method.getName());
//            }
//            if(roles != null && roles.size() > 0){
//            	List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
//                for(String role : roles){
//                	configAttributes.add(new SecurityConfig(role));
//                }
//                return new ConfigAttributeDefinition(configAttributes);
//            }
//            return null;
//        }
//        catch (IncorrectResultSizeDataAccessException ex) {
//            return null;
//        }
//
//    }
//
//    protected ConfigAttributeDefinition lookupAttributes(Method method) {
//        try {
//            logger.debug("autenticating method: " + method.getName());
//            List<String> roles = null;
//            if(method.getDeclaringClass().getName().equals("com.nopalsoft.service.Manager")){
////                if(methodInvocation.getArguments().length > 0){
////                    roles = securityManager.getAuthorizedRolesForMethod(method.getDeclaringClass().getName(), method.getName(), methodInvocation.getArguments()[0].getClass().getName());
////                }
//            }else{
//                roles = securityManager.getAuthorizedRolesForMethod(method.getDeclaringClass().getName(), method.getName());
//            }
//            if(roles != null && roles.size() > 0){
//            	List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
//                for(String role : roles){
//                	configAttributes.add(new SecurityConfig(role));
//                }
//                return new ConfigAttributeDefinition(configAttributes);
//            }
//            return null;
//        }
//        catch (IncorrectResultSizeDataAccessException ex) {
//            return null;
//        }
//
//    }
//
//
//	public ConfigAttributeDefinition getAttributes(Method method,
//			Class targetClass) {
//		return null;
//	}
//
//	public Collection getConfigAttributeDefinitions() {
//		return null;
//	}
//}
