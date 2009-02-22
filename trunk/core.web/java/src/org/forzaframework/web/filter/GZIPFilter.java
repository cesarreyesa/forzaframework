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

import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author cesarreyes
 *         Date: 15-ago-2008
 *         Time: 17:10:22
 */
/**
 * Filter that compresses output with gzip (assuming that browser supports gzip).
 * Code from <a href="http://www.onjava.com/pub/a/onjava/2003/11/19/filters.html">
 * http://www.onjava.com/pub/a/onjava/2003/11/19/filters.html</a>.
 *
 * &copy; 2003 Jayson Falkner You may freely use the code both commercially
 * and non-commercially.
 *
 * @web.filter name="compressionFilter"
 */
public class GZIPFilter extends OncePerRequestFilter {
    private final transient Log log = LogFactory.getLog(GZIPFilter.class);

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain)
    throws IOException, ServletException {

        if (isGZIPSupported(request)) {
            if (log.isDebugEnabled()) {
                log.debug("GZIP supported, compressing response");
            }

            GZIPResponseWrapper wrappedResponse =
                new GZIPResponseWrapper(response);

            chain.doFilter(request, wrappedResponse);
            wrappedResponse.finishResponse();

            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Convenience method to test for GZIP cababilities
     * @param req The current user request
     * @return boolean indicating GZIP support
     */
    private boolean isGZIPSupported(HttpServletRequest req) {

        String browserEncodings = req.getHeader("accept-encoding");
        boolean supported = ((browserEncodings != null) &&
                             (browserEncodings.indexOf("gzip") != -1));

        String userAgent = req.getHeader("user-agent");

        if ((userAgent != null) && userAgent.startsWith("httpunit")) {
            if (log.isDebugEnabled()) {
                log.debug("httpunit detected, disabling filter...");
            }

            return false;
        } else {
            return supported;
        }
    }
}
