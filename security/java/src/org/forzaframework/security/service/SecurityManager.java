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
