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

import org.forzaframework.beans.propertyeditors.CustomClassEditor;
import org.forzaframework.beans.propertyeditors.CustomEntityIdCollectionEditor;
import org.forzaframework.metadata.Attribute;
import org.forzaframework.metadata.SystemConfiguration;
import org.forzaframework.metadata.SystemEntity;
import org.hibernate.criterion.Projections;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.validation.DataBinder;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.PropertyValue;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.metadata.TranslatableCatalog;
import org.forzaframework.beans.propertyeditors.ExternalEntityEditor;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.beans.PropertyDescriptor;

/**
 * @author cesarreyes
 *         Date: 10-sep-2008
 *         Time: 9:31:41
 */
public abstract class BaseImporter implements Importer {

    protected EntityManager entityManager;
    protected MessageSourceAccessor messageSourceAccessor;
    protected SystemConfiguration systemConfiguration;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public DataBinder createBinder(Object command) {
        return createBinder(command, null);
    }

    public DataBinder createBinder(Object command, String objectName) {
        DataBinder binder = new DataBinder(command, objectName);
        binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, null, true));

        NumberFormat nf = NumberFormat.getNumberInstance();
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#####0.00");
        df.setMinimumFractionDigits(2);
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);

        binder.registerCustomEditor(Double.class, null, new CustomNumberEditor(Double.class, df, true));
        binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

        SimpleDateFormat dateFormat = new SimpleDateFormat(getText("date.format"));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));

        return binder;
    }

    protected String getText(String msgKey) {
        return messageSourceAccessor.getMessage(msgKey);
    }

    public Object getPropertyValue(Class clazz, String property, String valueToSearch, String layoutColumn) throws Exception {
        org.hibernate.Criteria crit = entityManager.getHibernateSession().createCriteria(clazz);
        crit.add(org.hibernate.criterion.Restrictions.eq(property, valueToSearch));
        crit.setProjection(Projections.projectionList().add(Projections.property("id")));
        Long id = (Long) crit.uniqueResult();
        Assert.notNull(id, "Error en la columna [" + layoutColumn + "] del archivo de importaci\u00F3n. No existe registro del valor [" + valueToSearch + "] en la BD.");
        return entityManager.load(clazz, id);
    }

    public Field getDeclaredField(Class clazz, String property) {
        Field field;
        try {
            field = clazz.getDeclaredField(property);
        } catch (NoSuchFieldException e) {
            field = null;
        }

        if (field == null) {
            Class superClazz = clazz.getSuperclass();
            if ( superClazz != null) {
                field = this.getDeclaredField(superClazz, property);
            }
        }

        return field;
    }

    public String resolveProperty(String property) {
        if(property.indexOf(".") > 0){
            return property.substring(property.indexOf(".") + 1);
        }
        return property;
    }

    public PropertyValue extractPropertyValue(DataBinder binder, Object command, SystemEntity entity, ColumnDefinition columnDefinition, String value) throws Exception{
        String property = columnDefinition.getBeanProperty();
        return extractPropertyValue(binder, command, entity, columnDefinition, property, value);
    }

    public PropertyValue extractPropertyValue(DataBinder binder, Object command, ColumnDefinition columnDefinition, String property, String value) throws Exception{
        PropertyValue pv = null;
        if (property != null && !"xx".equals(property)) {
            if (property.startsWith("externalCode(")) {
                String propertyName = property.substring(property.indexOf("(") + 1, property.indexOf(")"));
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(command, propertyName);
                Assert.notNull(pd, "Invalid layout property name [" + propertyName + "] does not exists");
                binder.registerCustomEditor(pd.getPropertyType(), propertyName, new ExternalEntityEditor(pd.getPropertyType(), columnDefinition.getFileDefinition().getExternalSystem(), this.entityManager));
                pv = new PropertyValue(propertyName, value);

            } else if (property.equals("externalCode")) {
                List superclasses = ClassUtils.getAllSuperclasses(command.getClass());
                for (Object superclass : superclasses) {
                    if (superclass.equals(TranslatableCatalog.class)) {
                        ((TranslatableCatalog) command).setTranslation(columnDefinition.getFileDefinition().getExternalSystem(), value);
                        break;
                    }
                }
            } else if(property.startsWith("entityCode(")){
                String propertyName = property.substring(property.indexOf("(") + 1, property.indexOf(")"));
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(command, propertyName);
                Assert.notNull(pd, "Invalid layout property name [" + propertyName + "] does not exists");
                binder.registerCustomEditor(pd.getPropertyType(), propertyName, new ExternalEntityEditor(pd.getPropertyType(), "", this.entityManager));
                pv = new PropertyValue(propertyName, value);

            } else{
                if(StringUtils.isNotBlank(columnDefinition.getFormat())){
                    if(property.toLowerCase().contains("date")){
                        SimpleDateFormat dateFormat = new SimpleDateFormat(columnDefinition.getFormat());
                        dateFormat.setLenient(false);
                        binder.registerCustomEditor(Date.class, property, new CustomDateEditor(dateFormat, true));
                    }
                }
                pv = new PropertyValue(property, value);
            }
        }
        return pv;
    }

    public PropertyValue extractPropertyValue(DataBinder binder, Object command, SystemEntity entity, ColumnDefinition columnDefinition, String property, String value) throws Exception{
        PropertyValue pv = null;
        if (property != null && !"xx".equals(property)) {
            // Busca el attributo para ver si se encuentra en la configuracion
            Attribute attribute = entity.findAttribute(property);
            if(attribute != null){
                // si es de tipo lista
                if(attribute.getType().equals("list")){
                    SystemEntity listType = systemConfiguration.getSystemEntity(attribute.getEntity());
                    binder.registerCustomEditor(List.class, property, new CustomEntityIdCollectionEditor(List.class, listType.getType(), entityManager));

                }else if(attribute.getType().equals("entity")){
                    SystemEntity attType = systemConfiguration.getSystemEntity(attribute.getEntity());
                    // Obtenemos el tipo de la propiedad y si el layout tiene configurado un sistema externo entonces
                    // usa el ExternalEntityEditor que asume que el codigo que se pasa es el del sistema externo
                    if(StringUtils.isNotBlank(columnDefinition.getFileDefinition().getExternalSystem())){
                        binder.registerCustomEditor(attType.getType(), property, new ExternalEntityEditor(attType.getType(), columnDefinition.getFileDefinition().getExternalSystem() , entityManager));
                    }else{
                        binder.registerCustomEditor(attType.getType(), property, new CustomClassEditor(attType.getType(), entityManager));
                    }
                }
            }
            else{
                SystemEntity attType = systemConfiguration.getSystemEntity(resolveProperty(property));
                if(attType != null){
                    if(StringUtils.isNotBlank(columnDefinition.getFileDefinition().getExternalSystem())){
                        binder.registerCustomEditor(attType.getType(), property, new ExternalEntityEditor(attType.getType(), columnDefinition.getFileDefinition().getExternalSystem() , entityManager));
                    }else{
                        binder.registerCustomEditor(attType.getType(), property, new CustomClassEditor("code", String.class, attType.getType(), entityManager));
                    }
                }
                else if (property.equals("externalCode")) {
                    List superclasses = ClassUtils.getAllSuperclasses(entity.getType());
                    for (Object superclass : superclasses) {
                        if (superclass.equals(TranslatableCatalog.class)) {
                            ((TranslatableCatalog) command).setTranslation(columnDefinition.getFileDefinition().getExternalSystem(), value);
                            break;
                        }
                    }
                }
            }

            // si es que tiene un formato entonces trata de aplicarlo.
            if(StringUtils.isNotBlank(columnDefinition.getFormat())){
                if(property.toLowerCase().contains("date")){
                    SimpleDateFormat dateFormat = new SimpleDateFormat(columnDefinition.getFormat());
                    dateFormat.setLenient(false);
                    binder.registerCustomEditor(Date.class, property, new CustomDateEditor(dateFormat, true));
                }
            }
            pv = new PropertyValue(property, value);
        }
        return pv;
    }
}
