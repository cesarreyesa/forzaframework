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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.forzaframework.validation.Information;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.web.servlet.view.XmlView;
import org.forzaframework.web.servlet.view.TextView;
import org.forzaframework.metadata.SystemEntity;
import org.forzaframework.metadata.SystemConfiguration;
import org.forzaframework.util.ExceptionTranslator;

/**
 * @author cesarreyes
 *         Date: 12-sep-2008
 *         Time: 17:46:47
 */
@Controller
@RequestMapping("/form.html")
public class SimpleEntityFormController extends BaseController {

    private SystemConfiguration systemConfiguration;
    protected EntityManager entityManager;
    private String entity;
    protected String formView;

    public SimpleEntityFormController() {
    }

    public SimpleEntityFormController(String entity) {
        this.entity = entity;
    }

    public SimpleEntityFormController(String entity, String formView) {
        this.entity = entity;
        this.formView = formView;
    }

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private SystemEntity getSystemEntity(WebRequest request) {
        // TODO: Lanzar excepcion en caso de no encontrar la entidad.
        return systemConfiguration.getSystemEntity(this.entity != null ? this.entity : request.getParameter("e"));
    }

    @RequestMapping(method = RequestMethod.GET, params = "e")
    public String setupForm(@RequestParam(value = "id", required = false) Long id, ModelMap model, WebRequest request) {

        SystemEntity entity = getSystemEntity(request);

        Object command;

        if (id != null) {
            command = entityManager.get(entity.getType(), id);
        } else {
            command = BeanUtils.instantiateClass(entity.getType());
        }
        model.addAttribute(entity.getCode(), command);

        return formView;
    }

    @RequestMapping(method = RequestMethod.POST, params = "e")
    protected ModelAndView processSubmit(WebRequest request, @ModelAttribute("command")BaseEntity command, BindingResult result) throws Exception {
        Information info = new Information();

        SystemEntity entity = getSystemEntity(request);

        if(result.hasErrors()) return this.processErrors(result);

        try{
            if (request.getParameter("mode") != null && request.getParameter("mode").equals("delete")) {
                entityManager.delete(entity.getType(), Long.valueOf(request.getParameter("id")));
                info.addMessage(entity.getCode() + ".deleted");
            }else{
                String key = command.getKey() == null ? entity.getName() + ".added" : entity.getName() + ".updated";
                entityManager.save(command);
                info.setEntityId(command.getKey().toString());
                info.addMessage(key);
                logger.debug("Form submitted succesfully. [" + key + "]");
            }
        }catch(Exception e){
            info.addError(ExceptionTranslator.translate(e, request.getLocale()));
        }

        if(request.getParameter("itype") != null && request.getParameter("itype").equals("json")){
            return new TextView(info.toJSONString());

        }else{
            return new XmlView(info.toXml());
        }
    }

}