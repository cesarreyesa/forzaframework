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

package org.forzaframework.appgen;

import java.util.Hashtable;

public class GeneratorUtility {

    public static String firstToUpperCase(String string) {
        String post = string.substring(1, string.length());
        String first = ("" + string.charAt(0)).toUpperCase();
        return first + post;
    }

    public static String firstToLowerCase(String string) {
        String post = string.substring(1, string.length());
        String first = ("" + string.charAt(0)).toLowerCase();
        return first + post;
    }

    public static String toPlural(String string) {
        if (!string.endsWith("y")) {
            return string + "s";
        } else {
            string = string.substring(0, string.length() - 1);
            return string + "ies";
        }
    }

    public static String packageToPath(String packageName) {
        return packageName.replace(".", "/") + "/";
    }

    // Type conversion

    private static Hashtable typeConvertor = new Hashtable();

    static {
        typeConvertor.put("integer", "int");
        typeConvertor.put("boolean", "boolean");
        typeConvertor.put("date", "Date");
        typeConvertor.put("long", "long");
        typeConvertor.put("short", "short");
        typeConvertor.put("double", "double");
        typeConvertor.put("string", "String");
    }

    public static String getJavaType(String type) {
        String javaType = (String) typeConvertor.get(type);
        if (javaType == null)
            return type;
        return javaType;
    }

}