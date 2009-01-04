package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.Item;
import org.springframework.util.Assert;

import javax.servlet.jsp.JspException;

/**
 * User: Cesar Reyes
 * Date: 21/12/2006
 * Time: 06:01:10 PM
 * Description:
 */
public class SeparatorTag extends BaseTag {

    private Boolean alignToRight = false;

    public Boolean getAlignToRight() {
        return alignToRight;
    }

    public void setAlignToRight(Boolean alignToRight) {
        this.alignToRight = alignToRight;
    }

    public int doEndTag() throws JspException {
        ToolbarTag parent = (ToolbarTag) findParent(ToolbarTag.class);
        if(parent != null){
            parent.addItem(new Item(this.toJSON()));
        }else{
        	MenuTag menu = (MenuTag) findParent(MenuTag.class);
        	Assert.notNull(menu, "SeparatorTag must be inside a ToolbarTag or MenuTag");
        	menu.addItem(new Item(this.toJSON()));
        }
        return EVAL_PAGE;
    }

    public Object toJSON(){
        return new JSONFunction("'-" + (alignToRight ? ">" : "") + "'");
    }
}
