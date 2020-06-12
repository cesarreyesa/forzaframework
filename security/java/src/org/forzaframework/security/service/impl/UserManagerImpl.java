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

package org.forzaframework.security.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.forzaframework.security.User;
import org.forzaframework.security.Role;
import org.forzaframework.security.service.UserManager;
import org.forzaframework.mail.MailEngine;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.core.persistance.Criteria;
import org.forzaframework.core.persistance.Restrictions;

import java.util.*;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 15:01:58
 */
public class UserManagerImpl implements UserManager, UserDetailsService {

    private SimpleMailMessage mailMessage;
    private MailEngine mailEngine;
    private EntityManager entityManager;

    public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User getUser(String username) {
        User user = entityManager.get(User.class, new Criteria().add(Restrictions.eq("username", username)));
        // esto es para evitar lazyInitEx
        for (Role role : user.getRoles()) {
            role.toString();
        }
        return user;
    }

    public User getUser(Long id) {
        return entityManager.get(User.class, id);
    }

    public List getUsers(User user) {
        return entityManager.getAll(User.class);
    }

    public void saveUser(User user) {
        if(user.getId() == null){
            newUser(user);
        }else{
            User originalUser = (User) entityManager.get(User.class, user.getId(), true);
            if(!originalUser.getPassword().equals(user.getPassword())){
                user.setPassword("{SHA-1}" + org.forzaframework.util.StringUtils.encodePassword(user.getPassword(), "SHA"));
            }
            entityManager.save(user);
        }
    }

    public void addRoleToUser(User user, Role role){
        user.addRole(role);
        entityManager.save(user);
    }

    public void newUser(User user) {
        newUser(user, null);
    }

    public void newUser(User user, Map model) {
        String password = user.getPassword();
        user.setPassword("{SHA-1}" + org.forzaframework.util.StringUtils.encodePassword(user.getPassword(), "SHA"));
        entityManager.save(user);

        if(mailMessage != null && mailEngine != null){
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Nuevo Usuario");
            if(model == null) model = new HashMap();
            model.put("user", user);
            model.put("password", password);
            mailEngine.sendMessage(mailMessage, "newUser.txt", model, false);
        }
    }

    public void removeUser(String username) {
        entityManager.delete(getUser(username));
    }

    public void removeUser(Long id) {
        entityManager.delete(getUser(id));
    }

    public List getRoles() {
        return entityManager.getAll(Role.class);
    }

    public Role getRole(String rolename) {
        return entityManager.get(Role.class, rolename);
    }

    public void saveRole(Role role) {
        entityManager.save(role);
    }

    public void removeRole(String rolename) {
        entityManager.delete(getRole(rolename));
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        try {
            return getUser(username);
        } catch (ObjectRetrievalFailureException e) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        }
    }

    public List<User> getUsersByRole(String roleName) {
      String hql = "select user from User as user join user.roles as role where role.name = ?0";

      return entityManager.find(hql, roleName);
    }

    public List<String> getRolesAsList(Set<Role> roles) {
        List <String> rolesAsList = new ArrayList<String>();
        for(Role role : roles){
            rolesAsList.add(role.getName());
        }
        return rolesAsList;
    }

    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRolesAsList(roles));
        return authList;
    }

}
