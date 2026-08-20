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
 *         Date: 12-ago-2008
 *         Time: 12:34:21
 */

public class TextView extends ModelAndView {

    private String text = "";

    public TextView() {
        super(createView(""));
    }

    public TextView(String text) {
        super(createView(text));
        this.text = text;
    }

    public String getText() {
        return text;
    }

    private static AbstractView createView(final String text) {
        return new AbstractView() {
            protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/plain;charset=utf-8");
                response.getWriter().write(text);
            }
        };
    }

}
