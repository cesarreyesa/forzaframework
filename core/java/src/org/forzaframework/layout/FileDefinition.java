package org.forzaframework.layout;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.NotNull;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.forzaframework.core.persistance.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author cesarreyes
 *         Date: 08-sep-2008
 *         Time: 18:22:01
 */
@Entity
@Table(name = "config_file_definition")
public class FileDefinition extends BaseEntity {

    private Long id;
    private String code;
    private String name;
    private String entity;
    private String externalSystem;
    private Boolean updateExistingRecords;
    private Boolean allowCreateNewRecords;
    private Boolean ignoreNotExistingRecords;
    private List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
//    private List<InnerDefinition> innerDefnitions = new ArrayList<InnerDefinition>();
    private String format;
    private String delimiter;

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

    @Column(name = "code", unique = true) @NotBlank
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

    @Column(name = "entity")
    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Column(name = "external_system")
    public String getExternalSystem() {
        return externalSystem;
    }

    public void setExternalSystem(String externalSystem) {
        this.externalSystem = externalSystem;
    }

    @Column(name = "update_existing_records")
    public Boolean getUpdateExistingRecords() {
        return updateExistingRecords;
    }

    public void setUpdateExistingRecords(Boolean updateExistingRecords) {
        this.updateExistingRecords = updateExistingRecords;
    }

    @Column(name = "allow_create_new_records")
    public Boolean getAllowCreateNewRecords() {
        if(allowCreateNewRecords == null) allowCreateNewRecords = false;
        return allowCreateNewRecords;
    }

    public void setAllowCreateNewRecords(Boolean allowCreateNewRecords) {
        this.allowCreateNewRecords = allowCreateNewRecords;
    }

    @Column(name = "ignore_not_existing_records")
    public Boolean getIgnoreNotExistingRecords() {
        if(ignoreNotExistingRecords == null) ignoreNotExistingRecords = false;
        return ignoreNotExistingRecords;
    }

    public void setIgnoreNotExistingRecords(Boolean ignoreNotExistingRecords) {
        this.ignoreNotExistingRecords = ignoreNotExistingRecords;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_definition_id")
    @OrderBy(value = "beanProperty")
    public List<ColumnDefinition> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDefinition> columns) {
        this.columns = columns;
    }

    public void addColumn(ColumnDefinition columnDefinition){
        this.columns.add(columnDefinition);
    }

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "file_definition_id")
//    @OrderBy(value = "id")
//    public List<InnerDefinition> getInnerDefnitions() {
//        return innerDefnitions;
//    }
//
//    public void setInnerDefnitions(List<InnerDefinition> innerDefnitions) {
//        this.innerDefnitions = innerDefnitions;
//    }

    @Column(name = "format")
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Column(name = "delimiter")
    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileDefinition that = (FileDefinition) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (delimiter != null ? !delimiter.equals(that.delimiter) : that.delimiter != null) return false;
        if (entity != null ? !entity.equals(that.entity) : that.entity != null) return false;
        if (externalSystem != null ? !externalSystem.equals(that.externalSystem) : that.externalSystem != null) return false;
        if (format != null ? !format.equals(that.format) : that.format != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (entity != null ? entity.hashCode() : 0);
        result = 31 * result + (externalSystem != null ? externalSystem.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (delimiter != null ? delimiter.hashCode() : 0);
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
        Element el = DocumentHelper.createElement("item");

        el.addElement("id").addText(this.getId().toString());
        el.addElement("code").addText(this.getCode() == null ? "" : this.getCode());
        el.addElement("name").addText(this.getName() == null ? "" : this.getName());
        el.addElement("externalSystem").addText(this.getExternalSystem() == null ? "" : this.getExternalSystem());
        el.addElement("updateExistingRecords").addText(this.getUpdateExistingRecords() == null ? "" : this.getUpdateExistingRecords().toString());
        el.addElement("allowCreateNewRecords").addText(this.getAllowCreateNewRecords() == null ? "" : this.getAllowCreateNewRecords().toString());
        el.addElement("ignoreNotExistingRecords").addText(this.getIgnoreNotExistingRecords() == null ? "" : this.getIgnoreNotExistingRecords().toString());
        el.addElement("entity").addText(this.getEntity() == null ? "" : this.getEntity());
        el.addElement("format").addText(this.getFormat() == null ? "" : this.getFormat());
        el.addElement("delimiter").addText(this.getDelimiter() == null ? "" : this.getDelimiter());
        if(this.getColumns().size() > 0){
            Element columns = el.addElement("columns");
            for(ColumnDefinition columnDefinition : this.getColumns()){
                Element column = columns.addElement("column");
                column.addElement("id").addText(columnDefinition.getId().toString());
                column.addElement("beanProperty").addText(columnDefinition.getBeanProperty());
                column.addElement("name").addText(columnDefinition.getName());
                column.addElement("format").addText(columnDefinition.getFormat() == null ? "" : columnDefinition.getFormat());
            }
        }
        return el;
    }

    public static FileDefinition fromXml(Element el){
        return new FileDefinition();
    }
}
