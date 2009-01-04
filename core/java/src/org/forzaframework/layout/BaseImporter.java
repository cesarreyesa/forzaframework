package org.forzaframework.layout;

import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.validation.DataBinder;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.PropertyValue;
import org.springframework.util.Assert;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.metadata.TranslatableCatalog;
import org.forzaframework.beans.propertyeditors.ExternalEntityEditor;

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
public abstract class BaseImporter extends WebApplicationObjectSupport implements Importer {

    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected DataBinder createBinder(Object command) {
        return createBinder(command, null);
    }

    protected DataBinder createBinder(Object command, String objectName) {
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
        return getMessageSourceAccessor().getMessage(msgKey);
    }

    public PropertyValue extractPropertyValue(DataBinder binder, Object command, ColumnDefinition columnDefinition, String value) throws Exception{
        PropertyValue pv = null;
        String property = columnDefinition.getBeanProperty();
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

}
