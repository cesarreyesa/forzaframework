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

package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONObject;
import org.forzaframework.web.servlet.tags.BaseBodyTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * User: Cesar Reyes
 * Date: 12/06/2007
 * Time: 11:45:43 AM
 * Description:
 */
public class ContainerTag extends BaseBodyTag {

    public void doInitBody() throws JspException {
        try{
            if(this.bodyContent != null){
                StringBuilder sb = new StringBuilder();

                sb.append("form.container(");
                JSONObject json = new JSONObject();
                json.elementOpt("id", id);
                sb.append(json.toString());
                sb.append(");");

                pageContext.getOut().write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
    }

    public int doEndTag() throws JspException {
        try {
            if(this.bodyContent != null){
                StringBuilder sb = new StringBuilder();

                sb.append("form.end();");

                JspWriter writer = bodyContent.getEnclosingWriter();
                bodyContent.writeOut(writer);
                pageContext.getOut().write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }
}
