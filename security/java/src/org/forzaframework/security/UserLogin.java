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

import org.apache.commons.lang.BooleanUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.util.DateUtils;
import org.forzaframework.util.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "app_user_login")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserLogin extends BaseEntity {

    private Long id;
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginDate = new Date();

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoginDate() { return loginDate; }

    public void setLoginDate(Date loginDate) { this.loginDate = loginDate; }

    public void setKey(Object id) {
        this.id = Long.valueOf(id.toString());
    }

    @Transient
    public Object getKey() {
        return id;
    }

    @Transient
    public Integer getDaysFromLastLogin() {
        if (this.loginDate == null)
            return null;
        return DateUtils.getDaysDifference(this.loginDate, new Date());
    }

    public Element toXml() {
        return toXml(true);
    }

    public Element toXml(Boolean includeUser) {
        return toXml("item", includeUser);
    }

    public Element toXml(String elementName, Boolean includeUser) {
        Element el = DocumentHelper.createElement(elementName);
        el.addElement("id").addText(StringUtils.getValue(this.getId()));
        el.addElement("loginDate").addText(StringUtils.getValue(this.getLoginDate()));
        el.addElement("loginDateFormat").addText(DateUtils.getDateTime(this.getLoginDate()));
        el.addElement("daysFromLastLogin").addText(StringUtils.getValue(this.getDaysFromLastLogin()));
        if(BooleanUtils.isTrue(includeUser) && this.getUser() != null) {
            Element userElement = el.addElement("user");
            userElement.addElement("id").addText(this.getUser().getId().toString());
            userElement.addElement("fullName").addText(this.getUser().getFullName());
        }
        return el;
    }
}
