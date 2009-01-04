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
