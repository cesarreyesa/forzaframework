package org.forzaframework.metadata;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.forzaframework.metadata.Attribute;
import org.forzaframework.core.persistance.BaseEntity;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * User: Cesar Reyes
 * Date: 7/03/2007
 * Time: 03:50:52 PM
 * Description:
 */
public class SystemEntity extends Catalog {

    private String entityType;
    private String discriminator;
    private String className;
    private List<Attribute> attributes = new ArrayList<Attribute>();

    public SystemEntity() {
    }

    public SystemEntity(String name) {
        this.name = name;
    }

    public SystemEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class getType(){
        if(this.className == null) return null;
        try {
            return Class.forName(this.className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute){
        this.attributes.add(attribute);
    }

    public Attribute findAttribute(String name){
        for(Attribute attribute : attributes){
            if(attribute.getName().equals(name)){
                return attribute;
            }
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemEntity entity = (SystemEntity) o;

        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;
        if (name != null ? !name.equals(entity.name) : entity.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
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
