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

package org.forzaframework.web.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.forzaframework.security.Role;
import org.forzaframework.security.User;
import org.forzaframework.security.service.UserManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 *
 * <p>Keep in mind that this listener is executed outside of OpenSessionInViewFilter,
 * so if you're using Hibernate you'll have to explicitly initialize all loaded data at the
 * Dao or service level to avoid LazyInitializationException. Hibernate.initialize() works
 * well for doing this.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *
 * @web.listener
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 17:18:31
 */
public class StartupListener extends ContextLoaderListener
    implements ServletContextListener {

    private static final Logger log = LogManager.getLogger(StartupListener.class);

    public void contextInitialized(ServletContextEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("initializing context...");
        }

        // call Spring's context ContextLoaderListener to initialize
        // all the context files specified in web.xml
        super.contextInitialized(event);

        ServletContext context = event.getServletContext();

        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);


        UserManager userManager = (UserManager) ctx.getBean("userManager");
        List users = userManager.getUsers(null);
        if(users.size() == 0){
            Role adminRole = null;
            List roles = userManager.getRoles();
            if(roles.size() == 0){
                adminRole = new Role();
                adminRole.setName("role_admin");
                adminRole.setDescription("Administrator role");
                userManager.saveRole(adminRole);
                log.debug("adding role admin");
            }else {
                for(Object object : roles){
                    Role role = (Role) object;
                    if(role.getName().equals("role_admin")){
                        adminRole = role;
                        break;
                    }
                }
            }
            User user = new User();
            user.setNew(true);
            user.setUsername("admin");
            user.setClient("demo");
            user.setPassword("chichen");
            user.setFirstName("Administrator");
            user.setLastName("Admin");
            user.setEmail("info@nopalsoft.net");
            user.setPasswordHint("tip");
            user.setEnabled(true);
            user.setAccountExpired(false);
            user.setAccountLocked(false);
            user.setCredentialsExpired(false);
            user.addRole(adminRole);

            userManager.newUser(user);
            log.debug("adding user admin");
        }

        setupContext(context);
    }

    public static void setupContext(ServletContext context) {
    }

}
