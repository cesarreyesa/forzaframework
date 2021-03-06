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

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 16:17:36
 */
public class CsvView extends ModelAndView {

    public CsvView() {
        super(createView(""));
    }

    public CsvView(String text) {
        super(createView(text));
    }

    public CsvView(String[] lines) {
        super(createView(lines, null));
    }

    public CsvView(String[] lines, String fileName) {
        super(createView(lines, fileName));
    }

    private static AbstractView createView(final String text) {
        return new AbstractView() {
            protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/csv");
                response.getWriter().write(text);
            }
        };
    }

    private static AbstractView createView(final String[] lines, final String fileName) {
        return new AbstractView() {
            protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/csv");
                if (fileName != null) {
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName.trim() + ".csv\"");
                }
                for(String line : lines){
                    response.getOutputStream().write((line + "\n").getBytes());
                }
            }
        };
    }

}
