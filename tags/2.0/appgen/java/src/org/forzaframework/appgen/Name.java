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

/**
 * User: Cesar Reyes
 * Date: 27/12/2006
 * Time: 12:05:04 PM
 * Description:
 */
public class Name {
    private String name;

    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toLowerCase() {
        String post = name.substring(1, name.length());
        String first = ("" + name.charAt(0)).toLowerCase();
        return first + post;
    }

    public String toPlural() {
        if (!name.endsWith("y")) {
            return name + "s";
        } else {
            String temp = name.substring(0, name.length() - 1);
            return temp + "ies";
        }
    }

    public String toPluralFirstToLower() {
        String temp = name;
        name = toPlural();
        String toReturn = toLowerCase();
        name = temp;
        return toReturn;
    }


    public String toString() {
        return name;
    }
}
