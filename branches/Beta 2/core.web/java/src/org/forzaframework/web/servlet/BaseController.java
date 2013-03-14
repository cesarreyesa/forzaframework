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

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 12:17:04
 */

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.validation.ObjectError;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.forzaframework.validation.Information;
import org.forzaframework.validation.Field;
import org.forzaframework.validation.Error;
import org.forzaframework.validation.Record;
import org.forzaframework.web.servlet.view.XmlView;
import org.forzaframework.web.servlet.view.TextView;
import org.forzaframework.security.User;
import org.forzaframework.core.persistance.BaseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.DecimalFormatSymbols;


public class BaseController extends WebApplicationObjectSupport {
    protected final transient Log logger = LogFactory.getLog(getClass());
    protected static final String AJAX_EXT = "AJAX_EXT";
    protected String ajaxMode = AJAX_EXT;

    public String getAjaxMode() {
        return ajaxMode;
    }

    public void setAjaxMode(String ajaxMode) {
        this.ajaxMode = ajaxMode;
    }

    public String getText(String key){
        return getText(key, "???" + key + "???");
    }

    public String getText(String key, String defaultMessage){
        return getMessageSourceAccessor().getMessage(key, defaultMessage);
    }

    public String getText(String key, Object[] arguments){
        return getText(key, arguments, null);
    }

    public String getText(String key, Object[] arguments, String defaultMessage){
        return getMessageSourceAccessor().getMessage(key, arguments, defaultMessage);
    }

    public User getUser() throws RuntimeException{
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            throw new RuntimeException("Security Exception");
        }
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public ModelAndView processErrors(BindingResult result){
        return processErrors(result, null);
    }

    public ModelAndView processErrors(BindingResult result, String responseType){
        Information info = new Information(false);
        for(Object o : result.getAllErrors()){
            ObjectError objectError = (ObjectError) o;
            if(objectError instanceof FieldError){
                Field field = new Field();
                field.setId(((FieldError) objectError).getField());
                List<Object> arguments = getArguments(objectError, field);

                Error error = new Error();
                error.setMessage(getText(objectError.getDefaultMessage(), arguments.toArray(), objectError.getDefaultMessage()));
                field.addError(error);
                info.addField(field);
            }
            else{
                Error error = new Error();
                error.setMessage(getText(objectError.getDefaultMessage(), objectError.getDefaultMessage()));
                info.addError(error);
            }
        }
        if(responseType != null && responseType.equals("json")){
            return new TextView(info.toJSONString());

        }else{
            return new XmlView(info.toXml());
        }
    }

    public ModelAndView processErrors(List<BindException> errors, String responseType) {
        Information info = new Information(false);
        Integer generatedKey = -10000;
        for(BindException bindError : errors){
            Record record = new Record();
            record.setEntity(bindError.getTarget().toString());
            if(bindError.getTarget() instanceof BaseEntity){
                Object key = ((BaseEntity) bindError.getTarget()).getKey();
                if(key == null){
                    key = ++generatedKey;
                }
                record.setEntityId(key.toString());
            }
            for(Object o : bindError.getAllErrors()){
                ObjectError objectError = (ObjectError) o;
                if(objectError instanceof FieldError){
                    Field field = new Field();
                    field.setId(((FieldError) objectError).getField());
                    List<Object> arguments = getArguments(objectError, field);

                    Error error = new Error();
                    error.setMessage(getText(objectError.getDefaultMessage(), arguments.toArray(), objectError.getDefaultMessage()));
                    field.addError(error);
                    record.addField(field);
                }
                else{
                    Error error = new Error();
                    error.setMessage(getText(objectError.getDefaultMessage(), objectError.getDefaultMessage()));
                    record.addError(error);
                }
            }
            info.addRecord(record);
        }
        if(responseType != null && responseType.equals("json")){
            return new TextView(info.toJSONString());

        }else{
            return new XmlView(info.toXml());
        }
    }

    private List<Object> getArguments(ObjectError objectError, Field field) {
        List<Object> arguments = new ArrayList<Object>();
        try{
            arguments.add(getText(objectError.getObjectName() + "." + field.getId()));
        }catch(Exception ex){
            arguments.add("???" + objectError.getObjectName() + "." + field.getId() + "???");
        }
        if(objectError.getArguments() != null) arguments.addAll(Arrays.asList(objectError.getArguments()));
        return arguments;
    }

    public String saveFile(HttpServletRequest request, String uploadDir, String fileName, Boolean keepOriginalExtension) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();
        //write the file to the file specified
        if(keepOriginalExtension){
            fileName += file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        }
        file.transferTo(new File(uploadDir + fileName));
//        OutputStream bos = new FileOutputStream(uploadDir + fileName);
//        int bytesRead;
//        byte[] buffer = new byte[8192];
//        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
//            bos.write(buffer, 0, bytesRead);
//        }
//        bos.close();
//        stream.close();
        return fileName;
    }

    public String saveFile(HttpServletRequest request, String uploadDir) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();
        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + file.getOriginalFilename());
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bos.close();
        stream.close();
        return file.getOriginalFilename();
    }

    public DecimalFormat getDecimalFormat() {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance();
        df.applyPattern("0.00");
        df.setMinimumFractionDigits(2);
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        return df;
    }
    
}