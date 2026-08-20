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

package org.forzaframework.layout;

import org.hibernate.annotations.GenericGenerator;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.forzaframework.core.persistance.BaseEntity;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

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
    private Boolean updateExistingRecords = false;
    private Boolean allowCreateNewRecords = false;
    private Boolean ignoreNotExistingRecords = false;
    private Boolean requireFiscalCompanies = false;
    private Boolean allowImportWizard = false;
    private List<ColumnDefinition> columns = new ArrayList<>();
    // private List<InnerDefinition> innerDefnitions = new ArrayList<InnerDefinition>();
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

    @Column(name = "require_fiscal_companies")
    public Boolean getRequireFiscalCompanies() {
        if(requireFiscalCompanies == null) requireFiscalCompanies = false;
        return requireFiscalCompanies;
    }

    public void setRequireFiscalCompanies(Boolean requireFiscalCompanies) {
        this.requireFiscalCompanies = requireFiscalCompanies;
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

    @Column(name = "allow_import_wizard")
    public Boolean getAllowImportWizard() {
        return allowImportWizard;
    }

    public void setAllowImportWizard(Boolean allowImportWizard) {
        this.allowImportWizard = allowImportWizard;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileDefinition that = (FileDefinition) o;

        if (!Objects.equals(code, that.code)) return false;
        if (!Objects.equals(delimiter, that.delimiter)) return false;
        if (!Objects.equals(entity, that.entity)) return false;
        if (!Objects.equals(externalSystem, that.externalSystem)) return false;
        if (!Objects.equals(format, that.format)) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;

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
        el.addElement("requireFiscalCompanies").addText(this.getRequireFiscalCompanies() == null ? "" : this.getRequireFiscalCompanies().toString());
        el.addElement("allowImportWizard").addText(this.getAllowImportWizard() == null ? "" : this.getAllowImportWizard().toString());
        el.addElement("entity").addText(this.getEntity() == null ? "" : this.getEntity());
        el.addElement("format").addText(this.getFormat() == null ? "" : this.getFormat());
        el.addElement("delimiter").addText(this.getDelimiter() == null ? "" : this.getDelimiter());
        if(!this.getColumns().isEmpty()){
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
