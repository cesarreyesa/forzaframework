package org.forzaframework.web.servlet.tags.form;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.PanelItem;

/**
 * User: Cesar Reyes
 * Date: 12/06/2007
 * Time: 11:49:47 AM
 * Description:
 */
public class ColumnTag extends PanelTag {
	
	private String columnWidth;	
	private Boolean hideLabels;
    private String labelAlign;

    public ColumnTag() {
        this.setBorder(false);
        this.setLayout("form");
    }

    public String getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
	}

	public Boolean getHideLabels() {
		return hideLabels;
	}

	public void setHideLabels(Boolean hideLabels) {
		this.hideLabels = hideLabels;
	}

	public String getLabelAlign() {
        return labelAlign;
    }

    public void setLabelAlign(String labelAlign) {
        this.labelAlign = labelAlign;
    }

    public void setTopToolbar(Item item) {
    }

    public void doInitBody() throws JspException {
        setItems(new ArrayList<PanelItem>());
    }

    public int doEndTag() throws JspException {
        PanelTag panel = (PanelTag) findParent(PanelTag.class);
        panel.addItem(new Item(this.toJSON()));
        return EVAL_PAGE;
    }

    public JSON toJSON() {
        JSONObject json = new JSONObject();

        json.put("layout", getLayout());
        json.elementOpt("border", getBorder());
        json.elementOpt("bodyStyle", getStyle());
        if(getColumnWidth() != null){
            json.put("columnWidth", Double.valueOf(getColumnWidth()));
        }
        if(getWidth() != null){
            json.elementOpt("width", Integer.valueOf(getWidth()));
        }
        json.elementOpt("labelAlign", labelAlign);
        json.elementOpt("hideLabels", hideLabels);

        if(getItems().size() > 0){
            JSONArray jsonItems = new JSONArray();

            for(PanelItem item : getItems()){
                jsonItems.add(item.toJSON());
            }
            json.put("items", jsonItems);
        }

        return json;
    }

}
