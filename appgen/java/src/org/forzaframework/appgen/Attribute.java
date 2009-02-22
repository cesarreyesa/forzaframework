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
