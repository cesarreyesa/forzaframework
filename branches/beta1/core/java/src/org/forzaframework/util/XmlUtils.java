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

package org.forzaframework.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.forzaframework.core.persistance.BaseEntity;

import java.util.*;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:56:47
 */
public class XmlUtils {

    public static Document buildEmptyListDocument() {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("ISO-8859-1");
        buildEmptyListDocument(doc);
        return doc;
    }

    public static void buildEmptyListDocument(Document doc) {
        Element root = doc.addElement("items");
        root.addAttribute("success", "true");
        doc.setRootElement(root);
        root.addElement("totalCount").addText("0");
    }

    public static void buildDocument(Document doc, List<? extends BaseEntity> list) {
        buildDocument(doc, list, list.size());
    }

    public static void buildDocument(Document doc, List<? extends BaseEntity> list, Integer totalCount) {
        Element root = doc.addElement("items");
        root.addAttribute("success", "true");
        doc.setRootElement(root);

        for (BaseEntity object : list) {
            root.add(object.toXml());
        }
        root.addElement("totalCount").addText(String.valueOf(totalCount));
    }

    public static Document buildDocument(List<? extends BaseEntity> list) {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("ISO-8859-1");
        buildDocument(doc, list, list.size());
        return doc;
    }

    public static Document buildDocument(List<? extends BaseEntity> list, Integer totalCount) {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("ISO-8859-1");
        buildDocument(doc, list, totalCount);
        return doc;
    }

    public static Document buildDocument(List list, String[] elements) throws Exception{
        Document doc = DocumentHelper.createDocument();
        buildDocument(doc, list, list.size(), elements);
        return doc;
    }

    public static Document buildDocument(List list, Integer size, String[] elements) throws Exception{
        Document doc = DocumentHelper.createDocument();
        buildDocument(doc, list, size, elements);
        return doc;
    }

    public static Document buildDocument(Document doc, List list, Integer size, String[] elements) throws Exception {
        Element root = doc.addElement("items");
        root.addAttribute("success", "true");
        doc.setRootElement(root);

        for (Object bean : list) {
            Element item = root.addElement("item");
            for (String element : elements) {
                Object value = PropertyUtils.getProperty(bean, element);
                if(value != null && value instanceof Date){
                    item.addElement(element).addText(DateUtils.getString((Date) value));
                }else{
                    item.addElement(element).addText(value == null ? "" : value.toString());
                }
            }
        }
        root.addElement("totalCount").addText(String.valueOf(size));
        return doc;
    }

//    public static Document buildDocument(List<? extends BaseEntity> list, Integer totalCount) {
//        Document doc = DocumentHelper.createDocument();
//        doc.setXMLEncoding("ISO-8859-1");
//        buildDocument(doc, list, totalCount);
//        return doc;
//    }

    public static Document buildDocumentFromMap(List<Map> list){
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("ISO-8859-1");

        Element root = doc.addElement("items");
        root.addAttribute("success", "true");
        doc.setRootElement(root);

        for(Map map : list){
            Element el = DocumentHelper.createElement("item");
            for(Iterator it = map.entrySet().iterator(); it.hasNext();){
                Map.Entry entry = (Map.Entry) it.next();
                if(entry.getKey().toString().equals("$type$"))
                    continue;

                el.addElement(entry.getKey().toString()).addText(entry.getValue() == null ? "" : entry.getValue().toString());
            }
            root.add(el);
        }

        return doc;
    }

    public static void removeChildren(Node node) {
        NodeList childNodes = node.getChildNodes();
        int length = childNodes.getLength();
        for (int i = length - 1; i > -1; i--)
            node.removeChild(childNodes.item(i));
    }

    public static void addTextElement(Element el, String elementName, Object value){
        if(value != null && value instanceof Date){
            el.addElement(elementName).addText(DateUtils.getString((Date) value));
        }else{
            el.addElement(elementName).addText(value == null ? "" : value.toString());
        }
    }

    public static org.w3c.dom.Element getChildElement(org.w3c.dom.Element parent, String childName) {
        NodeList children = parent.getChildNodes();
        int size = children.getLength();

        for (int i = 0; i < size; i++) {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;

                if (childName.equals(element.getNodeName())) {
                    return element;
                }
            }
        }

        return null;
    }

    public static List<org.w3c.dom.Element> getChildElements(org.w3c.dom.Element parent, String childName) {
        NodeList children = parent.getChildNodes();
        List<org.w3c.dom.Element> list = new ArrayList<org.w3c.dom.Element>();
        int size = children.getLength();

        for (int i = 0; i < size; i++) {
            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;

                if (childName.equals(element.getNodeName())) {
                    list.add(element);
                }
            }
        }

        return list;
    }

    public static String getChildText(org.w3c.dom.Element parent, String childName) {
        org.w3c.dom.Element child = getChildElement(parent, childName);

        if (child == null) {
            return null;
        }

        return getText(child);
    }

    public static String getText(org.w3c.dom.Element node) {
        StringBuffer sb = new StringBuffer();
        NodeList list = node.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);

            switch (child.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.TEXT_NODE:
                sb.append(child.getNodeValue());
            }
        }

        return sb.toString();
    }
}
