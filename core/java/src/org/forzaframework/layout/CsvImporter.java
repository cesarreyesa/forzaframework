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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ClassUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.DataBinder;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.util.Assert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.forzaframework.beans.propertyeditors.CustomClassEditor;
import org.forzaframework.beans.propertyeditors.ExternalEntityEditor;
import org.forzaframework.beans.propertyeditors.CustomEntityIdCollectionEditor;
import org.forzaframework.metadata.TranslatableCatalog;
import org.forzaframework.metadata.SystemEntity;
import org.forzaframework.metadata.Attribute;
import org.forzaframework.metadata.SystemConfiguration;
import org.forzaframework.util.CsvUtils;
import org.forzaframework.bind.CustomBindingErrorProcessor;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author cesarreyes
 *         Date: 10-sep-2008
 *         Time: 9:31:16
 */
public class CsvImporter<T> extends BaseImporter implements Importer {

    private Logger logger = LogManager.getLogger(CsvImporter.class);
    private SystemConfiguration systemConfiguration;
    private Validator validator;

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public List convert(Class clazz, FileDefinition fileDefinition, String path, List errors) throws Exception {
        Assert.notNull(entityManager, "Manager must not be null");

        List<String> lines = CsvUtils.getLines(path);
        List<String> columnsArray = CsvUtils.getTokens(lines.get(0), fileDefinition.getDelimiter());
        // remove header
        lines.remove(0);

        List<T> items = new ArrayList<T>();
        for (String line : lines) {
            T command = (T) clazz.newInstance();
            List<String> tokens = CsvUtils.getTokens(line, fileDefinition.getDelimiter());

            // if the file definition allows to update records, get the object by code
            if(fileDefinition.getUpdateExistingRecords()){
                String code = tokens.get(0);
                try{
                    command = (T) entityManager.getByCode(clazz, code);
                }catch(ObjectRetrievalFailureException ex){
                    logger.debug("command with code: " + code + " does not exist.");
//                    if(fileDefinition.getAllowCreateNewRecords()){
//                        throw new Exception("Empleado con codigo [" + code + "] no existe.");
//                    }
                    if(fileDefinition.getIgnoreNotExistingRecords()){
                        continue;
                    }
                }
            }

            SystemEntity entity = systemConfiguration.getSystemEntity(clazz);
            DataBinder binder = createBinder(command, entity.getCode());

            binder.setBindingErrorProcessor(new CustomBindingErrorProcessor());
            MutablePropertyValues mpvs = new MutablePropertyValues();

            for (ColumnDefinition columnDefinition : fileDefinition.getColumns()) {
                Integer i = 0;
                for (String value : tokens) {
                    String columnName = columnsArray.get(i++);
                    if(columnDefinition.getName().trim().equalsIgnoreCase(columnName.trim())){
                        PropertyValue pv = extractPropertyValue(binder, command, entity, columnDefinition, value);
                        if(pv != null) mpvs.addPropertyValue(pv);
                        break;
                    }
                }
            }
            binder.bind(mpvs);
            BindException objectErrors = new BindException(binder.getBindingResult());
            ValidationUtils.invokeValidator(validator, command, objectErrors);

            // Si no tiene errores entonces agregamos el objeto a la lista a regresar
            if (!objectErrors.hasErrors()) {
                items.add(command);
            }
            else{
                errors.add(objectErrors);
            }
        }
        return items;
    }

    public PropertyValue extractPropertyValue(DataBinder binder, Object command, SystemEntity entity, ColumnDefinition columnDefinition, String value) throws Exception{
        PropertyValue pv = null;
        String property = columnDefinition.getBeanProperty();
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
            }else{
                if (property.equals("externalCode")) {
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
