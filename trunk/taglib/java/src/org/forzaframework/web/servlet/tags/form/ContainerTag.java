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
