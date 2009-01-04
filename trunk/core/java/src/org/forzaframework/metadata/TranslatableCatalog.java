/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.metadata;

import org.dom4j.Element;
import org.dom4j.DocumentHelper;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Map;
import java.util.HashMap;

/**
 * @author cesarreyes
 *         Date: 08-sep-2008
 *         Time: 17:47:51
 */
@MappedSuperclass
public class TranslatableCatalog extends Catalog {

    protected Map<String, String> translations = new HashMap<String, String>();

    public Map<String, String> getTranslations() {
        if(translations == null) translations = new HashMap<String, String>();
        return translations;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public void setTranslation(String externalSystem, String code){
        this.getTranslations().put(externalSystem, code);
    }

    @Transient
    public String getExternalCode(String system){
        return translations.get(system);
    }

    public Element toXml(){
        return this.toXml("item");
    }

    public Element toXml(String elementName) {
        Element el = DocumentHelper.createElement(elementName);
        el.addElement("id").addText(this.getId().toString());
        el.addElement("code").addText(this.getCode() == null ? "" : this.getCode());
        el.addElement("name").addText(this.getName() == null ? "" : this.getName());
        if(this.translations != null){
            for (Object o : this.translations.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Object value = entry.getValue();
                el.addElement(entry.getKey().toString()).addText(value == null ? "" : value.toString());
            }
        }
        return el;
    }
}
