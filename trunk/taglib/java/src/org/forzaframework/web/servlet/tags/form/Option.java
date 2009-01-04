package org.forzaframework.web.servlet.tags.form;

/**
 * User: cesarreyes
 * Date: 25-oct-2007
 * Time: 14:35:58
 * Description:
 */
public class Option {

    private String value;
    private String text;

    public Option(String value, String text) {
        this.value = value;
        this.text = text;
    }

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
}
