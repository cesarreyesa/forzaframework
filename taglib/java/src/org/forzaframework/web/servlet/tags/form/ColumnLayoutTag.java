package org.forzaframework.web.servlet.tags.form;

import javax.servlet.jsp.JspException;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import java.util.ArrayList;

import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.PanelItem;

/**
 * Created by IntelliJ IDEA.
 * User: Cesar Reyes
 * Date: 3/10/2007
 * Time: 04:00:17 PM
 */
public class ColumnLayoutTag extends PanelTag {

    public ColumnLayoutTag() {
		this.setBorder(false);
	}

	public void setTopToolbar(Item item) {
    }

    public void doInitBody() throws JspException {
        setItems(new ArrayList<PanelItem>());
    }

    public int doEndTag() throws JspException {
        ((PanelTag) getParent()).addItem(new Item(this.toJSON()));
        return EVAL_PAGE;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("layout", "column");
        json.put("anchor", getAnchor());
        json.elementOpt("border", getBorder());

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
