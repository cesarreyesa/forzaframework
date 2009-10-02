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

import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.forzaframework.core.persistance.BaseEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 14:57:11
 */
@Entity
@Table(name = "role")
public class Role extends BaseEntity implements GrantedAuthority {
    private static final long serialVersionUID = 3690197650654049848L;
    private String name;
    private String description;
    private Set<User> users;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Transient
    public String getId() {
        return name;
    }

    public void setId(String id) {
        this.name = id;
    }

    @Transient
    public Object getKey() {
        return name;
    }

    public void setKey(Object id) {
        this.name = id.toString();
    }

    @Id
    @Column(name = "name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getAuthority() {
        return getName();
    }

    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_name"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (description != null ? !description.equals(role.description) : role.description != null) return false;
        if (name != null ? !name.equals(role.name) : role.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
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
        el.addElement("name").addText(this.getName());
        el.addElement("description").addText(this.getDescription() == null ? "" : this.getDescription());
        return el;
    }

	public int compareTo(GrantedAuthority o) {
		return 0;
	}

}
