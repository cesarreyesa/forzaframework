package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.ButtonTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.sf.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: cesarreyes
 * Date: 08-feb-2008
 * Time: 18:07:49
 */
public class TextTag extends ButtonTag {

	public TextTag(){
		this.type = "tbtext";
	}
}
