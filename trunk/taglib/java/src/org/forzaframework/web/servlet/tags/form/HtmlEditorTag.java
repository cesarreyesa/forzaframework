package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * User: Cesar Reyes
 * Date: 11/06/2007
 * Time: 03:11:23 PM
 * Description:
 */
public class HtmlEditorTag extends FieldTag {

    private String defaultFont;
    private Boolean showOnWindow = false;
    private String saveText;
    private String saveTextKey;
    private String cancelText;
    private String cancelTextKey;
    private Boolean enableAlignments = true;
    private Boolean enableColors = true;
    private Boolean enableFont = true;
    private Boolean enableFontSize = true;
    private Boolean enableFormat = true;
    private Boolean enableLinks = true;
    private Boolean enableLists = true;
    private Boolean enableSourceEdit = true;

    public String getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(String defaultFont) {
        this.defaultFont = defaultFont;
    }

    public Boolean getShowOnWindow() {
        return showOnWindow;
    }

    public void setShowOnWindow(Boolean showOnWindow) {
        this.showOnWindow = showOnWindow;
    }

    public String getSaveText() {
        return saveText;
    }

    public void setSaveText(String saveText) {
        this.saveText = saveText;
    }

    public String getSaveTextKey() {
        return saveTextKey;
    }

    public void setSaveTextKey(String saveTextKey) {
        this.saveTextKey = saveTextKey;
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public String getCancelTextKey() {
        return cancelTextKey;
    }

    public void setCancelTextKey(String cancelTextKey) {
        this.cancelTextKey = cancelTextKey;
    }

    public String getType() {
        return showOnWindow ? "windowhtmleditor" : "htmleditor";
    }

    public Boolean getEnableAlignments() {
		return enableAlignments;
	}

	public void setEnableAlignments(Boolean enableAlignments) {
		this.enableAlignments = enableAlignments;
	}

	public Boolean getEnableColors() {
		return enableColors;
	}

	public void setEnableColors(Boolean enableColors) {
		this.enableColors = enableColors;
	}

	public Boolean getEnableFont() {
		return enableFont;
	}

	public void setEnableFont(Boolean enableFont) {
		this.enableFont = enableFont;
	}

	public Boolean getEnableFontSize() {
		return enableFontSize;
	}

	public void setEnableFontSize(Boolean enableFontSize) {
		this.enableFontSize = enableFontSize;
	}

	public Boolean getEnableFormat() {
		return enableFormat;
	}

	public void setEnableFormat(Boolean enableFormat) {
		this.enableFormat = enableFormat;
	}

	public Boolean getEnableLinks() {
		return enableLinks;
	}

	public void setEnableLinks(Boolean enableLinks) {
		this.enableLinks = enableLinks;
	}

	public Boolean getEnableLists() {
		return enableLists;
	}

	public void setEnableLists(Boolean enableLists) {
		this.enableLists = enableLists;
	}

	public Boolean getEnableSourceEdit() {
		return enableSourceEdit;
	}

	public void setEnableSourceEdit(Boolean enableSourceEdit) {
		this.enableSourceEdit = enableSourceEdit;
	}

	public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("fieldLabel", title != null ? title : getText(titleKey));
        json.put("name", this.getField());
//        json.elementOpt("mapping", this.getMapping());
        json.elementOpt("inputValue", value);
        json.elementOpt("value", value);
        json.elementOpt("disabled", disabled);

        json.elementOpt("hideLabel", getHideLabel());
        json.elementOpt("labelSeparator", getLabelSeparator());

        json.elementOpt("enableAlignments", enableAlignments);
        json.elementOpt("enableColors", enableColors);
        json.elementOpt("enableFont", enableFont);
        json.elementOpt("enableFontSize", enableFontSize);
        json.elementOpt("enableFormat", enableFormat);
        json.elementOpt("enableLinks", enableLinks);
        json.elementOpt("enableLists", enableLists);
        json.elementOpt("enableSourceEdit", enableSourceEdit);

        json.elementOpt("height", height);
        json.elementOpt("width", width);

        json.elementOpt("autoHeight", autoHeight);
        json.elementOpt("anchor", anchor);
        if(showOnWindow){
            json.put("saveActionText", saveText != null ? saveText : getText(saveTextKey));
            json.put("cancelActionText", cancelText != null ? cancelText : getText(cancelTextKey));
        }
        json.put("validateOnBlur", false);

        json.put("xtype", getType());

        return json;
    }

    public String getHtmlDeclaration(){
        StringBuilder sb = new StringBuilder();

        sb.append("<input style=\"width: ").append(getWidth()).append(";\" class=\"x-form-text x-form-field\" size=\"20\" autocomplete=\"off\" id=\"");
        sb.append(getField());
        sb.append("\" name=\"");
        sb.append(getField());
        sb.append("\" type=\"text\">");

        return sb.toString();
    }
}