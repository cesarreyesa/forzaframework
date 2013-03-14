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

package org.forzaframework.web.servlet.view;

import org.forzaframework.util.AlphanumBeanComparator;
import org.springframework.util.Assert;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.metadata.SystemEntity;
import org.forzaframework.metadata.SystemConfiguration;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.core.persistance.Criteria;
import org.forzaframework.core.persistance.Restrictions;
import org.forzaframework.util.XmlUtils;
import org.forzaframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.List;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 17:35:39
 */
public class ListXmlView extends BaseView {

    private EntityManager entityManager;
    private SystemConfiguration systemConfiguration;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    private SystemEntity getSystemEntity(HttpServletRequest request, Map model) {
        // TODO: Lanzar excepcion en caso de no encontrar la entidad.
        SystemEntity entity = systemConfiguration.getSystemEntity(model.get("entity") != null ? model.get("entity").toString() : request.getParameter("e"));
        Assert.notNull(entity, "No existe la entidad [" + entity +"]. Favor de revisar la configuracion");
        return entity;
    }

    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Document doc = DocumentHelper.createDocument();

        String query = request.getParameter("query");
        String id = request.getParameter("id");

        if(StringUtils.isNotBlank(id)){
            SystemEntity entity = getSystemEntity(request, model);
            BaseEntity object = (BaseEntity) entityManager.get(entity.getType(), Long.valueOf(id));
            Element element = object.toXml();
            Element root = doc.addElement("response");
            root.addAttribute("success", "true");
            root.add(element);
            doc.setRootElement(root);

        }else if(request.getParameter("id") != null){
            XmlUtils.buildEmptyListDocument(doc);
        }else{
            List<? extends BaseEntity> list;
            if(request.getParameter("e").equals("entity") || request.getParameter("e").equals("externalSystem")){
                if(request.getParameter("e").equals("entity")){
                    list = systemConfiguration.getSystemEntities();
                }else{
                    list = systemConfiguration.getExternalSystems();
                }
            }else{
                SystemEntity entity = getSystemEntity(request, model);
                Criteria criteria = new Criteria();
                if(entity.findAttribute("name") != null && query != null){
                    criteria.add(Restrictions.like("name", "%" + query + "%").ignoreCase());
                }
                list = entityManager.find(entity.getType(), criteria);
            }
            if (StringUtils.isNotBlank(request.getParameter("sort"))){
                Collections.sort(list, new AlphanumBeanComparator(request.getParameter("sort"), request.getParameter("dir")));
            }
            List<? extends BaseEntity> objects = CollectionUtils.paginate(list, request.getParameterMap(), false);

            XmlUtils.buildDocument(doc, objects, list.size());
        }

        response.setContentType("text/xml");

        response.getWriter().write(doc.asXML());

    }
}
