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
import org.springframework.validation.Validator;
import org.springframework.validation.DataBinder;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.util.Assert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.forzaframework.beans.propertyeditors.CustomClassEditor;
import org.forzaframework.metadata.SystemEntity;
import org.forzaframework.util.CsvUtils;
import org.forzaframework.bind.CustomBindingErrorProcessor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;

/**
 * @author cesarreyes
 *         Date: 10-sep-2008
 *         Time: 9:31:16
 */
public class CsvImporter<T> extends BaseImporter implements Importer {

    private Logger logger = LogManager.getLogger(CsvImporter.class);
    private Validator validator;

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public List convert(Class clazz, FileDefinition fileDefinition, String path, List errors) throws Exception {
        Assert.notNull(entityManager, "Manager must not be null");

        List<String> lines = CsvUtils.getLines(path);
        List<String> columnsArray = CsvUtils.getTokens(lines.get(0), fileDefinition.getDelimiter());
        Integer codeIndex = CsvUtils.getColumnIndex(columnsArray);

        lines.remove(0); // remove header

        List<T> items = new ArrayList<>();
        for (String line : lines) {
            if (fileDefinition.getAllowCreateNewRecords() && line.startsWith(fileDefinition.getDelimiter())){
                line = fileDefinition.getDelimiter() + line;
            }
            List<String> tokens = CsvUtils.getTokens(line, fileDefinition.getDelimiter());

            T command = (T) clazz.newInstance();
            // if the file definition allows to update records, get the object by code
            if(fileDefinition.getUpdateExistingRecords()){
                String code = tokens.get(codeIndex);
                if(StringUtils.isNotBlank(code)) {
                    try {
                        command = entityManager.getByCode(clazz, code);
                    } catch (ObjectRetrievalFailureException ex) {
                        logger.debug("command with code: " + code + " does not exist.");
                        if (fileDefinition.getIgnoreNotExistingRecords()) {
                            continue;
                        }
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
                        PropertyValue pv;
                        if (columnDefinition.getBeanProperty().contains(".")) {
                            Object propertyValue;
                            String principalProperty = StringUtils.substringBefore(columnDefinition.getBeanProperty(), ".");
                            Field field = getDeclaredField(clazz, principalProperty);
                            if (field == null) {
                                throw new Exception("No existe la propiedad [" + principalProperty + "] en la clase [" + clazz.getSimpleName() + "]");
                            }

                            String nestedProperty = StringUtils.substringAfter(columnDefinition.getBeanProperty(), ".");
                            propertyValue = getPropertyValue(field.getType(), nestedProperty, value, columnDefinition.getName());
                            pv = new PropertyValue(principalProperty, propertyValue);
                            binder.registerCustomEditor(field.getType(), field.getName(), new CustomClassEditor(field.getType()));
                        }
                        else {
                            pv = extractPropertyValue(binder, command, entity, columnDefinition, value);
                        }

                        if(pv != null){
                            mpvs.addPropertyValue(pv);
                        }
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
}
