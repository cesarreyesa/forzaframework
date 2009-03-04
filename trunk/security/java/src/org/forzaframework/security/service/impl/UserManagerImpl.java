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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.ModelMap;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.forzaframework.security.User;
import org.forzaframework.security.Role;
import org.forzaframework.security.service.UserManager;
import org.forzaframework.mail.MailEngine;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.core.persistance.Criteria;
import org.forzaframework.core.persistance.Restrictions;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
        return entityManager.get(User.class, new Criteria().add(Restrictions.eq("username", username)));
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
                user.setPassword(org.forzaframework.util.StringUtils.encodePassword(user.getPassword(), "SHA"));
            }
            entityManager.save(user);
        }
    }

    public void newUser(User user) {
        newUser(user, null);
    }

    public void newUser(User user, Map model) {
        String password = user.getPassword();
        user.setPassword(org.forzaframework.util.StringUtils.encodePassword(user.getPassword(), "SHA"));
        entityManager.save(user);

        if(mailMessage != null && mailEngine != null){
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Nuevo Usuario");
            if(model == null) model = new HashMap();
            model.put("user", user);
            model.put("password", password);
            mailEngine.sendMessage(mailMessage, "newUser.vm", model, false);
        }
    }

    public void removeUser(String username) {
        entityManager.delete(getUser(username));
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
}
