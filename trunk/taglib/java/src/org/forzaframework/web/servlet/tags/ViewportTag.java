package org.forzaframework.web.servlet.tags;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.Item;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: cesarreyes
 * Date: 25-nov-2007
 * Time: 19:39:02
 * Description:
 */
public class ViewportTag extends PanelTag {
	
    private List<PanelItem> panels = new ArrayList<PanelItem>();

    public List<PanelItem> getPanels() {
        return panels;
    }

    public void setTopToolbar(Item item) {
    }

    public void addItem(PanelItem panel){
        this.panels.add(panel);
    }

    public void doInitBody() throws JspException {
        panels = new ArrayList<PanelItem>();
    }

    public int doEndTag() throws JspException {
        try {
            if(this.bodyContent != null){
                StringBuffer sb = new StringBuffer();

                sb.append("<script type=\"text/javascript\">");

                sb.append("var System = {};\n");
                sb.append("System = function(){\n");
                sb.append("var viewport, center;\n");
                sb.append("return {\n");
                sb.append("getViewport : function(){\n");
                sb.append("return viewport;\n");
                sb.append("},\n");
                sb.append("getCenter : function(){ return Ext.getCmp('center'); },\n");
                sb.append("init : function() {\n");
                sb.append("Ext.QuickTips.init();\n");
                sb.append("<!--Ext.state.Manager.setProvider(new Ext.state.CookieProvider());-->\n");

                sb.append("viewport = new Ext.Viewport(\n");

                JSONObject config = new JSONObject();

                config.put("layout", getLayout());
                config.elementOpt("border", getBorder());
                //config.elementOpt("el", id);
                config.elementOpt("id", id);

                JSONArray items = new JSONArray();

                for(PanelItem item : panels){
                    items.add(item.toJSON());
                }

                config.put("items", items);

                sb.append(config.toString(2)).append(");\n");
                sb.append("}\n");
                sb.append("};\n");
                sb.append("}();\n");
                sb.append("Ext.onReady(System.init, System, true);\n");
                sb.append("</script>");

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
