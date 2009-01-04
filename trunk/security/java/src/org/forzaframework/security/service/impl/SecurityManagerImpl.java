package org.forzaframework.security.service.impl;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.forzaframework.security.Permission;
import org.forzaframework.security.ActionDefinition;
import org.forzaframework.security.service.SecurityManager;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.core.persistance.Criteria;
import org.forzaframework.core.persistance.Restrictions;
import org.forzaframework.core.persistance.Dao;

import java.util.List;
import java.util.ArrayList;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 15:38:22
 */
public class SecurityManagerImpl implements SecurityManager {

    private Dao dao;

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public List<String> getAuthorizedRolesForUrl(String url){
        Criteria criteria = new Criteria().add(Restrictions.eq("url", url));
        List<ActionDefinition> permissions = dao.find(ActionDefinition.class, criteria);

        List<String> roles = new ArrayList<String>();
        if(permissions.size() == 0){
            return null;
        }
        for(ActionDefinition actionDefinition : permissions){
            for(Permission permission : actionDefinition.getPermissions()){
                roles.add(permission.getRole().getName());
            }
        }
        return roles;
    }

    public List<String> getAuthorizedRolesForMethod(String clazz, String method) {

        Criteria criteria = new Criteria()
                .add(Restrictions.eq("classes.className", clazz))
                .add(Restrictions.eq("classes.methodName", method));

        List<ActionDefinition> permissions = dao.find(ActionDefinition.class, criteria);

        List<String> roles = new ArrayList<String>();
        for(ActionDefinition actionDefinition : permissions){
            for(Permission permission : actionDefinition.getPermissions()){
                roles.add(permission.getRole().getName());
            }
        }
        return roles;
    }

    public List<String> getAuthorizedRolesForMethod(String managerClass, String method, String targetClass) {
        Criteria criteria = new Criteria()
                .add(Restrictions.eq("className", managerClass))
                .add(Restrictions.eq("methodName", method))
                .add(Restrictions.eq("targetClass", targetClass));

        List<ActionDefinition> permissions = dao.find(ActionDefinition.class, criteria);

        List<String> roles = new ArrayList<String>();
        for(ActionDefinition actionDefinition : permissions){
            for(Permission permission : actionDefinition.getPermissions()){
                roles.add(permission.getRole().getName());
            }
        }
        return roles;
    }

    public List<ActionDefinition> getActionDefinitions() {
        return dao.getAll(ActionDefinition.class);
    }

    public void addPermission(Permission permission) {
        dao.save(permission);
    }

    public void deletePermission(Permission permission) {
        dao.delete(permission);
    }

    public Boolean hasPermission(Authentication authentication, String url){
        List<String> roles = this.getAuthorizedRolesForUrl(url);
        if(roles == null) return true;
        for(GrantedAuthority authority : authentication.getAuthorities()){
            for(String role : roles){
                if(role.equals(authority.getAuthority())){
                    return true;
                }
            }
        }
        return false;
    }
}
