package org.forzaframework.appgen;

import java.util.ArrayList;
import java.util.List;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 18:18:51
 * Description:
 */
public class Attribute {

    private Name name;
    private String type;
    private String displayType;
    private Boolean displayInFormLayout;
    private Boolean displayInTableLayout;
    private List<String> validations = new ArrayList<String>();


    public Attribute() {
    }

    public Attribute(Name name, String type) {
        this.name = name;
        this.type = type;
    }

    public Attribute(Name name, String type, String validation) throws Exception {
        this.name = name;
        this.type = type;
        if (type.equals("Integer") && validation.equals("not-blank")) {
            throw new Exception("Invalid validation for a type Integer");
        }
        validations.add(validation);
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValidated() {
        return validations.size() > 0;
    }

    public List<String> getValidations() {
        return validations;
    }

    public void setValidations(List<String> validations) {
        this.validations = validations;
    }

    public void addValication(String validation) {
        this.validations.add(validation);
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public Boolean getDisplayInFormLayout() {
        return displayInFormLayout;
    }

    public void setDisplayInFormLayout(Boolean displayInFormLayout) {
        this.displayInFormLayout = displayInFormLayout;
    }

    public Boolean getDisplayInTableLayout() {
        return displayInTableLayout;
    }

    public void setDisplayInTableLayout(Boolean displayInTableLayout) {
        this.displayInTableLayout = displayInTableLayout;
    }
}
