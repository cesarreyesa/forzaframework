package org.forzaframework.layout;

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
 *         Date: 08-sep-2008
 *         Time: 18:23:49
 */
@Entity
@Table(name = "config_column_definition")
public class ColumnDefinition extends BaseEntity {
    private Long id;
    private String name;
    private String beanProperty;
    private String format;
    private FileDefinition fileDefinition;

    public ColumnDefinition() {
    }

    public ColumnDefinition(String name, String beanProperty) {
        this.name = name;
        this.beanProperty = beanProperty;
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

    @ManyToOne
    @JoinColumn(name="file_definition_id", nullable = true)
    public FileDefinition getFileDefinition() {
        return fileDefinition;
    }

    public void setFileDefinition(FileDefinition fileDefinition) {
        this.fileDefinition = fileDefinition;
    }

    @Column(name = "name") @NotBlank
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "bean_property") @NotBlank @NotNull
    public String getBeanProperty() {
        return beanProperty;
    }

    public void setBeanProperty(String beanProperty) {
        this.beanProperty = beanProperty;
    }

    @Column(name = "format")
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnDefinition that = (ColumnDefinition) o;

        if (beanProperty != null ? !beanProperty.equals(that.beanProperty) : that.beanProperty != null) return false;
        if (format != null ? !format.equals(that.format) : that.format != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (beanProperty != null ? beanProperty.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.name)
                .toString();
    }

    public Element toXml() {
        return null;
    }

    public Element toXml(String elementName) {
        return null;
    }
}
