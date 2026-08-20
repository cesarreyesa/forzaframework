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
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;
import org.forzaframework.core.persistance.BaseEntity;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

import javax.persistence.*;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 15:37:13
 */
@Entity
@Table(name = "system_security_permission_url")
public class PermissionUrl extends BaseEntity {

    private Long id;
    private String url;
    private ActionDefinition actionDefinition;

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

    @Column(name = "url") @NotBlank
    @NotNull
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ManyToOne
    @JoinColumn(name="action_definition_id", nullable=false)
    public ActionDefinition getPermissionDefinition() {
        return actionDefinition;
    }

    public void setPermissionDefinition(ActionDefinition actionDefinition) {
        this.actionDefinition = actionDefinition;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionUrl permissionUrl = (PermissionUrl) o;

        if (id != null ? !id.equals(permissionUrl.id) : permissionUrl.id != null) return false;
        if (url != null ? !url.equals(permissionUrl.url) : permissionUrl.url != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.url
                )
                .toString();
    }

    public Element toXml() {
        return null;
    }

    public Element toXml(String elementName) {
        return null;
    }
}
