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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gabriel.chulim on 27/04/2015.
 */
public class CustomExceptionMappingAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {
    private final Map<String, String> failureUrlMap = new HashMap<String, String>();
    public static final String LAST_USERNAME_KEY = "LAST_USERNAME";

    @Autowired
    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String url = failureUrlMap.get(exception.getClass().getName());

        if (url != null) {
            String usernameParameter = usernamePasswordAuthenticationFilter.getUsernameParameter();
            String lastUserName = request.getParameter(usernameParameter);

            HttpSession session = request.getSession(false);
            if (session != null || isAllowSessionCreation()) {
                request.getSession().setAttribute(LAST_USERNAME_KEY, lastUserName);
            }

            if("true".equals(request.getParameter("changePassword"))) {
                request.getSession().setAttribute("j_password", request.getParameter("j_password"));
                request.getSession().setAttribute("j_password_new", request.getParameter("j_password_new"));
                request.getSession().setAttribute("j_password_confirm", request.getParameter("j_password_confirm"));
            }
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
//            getRedirectStrategy().sendRedirect(request, response, url);
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }

    /**
     * Sets the map of exception types (by name) to URLs.
     *
     * @param failureUrlMap the map keyed by the fully-qualified name of the exception class, with the corresponding
     * failure URL as the value.
     *
     * @throws IllegalArgumentException if the entries are not Strings or the URL is not valid.
     */
    @Override
    public void setExceptionMappings(Map<?,?> failureUrlMap) {
        this.failureUrlMap.clear();
        for (Map.Entry<?,?> entry : failureUrlMap.entrySet()) {
            Object exception = entry.getKey();
            Object url = entry.getValue();
            Assert.isInstanceOf(String.class, exception, "Exception key must be a String (the exception classname).");
            Assert.isInstanceOf(String.class, url, "URL must be a String");
            Assert.isTrue(UrlUtils.isValidRedirectUrl((String) url), "Not a valid redirect URL: " + url);
            this.failureUrlMap.put((String)exception, (String)url);
        }
    }



}
