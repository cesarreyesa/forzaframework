package org.forzaframework.web.servlet.tags;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 1:17:18
 * Description:
 */
public class JSONFunction extends net.sf.json.JSONFunction {

    public JSONFunction(String text) {
        super(text);
    }

    public JSONFunction(String[] params, String text) {
        super(params, text);
    }


    public String toString() {
        StringBuffer b = new StringBuffer(getText());
        return b.toString();
    }
}
