package org.forzaframework.beans.propertyeditors;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 17:41:29
 */
public class DefaultCustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

    private String dateFormat = "dd/MM/yyyy";

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void registerCustomEditors(PropertyEditorRegistry registry) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat);
        dateFormat.setLenient(false);
        registry.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
    }
}
