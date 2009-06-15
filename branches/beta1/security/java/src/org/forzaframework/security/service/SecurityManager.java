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

package org.forzaframework.security.service;

import org.springframework.security.Authentication;
import org.forzaframework.security.Permission;
import org.forzaframework.security.ActionDefinition;

import java.util.List;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 15:33:31
 */
public interface SecurityManager {

    List<String> getAuthorizedRolesForUrl(String url);

    List<String> getAuthorizedRolesForMethod(String clazz, String method);

    List<ActionDefinition> getActionDefinitions();

    void addPermission(Permission permission);

    void deletePermission(Permission permission);

    List<String> getAuthorizedRolesForMethod(String clazz, String method, String targetClass);

    Boolean hasPermission(Authentication authentication, String url);
}
