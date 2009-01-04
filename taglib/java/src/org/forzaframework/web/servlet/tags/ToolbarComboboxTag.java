package org.forzaframework.web.servlet.tags;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.BaseTag;
import org.forzaframework.web.servlet.tags.Item;

import javax.servlet.jsp.JspException;

/**
 * User: Cesar Reyes
 * Date: 26/06/2007
 * Time: 11:50:26 AM
 * Description:
 */
public class ToolbarComboboxTag extends BaseTag {

    private String value;
    private String text;
    private String emptyText;
    private String url;
    private String valueField = "id";
    private String displayField = "name";
    private String handler;
    private Integer width;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public int doEndTag() throws JspException {
        ToolbarTag parent = (ToolbarTag) findParent(ToolbarTag.class);
        parent.addItem(new Item(this.toJSON()));
        return EVAL_PAGE;
    }

    public Object toJSON(){
        final StringBuilder sb = new StringBuilder();

        JSONObject json = new JSONObject();

        json.put("xtype", "combo");
        json.elementOpt("id", id);
        json.elementOpt("text", text);
        json.elementOpt("value", value);

        sb.append("new Ext.data.Store({");
        sb.append("proxy: new Ext.data.HttpProxy(new Ext.data.Connection({url: \"").append(url).append("\"})),");
        sb.append("reader: new Ext.data.XmlReader({");
        sb.append("record: \"item\", totalRecords: \"totalCount\", id: \"" + valueField + "\"},");
        sb.append("[{name: \"").append(displayField).append("\"}, {name: \"" + valueField + "\"}] )");
        sb.append("})");

        json.put("store", new JSONFunction(sb.toString()));

        json.put("displayField", displayField);
        json.put("valueField", valueField);
        json.put("typeAhead", true);
        json.put("triggerAction", "all");
        json.put("emptyText", emptyText);
        json.put("selectOnFocus", true);
        json.put("width", 200);

        if(StringUtils.isNotBlank(handler)){
            JSONObject listeners = new JSONObject();
            listeners.put("select", new JSONFunction(handler));
            json.put("listeners", listeners);
        }


        return json;
    }
}
