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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author cesarreyes
 *         Date: 15-ago-2008
 *         Time: 17:11:27
 */
/**
 * Wraps Response for GZipFilter
 *
 * @author  Matt Raible, cmurphy@intechtual.com
 */
public class GZIPResponseWrapper extends HttpServletResponseWrapper {
    private transient final Log log = LogFactory.getLog(GZIPResponseWrapper.class);
    protected HttpServletResponse origResponse = null;
    protected ServletOutputStream stream = null;
    protected PrintWriter writer = null;
    protected int error = 0;

    public GZIPResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;
    }

    public ServletOutputStream createOutputStream() throws IOException {
        return (new GZIPResponseStream(origResponse));
    }

    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
        }
    }

    public void flushBuffer() throws IOException {
        if (stream != null) {
            stream.flush();
        }
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }

        if (stream == null) {
            stream = createOutputStream();
        }

        return (stream);
    }

    public PrintWriter getWriter() throws IOException {
        // From cmurphy@intechtual.com to fix:
        // https://appfuse.dev.java.net/issues/show_bug.cgi?id=59
        if (this.origResponse != null && this.origResponse.isCommitted()) {
            return super.getWriter();
        }

        if (writer != null) {
            return (writer);
        }

        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }

        stream = createOutputStream();
        writer =
            new PrintWriter(new OutputStreamWriter(stream,
                                                   origResponse.getCharacterEncoding()));

        return (writer);
    }

    public void setContentLength(int length) {
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
     */
    public void sendError(int error, String message) throws IOException {
        super.sendError(error, message);
        this.error = error;

        if (log.isDebugEnabled()) {
            log.debug("sending error: " + error + " [" + message + "]");
        }
    }
}
