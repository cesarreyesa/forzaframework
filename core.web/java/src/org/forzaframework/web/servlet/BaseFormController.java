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

package org.forzaframework.web.servlet;

//import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.FieldError;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.forzaframework.validation.Information;
import org.forzaframework.validation.Field;
import org.forzaframework.validation.Error;
import org.forzaframework.web.servlet.view.TextView;
import org.forzaframework.web.servlet.view.XmlView;
import org.forzaframework.web.servlet.mvc.BinderConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

/**
 * @author cesarreyes
 * Date: 09-sep-2008
 * Time: 15:08:57
 */
@Deprecated
public class BaseFormController {
//        extends SimpleFormController {
//    protected final transient Logger log = LogManager.getLogger(getClass());
//    protected static final String AJAX_EXT = "AJAX_EXT";
//    protected String ajaxMode = "";
//    protected String cancelView;
//    protected String parentView;
//    private BinderConfiguration binderConfiguration = new BinderConfiguration();
//    private List<Map> customEditors = new ArrayList<Map>();
//
//    public List<Map> getCustomEditors() {
//        return customEditors;
//    }
//
//    public void setCustomEditors(List<Map> customEditors) {
//        this.customEditors = customEditors;
//    }
//
//    public String getAjaxMode() {
//        return ajaxMode;
//    }
//
//    public void setAjaxMode(String ajaxMode) {
//        this.ajaxMode = ajaxMode;
//    }
//
//    public void saveMessage(HttpServletRequest request, String msg) {
//        List messages = (List) request.getSession().getAttribute("messages");
//
//        if (messages == null) {
//            messages = new ArrayList();
//        }
//
//        messages.add(msg);
//        request.getSession().setAttribute("messages", messages);
//    }
//
//    public void saveErrorMessage(HttpServletRequest request, String msg) {
//        List messages = (List) request.getSession().getAttribute("errors");
//
//        if (messages == null) {
//            messages = new ArrayList();
//        }
//
//        messages.add(msg);
//        request.getSession().setAttribute("errors", messages);
//    }
//
//    public String getText(String msgKey) {
//        return getMessageSourceAccessor().getMessage(msgKey);
//    }
//
//    public String getText(String msgKey, Object param[]) {
//        return getMessageSourceAccessor().getMessage(msgKey, param);
//    }
//
//    public String getText(String msgKey, Locale locale) {
//        return getMessageSourceAccessor().getMessage(msgKey, locale);
//    }
//
//    public String getText(String msgKey, String arg, Locale locale) {
//        return getText(msgKey, new Object[]{arg}, locale);
//    }
//
//    public String getText(String msgKey, Object[] args, Locale locale) {
//        return getMessageSourceAccessor().getMessage(msgKey, args, locale);
//    }
//
//    public String getText(String msgKey, Object[] args, String defaultMessage) {
//        return getMessageSourceAccessor().getMessage(msgKey, args, defaultMessage);
//    }
//
//    public String getText(String msgKey, Object[] args, String defaultMessage, Locale locale) {
//        return getMessageSourceAccessor().getMessage(msgKey, args, defaultMessage, locale);
//    }
//
//    public String getText(String msgKey, String defaultMessage) {
//        return getMessageSourceAccessor().getMessage(msgKey, new String[]{}, defaultMessage);
//    }
//
//    /**
//     * Default behavior for FormControllers - redirect to the successView
//     * when the cancel button has been pressed.
//     */
//    public ModelAndView processFormSubmission(HttpServletRequest request,
//                                              HttpServletResponse response,
//                                              Object command,
//                                              BindException errors)
//            throws Exception {
//        if (request.getParameter("mode") != null && request.getParameter("mode").equals("cancel")) {
//            return new ModelAndView(getCancelView());
//        }
//        if(ajaxMode.equals(AJAX_EXT) && errors.hasErrors()){
//            return processErrors(request, errors);
//        }
//        return super.processFormSubmission(request, response, command, errors);
//    }
//
//    public ModelAndView processErrors(HttpServletRequest request, BindException errors){
//        Information info = new Information();
//        info.setSuccess(false);
//        for(Object o : errors.getAllErrors()){
//            ObjectError objectError = (ObjectError) o;
//            if(objectError instanceof FieldError){
//                Field field = new Field();
//                field.setId(((FieldError) objectError).getField());
//                List<Object> arguments = new ArrayList<Object>();
//                try{
//                    arguments.add(getText(objectError.getObjectName() + "." + field.getId()));
//                }catch(Exception ex){
//                    arguments.add("???" + objectError.getObjectName() + "." + field.getId() + "???");
//                }
//                if(objectError.getArguments() != null) arguments.addAll(Arrays.asList(objectError.getArguments()));
//
//                Error error = new Error();
//                error.setMessage(getText(objectError.getDefaultMessage(), arguments.toArray(), objectError.getDefaultMessage()));
//                field.addError(error);
//                info.addField(field);
//            }
//            else{
//                Error error = new Error();
//                error.setMessage(getText(objectError.getDefaultMessage(), objectError.getDefaultMessage()));
//                info.addError(error);
//            }
//        }
//        if(request.getParameter("itype") != null && request.getParameter("itype").equals("json")){
//            return new TextView(info.toJSONString());
//
//        }else{
//            return new XmlView(info.toXml());
//        }
//    }
//
//
//    /**
//     * Set up a custom property editor for converting form inputs to real objects
//     */
//    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
//        binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, null, true));
//
//        NumberFormat nf = NumberFormat.getNumberInstance();
//        DecimalFormat df = (DecimalFormat) nf;
//        df.applyPattern("#####0.00");
//        df.setMinimumFractionDigits(2);
//        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
//        dfs.setDecimalSeparator('.');
//        dfs.setGroupingSeparator(',');
//        df.setDecimalFormatSymbols(dfs);
//
//        binder.registerCustomEditor(Double.class, null, new CustomNumberEditor(Double.class, df, true));
//
//        binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
//        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
//        SimpleDateFormat dateFormat = new SimpleDateFormat(getText("date.format", request.getLocale()));
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
//
//        this.binderConfiguration.configureBinder(this.getApplicationContext(), this.customEditors, binder);
//
//        super.initBinder(request, binder);
//    }
//
//    protected void onBind(HttpServletRequest request, Object command)
//            throws Exception {
//        // if the user is being deleted, turn off validation
//        if (request.getParameter("mode") != null) {
//            if(request.getParameter("mode").equals("delete") || request.getParameter("mode").equals("cancel"))
//                super.setValidateOnBinding(false);
//            else
//                super.setValidateOnBinding(true);
//        } else {
//            super.setValidateOnBinding(true);
//        }
//    }
//
//    protected void logError(){
//
//    }
//
//
//    /**
//     * Indicates what view to use when the cancel button has been pressed.
//     */
//    public final void setCancelView(String cancelView) {
//        this.cancelView = cancelView;
//    }
//
//    public final String getCancelView() {
//        // Default to successView if cancelView is invalid
//        if (this.cancelView == null || this.cancelView.length() == 0) {
//            return getSuccessView();
//        }
//        return this.cancelView;
//    }
//
//    public String getParentView() {
//        return parentView;
//    }
//
//    public void setParentView(String parentView) {
//        this.parentView = parentView;
//    }

}
