package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.PanelItem;

/**
 * User: cesarreyes
 * Date: 06-oct-2007
 * Time: 12:37:55
 * Description:
 */
public class Item implements PanelItem {

    private Object json;

    public Item(Object json) {
        this.json = json;
    }

    public Object toJSON() {
        return json;
    }
}
