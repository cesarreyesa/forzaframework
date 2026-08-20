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

package org.forzaframework.orm.hibernate3.support;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.dao.DataAccessResourceFailureException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.FlushMode;

import java.util.Date;
import java.util.List;

/**
 * User: cesar.reyes
 * Date: 22/08/2009
 * Time: 12:46:14 PM
 */
public abstract class OpenSessionInThreadTask implements Runnable{

    private ApplicationContext ctx;

    public OpenSessionInThreadTask(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    protected abstract void runInternal();

    public final void run(){
        SessionFactory sessionFactory = lookupSessionFactory();
        boolean participate = false;

        // single session mode
        if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
            // Do not modify the Session: just set the participate flag.
            participate = true;
        }
        else {
            Session session = getSession(sessionFactory);
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        }

        try{
            runInternal();
        }

        finally {
            if (!participate) {
                // single session mode
                SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
                closeSession(sessionHolder.getSession(), sessionFactory);
            }
        }
    }

    protected SessionFactory lookupSessionFactory() {
        return ctx.getBean("sessionFactory", SessionFactory.class);
    }

    protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
//        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        Session session = sessionFactory.getCurrentSession();
        FlushMode flushMode = FlushMode.MANUAL;
        if (flushMode != null) {
            session.setFlushMode(flushMode);
        }
        return session;
    }

    protected void closeSession(Session session, SessionFactory sessionFactory) {
        SessionFactoryUtils.closeSession(session);
    }

}
