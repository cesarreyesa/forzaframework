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
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 21:37:58
 * Description:
 */
public class Association extends Attribute {

    private Name name;
    private Entity entity;
    private LayoutStyle layoutStyle;
    private LovLayoutStyle lovLayoutStyle;
    private String multiplicity;

    public Association() {
    }

    public Association(Entity entity, String multiplicity) {
        this(null, entity, multiplicity);
    }

    public Association(Name name, Entity entity, String multiplicity) {
        this.name = name;
        this.entity = entity;
        this.multiplicity = multiplicity;
    }

    public Association(Name name, Entity entity, String multiplicity, LovLayoutStyle lovLayoutStyle) {
        this.name = name;
        this.entity = entity;
        this.multiplicity = multiplicity;
        this.lovLayoutStyle = lovLayoutStyle;
    }

    public Association(Name name, Entity entity, String multiplicity, LayoutStyle layoutStyle) {
        this.name = name;
        this.entity = entity;
        this.layoutStyle = layoutStyle;
        this.multiplicity = multiplicity;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public LayoutStyle getLayoutStyle() {
        return layoutStyle;
    }

    public void setLayoutStyle(LayoutStyle layoutStyle) {
        this.layoutStyle = layoutStyle;
    }

    public LovLayoutStyle getLovLayoutStyle() {
        return lovLayoutStyle;
    }

    public void setLovLayoutStyle(LovLayoutStyle lovLayoutStyle) {
        this.lovLayoutStyle = lovLayoutStyle;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }
}
