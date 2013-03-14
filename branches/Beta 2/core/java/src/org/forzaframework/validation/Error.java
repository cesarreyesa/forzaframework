/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
