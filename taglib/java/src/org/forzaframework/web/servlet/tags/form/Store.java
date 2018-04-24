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

package org.forzaframework.web.servlet.tags.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

@SuppressWarnings({"unchecked"})
public class Store {
	
	private String id;
	private String reader;
    private String type = "remote";
	private String itemTag = "item";
	private String idField;
	private String name;
	private Boolean loadOnStart;
	private String valueField;
	private String displayField;
	private List<Field> fields;
	private String url;
	private Object items;
	private List<Option> options;
    private String groupField;
    private String noSelection;
    private Boolean remoteSort = true;
    private Integer connectionTimeOut;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getItemTag() {
		return itemTag;
	}
	
	public void setItemTag(String itemTag) {
		this.itemTag = itemTag;
	}
	
	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getLoadOnStart() {
		return loadOnStart;
	}
	
	public void setLoadOnStart(Boolean loadOnStart) {
		this.loadOnStart = loadOnStart;
	}
	
	
	public String getValueField() {
		return valueField;
	}
	
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	
	public String getDisplayField() {
		return displayField;
	}
	
	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}
	
	public List<Field> getFields() {
		return fields;
	}
	
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Object getItems() {
		return items;
	}
	
	public void setItems(Object items) {
		this.items = items;
	}
	
	public List<Option> getOptions() {
		return options;
	}
	
	public void setOptions(List<Option> options) {
		this.options = options;
	}

    public String getGroupField() {
        return groupField;
    }

    public void setGroupField(String groupField) {
        this.groupField = groupField;
    }

    public String getNoSelection() {
        return noSelection;
    }

    public void setNoSelection(String noSelection) {
        this.noSelection = noSelection;
    }

    public Integer getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(Integer connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public Store(String name, Boolean loadOnStart, List<Field> fields){
		this.name = name;
		this.loadOnStart = loadOnStart;
		this.fields = fields;
	}

    public Boolean getRemoteSort() {
        return remoteSort;
    }

    public void setRemoteSort(Boolean remoteSort) {
        this.remoteSort = remoteSort;
    }

    public Store(String name, Boolean loadOnStart, String valueField, String displayField, List<Field> fields){
		this.name = name;
		this.loadOnStart = loadOnStart;
		this.valueField = valueField;
		this.displayField = displayField;
		this.fields = fields;
	}
	
    public String buildStoreDeclaration(){
        StringBuilder sb = new StringBuilder();
        if(type.equals("remote")){
            if(StringUtils.isNotBlank(groupField)){
                sb.append("var ").append(name).append(" = new Ext.data.GroupingStore({");
                sb.append("groupField: '").append(groupField).append("',");
                if("json".equals(reader)) {
                    sb.append("groupOnSort: ").append(true).append(",");
                }
            }else{
                sb.append("var ").append(name).append(" = new Ext.data.Store({");
                if(id != null) {
                    sb.append("storeId: '").append(id).append("',");
                }
            }
            sb.append("proxy: new Ext.data.HttpProxy(new Ext.data.Connection({url: \"").append(StringUtils.isBlank(url) ? "" : url).append("\"").append(connectionTimeOut != null ? ", timeout: " + connectionTimeOut : "").append("})),");
            sb.append("remoteSort: " + remoteSort + ",");
            
            if(reader == null || reader.equals("xml")){
                sb.append("reader: new Ext.data.XmlReader({");
                sb.append("allowFunctions: true,");
                if(idField == null) idField = valueField;
                sb.append("record: '").append(itemTag).append("', totalRecords: 'totalCount', id: '").append(idField).append("'},");
            }else if(reader.equals("json")){
                sb.append("reader: new Ext.data.JsonReader({");
                if(idField == null) idField = valueField;
                sb.append("root: '").append(itemTag).append("', totalProperty: 'totalCount', id: '").append(idField).append("'},");
            }

            JSONArray record = new JSONArray();
            if(valueField != null){
            	JSONObject valueField = new JSONObject();
            	valueField.put("name", this.valueField);
                record.add(valueField);
            }
            if(displayField != null){
            	JSONObject displayField = new JSONObject();
            	displayField.put("name", this.displayField);
                record.add(displayField);            	
            }
            if(fields.size() > 0){
                for(Field field : fields){
                    JSONObject json = new JSONObject();
                    json.put("name", field.getField());
                    json.elementOpt("mapping", field.getMapping());

                    if(field.getType() != null){
                        if(field.getType().equals("string")){
                            json.put("type", "string");
                        }else if(field.getType().equals("float")){
                            json.put("type", "float");
                        }else if(field.getType().equals("date")){
                            json.put("type", "date");
                            json.put("dateFormat", "d/m/Y");
                        }
                    }
                    record.add(json);
                }
            }
            sb.append(record.toString()).append(")");

            sb.append("});");

        }else{
            JSONObject store = new JSONObject();
            if(items != null){
                sb.append("var ").append(name).append(" = new Ext.data.JsonStore(");
            }else{
                sb.append("var ").append(name).append(" = new Ext.data.SimpleStore(");
                store.put("id", 0);
            }
            JSONArray fieldsArray = new JSONArray();
            if(fields.size() > 0){
                for(Field field : fields){
                    JSONObject json = new JSONObject();
                    json.put("name", field.getField());
                    json.elementOpt("mapping", field.getMapping());

                    if(field.getType() != null){
                        if(field.getType().equals("string")){
                            json.put("type", "string");
                        }else if(field.getType().equals("float")){
                            json.put("type", "float");
                        }else if(field.getType().equals("date")){
                            json.put("type", "date");
                            json.put("dateFormat", "d/m/Y");
                        }
                    }
                    fieldsArray.add(json);
                }
            }

            store.put("fields", fieldsArray);
            if(options != null && options.size() > 0){
                JSONArray data = new JSONArray();
                for(Option option : options){
                    JSONArray optionArray = new JSONArray();
                    optionArray.add(option.getValue());
                    optionArray.add(option.getText());
                    data.add(optionArray);
                }
                store.put("data", data);
            }else if(items != null){
                if (items.getClass().isArray()) {
                    //renderFromArray(tagWriter);
                    throw new RuntimeException("Items as array not implemented");
                }
                else if (items instanceof Collection) {
                    JSONArray data = new JSONArray();
                    Collection optionCollection = (Collection) items;
                    if (noSelection != null) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", "");
                        jsonObject.put(StringUtils.isNotBlank(displayField) ? displayField : "name", noSelection);
                        data.add(jsonObject);
                    }
                    for (Object item : optionCollection) {
                        Assert.notNull(item, "Algun objeto en la coleccion es nulo.");
                        JSONObject jsonObject = new JSONObject();

                        if(item instanceof Map){ // si es un map
                            Map<String, ?> map = (Map<String, ?>) item;
                            for (String key : map.keySet()) {
                                jsonObject.put(key, map.get(key));
                            }
                        }else if(item instanceof Integer){
                            jsonObject.put("id", item);
                            jsonObject.put("name", item);
                        }else{
                            BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
                            for(Field field : fields){
                                jsonObject.put(field.getField(), wrapper.getPropertyValue(field.getField()));
                            }
                        }

                        data.add(jsonObject);
                    }
                    store.put("data", data);
                }
                else if (items instanceof JSONObject) {
                    store.put("data", items);
                }
                else if (items instanceof Map) {
                    //renderFromMap(tagWriter);
                    throw new RuntimeException("Items as map not implemented");
                }
                else {
                    throw new RuntimeException("Type [" + items.getClass().getName() + "] is not valid for option items");
                }
            }
            sb.append(store.toString()).append(");");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("js", sb.toString());
        if(loadOnStart != null){
            map.put("load", loadOnStart);
        }else{
            if(type.equals("remote")){
                map.put("load", options.size() == 0 && StringUtils.isNotBlank(url));
            }
        }

        return sb.toString();
    }

}
