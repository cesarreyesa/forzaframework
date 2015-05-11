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

import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.util.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.NotNull;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 14:55:51
 */
@Entity
@Table(name = "app_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("USER")
public class User extends BaseEntity implements Serializable, UserDetails {
    private static final long serialVersionUID = 3832626162173359411L;

    protected Long id;
    protected String client;
    protected String username;                    // assigned primary key
    protected String password;                    // required
    protected String confirmPassword;
    protected String firstName;                    // required
    protected String lastName;                    // required
    protected String phoneNumber;
    protected String email;                        // required; unique
    protected String website;
    protected String passwordHint;
    protected List<Role> roles = new ArrayList<Role>();
    protected boolean enabled;
    protected boolean accountExpired;
    protected boolean accountLocked;
    protected boolean credentialsExpired;
    protected boolean isNew;
    protected String lastPasswordAssistanceToken;
    protected String preferredLocale;
    protected Date passwordChangeDate;
    protected Boolean enableExpiredPasswordProcess;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    @Transient
    public Object getKey() {
        return id;
    }

    public void setKey(Object id) {
        this.id = Long.valueOf(id.toString());
    }

    @Transient
    public boolean isNew() {
        return isNew || id == null;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
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

    @Column(name = "client_id")
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Column(name = "username") @NotBlank
    @NotNull
    public String getUsername() {
        return username;
    }

    @Column(name = "password") @NotBlank @NotNull
    public String getPassword() {
        return password;
    }

    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Column(name = "first_name") @NotBlank @NotNull
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name") @NotBlank @NotNull
    public String getLastName() {
        return lastName;
    }

    @Transient
    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    @Column(name = "email") @NotBlank @NotNull
    public String getEmail() {
        return email;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Column(name = "website")
    public String getWebsite() {
        return website;
    }

    @Column(name = "password_hint")
    public String getPasswordHint() {
        return passwordHint;
    }

    @Column(name = "preferred_locale")
    public String getPreferredLocale() {
        return preferredLocale;
    }

    //, fetch = FetchType.EAGER se quito esto porque trae 1 registro de usuario por cada role que tiene asignado.
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_name"))
    public List<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        getRoles().add(role);
    }

    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        for (Role role : roles) {
            list.add(role);
        }
        return list;
    }

    @Column(name = "account_enabled")
    public boolean isEnabled() {
        return enabled;
    }

    @Column(name = "account_expired") @NotNull
    public boolean isAccountExpired() {
        return accountExpired;
    }

    @Transient
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    @Column(name = "account_locked") @NotNull
    public boolean isAccountLocked() {
        return accountLocked;
    }

    @Transient
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    @Column(name = "credentials_expired") @NotNull
    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    @Transient
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public void setPreferredLocale(String preferredLocale) {
        this.preferredLocale = preferredLocale;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    @Column(name = "last_pa_token")
    public String getLastPasswordAssistanceToken() {
        return lastPasswordAssistanceToken;
    }

    public void setLastPasswordAssistanceToken(String lastPasswordAssistanceToken) {
        this.lastPasswordAssistanceToken = lastPasswordAssistanceToken;
    }

    @Column(name = "password_change_date")
    public Date getPasswordChangeDate() {
        return passwordChangeDate;
    }

    public void setPasswordChangeDate(Date passwordChangeDate) {
        this.passwordChangeDate = passwordChangeDate;
    }


    @Column(name = "enable_expired_password_process")
    public Boolean getEnableExpiredPasswordProcess() {
        return enableExpiredPasswordProcess == null ? false : enableExpiredPasswordProcess;
    }

    public void setEnableExpiredPasswordProcess(Boolean enableExpiredPasswordProcess) {
        this.enableExpiredPasswordProcess = enableExpiredPasswordProcess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this,
                ToStringStyle.DEFAULT_STYLE).append("username", this.username)
                .append("client", this.client)
                .append("enabled", this.enabled)
                .append("accountExpired",this.accountExpired)
                .append("credentialsExpired",this.credentialsExpired)
                .append("accountLocked",this.accountLocked);

//        Collection<GrantedAuthority> auths = this.getAuthorities();
//        if (auths != null) {
//            sb.append("Granted Authorities: ");
//
//            for (int i = 0; i < auths.size(); i++) {
//                if (i > 0) {
//                    sb.append(", ");
//                }
//                sb.append(auths.get(i).toString());
//            }
//        } else {
//            sb.append("No Granted Authorities");
//        }
        return sb.toString();
    }

    public Element toXml() {
        return toXml("item");
    }

    public Element toXml(String elementName) {
        Element el = DocumentHelper.createElement(elementName);
        el.addElement("id").addText(this.getId().toString());
        el.addElement("username").addText(this.getUsername() == null ? "" : this.getUsername());
        el.addElement("password").addText(this.getPassword() == null ? "" : this.getPassword());
        el.addElement("firstName").addText(this.getFirstName() == null ? "" : this.getFirstName());
        el.addElement("lastName").addText(this.getLastName() == null ? "" : this.getLastName());
        el.addElement("name").addText(this.getFullName());
        el.addElement("email").addText(this.getEmail() == null ? "" : this.getEmail());
        el.addElement("preferredLocale").addText(this.getPreferredLocale() == null ? "" : this.getPreferredLocale());
        el.addElement("enabled").addText(String.valueOf(this.isEnabled()));
        el.addElement("accountExpired").addText(String.valueOf(this.isAccountExpired()));
        el.addElement("accountLocked").addText(String.valueOf(this.isAccountLocked()));
        el.addElement("credentialsExpired").addText(String.valueOf(this.isCredentialsExpired()));
        el.addElement("enableExpiredPasswordProcess").addText(String.valueOf(this.getEnableExpiredPasswordProcess()));
        if(this.getRoles().size() > 0){
            Element roles = el.addElement("roles");
            for(Role role : this.getRoles()){
                Element roleEl = roles.addElement("role");
                roleEl.addElement("id").addText(role.getName());
                roleEl.addElement("name").addText(role.getName());
            }
        }
        return el;
    }
}
