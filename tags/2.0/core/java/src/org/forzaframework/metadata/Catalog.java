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

package org.forzaframework.metadata;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.forzaframework.core.persistance.BaseEntity;

import javax.persistence.*;

/**
 * User: Cesar Reyes
 * Date: 8/03/2007
 * Time: 12:13:28 PM
 * Description:
 */
@MappedSuperclass
public class Catalog extends BaseEntity {

    protected Long id;
    protected String code;
    protected String name;
    protected String translatedName;

    public Catalog() {
    }

    public Catalog(Long id) {
        this.id = id;
    }

    public Catalog(String code, String name){
    	this.code = code;
    	this.name = name;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Transient
    public Object getKey() {
        return id;
    }

    public void setKey(Object id) {
        this.id = Long.valueOf(id.toString());
    }

    @Column(name = "code", unique = true) @NotNull
    @NotBlank
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name") @NotNull @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getTranslatedName() {
		return translatedName;
	}

	public void setTranslatedName(String translatedName) {
		this.translatedName = translatedName;
	}

	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Catalog)) return false;

        Catalog that = (Catalog) o;

        if (code != null ? !code.equals(that.getCode()) : that.getCode() != null) return false;
        if (name != null ? !name.equals(that.getName()) : that.getName() != null) return false;

        return true;
    }

    public int hashCode() {
        int result = (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.name)
                .toString();
    }

    public Element toXml(){
        return this.toXml("item");
    }

    public Element toXml(String elementName) {
        Element el = DocumentHelper.createElement(elementName);
        el.addElement("id").addText(this.getId() == null ? "" : this.getId().toString());
        el.addElement("code").addText(this.getCode() == null ? "" : this.getCode());
        el.addElement("name").addText(this.getName() == null ? "" : this.getName());
        return el;
    }
}
