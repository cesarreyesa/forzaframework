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

package org.forzaframework.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Enumeration;
import java.util.List;
import java.util.Collections;

/**
 * @author cesarreyes
 *         Date: 15-ago-2008
 *         Time: 17:17:19
 */
/**
 * HttpRequestWrapper overriding methods getLocale(), getLocales() to include
 * the user's preferred locale.
 */
public class LocaleRequestWrapper extends HttpServletRequestWrapper {
    private final transient Logger log = LogManager.getLogger(LocaleRequestWrapper.class);
    private final Locale preferredLocale;

    public LocaleRequestWrapper(HttpServletRequest decorated, Locale userLocale) {
        super(decorated);
        preferredLocale = userLocale;
        if (null == preferredLocale) {
            log.error("preferred locale = null, it is an unexpected value!");
        }
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getLocale()
     */
    public Locale getLocale() {
        if (null != preferredLocale) {
            return preferredLocale;
        } else {
            return super.getLocale();
        }
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getLocales()
     */
    public Enumeration getLocales() {
        if (null != preferredLocale) {
            List l = Collections.list(super.getLocales());
            if(l.contains(preferredLocale))
            {
                l.remove(preferredLocale);
            }
            l.add(0, preferredLocale);
            return Collections.enumeration(l);
        } else {
            return super.getLocales();
        }
    }

}
