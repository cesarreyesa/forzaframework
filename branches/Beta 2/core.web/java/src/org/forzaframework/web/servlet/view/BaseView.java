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

package org.forzaframework.web.servlet.view;

import org.springframework.web.servlet.view.AbstractView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 17:36:02
 */
public abstract class BaseView extends AbstractView {

    protected Log log = LogFactory.getLog(getClass());

    /**
     * Convenience method for getting a i18n key's value.  Calling
     * getMessageSourceAccessor() is used because the RequestContext variable
     * is not set in unit tests b/c there's no DispatchServlet Request.
     *
     * @param msgKey
     * @return text
     */
    public String getText(String msgKey) {
        return getMessageSourceAccessor().getMessage(msgKey);
    }

    /**
     * Convenience method for getting a i18n key's value.  Calling
     * getMessageSourceAccessor() is used because the RequestContext variable
     * is not set in unit tests b/c there's no DispatchServlet Request.
     *
     * @param msgKey
     * @param param the current locale
     * @return text
     */
    public String getText(String msgKey, Object param[]) {
        return getMessageSourceAccessor().getMessage(msgKey, param);
    }

    /**
     * Convenience method for getting a i18n key's value.  Calling
     * getMessageSourceAccessor() is used because the RequestContext variable
     * is not set in unit tests b/c there's no DispatchServlet Request.
     *
     * @param msgKey
     * @param locale the current locale
     * @return text
     */
    public String getText(String msgKey, Locale locale) {
        return getMessageSourceAccessor().getMessage(msgKey, locale);
    }

    /**
     * Convenient method for getting a i18n key's value with a single
     * string argument.
     *
     * @param msgKey
     * @param arg
     * @param locale the current locale
     * @return text
     */
    public String getText(String msgKey, String arg, Locale locale) {
        return getText(msgKey, new Object[] { arg }, locale);
    }

    /**
     * Convenience method for getting a i18n key's value with arguments.
     *
     * @param msgKey
     * @param args
     * @param locale the current locale
     * @return text
     */
    public String getText(String msgKey, Object[] args, Locale locale) {
        return getMessageSourceAccessor().getMessage(msgKey, args, locale);
    }

    public String getText(String msgKey, Object[] args, String defaultMessage, Locale locale) {
        return getMessageSourceAccessor().getMessage(msgKey, args, defaultMessage, locale);
    }
}

