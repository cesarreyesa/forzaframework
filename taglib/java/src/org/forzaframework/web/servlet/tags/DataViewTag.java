package org.forzaframework.web.servlet.tags;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.form.Field;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: cesarreyes
 * Date: 29-nov-2007
 * Time: 23:03:21
 * Description:
 */
public class DataViewTag extends PanelTag implements PanelItem {

    private String url;
    private String root;
    private String template;
    private Boolean loadOnStart = true;

    private List<Field> fields = new ArrayList<Field>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Boolean getLoadOnStart() {
        return loadOnStart;
    }

    public void setLoadOnStart(Boolean loadOnStart) {
        this.loadOnStart = loadOnStart;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void addField(Field field){
        fields.add(field);
    }

    public void doInitBody() throws JspException {
        try{
            if(this.bodyContent != null){
                fields = new ArrayList<Field>();
                contextMenu = null;

                JspWriter out = pageContext.getOut();
                StringBuilder sb = new StringBuilder();


                out.write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
    }

    public int doEndTag() throws JspException {
        try {
            if(this.bodyContent != null){
                StringBuilder sb = new StringBuilder();

                sb.append("<script type=\"text/javascript\">\n");
                sb.append("Ext.onReady(function(){\n");
                sb.append("var store = new Ext.data.JsonStore(");
                JSONObject storeConfig = new JSONObject();
                storeConfig.put("url", url);
                storeConfig.put("root", root);

                JSONArray record = new JSONArray();
                for(Field field : fields){
                    JSONObject json = new JSONObject();
                    json.put("name", field.getField());
                    json.elementOpt("mapping", field.getMapping());
                    record.add(json);
                }
                storeConfig.put("fields", record);
                sb.append(storeConfig.toString()).append(");\n");

                String template = this.template;
                template = template.replace("\n", "','");
                sb.append("var tpl = new Ext.XTemplate('").append(template).append("');\n");

                JSONObject dataViewConfig = new JSONObject();
                dataViewConfig.put("id", id);
                dataViewConfig.put("store", new JSONFunction("store"));
                dataViewConfig.put("tpl", new JSONFunction("tpl"));
                dataViewConfig.put("singleSelect", true);
                dataViewConfig.put("overClass", "x-view-over");
                dataViewConfig.put("itemSelector", "div.thumb-wrap");
                dataViewConfig.put("emptyText", "No data to display");

                sb.append("var dv = new Ext.DataView(").append(dataViewConfig.toString()).append(");");

                if(contextMenu != null){
                    sb.append("dv.contextMenu = ").append(contextMenu.toString()).append(";");
                    sb.append("dv.on('contextmenu', function(view, index, node, e) {");
                    sb.append("e.stopEvent();");
                    sb.append("dv.select(node, false);");
                    sb.append("var coords = e.getXY();");
                    sb.append("dv.contextMenu.showAt([coords[0], coords[1]]);");
                    sb.append("});");
                }

                if(loadOnStart){
                    sb.append("store.load();");
                }

                Tag panel = findParent(PanelTag.class);
                if(panel != null){
                    ((PanelTag) panel).addItem(new Item(this.toJSON()));
                }

                sb.append("});");
                sb.append("</script>\n");
                
                JspWriter writer = bodyContent.getEnclosingWriter();
                bodyContent.writeOut(writer);
                JspWriter out = pageContext.getOut();
                out.write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    private Object topToolbar;
    public void setTopToolbar(Item item) {
        topToolbar = item.toJSON();
    }

    private Object contextMenu;
    public void setContextMenu(Item item){
        contextMenu = ((JSONObject) item.toJSON()).get("menu");
    }

    public void addItem(PanelItem item) {
    }

    public Object toJSON() {
        return new JSONFunction("Ext.getCmp('" + id + "')");
    }
}
