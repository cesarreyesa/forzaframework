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
import java.util.List;
import java.util.ArrayList;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 15:34:01
 */
@Entity
@Table(name = "system_security_action_def")
public class ActionDefinition extends BaseEntity {

    private Long id;
    private String code;
    private String action;
    private String description;
    private List<ActionClass> classes = new ArrayList<ActionClass>();
    private List<Permission> permissions = new ArrayList<Permission>();
    private List<PermissionUrl> urls = new ArrayList<PermissionUrl>();

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

    @Column(name = "action") @NotBlank @NotNull
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "action_definition_id")
    public List<ActionClass> getClasses() {
        return classes;
    }

    public void setClasses(List<ActionClass> classes) {
        this.classes = classes;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "action_definition_id")
    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission){
        this.permissions.add(permission);
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "action_definition_id", updatable = false)
    public List<PermissionUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<PermissionUrl> urls) {
        this.urls = urls;
    }

    public void addActionUrl(PermissionUrl permissionUrl){
        this.urls.add(permissionUrl);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionDefinition that = (ActionDefinition) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;

        return true;
    }

    public int hashCode() {
        int result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.action)
                .toString();
    }

    public Element toXml() {
        return null;
    }

    public Element toXml(String elementName) {
        return null;
    }
}
