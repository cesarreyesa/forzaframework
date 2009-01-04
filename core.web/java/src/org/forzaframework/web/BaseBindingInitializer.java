package org.forzaframework.web;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.forzaframework.metadata.SystemConfiguration;
import org.forzaframework.core.persistance.EntityManager;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 16:17:48
 */
public class BaseBindingInitializer extends ApplicationObjectSupport implements WebBindingInitializer {

    private SystemConfiguration systemConfiguration;
    private EntityManager entityManager;

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void initBinder(WebDataBinder binder, WebRequest request) {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat(getMessageSourceAccessor().getMessage("date.format"));
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));

        EntityBindingHelper.configureBinder(entityManager, systemConfiguration, binder);
    }

}
