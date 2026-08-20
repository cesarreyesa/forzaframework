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

import net.sf.json.JSONObject;
import net.sf.json.JSONException;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.beans.propertyeditors.CustomClassEditor;
import org.forzaframework.bind.JsonDataBinder;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.metadata.SystemConfiguration;
import org.forzaframework.metadata.SystemEntity;
import org.forzaframework.metadata.TranslatableCatalog;
import org.forzaframework.metadata.ExternalSystem;
import org.forzaframework.validation.Information;
import org.forzaframework.web.servlet.view.TextView;
import org.forzaframework.web.servlet.view.XmlView;
import org.hibernate.NonUniqueObjectException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * @author cesarreyes
 *         Date: 19-ago-2008
 *         Time: 13:11:48
 */
@SuppressWarnings({"unchecked"})
@Controller
@RequestMapping("/listForm.html")
public class CollectionSubmitController extends BaseController {

    public static String defaultItemsParameterName = "items";

    private SystemConfiguration systemConfiguration;
    private EntityManager entityManager;

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView processSubmit(@RequestParam("e") String entityName, WebRequest request) throws Exception {
            Information info = new Information();

        SystemEntity entity = systemConfiguration.getSystemEntity(entityName);
        String[] items = request.getParameterValues(defaultItemsParameterName);

        try{
            for(String item : items){
                Object bean = BeanUtils.instantiateClass(entity.getType());

                JsonDataBinder binder = new JsonDataBinder(bean);

                Object command = BeanUtils.instantiateClass(entity.getType());
                BeanWrapper beanWrapper = new BeanWrapperImpl(command);
                for(PropertyDescriptor pd : beanWrapper.getPropertyDescriptors()){
                    if(ClassUtils.isAssignable(pd.getPropertyType(), BaseEntity.class)){
                        binder.registerCustomEditor(pd.getPropertyType(), pd.getName(), new CustomClassEditor(pd.getPropertyType(), entityManager));
                    }
                }

                JSONObject json = JSONObject.fromObject(item);
                binder.bind(json);

                if(systemConfiguration.getEnableExternalSystems()){
                    if(bean instanceof TranslatableCatalog){
                        List<ExternalSystem> externalSystems = systemConfiguration.getExternalSystems();
                        for(ExternalSystem system : externalSystems){
                            try{
                                String code = json.getString(system.getCode());
                                if(StringUtils.isNotBlank(code)) {
                                    ((TranslatableCatalog) bean).setTranslation(system.getCode(), code);
                                }
                            }catch(JSONException ex){
                                //
                            }
                        }
                    }else{
                        logger.warn("Entity must be instance of TranslatableCatalog");
                    }
                }

                try {
                    entityManager.save(bean);
                } catch (NonUniqueObjectException e) {
                    entityManager.merge(bean);
                }
//                entityManager.getHibernateSession().clear();
//                entityManager.getHibernateSession().flush();
                info.addMessage(getText("entityList.saved"));
            }

        }catch(Exception e){
            info.addError(e.getMessage());
        }

        if(request.getParameter("itype") != null && request.getParameter("itype").equals("json")){
            return new TextView(info.toJSONString());

        }else{
            return new XmlView(info.toXml());
        }
    }
}