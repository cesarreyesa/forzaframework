package org.forzaframework.validation;

import net.sf.json.JSONObject;

/**
 * User: cesarreyes
 * Date: 14-may-2007
 * Time: 22:32:47
 * Description:
 */
public class Error {

    private String code;
    private String message;
    private String stacktrace;

    public Error() {
    }

    public Error(String message) {
        this.message = message;
    }

    public Error(String message, String stacktrace) {
        this.message = message;
        this.stacktrace = stacktrace;
    }

    public Error(String code, String message, String stacktrace) {
        this.code = code;
        this.message = message;
        this.stacktrace = stacktrace;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);
        json.put("stacktrace", stacktrace);
        return json;
    }
}
