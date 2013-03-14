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

package org.forzaframework.validation;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * User: cesarreyes
 * Date: 14-may-2007
 * Time: 22:32:17
 * Description:
 */
public class Information {

    private Boolean success = true;
    private String entityId;
    private Object entity;
    private List<String> messages = new ArrayList<String>();
    private List<Field> fields = new ArrayList<Field>();
    private List<Error> errors = new ArrayList<Error>();
    private List<Record> records = new ArrayList<Record>();

    public Information() {
    }

    public Information(Boolean success) {
        this.success = success;
    }

    public Information(String error){
        this.addError(error);
    }

    public Boolean getSuccess() {
        if((this.errors.size() > 0) || (this.fields.size() > 0)) return false;
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message){
        this.messages.add(message);
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field){
        this.fields.add(field);
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void addError(Error error){
        this.errors.add(error);
    }

    public void addError(String message){
        this.errors.add(new Error(message));
    }

    public List<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record){
        this.records.add(record);
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("success", getSuccess());
        json.put("entityId", entityId);
        json.elementOpt("entity", entity);
        if(this.messages.size() > 0){
            JSONArray messages = new JSONArray();
            for(String message : this.messages){
                messages.add(message);
            }
            json.put("messages", messages);
        }
        if(this.errors.size() > 0){
            JSONArray errors = new JSONArray();
            for(Error error : this.errors){
                errors.add(error.toJSON());
            }
            json.put("errors", errors);
        }
        if(this.fields.size() > 0){
            JSONArray fields = new JSONArray();
            for(Field field : this.fields){
                fields.add(field.toJSON());
            }
            json.put("fields", fields);
        }        
        if(this.records.size() > 0){
            JSONArray records = new JSONArray();
            for(Record record : this.records){
                records.add(record.toJSON());
            }
            json.put("records", records);
        }

        return json;
    }

    public String toJSONString(){
        JSONObject json = toJSON();
        return json.toString();
    }

    public String toXml(){
        Document doc = DocumentHelper.createDocument();
        Element info = doc.addElement("information");
        info.addAttribute("success", this.getSuccess().toString());
        info.addAttribute("entityId", entityId);
        if(this.messages.size() > 0){
            Element messages = info.addElement("messages");
            for(String message : this.messages){
                messages.addElement("message").addCDATA(message);
            }
        }
        if(errors.size() > 0){
            Element fieldEl = info.addElement("field");
            StringBuffer message = new StringBuffer();
            for(Error error : errors){
                message.append(error.getMessage()).append("<br />");
            }
            fieldEl.addElement("msg").addCDATA(message.toString());
        }
        if(fields.size() > 0){
            Element fieldsEl = info.addElement("fields");
            for(Field field : fields){
                Element fieldEl = fieldsEl.addElement("field");
                fieldEl.addElement("id").addText(field.getId() == null ? "" : field.getId());
                fieldEl.addElement("code").addText(field.getCode() == null ? "" : field.getCode());
                StringBuffer message = new StringBuffer();
                for(Error error : field.getErrors()){
                    message.append(error.getMessage()).append("<br />");
                }
                fieldEl.addElement("msg").addCDATA(message.toString());
            }
        }
        return doc.asXML();
    }
}
