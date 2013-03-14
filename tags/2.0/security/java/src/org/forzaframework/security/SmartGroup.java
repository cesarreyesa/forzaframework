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

package org.forzaframework.security;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.forzaframework.core.persistance.Criteria;
import org.forzaframework.core.persistance.BaseEntity;

import javax.persistence.*;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 16:25:56
 */
@Entity
@Table(name = "config_sg")
public class SmartGroup extends BaseEntity {

    private Long id;
    private String code;
    private String name;
    private Criteria criteria;

    public SmartGroup() {
    }

    public SmartGroup(Long id) {
        this.id = id;
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

    @Column(name = "code") @NotBlank
    @NotNull
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name") @NotBlank @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmartGroup that = (SmartGroup) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.name)
                .toString();
    }

    public Element toXml() {
        return toXml("item");
    }

    public Element toXml(String elementName) {
        Element el = DocumentHelper.createElement(elementName);
        el.addElement("id").addText(this.getId().toString());
        el.addElement("code").addText(this.getCode() == null ? "" : this.getCode());
        el.addElement("name").addText(this.getName());
//        if(this.getFilters().size() > 0){
//            Element filters = el.addElement("filters");
//            for(SmartGroupFilter smartGroupFilter : this.getFilters()){
//                Element filter = filters.addElement("filterdef");
//                filter.addElement("id").addText(smartGroupFilter.getId().toString());
//                filter.addElement("property").addText(smartGroupFilter.getProperty());
//                filter.addElement("type").addText(smartGroupFilter.getType());
//                filter.addElement("filter").addText(smartGroupFilter.getFilter());
//            }
//        }
        return el;
    }
}
