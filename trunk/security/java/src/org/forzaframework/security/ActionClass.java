package org.forzaframework.security;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;
import org.forzaframework.core.persistance.BaseEntity;

import javax.persistence.*;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 15:36:15
 */
@Entity
@Table(name = "system_security_action_class")
public class ActionClass extends BaseEntity {

    private Long id;
    private String className;
    private String methodName;
    private String targetClass;

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

    @Column(name = "class_name") @NotBlank
    @NotNull
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Column(name = "method_name") @NotBlank @NotNull
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Column(name = "target_class")
    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionClass that = (ActionClass) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.className + "." + this.methodName)
                .toString();
    }

    public Element toXml() {
        return null;
    }

    public Element toXml(String elementName) {
        return null;
    }
}
