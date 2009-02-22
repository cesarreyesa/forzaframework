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
 * Time: 18:12:23
 * Description:
 * <p/>
 * TODO: to increase performance calculate all the transisent methods on the fly
 */
public class Entity {

    private Name name;
    private Attribute id;
    private Module module;
    private Class clazz;
    private List<Attribute> attributes = new ArrayList<Attribute>();
    private LayoutStyle layoutStyle;
    private Boolean isEnabled;
    private Boolean generateDataLayer = true;
    private Boolean generateServiceLayer = true;
    private Boolean generateViewLayer = true;

    public Entity() {
    }

    public Entity(Name name, Module module, Boolean enabled) {
        this.name = name;
        this.module = module;
        isEnabled = enabled;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Name getSimpleName() {
        return name;
    }

    public String getPackageName() {
        return module.getPackageName();
    }

    public String getParentPackageName() {
        String packageName = getPackageName();
        if (packageName != null)
            return packageName.substring(0, packageName.lastIndexOf("."));
        else
            return "";
    }

    public Attribute getId() {
        return id;
    }

    public void setId(Attribute id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Boolean getGenerateDataLayer() {
        return generateDataLayer;
    }

    public void setGenerateDataLayer(Boolean generateDataLayer) {
        this.generateDataLayer = generateDataLayer;
    }

    public Boolean getGenerateServiceLayer() {
        return generateServiceLayer;
    }

    public void setGenerateServiceLayer(Boolean generateServiceLayer) {
        this.generateServiceLayer = generateServiceLayer;
    }

    public Boolean getGenerateViewLayer() {
        return generateViewLayer;
    }

    public void setGenerateViewLayer(Boolean generateViewLayer) {
        this.generateViewLayer = generateViewLayer;
    }

    public List<Attribute> getAttributes() {
        List<Attribute> atts = new ArrayList<Attribute>();
        for (Attribute att : attributes) {
            if (!(att instanceof Association)) {
                atts.add(att);
            }
        }
        return atts;
    }

    public List<Attribute> getAttributes(Boolean includeAssociations) {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public List<Association> getAssociations() {
        List<Association> associations = new ArrayList<Association>();
        for (Attribute attribute : attributes) {
            if (attribute instanceof Association) {
                associations.add((Association) attribute);
            }
        }
        return associations;
    }

    public List<Association> getOneToManyAssociations() {
        List<Association> associations = new ArrayList<Association>();
        for (Association association : getAssociations()) {
            if (association.getMultiplicity().equals("OneToMany")) {
                associations.add(association);
            }
        }
        return associations;
    }

    public List<Association> getManyToOneAssociations() {
        List<Association> associations = new ArrayList<Association>();
        for (Association association : getAssociations()) {
            if (association.getMultiplicity().equals("ManyToOne")) {
                associations.add(association);
            }
        }
        return associations;
    }

    public Boolean hasOneToManyAssociations() {
        Boolean hasAssociations = false;
        for (Association association : getAssociations()) {
            if (association.getMultiplicity().equals("OneToMany")) {
                hasAssociations = true;
                break;
            }
        }
        return hasAssociations;
    }

    public Boolean hasManyToOneAssociations() {
        Boolean hasAssociations = false;
        for (Association association : getAssociations()) {
            if (association.getMultiplicity().equals("ManyToOne")) {
                hasAssociations = true;
                break;
            }
        }
        return hasAssociations;
    }
//    public List<Association> getAssociations() {
//        return associations;
//    }
//
//    public void setAssociations(List<Association> associations) {
//        this.associations = associations;
//    }
//
//    public void addAssociation(Association association){
//        this.associations.add(association);
//    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public LayoutStyle getLayoutStyle() {
        return layoutStyle;
    }

    public void setLayoutStyle(LayoutStyle layoutStyle) {
        this.layoutStyle = layoutStyle;
    }
}
